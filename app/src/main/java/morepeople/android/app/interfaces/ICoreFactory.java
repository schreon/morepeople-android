package morepeople.android.app.interfaces;

/**
 * Created by schreon on 3/22/14.
 */
public interface ICoreFactory {
    public void createCoreApi(ICallback onNoUserNameFound, ICallback onFinish, IErrorCallback onError);
}
