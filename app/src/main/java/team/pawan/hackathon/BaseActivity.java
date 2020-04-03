package team.pawan.hackathon;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public abstract class BaseActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    protected int getLayoutId() {
        return R.layout.map;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap != null) {
            return;
        }
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    /**
     * Run the demo-specific code.
     */
    protected abstract void startOverlay(GoogleMap googleMap);

    protected GoogleMap getMap() {
        setUpMapIfNeeded();
        return mMap;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        startOverlay(googleMap);
    }
}