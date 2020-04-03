package team.pawan.hackathon;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.android.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

//import com.google.android.gms.location.LocationListener;

public class MapsActivity extends BaseActivity {

    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private LatLng mPlaceLatLong;

    protected int getLayoutId() {
        return R.layout.activity_kml;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    public void startOverlay(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            mPlaceLatLong = new LatLng(28.62, 77.21);
            CameraUpdate center = CameraUpdateFactory.newLatLng(mPlaceLatLong);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);

            mMap.moveCamera(center);
            mMap.animateCamera(zoom);
            retrieveFileFromResource();

        } catch (Exception e) {
            Log.e("Exception caught", e.toString());
        }
    }

    private void retrieveFileFromResource() {
        try {
            KmlLayer kmlLayer = new KmlLayer(mMap, R.raw.delhi_coordinates, getApplicationContext
                    ());
            kmlLayer.addLayerToMap();
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }

    }
}