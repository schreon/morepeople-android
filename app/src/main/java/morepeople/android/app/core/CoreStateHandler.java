package morepeople.android.app.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import morepeople.android.app.interfaces.ICallback;
import morepeople.android.app.interfaces.ICoreStateHandler;
import morepeople.android.app.interfaces.IDataCallback;
import morepeople.android.app.structures.UserState;

/**
 * Created by schreon on 3/20/14.
 */
public class CoreStateHandler implements ICoreStateHandler {
    private static final String TAG = "CoreStateHandler";

    private UserState currentState;

    // Map of onEnter callbacks
    private Map<UserState, List<IDataCallback>> callbacks;

    public CoreStateHandler(UserState currentState) {
        this.currentState = currentState;

        callbacks = new HashMap<UserState, List<IDataCallback>>();
        for (UserState state : UserState.values()) {
            callbacks.put(state, new ArrayList<IDataCallback>());
        }
    }

    @Override
    public UserState getCurrentState() {
        return currentState;
    }

    @Override
    public void transferToState(UserState newState, final Map<String, Object> data) {
        for (IDataCallback callback : callbacks.get(newState)) {
            callback.run(data);
        }
        currentState = newState;
    }

    @Override
    public void addEnterStateListener(UserState observedState, IDataCallback onEnterState) {
        callbacks.get(observedState).add(onEnterState);
    }

    @Override
    public void removeEnterStateListener(UserState observedState, IDataCallback onEnterState) {
        callbacks.get(observedState).remove(onEnterState);
    }
}
