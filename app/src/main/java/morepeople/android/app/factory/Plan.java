package morepeople.android.app.factory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import morepeople.android.app.interfaces.ICallback;

/**
 * Created by schreon on 3/22/14.
 */
public class Plan {
    private List<ICallback> steps;
    private int index;
    private ICallback onFinish;

    public Plan(ICallback onFinish) {
       steps = new LinkedList<ICallback>();
        this.onFinish = onFinish;
    }

    public void start() {
        index = 0;
        steps.get(0).run();
    }

    public synchronized void next() {
        index += 1;
        if (index+1 > steps.size()) {
            onFinish.run();
        } else {
            steps.get(index).run();
        }
    }

    public void addStep(ICallback step) {
        steps.add(step);
    }
}
