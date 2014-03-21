package morepeople.android.app.morepeople.android.app.core;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import morepeople.android.app.CancelActivity;
import morepeople.android.app.ChatActivity;
import morepeople.android.app.ConfirmationActivity;
import morepeople.android.app.EvaluationActivity;
import morepeople.android.app.SearchActivity;

/**
 * Created by schreon on 3/20/14.
 */
public class CoreStateHandler implements ICoreStateHandler {
    private static final String TAG = "CoreStateHandler";

    private ICoreLogic.UserState currentState;
    private Context context;

    public CoreStateHandler(Context context, ICoreLogic.UserState currentState) {
        this.currentState = currentState;
        this.context = context;
    }

    @Override
    public ICoreLogic.UserState getCurrentState() {
        return currentState;
    }

    @Override
    public void transferToState(ICoreLogic.UserState newState) {
        if (newState != currentState) {
            onStateChanged(newState);
        }
        currentState = newState;
    }

    @Override
    public void onStateChanged(ICoreLogic.UserState newState) {
        Intent intent;
        switch (newState) {
            case OFFLINE:
                Log.d(TAG, "SearchActivity");
                intent = new Intent(context, SearchActivity.class);
                break;
            case QUEUED:
                Log.d(TAG, "launch SearchActivity");
                intent = new Intent(context, SearchActivity.class);
                break;
            case OPEN:
                Log.d(TAG, "launch ConfirmationActivity");
                intent = new Intent(context, ConfirmationActivity.class);
                break;
            case ACCEPTED:
                Log.d(TAG, "launch ConfirmationActivity");
                intent = new Intent(context, ConfirmationActivity.class);
                break;
            case RUNNING:
                Log.d(TAG, "launch ChatActivity");
                intent = new Intent(context, ChatActivity.class);
                break;
            case FINISHED:
                Log.d(TAG, "launch EvaluationActivity");
                intent = new Intent(context, EvaluationActivity.class);
                break;
            case CANCELLED:
                Log.d(TAG, "launch CancelActivity");
                intent = new Intent(context, CancelActivity.class);
                break;
            default:
                Log.d(TAG, "invalid state");
                intent = new Intent(context, CancelActivity.class);
                break;
        }
        // put the state into the intent
        intent.putExtra(ICoreLogic.PROPERTY_STATE, newState.toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
