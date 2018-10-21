package dev.vonabe.here.location;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * This class encapsulates the properties and functionality of the Map view.
 */
public class MapFragmentView implements ApplicationStatusListener, PositioningManager.OnPositionChangedListener{

    private MapFragment m_mapFragment;
    private Activity m_activity;
    private Map m_map;
    private PositioningManager positionManager = null;
    private boolean addListener = false, showMessage = false;

    public MapFragmentView(Activity activity) {
        m_activity = activity;

        View viewById = m_activity.findViewById(R.id.view_dragged);
        viewById.setOnTouchListener(new OnSwipeTouchListener(this.m_activity){
            @Override
            public void onSwipeRight() {
                m_activity.onBackPressed();
            }
        });
        initMapFragment();
    }

    // Google has deprecated android.app.Fragment class. It is used in current SDK implementation.
    // Will be fixed in future SDK version.
    @SuppressWarnings("deprecation")
    private MapFragment getMapFragment() {
        return (MapFragment) m_activity.getFragmentManager().findFragmentById(R.id.mapfragment);
    }

    private void initMapFragment() {
        /* Locate the mapFragment UI element */
        m_mapFragment = getMapFragment();

        // Set path of isolated disk cache
        String diskCacheRoot = m_activity.getApplicationContext().getExternalFilesDir(null)
                + File.separator + ".here-maps";
        // Retrieve intent name from manifest
        String intentName = "";
        try {
            ApplicationInfo ai = m_activity.getPackageManager().getApplicationInfo(m_activity.getPackageName(),
                    PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            intentName = bundle.getString("INTENT_NAME");
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(m_activity,
                    this.getClass().toString()+ ": Failed to find intent name, NameNotFound: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        boolean success = com.here.android.mpa.common.MapSettings.setIsolatedDiskCacheRootPath(diskCacheRoot, intentName);
        if (!success) {
            // Setting the isolated disk cache was not successful, please check if the path is valid and
            // ensure that it does not match the default location
            // (getExternalStorageDirectory()/.here-maps).
            // Also, ensure the provided intent name does not match the default intent name.
            Toast.makeText(m_activity.getApplicationContext(), "Setting the isolated disk cache was not successful.", Toast.LENGTH_SHORT).show();
        } else {
            /* Initialize the MapFragment, results will be given via the called back. */
            if (m_mapFragment != null) m_mapFragment.init(new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(Error error) {

                    if (error == Error.NONE) {
                        /*
                         * If no error returned from map fragment initialization, the map will be
                         * rendered on screen at this moment.Further actions on map can be provided
                         * by calling Map APIs.
                         */
                        m_map = m_mapFragment.getMap();
                        positionManager = PositioningManager.getInstance();
                        Image img = new Image();
                        try {
                            img.setImageResource(R.drawable.peka);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        m_map.getPositionIndicator().setMarker(img);
                        m_mapFragment.getPositionIndicator().setVisible(true);

                        /*
                         * Map center can be set to a desired location at this point.
                         * It also can be set to the current location ,which needs to be delivered by the PositioningManager.
                         * Please refer to the user guide for how to get the real-time location.
                         */
                        m_map.setCenter(new GeoCoordinate(49.258576, -123.008268), Map.Animation.LINEAR);
                    } else {
                        Toast.makeText(m_activity,
                                "ERROR: Cannot initialize Map with error " + error,
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public void pause() {
        if (positionManager != null && positionManager.isActive()){
            positionManager.stop();
        }
    }

    @Override
    public void target() {
        if(positionManager==null)return;
        if(!addListener) {
            m_mapFragment.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    Toast.makeText(m_activity, "touch", Toast.LENGTH_SHORT);
                    return false;
                }
            });

            positionManager.addListener(new WeakReference<PositioningManager.OnPositionChangedListener>(this));
            addListener = true;
        }
        positionManager.start(PositioningManager.LocationMethod.GPS_NETWORK);
    }

    @Override
    public void destroy() {
        if(positionManager!=null) {
            positionManager.stop();
            if (addListener) positionManager.removeListener(this);
        }
    }

    @Override
    public boolean isActive() {
        return (positionManager!=null)?positionManager.isActive():false;
    }

    @Override
    public void onPositionUpdated(PositioningManager.LocationMethod locationMethod, GeoPosition geoPosition, boolean b) {
        if(positionManager!=null && positionManager.isActive()){
            m_map.setCenter(geoPosition.getCoordinate(), Map.Animation.LINEAR);
            if(!showMessage){
                Snackbar.make(m_mapFragment.getView(), "Ожидайте, за вами уже выехали!", Snackbar.LENGTH_INDEFINITE).show();
                showMessage = true;
            }
        }
    }

    @Override
    public void onPositionFixChanged(PositioningManager.LocationMethod locationMethod, PositioningManager.LocationStatus locationStatus) {

    }

}