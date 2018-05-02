package de.upb.sede.procedure;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.upb.sede.exec.ExecutionEnvironment;
import de.upb.sede.exec.SEDEObject;
import de.upb.sede.exec.Task;

public class InstructionProcedure implements Procedure {
	static Logger logger = LogManager.getLogger(InstructionProcedure.class);

	@Override
	public void process(Task task) {
		ExecutionEnvironment environment = task.getExecution().getExecutionEnvironment();
		InstructionNodeAttributes nodeAttributes = new InstructionNodeAttributes(task);
		try {
			Map<String, SEDEObject> parameterObjects = getParameterObjects(nodeAttributes.getParameters(), environment);
			Class<?>[] parameterClasses = getParameterClasses(parameterObjects);

			Object[] parameterValues = getParameterValues(parameterObjects);
			Class<?> contextClass = addClassInClassLoader(nodeAttributes.getContext());
			if (nodeAttributes.isConstructor()) {
				Constructor<?> constructor = contextClass.getConstructor(parameterClasses);
				Object newInstance = constructor.newInstance(parameterValues);
				SEDEObject newInstanceSEDEObject = new SEDEObject(nodeAttributes.context, newInstance);
				environment.put(nodeAttributes.leftsidefieldname, newInstanceSEDEObject);
			} else {
				SEDEObject environmentInstance = environment.get(nodeAttributes.leftsidefieldname);
				Class<?> typeClass = Class.forName(environmentInstance.getType());
				Method calledMethod = typeClass.getMethod(nodeAttributes.method, parameterClasses);
				Object returnValue = calledMethod.invoke(environmentInstance.getObject(), parameterValues);
				String returnType = calledMethod.getReturnType().getName();
				SEDEObject returnAsSEDEObject = new SEDEObject(returnType, returnValue);
				environment.put(nodeAttributes.getLeftsidefieldname(), returnAsSEDEObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Map<String, SEDEObject> getParameterObjects(List<String> parameters, ExecutionEnvironment environment) {
		Map<String, SEDEObject> result = new LinkedHashMap<>(parameters.size());
		parameters.forEach(varName -> result.put(varName, environment.get(varName)));
		return result;
	}

	private Class<?>[] getParameterClasses(Map<String, SEDEObject> parameterObjects) throws ClassNotFoundException {
		List<Class<?>> inOrderClasses = new ArrayList<>(parameterObjects.size());
		for (SEDEObject sedeObject : parameterObjects.values()) {
			String classType = sedeObject.getType();
			Class<?> clazz = Class.forName(classType);
			inOrderClasses.add(clazz);
		}
		Class<?>[] inOrderClassesArray = new Class<?>[inOrderClasses.size()];
		inOrderClassesArray = inOrderClasses.toArray(inOrderClassesArray);
		return inOrderClassesArray;
	}

	private Object[] getParameterValues(Map<String, SEDEObject> parameterObjects) {
		List<Object> inOrderObjects = new ArrayList<>(parameterObjects.size());
		for (SEDEObject sedeObject : parameterObjects.values()) {
			inOrderObjects.add(sedeObject.getObject());
		}
		Object[] parameterArray = new Object[inOrderObjects.size()];
		parameterArray = inOrderObjects.toArray(parameterArray);
		return parameterArray;
	}

	private Class<?> addClassInClassLoader(String context) throws Exception {
		URL serviceFilesURL = new URL("jar", "", context + "!/");
		JarURLConnection jarURLConnection = (JarURLConnection) serviceFilesURL.openConnection();
		Attributes attr = jarURLConnection.getMainAttributes();
		if (attr != null) {
			logger.info("Servicefile: " + context + "\n Main class:" + attr.getValue(Attributes.Name.MAIN_CLASS));
		} else {
			logger.error("No entry point in: \"" + context + "\"");
		}
		URLClassLoader urlClassLoader = new URLClassLoader(new URL[] { serviceFilesURL },
				this.getClass().getClassLoader());
		Class<?> reflectedClass = Class.forName(context, true, urlClassLoader);
		addURLToClassLoader(serviceFilesURL);
		return reflectedClass;
	}

	private static void addURLToClassLoader(URL url) throws Exception {
		ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
		if (systemClassLoader instanceof URLClassLoader) {
			URLClassLoader urlClassLoader = (URLClassLoader) systemClassLoader;
			// addURL is a protected method, but we can use reflection to call it
			Method addURLMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			addURLMethod.setAccessible(true);
			addURLMethod.invoke(urlClassLoader, url);
		} else {
			logger.error("System ClassLoader is no URLClassLoader.");
		}
	}

	class InstructionNodeAttributes {
		private final boolean isConstructor;
		private final boolean isContextAFieldname;
		private final String method;
		private final String leftsidefieldname;
		private final String host;
		private final String context;
		private final String fmInstruction;
		private final List<String> parameters;

		@SuppressWarnings("unchecked")
		public InstructionNodeAttributes(Task task) {
			Map<String, Object> parameters = task.getAttributes();
			this.isConstructor = (boolean) parameters.get("is-service-construction");
			this.isContextAFieldname = (boolean) parameters.get("is-context-a-fieldname");
			this.method = (String) parameters.get("method");
			this.leftsidefieldname = (String) parameters.get("leftsidefieldname");
			this.host = (String) parameters.get("host");
			this.context = (String) parameters.get("context");
			this.fmInstruction = (String) parameters.get("fmInstruction");
			this.parameters = (List<String>) parameters.get("params");
		}

		public String getFmInstruction() {
			return fmInstruction;
		}

		public boolean isConstructor() {
			return isConstructor;
		}

		public boolean isContextAFieldname() {
			return isContextAFieldname;
		}

		public String getMethod() {
			return method;
		}

		public String getLeftsidefieldname() {
			return leftsidefieldname;
		}

		public String getHost() {
			return host;
		}

		public String getContext() {
			return context;
		}

		public List<String> getParameters() {
			return parameters;
		}

	}
}
