package morepeople.android.app.core;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import morepeople.android.app.CancelActivity;
import morepeople.android.app.ChatActivity;
import morepeople.android.app.ConfirmationActivity;
import morepeople.android.app.EvaluationActivity;
import morepeople.android.app.SearchActivity;
import morepeople.android.app.interfaces.Constants;
import morepeople.android.app.interfaces.ICallback;
import morepeople.android.app.interfaces.ICoreStateHandler;
import morepeople.android.app.interfaces.UserState;

/**
 * Created by schreon on 3/20/14.
 */
public class CoreStateHandler implements ICoreStateHandler {
    private static final String TAG = "CoreStateHandler";

    private UserState currentState;

    // Map of onEnter callbacks
    private Map<UserState, List<ICallback>> callbacks;

    public CoreStateHandler(UserState currentState) {
        this.currentState = currentState;

        callbacks = new HashMap<UserState, List<ICallback>>();
        for (UserState state : UserState.values()) {
            callbacks.put(state, new ArrayList<ICallback>());
        }
    }

    @Override
    public UserState getCurrentState() {
        return currentState;
    }

    @Override
    public void transferToState(UserState newState) {
        if (newState != currentState) {
            for (ICallback callback : callbacks.get(newState)) {
                callback.run();
            }
        }
        currentState = newState;
    }

    @Override
    public void addEnterStateListener(UserState observedState, ICallback onEnterState) {
        callbacks.get(observedState).add(onEnterState);
    }

    @Override
    public void removeEnterStateListener(UserState observedState, ICallback onEnterState) {
        callbacks.get(observedState).remove(onEnterState);
    }
}
