package pl.polsl.marurb.geoLocApp.tools.gps;


import android.content.Context;
import android.location.LocationManager;

/**
 * Created by B3stia on 30.12.13.
 */
public class CheckGpsStatus{

    private Context c;

    public CheckGpsStatus(Context c) {
        this.c = c;

    }

    public boolean check(){

        final LocationManager manager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            //buildAlertMessageNoGps();
            return  true;
        }
        return false;
    }

}

  
