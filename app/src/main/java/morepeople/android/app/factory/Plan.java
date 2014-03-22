package morepeople.android.app.factory;

import java.util.LinkedList;
import java.util.List;

import morepeople.android.app.interfaces.ICallback;

/**
 * Created by schreon on 3/22/14.
 */
public class Plan {
    private List<ICallback> steps;
    private int index;

    public Plan() {
        steps = new LinkedList<ICallback>();
    }

    public void start() {
        index = 0;
        steps.get(0).run();
    }

    public synchronized void next() {
        index += 1;
        steps.get(index).run();
    }

    public void addStep(ICallback step) {
        steps.add(step);
    }
}
