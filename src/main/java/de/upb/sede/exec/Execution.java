package de.upb.sede.exec;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import de.upb.sede.core.SEDEObject;
import de.upb.sede.util.Observable;
import de.upb.sede.util.Observer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents one execution.
 */
public class Execution {
	private static final Logger logger = LogManager.getLogger();

	private final ExecutionEnvironment environment;

	private final String execId;

	private final Observable<Execution> state;

	private final Observable<Task> newTask = new Observable<Task>();

	private final ExecutorConfiguration executorConfiguration;

	/**
	 * Flag that indicates that the execution has been interrupted.
	 */
	private boolean interrupted = false;

	/**
	 * Tasks that are resolved but haven't started processing yet.
	 */
	private final Set<Task> waitingTasks = new HashSet<>();

	/**
	 * Set of tasks that are waiting for an event to happen. E.g. waiting for input
	 * data.
	 */
	private final Set<Task> unfinishedTasks = new HashSet<>();

	/**
	 * Observes all tasks. Once an observed task changes its state to resolved, this
	 * observer will put it into the waiting-Tasks set.
	 */
	private final Observer<Task> unresolvedTasksObserver = Observer.lambda(Task::isWaiting, this::taskResolved);

	/**
	 * Observes all tasks. Once an observed task has started processing, it will be
	 * removed from the waiting-Tasks set.
	 */
	private final Observer<Task> waitingTasksObserver = Observer.lambda(Task::hasStarted, this::taskStarted);

	/**
	 * Observes all tasks. Once an observed tasks has finished (success or fail), it
	 * will be removed from the unfinished-Tasks set.
	 */
	private final Observer<Task> unfinishedTasksObserver = Observer.lambda(Task::hasFinished, this::taskFinished);

	/**
	 * An array of any observer of this class that needs to observe every task added
	 * to this execution object. See: addTask method.
	 */
	private final List<Observer<Task>> observersOfAllTasks = Arrays.asList(waitingTasksObserver,
			unresolvedTasksObserver, unfinishedTasksObserver);

	/**
	 * Default constructor.
	 * 
	 * @param execId
	 *            identifier of this execution.
	 */

	public Execution(String execId, ExecutorConfiguration executorConfiguration) {
		Objects.requireNonNull(execId);
		this.execId = execId;
		this.environment = new ExecutionInv();
		this.state = Observable.ofInstance(this);
		this.executorConfiguration = executorConfiguration;
	}

	/**t
	 * Returns the execution-id of this execution.
	 *
	 * @return the execution-id
	 */
	public String getExecutionId() {
		return execId;
	}

	/**
	 * Returns an observable instance of the execution state. This state updates
	 * whenever a task finishes.
	 *
	 * @return observable of the execution.
	 */
	public Observable<Execution> getState() {
		return state;
	}

	/**
	 * Returns the environment of this execution.
	 *
	 * @return the exeuction environment.
	 */
	public ExecutionEnvironment getEnvironment() {
		return environment;
	}

	/*
	 * private methods to allow synchronized access to the 2 set of tasks. Used by
	 * the observers.
	 */

	private final void taskResolved(Task task) {
		synchronized (this) {
			waitingTasks.add(task);
			newTask.update(task);
		}
		state.update(this);
	}

	private final void taskStarted(Task task) {
		synchronized (this) {
			waitingTasks.remove(task);
		}
		state.update(this);
	}

	private final void taskFinished(Task task) {
		synchronized (this) {
			unfinishedTasks.remove(task);
		}
		state.update(this);
	}

	/**
	 * Adds the given task to this execution.
	 * <p>
	 * If it wasn't added before, it will be added to the set of unfinished tasks
	 * and waiting tasks.
	 * <p>
	 * Additionally every observer in <tt>observersOfAllTasks</tt> will observe the
	 * given task.
	 *
	 * @param task
	 *            assigned to this execution.
	 */
	synchronized void addTask(Task task) {
		if (task.getExecution() != this) {
			throw new RuntimeException("Bug: Task states that it belongs to another execution.");
		}
		if (task.hasStarted()) {
			throw new RuntimeException("Bug: the given task was already started before.");
		}
		boolean newTask = unfinishedTasks.add(task);
		if (newTask) {
			waitingTasks.add(task);
			for (Observer<Task> taskObserver : observersOfAllTasks) {
				task.getState().observe(taskObserver);
			}
		}
	}

	/**
	 * Returns true if the set of all unfinished tasks is empty or the execution was
	 * interrupted. IF true, the set returned by getWaitingTasks is empty too.
	 *
	 * @return true if the execution has finished
	 */
	public synchronized boolean hasExecutionFinished() {
		return interrupted || unfinishedTasks.isEmpty();
	}

	/**
	 * Returns true if the execution was interrupted. IF true, the set returned by
	 * getWaitingTasks is empty too.
	 *
	 * @return true if the execution was interrupted
	 */
	public synchronized boolean hasExecutionBeenInterrupted() {
		return interrupted;
	}

	/**
	 * Returns true if the set of all unfinished tasks is empty. IF true, the set
	 * returned by getWaitingTasks is empty too.
	 *
	 * @return true all tasks of this execution is done.
	 */
	public synchronized boolean zeroTasksRemaining() {
		return unfinishedTasks.isEmpty();
	}

	/**
	 * Returns the set of tasks that are waiting to be executed at the time the
	 * query is made.
	 *
	 * @return set of waiting tasks
	 */
	synchronized Set<Task> getWaitingTasks() {
		if (hasExecutionFinished()) {
			return Collections.EMPTY_SET;
		} else {
			return Collections.unmodifiableSet(waitingTasks);
		}
	}

	Observable<Task> getNewTasksObservable() {
		return newTask;
	}

	synchronized void interrupt() {
		interrupted = true;
		state.update(this);
	}

	public ExecutorConfiguration getConfiguration() {
		return executorConfiguration;
	}

	static class ExecutionInv extends ConcurrentHashMap<String, SEDEObject> implements ExecutionEnvironment {
		final Observable<ExecutionEnvironment> state = Observable.ofInstance(this);
		@Override
		public SEDEObject put(String key, SEDEObject value) {
			SEDEObject prevValue = super.put(key, value);
			state.update(this);
			return prevValue;
		}


		@Override
		public Observable<ExecutionEnvironment> getState() {
			return state;
		}
	}

}