package morepeople.android.app.interfaces;

import java.util.Map;

import morepeople.android.app.structures.UserState;

/**
 * Handles state transfers in terms of the morepeople protocol.
 */
public interface ICoreStateHandler {

    /**
     * @return the current state of the user.
     */
    public UserState getCurrentState();

    /**
     * Enter the new state. Listeners will be called.
     *
     * @param newState the new state to enter.
     * @param data     data returned by the server.
     */
    public void transferToState(UserState newState, Map<String, Object> data);

    /**
     * Add a listener to the "onEnter" event of the according state.
     *
     * @param observedState the state which will be observed.
     * @param onEnterState  will be called whenever the user enters this state.
     */
    public void addEnterStateListener(UserState observedState, IDataCallback onEnterState);

    /**
     * Remove a listener.
     *
     * @param observedState the state which had been observed.
     * @param onEnterState  the listener to remove.
     */
    public void removeEnterStateListener(UserState observedState, IDataCallback onEnterState);
}
