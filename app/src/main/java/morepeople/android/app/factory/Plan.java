package morepeople.android.app.factory;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import morepeople.android.app.interfaces.ICallback;

/**
 * Created by schreon on 3/22/14.
 */
public class Plan {
    private static final String TAG = "morepeople.android.app.factory.Plan";
    private List<ICallback> steps;
    private int index;

    public Plan() {
        steps = new LinkedList<ICallback>();
    }

    public void start() {
        Log.d(TAG, "start");
        index = 0;
        steps.get(0).run();
    }

    public synchronized void next() {
        Log.d(TAG, "executing next factory step: " + index);
        index += 1;
        steps.get(index).run();
    }

    public void addStep(ICallback step) {
        Log.d(TAG, "add step");
        steps.add(step);
    }
}
