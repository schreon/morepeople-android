package morepeople.android.app.interfaces;

/**
 * Callback for errors that occur during asynchronous processes.
 */
public interface IErrorCallback {
    /**
     * @param errorMessage the error message.
     */
    public void run(String errorMessage);
}
