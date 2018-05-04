package de.upb.sede.exec;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

import de.upb.sede.core.ServiceInstanceHandle;
import de.upb.sede.util.DefaultMap;
import de.upb.sede.util.Observer;
import de.upb.sede.webinterfaces.client.BasicClientRequest;
import de.upb.sede.webinterfaces.client.HTTPClientRequest;

public abstract class Execution implements Observer<Task> {

	private ExecutionEnvironment environment;
	private final String requestID;


	/**
	 * Tasks that are resolved but haven't started processing yet.
	 */
	private final Set<Task> openTasks = new HashSet<>();

	/**
	 * Set of tasks that are waiting for an event to happen. E.g. waiting for input data.
	 */
	private final Set<Task> unresultedTasks = new HashSet<>();

	public Execution(String requestID) {
		Objects.requireNonNull(requestID);
		this.requestID = requestID;
		this.environment = new ExecutionInv();
	}
	
	public ExecutionEnvironment getExecutionEnvironment() {
		return environment;
	}


	public ExecutionEnvironment getEnvironment() {
		return environment;
	}

	public void setEnvironment(ExecutionEnvironment environment) {
		this.environment = environment;
	}

	public String getRequestID() {
		return requestID;
	}

	public abstract BasicClientRequest createClientRequest(Object o);

	public abstract ServiceInstanceHandle createServiceInstanceHandle(Object serviceInstance);

	public void addTask(Task task) {
		task.getState().observe(this);
	}

	@Override
	public boolean notifyCondition(Task task) {
		if(task.isResolved() && !task.hasStarted()){
			return true;
		}
		if(task.isRunning() && openTasks.contains(task)) {
			return true;
		}
		if(task.isDoneRunning() && !task.resulted()){
			return true;
		}
		if(task.resulted() && unresultedTasks.contains(task)){
			return true;
		}
		return false;
	}

	@Override
	public void notification(Task task) {
		if(task.isResolved() && !task.hasStarted()){
			openTasks.add(task);
		}
		if(task.hasStarted() && openTasks.contains(task)) {
			openTasks.remove(task);
		}
		if(task.isDoneRunning() && !task.resulted()){
			unresultedTasks.add(task);
		}
		if(task.resulted() && unresultedTasks.contains(task)){
			unresultedTasks.remove(task);
		}
	}

	public boolean executionDone(){
		return openTasks.isEmpty() && unresultedTasks.isEmpty();
	}

	public synchronized Set<Task> getOpenTasks(){
		return Collections.unmodifiableSet(openTasks);
	}

	@Override
	public boolean removeAfterNotification(Task task) {
		return false;
	}

	@Override
	public boolean synchronizedNotification() {
		return true;
	}
	static class ExecutionInv extends ConcurrentHashMap<String, SEDEObject> implements ExecutionEnvironment {

	}

}