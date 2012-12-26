package sk.benko.appsresource.client.model;

/**
 * An observer interface that provides callbacks useful for giving the user
 * feedback about calls to the server.
 */
public interface StatusObserver {
  /**
   * Invoked when RPC calls begin to succeed again after a failure was
   * reported.
   */
  void onServerCameBack();

  /**
   * Invoked when RPC calls to the server are failing.
   */
  void onServerWentAway();

  /**
   * Invoked when current task has finished. This is often used to stop
   * displaying status Ui that was made visible in the
   * {@link StatusObserver#onTaskStarted(String)} callback.
   */
  void onTaskFinished();

  /**
   * Invoked when a task that requires user feedback starts.
   *
   * @param description
   *          a description of the task that is starting
   */
  void onTaskStarted(String description);
}
