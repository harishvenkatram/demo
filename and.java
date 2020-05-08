import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {
    TextView lat,longi,accu,time,cp,tp;
    long i,f,t;
    LocationManager lm;
    List<String> list;
    String[] per = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lat=findViewById(R.id.latitude);
        longi=findViewById(R.id.longitude);
        time=findViewById(R.id.time);
        accu=findViewById(R.id.accuracy);
        cp=findViewById(R.id.cp);
        tp=findViewById(R.id.tp);
        lm = (LocationManager)getSystemService(LOCATION_SERVICE);
        checkPermissions();
    }

    @Override
    protected void onResume() {
        StringBuffer sb = new StringBuffer();
        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_COARSE);
        list = lm.getProviders(c,true);  /// gps,network
        if(list.isEmpty()){
            tp.setText("No Providers Available");
        }
        else{
            for(String x:list){
                sb.append(x).append(",");
                lm.requestSingleUpdate(x,MainActivity.this,null);
            }
            tp.setText("Total Providers -"+sb);
        }
        i = SystemClock.uptimeMillis();
        super.onResume();
    }

    @Override
    public void onLocationChanged(Location location) {
        lat.setText("Latitude - "+location.getLatitude());
        longi.setText("Longitude - "+location.getLongitude());
        accu.setText("Accuracy - "+location.getAccuracy());
        cp.setText("Current Provider - "+location.getProvider());
        f = SystemClock.uptimeMillis();
        t = (f-i)/1000;  /// convert to secs
        time.setText("Time "+t+" Secs");
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : per) {
            result = ContextCompat.checkSelfPermission(MainActivity.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(MainActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
            }
            return;
        }
    }
}
