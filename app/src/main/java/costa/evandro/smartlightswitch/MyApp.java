package costa.evandro.smartlightswitch;

import android.app.Application;
import android.content.Context;

/**
 * Created by teste on 01/11/2017.
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
