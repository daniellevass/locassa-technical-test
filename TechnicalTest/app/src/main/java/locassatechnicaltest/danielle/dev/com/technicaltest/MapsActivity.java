package locassatechnicaltest.danielle.dev.com.technicaltest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import cyd.awesome.material.AwesomeButton;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //google map
    private GoogleMap mMap;

    //interface variables
    private TextView lblTitle;
    private AwesomeButton btnSearch;
    private ViewGroup mapBackground;
    private boolean setToFirstFoundLocation;


    private ArrayList<MapMarkerItem> mapMarkerItemList;
    private static final String KEY_MAP_ITEMS = "MAP_ITEMS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //hook up the outlets
        lblTitle = (TextView)findViewById(R.id.lblLocation);
        btnSearch = (AwesomeButton)findViewById(R.id.btnSearch);
        mapBackground = (ViewGroup)findViewById(R.id.mapBackground);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        setToFirstFoundLocation = false;



        //search button pressed
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get new data for where the user is looking at
                getWeatherData(mMap.getCameraPosition().target);
            }
        });


        if (savedInstanceState != null){
            //rotated screen or changed something on screen so we already had map items
            mapMarkerItemList = savedInstanceState.getParcelableArrayList(KEY_MAP_ITEMS);
        } else {
            //no map items, make a new array list to save to
            mapMarkerItemList = new ArrayList<MapMarkerItem>();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //save our map items if we change screen orientation
        outState.putParcelableArrayList(KEY_MAP_ITEMS, mapMarkerItemList);
    }

    private void getWeatherData(LatLng coordinates){

        lblTitle.setText("Searching...");

        //rest adapter with the base yahoo api url
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://query.yahooapis.com/v1/public")
                .build();

        //set up a new weather service
        WeatherService service = restAdapter.create(WeatherService.class);


        //start to build the query with the provided coordinates
        StringBuilder sb = new StringBuilder();

        sb.append("select * from weather.forecast where woeid in (select woeid from geo.placefinder where text='");
        sb.append(coordinates.latitude);
        sb.append(",");
        sb.append(coordinates.longitude);
        sb.append("' and gflags='R') and u='c'");

            //send off the request
            service.getWeather(sb.toString(),
                    new Callback<WeatherResponse>() {
                        @Override
                        public void success(WeatherResponse weatherResponse, Response response) {

                            //get the current weather item
                            WeatherResponse.Item item = weatherResponse.getQuery().getResults().getChannel().getItem();

                            //set the title to a short bit of information
                            if (item.getShortDescription() != null) {
                                lblTitle.setText(item.getShortDescription());

                                //change the background colour depending on the temperature
                                mapBackground.setBackgroundColor(
                                        ContextCompat.getColor(
                                                MapsActivity.this,
                                                item.getCondition().getBackgroundColourBasedOnTemp()));

                                //add map marker item
                                MapMarkerItem marker = new MapMarkerItem(mMap.getCameraPosition().target,
                                        item.getCondition().getTemp(),
                                        item.getCondition().getBackgroundColourBasedOnTemp(),
                                        item.getShortDescription());
                                mapMarkerItemList.add(marker);

                                mMap.addMarker(new MarkerOptions()
                                        .position(marker.getCoordinates())
                                        .title(marker.getShortDescription())
                                        .icon(BitmapDescriptorFactory.fromBitmap(drawCircle(marker.getTemp(), marker.getColor())))
                                );

                            } else {
                                lblTitle.setText("Location not found in Yahoo's Weather Database");
                                mapBackground.setBackgroundColor(
                                        ContextCompat.getColor(MapsActivity.this,
                                                R.color.md_blue_grey_900));
                            }


                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d("DANIELLE", "something went wrong :( " + error.getLocalizedMessage());
                            //display to the user what went wrong so they can possibly fix it
                            lblTitle.setText(error.getLocalizedMessage());
                        }
                    });


    }//end function to get weather data


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);

        }


        //http://stackoverflow.com/a/26267862/2623314
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                if (!setToFirstFoundLocation) {
                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(11);
                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);

                    setToFirstFoundLocation = true;

                    getWeatherData(mMap.getCameraPosition().target);
                }


            }
        });

        //watch for when the user moves the map.
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }
        });//end on camera change listener


        //check if we need to display items from a screen orientation
        if (mapMarkerItemList.size() > 0){
            for (MapMarkerItem m : mapMarkerItemList){
                mMap.addMarker(new MarkerOptions()
                                .position(m.getCoordinates())
                                .title(m.getShortDescription())
                                .icon(BitmapDescriptorFactory.fromBitmap(drawCircle(m.getTemp(), m.getColor())))
                );
            }
        }


    }//end on map ready


    private Bitmap drawCircle(String number, int colour){

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(40, 40, conf);
        Canvas canvas = new Canvas(bmp);

        //draw a circle
        Paint color = new Paint();
        color.setColor(ContextCompat.getColor(
                MapsActivity.this, colour));
        canvas.drawCircle(20, 20, 20, color);

        //draw text
        color.setColor(Color.BLACK);



        color.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

            color.setTextSize(18);
            canvas.drawText(number, 10, 27, color);


        return bmp;
    }


} // end maps activity
