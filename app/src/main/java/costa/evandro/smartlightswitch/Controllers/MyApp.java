package costa.evandro.smartlightswitch.Controllers;
import android.app.Application;
import android.content.Context;

/**
 * Created by Evandro Costa on 15/07/2017.
 */

public class MyApp extends Application {

    private static Context mContext;
    @Override
    public void onCreate() {
        mContext = getApplicationContext();
        super.onCreate();
    }
    public static Context getmContext(){
        return mContext;
    }
}
