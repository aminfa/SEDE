package de.upb.sede.exec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.sede.config.ExecutorConfiguration;
import de.upb.sede.core.PrimitiveDataField;
import de.upb.sede.core.SEDEObject;
import de.upb.sede.core.SemanticStreamer;
import de.upb.sede.procedure.FinishProcedure;
import de.upb.sede.procedure.SendGraphProcedure;
import de.upb.sede.procedure.TransmitDataProcedure;
import de.upb.sede.requests.DataPutRequest;
import de.upb.sede.requests.ExecRequest;
import de.upb.sede.requests.ExecutorRegistration;
import de.upb.sede.util.Observer;
import de.upb.sede.util.Streams;
import de.upb.sede.webinterfaces.client.BasicClientRequest;
import de.upb.sede.webinterfaces.client.HttpURLConnectionClientRequest;
import de.upb.sede.webinterfaces.server.HTTPServerResponse;
import de.upb.sede.webinterfaces.server.ImServer;
import de.upb.sede.webinterfaces.server.StringServerResponse;
import de.upb.sede.webinterfaces.server.SunHttpHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExecutorJettyServer implements HttpExecutor {

	private static final Logger logger = LoggerFactory.getLogger(ExecutorJettyServer.class);

	private final Executor basis;
	private String hostAddress;

	private final List<Handler> handlers = new ArrayList<Handler>();
	private final ContextHandlerCollection contexts = new ContextHandlerCollection();
	private final Server server;
	private final int port;

	private final Pattern PUT_DATA_URL_PATTERN = Pattern.compile("/put/(?:[\\-\\w]+)/(?<executionId>[\\-\\w]+)/(?<fieldname>(?:[&_a-zA-Z][&\\-\\w]*+))/(?<semtype>[\\-\\w]+)");
	private final Pattern INTERRUPT_URL_PATTERN = Pattern.compile("/interrupt/(?:[\\-\\w]+)/(?<executionId>[\\-\\w]+)");

	public static ExecutorJettyServer enablePlugin(Executor basis, String hostAddress, int port) {
		return new ExecutorJettyServer(basis, hostAddress, port);
	}

	public ExecutorJettyServer(ExecutorConfiguration execConfig, String hostAddress, int port) {
		this(new Executor(execConfig), hostAddress, port);
	}

	public ExecutorJettyServer(Executor basis, String hostAddress, int port) {
		this.basis = basis;
		this.port = port;
		setHostAddress(hostAddress + ":" + port);

		Optional<String> envPort = Optional.ofNullable(System.getenv("EXECUTOR_SERVER_PORT"));
		int actualPort = port;
		if(envPort.isPresent()) {
			logger.info("EXECUTOR_SERVER_PORT was set to `" + envPort.get() + "`.");
			try{
				actualPort = Integer.parseInt(envPort.get());
			} catch(NumberFormatException ex) {
				logger.warn("Format error: `" + envPort.get() + "`. Using fallback port `" + actualPort + "`.");
			}
		}
		logger.info("Creating Executor http server with port=" + actualPort);

		QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setMaxThreads(500);
		server = new Server(threadPool);
		ServerConnector connector=new ServerConnector(server);
		connector.setHost("0.0.0.0");
		connector.setPort(actualPort);
		server.setConnectors(new Connector[]{connector});

		addHandle("/put", PutDataHandler::new);
		addHandle("/execute", ExecuteGraphHandler::new);
		addHandle("/interrupt", InterruptHandler::new);

		server.setHandler(contexts);
		try {
			server.start();
			server.dumpStdErr();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}


		bindHttpProcedures();

		/*
		 *  Shutdown the http server if the base executor is being shutdown.
		 */
		basis.shutdownHook.observe(Observer.alwaysNotify(executor -> this.shutdown()));

		registerToEveryGateway();
	}


	/**
	 * Register to every gateway stated by the config.
	 */
	public void registerToEveryGateway() {
		List<String> gatewaysToBeRegistered = new ArrayList<>(basis.getExecutorConfiguration().getGateways());
		for(String gatewayAddress : gatewaysToBeRegistered) {
			try{
				registerToGateway(gatewayAddress);
			}
			catch(Exception ex) {
				logger.error("Error during registration to gateway: {}", gatewayAddress, ex);
			}
		}
	}

	@Override
	public String getExecutorId() {
		return getBasisExecutor().getExecutorConfiguration().getExecutorId();
	}

	/**
	 * Changes the host address of this executor http server.
	 * The host address is put into the contact info map which is shared with executors
	 * who use the contact info map to reach this executor.
	 * It is thus important to set hostAddress to the external http address of this executor.
	 * @param hostAddress the new host address.
	 */
	public void setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
		basis.getModifiableContactInfo().put("host-address", this.hostAddress);
	}

	public String getHostAddress() {
		return hostAddress;
	}



	public Executor getBasisExecutor() {
		return basis;
	}

	public int getServerTCPPort() {
		return port;
	}

	public void addHandle(String context, Supplier<HTTPServerResponse> serverResponder) {
		this.addHandle(context, new AbstractHandler() {
			@Override
			public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
				HTTPServerResponse responder = serverResponder.get();
				responder.receive(Optional.of(context + target), request.getInputStream(), response.getOutputStream());
			}
		});
	}

	public void addHandle(String context, Handler handler) {
		ContextHandler contextHandler = new ContextHandler(context);
		contextHandler.setHandler(handler);
		handlers.add(contextHandler);
		contexts.setHandlers(handlers.toArray(new Handler[0]));
	}

	public void registerToGateway(String gatewayHost) {
		ExecutorRegistration registration = basis.registration();

		HttpURLConnectionClientRequest httpRegistration = new HttpURLConnectionClientRequest(gatewayHost + "/register");

		String registrationAnswer = httpRegistration.send(registration.toJsonString());
		if (!registrationAnswer.isEmpty()) {
			throw new RuntimeException("Registration to gateway \"" + gatewayHost
					+ "\" failed with non empty return message:\n" + registrationAnswer);
		}
		if(!basis.getExecutorConfiguration().getGateways().contains(gatewayHost)) {
			basis.getExecutorConfiguration().getGateways().add(gatewayHost);
		}
		logger.debug("Registered to gateway: " + gatewayHost);
	}

	private void bindHttpProcedures() {
		WorkerPool wp = basis.getWorkerPool();
		wp.bindProcedure("TransmitData", TransmitDataOverHttp::new);
		wp.bindProcedure("SendGraph", SendGraphOverHttp::new);
		wp.bindProcedure("Finish", FinishOverHttp::new);
	}

	@Override
	public void shutdown() {
	}

	private static BasicClientRequest createPutDataRequest(String host, String targetExecutorId, String fieldname, String semType, String executionId, boolean failed) {
		if (host == null || fieldname == null) {
			throw new RuntimeException(
					"Cannot create a put data request without host or fieldname");
		}
		String dataPutUrl = host + "/put/" + targetExecutorId + "/" + executionId + "/" + fieldname;
		if(failed) {
			dataPutUrl += "/unavailable";
		} else {
			dataPutUrl += "/" + semType;
		}
		BasicClientRequest clientRequest = new HttpURLConnectionClientRequest(dataPutUrl);
		return clientRequest;

	}

	static class TransmitDataOverHttp extends TransmitDataProcedure {

		@Override
		public BasicClientRequest getPutDataRequest(Task task, boolean unavailable) {
			Map<String, String> contactInfo = (Map<String, String>) task.getAttributes().get("contact-info");
			String host = contactInfo.get("host-address");
			String fieldname = (String) task.getAttributes().get("fieldname");
			String semType = (String) task.getAttributes().get("semantic-type");
			String executionId = task.getExecution().getExecutionId();
			String targetExecutorId =  contactInfo.get("id");
			if (!unavailable && semType == null) {
				SEDEObject sedeObject = task.getExecution().getEnvironment().get(fieldname);
				if (sedeObject.isReal()) {
//					throw new RuntimeException("The task doesn't contain the 'semantic-type' field "
//							+ "but when transmitting real data the semantic type needs to be defined. \n" + "task: "
//							+ task.getAttributes().toString() + "\nfield to be sent: " + sedeObject.toString());
					semType = "SemanticType";
				} else {
					semType = sedeObject.getType();
				}
			}
			String dataPutUrl = host + "/put/" + targetExecutorId + "/" + executionId + "/" + fieldname;
			if(unavailable) {
				dataPutUrl += "/unavailable";
			} else {
				dataPutUrl += "/" + semType;
			}
			BasicClientRequest clientRequest = new HttpURLConnectionClientRequest(dataPutUrl);
			return clientRequest;
		}
	}

	static class FinishOverHttp extends FinishProcedure {

		@Override
		public BasicClientRequest getFinishFlagRequest(Task task) {
			Map<String, String> contactInfo = (Map<String, String>) task.getAttributes().get("contact-info");
			String host = contactInfo.get("host-address");
			String targetExecutorId = contactInfo.get("id");
			String fieldname = (String) task.getAttributes().get("fieldname");
			String semType = PrimitiveDataField.PrimitiveType.Bool.name();
			String executionId = task.getExecution().getExecutionId();
			boolean failed = false;
			return createPutDataRequest(host, targetExecutorId, fieldname, semType, executionId, failed);
		}
	}

	static class SendGraphOverHttp extends SendGraphProcedure {

		@Override
		public BasicClientRequest getExecRequest(Task task) {
			Map<String, String> contactInfo = (Map<String, String>) task.getAttributes().get("contact-info");
			String host = contactInfo.get("host-address");
			String targetExecutorId = contactInfo.get("id");
			if (host == null) {
				throw new RuntimeException(
						"The task doesn't contain all necessary fields: " + task.getAttributes().toString());
			}
			String executeGraphUrl = host + "/execute/" + targetExecutorId;
			BasicClientRequest clientRequest = new HttpURLConnectionClientRequest(executeGraphUrl);
			return clientRequest;
		}

		@Override
		public void handleInterrupt(Task task) {
			Map<String, String> contactInfo = (Map<String, String>) task.getAttributes().get("contact-info");
			String host = contactInfo.get("host-address");
			String targetExecutorId = contactInfo.get("id");
			String interruptUrl = host + "/interrupt/" + targetExecutorId + "/" + task.getExecution().getExecutionId();
			BasicClientRequest interruptRequest = new HttpURLConnectionClientRequest(interruptUrl);
			logger.debug("Interrupting remote execution: {}", contactInfo);
			String response = interruptRequest.send("");
			if(!response.isEmpty()) {
				logger.error("Interrupting remote execution with an http request failed. The response is not empty: {}\nexec Id: {}\ncontact info: {}", response, task.getExecution().getExecutionId(), contactInfo.toString());
			}
		}
	}

	class PutDataHandler implements HTTPServerResponse {

		@Override
		public void receive(Optional<String> url, InputStream payload, OutputStream answer) {
			String response;
			try {
				if (!url.isPresent()) {
					throw new RuntimeException("Put data needs to specify URL with fieldname and execution handle");
				}

				Matcher matcher = PUT_DATA_URL_PATTERN.matcher(url.get());
				if(!matcher.matches()){
					// pattern: put/(?<executionId>\w+)/(?<fieldname>\w+)/(?<semtype>\w+)
					throw new RuntimeException("URL syntax error: " + url.get());
				}
				String execId = matcher.group("executionId");
				String fieldname = matcher.group("fieldname");
				String semanticType = matcher.group("semtype");

				DataPutRequest putRequest;
				if(semanticType.equals("unavailable")) {
					putRequest = DataPutRequest.unavailableData(execId, fieldname);
				} else{
					SEDEObject inputObject = SemanticStreamer.readFrom(payload, semanticType);
					putRequest = new DataPutRequest(execId, fieldname, inputObject);
				}
				basis.put(putRequest);
				response = "";
			} catch (Exception ex) {
				logger.error("Error at put request: {}\n", url.get(), ex);
				response = ex.getMessage();
			}
			Streams.OutWriteString(answer, response, true);
		}
	}

	class ExecuteGraphHandler extends StringServerResponse {
		@Override
		public String receive(String payload) {
			try {
				ExecRequest request = new ExecRequest();
				request.fromJsonString(payload);
				basis.exec(request);
				return "";
			} catch (Exception ex) {
				return ex.getMessage();
			}
		}
	}

	class InterruptHandler implements HTTPServerResponse {
		@Override
		public void receive(Optional<String> url, InputStream payload, OutputStream answer) {
			try {
				if (!url.isPresent()) {
					throw new RuntimeException("Put data needs to specify URL with fieldname and execution handle");
				}
				Streams.InReadString(payload);
				Matcher matcher = INTERRUPT_URL_PATTERN.matcher(url.get());
				if(!matcher.matches()){
					// pattern: /interrupt/(?<executionId>\\w+)
					throw new RuntimeException("URL syntax error: " + url.get());
				}
				String executionId = matcher.group("executionId");
				basis.interrupt(executionId);
				answer.close();
			} catch (Exception ex) {
				Streams.OutWriteString(answer, ex.getMessage(), true);
			}
		}
	}

}