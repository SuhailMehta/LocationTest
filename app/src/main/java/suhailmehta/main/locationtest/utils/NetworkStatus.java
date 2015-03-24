package suhailmehta.main.locationtest.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by suhailmehta on 24/03/15.
 */
public class NetworkStatus {

    public static boolean isConnected(Context context)
    {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        NetworkInfo networkInfoGprs=null;

        if (connectivityManager != null) {

            networkInfo =
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            networkInfoGprs=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        }

        boolean b  = networkInfo.isConnected();
        boolean bGprs = false ;
        try{
            bGprs=networkInfoGprs.isConnected();
        }catch(Exception e)
        {
              	e.printStackTrace();
        }
        boolean result=(b|bGprs);

        return ((networkInfo == null)&& (networkInfoGprs==null)) ? false : result;
    }

}
