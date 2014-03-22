package morepeople.android.app.interfaces;

/**
 * Created by schreon on 3/20/14.
 */
public interface ICoreStateHandler {
    public ICoreAPI.UserState getCurrentState();

    public void transferToState(ICoreAPI.UserState newState);

    public void onStateChanged(ICoreAPI.UserState newState);
}
