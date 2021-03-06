package de.upb.sede.exec;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.sede.util.Observable;
import de.upb.sede.util.Observer;

public final class Task implements Observer<Task>{

	private static final Logger logger = LoggerFactory.getLogger(Task.class);

	private final Execution execution;

	private final String taskName;

	private final Map<String, Object> attributes;

	private final Set<Task> dependencies = new HashSet<>();

	private Optional<Exception> error = Optional.empty();

	/*
	 * Flags which define the state of the task.
	 */
	private boolean resolved = false, 		// resolve: true if the dependencies are all resolved. (This value is set by the notification method)
					started = false,  		// started: true if a worker has started processing this task.
					doneRunning = false,	// doneRunning: true if started and the worker has finished processing.
					failed = false,			// failed: true an error has occured during execution.
					succeeded = false;		// setSucceeded: true if done and the worker successfully carried out the task.

	/**
	 * Flag that indicates that at least of the dependency has failed.
	 */
	private boolean dependecyHasFailed = false;

	/**
	 *	Observable state which will be updated when state flags (resolve, .., succeeded) change.
	 */
	private Observable<Task> taskState = Observable.ofInstance(this);


	{
		/* enable trace of task */
		if(logger.isTraceEnabled()) {
			final Observer<Task> logObserver = Observer.lambda(t -> true, t -> logger.debug("{}\n\tUPDATE: {}", t, t.getDescription()), t->false);
			taskState.observe(logObserver);
		}
	}


	public Task(Execution execution, String taskName, Map<String, Object> parameters) {
		this.execution = Objects.requireNonNull(execution);
		this.taskName = Objects.requireNonNull(taskName);
		this.attributes = Objects.requireNonNull(parameters);
	}

	public Execution getExecution() {
		return execution;
	}

	public String getTaskName() {
		return taskName;
	}

	public Map getAttributes() {
		return attributes;
	}

	public <T> T getAttribute(String attrName) {
		return (T) attributes.get(attrName);
	}

	public Observable<Task> getState(){
		return taskState;
	}

	public void addDependency(Task task) {
		this.dependencies.add(task);
		task.getState().observe(this);
	}

	/**
	 * @param task  task with updated state
	 * @return true if the3 task is done and the dependency contains this task.
	 */
	@Override
	public boolean notifyCondition(Task task) {
		return task.hasFinished() && this.dependencies.contains(task);
	}

	/**
	 * Notification is invoked when a dependency task has hasFinished (failed or Succeeded).
	 * @param task dependency task.
	 */
	@Override
	public void notification(Task task) {
		if(this.hasFinished()){
			/* this task may have failed already if another dependency of this task has failed. */
			return;
		}

		this.dependencies.remove(task);

		if(task.hasFailed()){
			dependecyHasFailed();
		}
		updateDependendency();

	}

	public void updateDependendency(){
		if(dependencies.isEmpty()){
			setResolved();
		}
	}

	@Override
	public boolean synchronizedNotification() {
		return true;
	}

	public boolean isResolved(){
		return resolved;
	}

	public boolean hasStarted() {
		return started;
	}

	public boolean isWaiting() {
		return isResolved() && !hasStarted();
	}

	public boolean isDoneRunning(){
		return doneRunning;
	}

	public boolean isRunning() {
		return hasStarted() && !isDoneRunning();
	}

	public boolean hasFailed(){
		return failed;
	}
	public boolean hasDependencyFailed() {
		return dependecyHasFailed;
	}

	public boolean hasSucceeded(){
		return succeeded;
	}

	/**
	 * @return true, if the task has hasFinished in failure or success.
	 */
	public boolean hasFinished() {
		return hasFailed() || hasSucceeded();
	}

	public boolean hasNotFinished() {
		return! hasFinished();
	}

	public void setResolved(){
		if(!isResolved()) {
			resolved = true;
			taskState.update(this);
		}
	}

	public synchronized void setStarted(){
		if(!hasStarted()) {
			resolved = true;
			started = true;
			taskState.update(this);
		}
	}

	public synchronized void setDone(){
		if(!isDoneRunning()) {
			resolved = true;
			started = true;
			doneRunning = true;
			taskState.update(this);
		}
	}

	public synchronized void setSucceeded(){
		if(!hasFinished()) {
			resolved = true;
			started = true;
			doneRunning = true;
			succeeded = true;
			taskState.update(this);
		}
	}

	public synchronized void setFailed(){
		if(!hasFinished()) {
			resolved = true;
			started = true;
			doneRunning = true;
			failed = true;
			taskState.update(this);
		}
	}

	private void dependecyHasFailed() {
		dependecyHasFailed = true;
	}

	public void setError(Exception ex) {
		error = Optional.of(ex);
	}

	public Optional<Exception> getError() {
		return error;
	}

	public final boolean equals(Object otherObject) {
		return super.equals(otherObject);
	}

	public final int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return "Task {" +
				"'" + taskName + '\'' +
				(resolved ? ", resolved " : "") +
				(started ? ", started "  : "") +
				(doneRunning ? ", doneRunning "   : "") +
				(failed ? ", failed " : "") +
				(succeeded ? ", succeeded "  : "") +
				//", \nattr:" + attributes.toString() +
				"}";
	}


	public String getDescription() {
		if(attributes == null) {
			return "attributes are null.";
		}
		return (String) attributes.get("description");
	}

}
