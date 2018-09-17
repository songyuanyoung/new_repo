package veoride.com.veoridedemo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.exceptions.InvalidLatLngBoundsException;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

import veoride.com.veoridedemo.R;

public class RouteSumaryActivity extends AppCompatActivity implements OnMapReadyCallback{
    private static final int CAMERA_ANIMATION_DURATION = 1000;
    private Button okButton;
    private DirectionsRoute directionsRoute;
    private TextView travledDistanceTextView;
    private MapView mapView;
    private MapboxMap mapboxMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_sumary);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        directionsRoute = (DirectionsRoute) bundle.getSerializable("currentRoute");
        travledDistanceTextView = (TextView) findViewById(R.id.distance_tv);

        travledDistanceTextView.setText(String.valueOf(directionsRoute.distance() + " feet"));
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.getMapAsync(this);
        okButton = (Button) findViewById(R.id.ok_btn);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * 0.8));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
    }
    private Polyline polyline;
    private void drawRoute(DirectionsRoute route) {
        List<LatLng> points = new ArrayList<>();
        List<Point> coords = LineString.fromPolyline(route.geometry(), Constants.PRECISION_6).coordinates();

        for (Point point : coords) {
            points.add(new LatLng(point.latitude(), point.longitude()));
        }

        if (!points.isEmpty()) {

            if (polyline != null) {
                mapboxMap.removePolyline(polyline);
            }

            // Draw polyline on map
            polyline = mapboxMap.addPolyline(new PolylineOptions()
                    .addAll(points)
                    .color(Color.parseColor("#4264fb"))
                    .width(5));
        }
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        drawRoute(directionsRoute);
        boundCameraToRoute();
    }

    @Override

    public void onResume() {

        super.onResume();

        mapView.onResume();

    }



    @Override

    protected void onStart() {

        super.onStart();

        mapView.onStart();

    }



    @Override

    protected void onStop() {

        super.onStop();

        mapView.onStop();

    }



    @Override

    public void onPause() {

        super.onPause();

        mapView.onPause();

    }



    @Override

    public void onLowMemory() {

        super.onLowMemory();

        mapView.onLowMemory();

    }



    @Override

    protected void onDestroy() {

        super.onDestroy();

        mapView.onDestroy();

    }



    @Override

    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        mapView.onSaveInstanceState(outState);

    }


    public void boundCameraToRoute() {
        if (directionsRoute != null) {
            List<Point> routeCoords = LineString.fromPolyline(directionsRoute.geometry(),
                    Constants.PRECISION_6).coordinates();
            List<LatLng> bboxPoints = new ArrayList<>();
            for (Point point : routeCoords) {
                bboxPoints.add(new LatLng(point.latitude(), point.longitude()));
            }
            if (bboxPoints.size() > 1) {
                try {
                    LatLngBounds bounds = new LatLngBounds.Builder().includes(bboxPoints).build();
                    // left, top, right, bottom
                    animateCameraBbox(bounds, CAMERA_ANIMATION_DURATION, new int[] {50, 100, 50, 100});
                } catch (InvalidLatLngBoundsException exception) {
                    Toast.makeText(this, R.string.error_valid_route_not_found, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void animateCameraBbox(LatLngBounds bounds, int animationTime, int[] padding) {
        CameraPosition position = mapboxMap.getCameraForLatLngBounds(bounds, padding);
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), animationTime);
    }
}
