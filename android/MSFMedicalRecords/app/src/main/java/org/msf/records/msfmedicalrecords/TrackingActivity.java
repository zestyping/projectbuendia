package org.msf.records.msfmedicalrecords;

import android.app.Activity;
import com.estimote.sdk.Region;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Utils;

import java.util.List;


/**
 * Created by Gil on 01/10/2014.
 */
public class TrackingActivity extends Activity implements BeaconManager.RangingListener {

    private static final String TAG = TrackingActivity.class.getSimpleName();

    private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", ESTIMOTE_PROXIMITY_UUID, null, null);

    private BeaconManager beaconManager = new BeaconManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beaconManager.setRangingListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        try {
            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
        } catch (RemoteException e) {
            Log.e(TAG, "Cannot stop but it does not matter now", e);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.disconnect();
    }

    @Override
    public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
        //Remember this isn't called in the UI thread
        for (Beacon beacon : beacons){
            double distance = Utils.computeAccuracy(beacon);
            if (distance >= 0 && distance <= 0.5){
                Toast.makeText(this, "Beacon in range: " + distance + "m ", Toast.LENGTH_SHORT);
            }
        }
    }
}
