package suhailmehta.main.locationtest.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by suhailmehta on 24/03/15.
 */
public class LocationFinder {

	Timer timer1;
	LocationManager lm;
	LocationResult locationResult;
	boolean gps_enabled = false;
	boolean network_enabled = false;

	public boolean getLocation(Context context, LocationResult result) {
		// I use LocationResult callback class to pass location value from
		// MyLocation to user code.
		locationResult = result;
		if (lm == null)
			lm = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);

		// exceptions will be thrown if provider is not permitted.
		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
            ex.printStackTrace();
		}
		try {
			network_enabled = lm
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
            ex.printStackTrace();
		}

		// don't start listeners if no provider is enabled
		if (!gps_enabled && !network_enabled) {
			return false;
		} else if (gps_enabled) {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
					locationListenerGps);
			timer1 = new Timer();
			timer1.schedule(new GetLastLocation(), 0);
			return true;
		}else if (network_enabled) {
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
					locationListenerNetwork);
			timer1 = new Timer();
			timer1.schedule(new GetLastLocation(), 0);
			return true;
		} else {
			return false;
		}

	}

	LocationListener locationListenerGps = new LocationListener() {
		public void onLocationChanged(Location location) {
			timer1.cancel();
			locationResult.gotLocation(location.getLatitude(),
					location.getLongitude());
			lm.removeUpdates(this);
			lm.removeUpdates(locationListenerNetwork);
		}

		public void onProviderDisabled(String provider) {

		}

		public void onProviderEnabled(String provider) {

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

		}
	};

	LocationListener locationListenerNetwork = new LocationListener() {
		public void onLocationChanged(Location location) {
			try {
				timer1.cancel();
				locationResult.gotLocation(location.getLatitude(),
						location.getLongitude());
				lm.removeUpdates(this);
				lm.removeUpdates(locationListenerGps);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

	};

	class GetLastLocation extends TimerTask {
		@Override
		public void run() {
			lm.removeUpdates(locationListenerGps);
			lm.removeUpdates(locationListenerNetwork);

			Location net_loc = null, gps_loc = null;
			if (gps_enabled) {
				gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			}
			if (network_enabled) {
				net_loc = lm
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}

			// if there are both values use the latest one
			if (gps_loc != null && net_loc != null) {
				if (gps_loc.getTime() > net_loc.getTime()) {
					locationResult.gotLocation(gps_loc.getLatitude(),
							gps_loc.getLongitude());
				} else {
					locationResult.gotLocation(net_loc.getLatitude(),
							net_loc.getLongitude());
				}
				return;
			}

			if (gps_loc != null) {
				locationResult.gotLocation(gps_loc.getLatitude(),
						gps_loc.getLongitude());
				return;
			}
			if (net_loc != null) {
				locationResult.gotLocation(net_loc.getLatitude(),
						net_loc.getLongitude());
				return;
			}

			locationResult.gotLocation(0, 0);
		}
	}

	public interface LocationResult {
		void gotLocation(double latitude, double longitude);
	}

}
