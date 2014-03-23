package morepeople.android.app.controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import morepeople.android.app.CancelActivity;
import morepeople.android.app.ChatActivity;
import morepeople.android.app.ConfirmationActivity;
import morepeople.android.app.EvaluationActivity;
import morepeople.android.app.SearchActivity;
import morepeople.android.app.interfaces.ICoreViewController;
import morepeople.android.app.interfaces.ICoreWritablePreferences;
import morepeople.android.app.structures.UserState;

/**
 * This controller handles view updates.
 * <p/>
 * Contract: This is the only Core class which has knowledge of the activity classes !!!
 */
public class CoreViewController implements ICoreViewController {
    private static final String TAG = "morepeople.android.app.controller.CoreViewController";
    private Context context;

    public CoreViewController(Context context) {
        this.context = context;
    }

    @Override
    public void handleUpdate(ICoreWritablePreferences preferences) {
        UserState userState = preferences.getCurrentUserState();
        Log.d(TAG, "current state: " + userState);

        Intent intent;
        switch (userState) {
            case OFFLINE:
                Log.d(TAG, "launch SearchActivity");
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
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);
    }
}
