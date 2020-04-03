package team.pawan.hackathon;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class TrackingService extends Service {

    private static final String TAG = TrackingService.class.getSimpleName();
    ArrayList<String> arrayList = new ArrayList<>();
    String end = "</Folder></Document></kml>\n";
    private int points = 0;
    public boolean isStop = false;
    public Date dateTime = new Date();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildNotification();
        requestLocationUpdates();
        String head = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n<Document>\n<Folder>\n";
        arrayList.add(head);
    }

    private void buildNotification() {
//        String stop = "stop";
//        registerReceiver(stopReceiver, new IntentFilter(stop));
//        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
//                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Tracking your location")
                .setOngoing(true)
//                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.mipmap.ic_launcher);
        startForeground(1, builder.build());
    }

//    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            unregisterReceiver(stopReceiver);
//            stopSelf();
//            Toast.makeText(context, "unregistered", Toast.LENGTH_SHORT).show();
//            arrayList.add(end);
//        }
//    };

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(10000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_GRANTED) {
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    Location currentLocation = locationResult.getLastLocation();
                    Log.d("Lat", String.valueOf(currentLocation.getLatitude()));
                    Log.d("Long", String.valueOf(currentLocation.getLongitude()));
                    KMLMaker km = new KMLMaker();
                    Date currentDate = new Date();
                    long diff = (currentDate.getTime() - dateTime.getTime()) / 1000;
                    Toast.makeText(TrackingService.this, String.valueOf(diff), Toast.LENGTH_SHORT).show();
                    arrayList.add(km.getPlacemark(points++,String.valueOf(diff), String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()), "0"));

                    PrintWriter writer = null;
                    try {
                        File file = new File("/storage/emulated/0/Code Knight/datapoints.txt");
                        writer = new PrintWriter(file);
                        if (file.createNewFile()) {
                            Toast.makeText(TrackingService.this, "Created", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    for (String line : arrayList) {
                        if (writer != null) {
                            writer.println(line);
                        }
                    }
                    if (writer != null) {
                        writer.close();
                    }
                }
            }, null);
        }
    }

    @Override
    public void onDestroy() {
        stopSelf();
        arrayList.add(end);
    }
}
