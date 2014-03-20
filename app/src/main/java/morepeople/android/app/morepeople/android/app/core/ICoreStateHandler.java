package morepeople.android.app.morepeople.android.app.core;

/**
 * Created by schreon on 3/20/14.
 */
public interface ICoreStateHandler {
    public ICoreLogic.UserState getCurrentState();
    public void transferToState(ICoreLogic.UserState newState);
    public void onStateChanged(ICoreLogic.UserState newState);
}
