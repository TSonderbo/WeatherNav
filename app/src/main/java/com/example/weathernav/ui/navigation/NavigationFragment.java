package com.example.weathernav.ui.navigation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.weathernav.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.example.weathernav.util.Constants.MY_PERMISSIONS_REQUEST_LOCATION;

public class NavigationFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener
{
    private NavigationViewModel mViewModel;
    //Navigation
    private MapView mMapView;
    private GoogleMap mMap;
    private FusedLocationProviderClient mLocationProvider;
    private static List<Marker> mMarkerList = new ArrayList<>();
    //Directions
    private GeoApiContext mGeoApi = null;
    private Polyline mPolyline;
    private DirectionsRoute mRoute;
    //Other
    private static boolean routeSaved = false;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_navigation, container, false);

        //Maps stuff
        mMapView = (MapView) root.findViewById(R.id.map);
        initializeMap(savedInstanceState);
        mLocationProvider = LocationServices.getFusedLocationProviderClient(getActivity());

        //Buttons
        Button navigate = root.findViewById(R.id.button_navigate);
        Button clear = root.findViewById(R.id.button_clear);
        Button saveRoute = root.findViewById(R.id.button_saveRoute);

        //Buttons on click listeners
        navigate.setOnClickListener(this::navigate);
        clear.setOnClickListener(this::clear);
        saveRoute.setOnClickListener(this::saveRoute);

        //ViewModel
        mViewModel = new ViewModelProvider(this).get(NavigationViewModel.class);

        return root;
    }


    private void navigate(View view)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.alert_navigate)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.alert_positive_button_text), (dialogInterface, i) ->
                {
                    dialogInterface.dismiss();
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED)
                    {
                        mLocationProvider.getLastLocation().addOnSuccessListener(this::calculateDirections);
                    }
                    mViewModel.calculateWeatherForMarkers(mMarkerList);
                })
                .setNegativeButton(getString(R.string.alert_negative_button_text), (dialogInterface, i) ->
                {
                    dialogInterface.cancel();
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void clear(View view)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.alert_clear)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.alert_positive_button_text), (dialogInterface, i) ->
                {
                    dialogInterface.dismiss();
                    resetRoute();
                })
                .setNegativeButton(getString(R.string.alert_negative_button_text), (dialogInterface, i) ->
                {
                    dialogInterface.cancel();
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void saveRoute(View view)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.alert_save_route)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.alert_positive_button_text), (dialogInterface, i) ->
                {
                    dialogInterface.dismiss();
                    if(mRoute != null && !routeSaved)
                    {
                        mViewModel.saveRoute(mRoute);
                        routeSaved = true;
                        mRoute = null;
                        Context context = getContext();
                        String text = "Route saved";
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                    }
                    else if(routeSaved)
                    {
                        Context context = getContext();
                        String text = "Route already saved";
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Context context = getContext();
                        String text = "No active route";
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(getString(R.string.alert_negative_button_text), (dialogInterface, i) ->
                {
                    dialogInterface.cancel();
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void resetRoute()
    {
        for (Marker marker : mMarkerList)
        {
            Log.d("Reset","Removing marker");
            marker.remove();
        }
        if (mMarkerList != null)
            mMarkerList.clear();
        if (mPolyline != null)
            mPolyline.remove();
        mViewModel.clearWeatherForMarkers();
    }

    private void calculateDirections(Location currentLocation)
    {
        //Set destination to be final marker
        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                mMarkerList.get(mMarkerList.size() - 1).getPosition().latitude,
                mMarkerList.get(mMarkerList.size() - 1).getPosition().longitude
        );
        //Set remaining markers as waypoints
        DirectionsApiRequest.Waypoint[] waypoints = new DirectionsApiRequest.Waypoint[mMarkerList.size() - 1];
        for (int i = 0; i < waypoints.length; i++)
        {
            DirectionsApiRequest.Waypoint waypoint = new DirectionsApiRequest.Waypoint(
                    new com.google.maps.model.LatLng(
                            mMarkerList.get(i).getPosition().latitude,
                            mMarkerList.get(i).getPosition().longitude
                    )
            );
            waypoints[i] = waypoint;
        }
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApi);
        directions.origin(new com.google.maps.model.LatLng(
                currentLocation.getLatitude(),
                currentLocation.getLongitude()
        ));

        Log.d("CalculateDirections", "Destination is : " + destination.toString());

        if (waypoints.length > 0)
            directions.waypoints(waypoints);
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>()
        {
            @Override
            public void onResult(DirectionsResult result)
            {
                Log.d("CalculateDirections", "Calculate directions routes: " + result.routes[0].toString());
                addPolylinesToMap(result);
                mRoute = result.routes[0];
            }

            @Override
            public void onFailure(Throwable e)
            {
                Log.d("CalculateDirections", "Failed to calculate route");
                Log.d("CalculateDirections", e.toString());
            }
        });
    }

    //Method inspired by mitchtabian @ https://goo.gl/qaNR9L
    private void addPolylinesToMap(final DirectionsResult result)
    {
        //Run on main thread
        new Handler(Looper.getMainLooper()).post(() ->
        {
            Log.d("PolyLines", "run: result routes: " + result.routes.length);
            //Technically only ever runs once due to not having alternative routes..
            for (DirectionsRoute route : result.routes)
            {
                List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                List<LatLng> newDecodedPath = new ArrayList<>();

                // This loops through all the LatLng coordinates of ONE polyline.
                for (com.google.maps.model.LatLng latLng : decodedPath)
                {

                    Log.d("PolyLines", "run: latlng: " + latLng.toString());

                    newDecodedPath.add(new LatLng(
                            latLng.lat,
                            latLng.lng
                    ));
                }
                mPolyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                mPolyline.setColor(ContextCompat.getColor(getActivity(), R.color.teal_200));
            }
        });
    }

    private void initializeMap(Bundle savedInstanceState)
    {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null)
        {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);

        if (mGeoApi == null)
        {
            mGeoApi = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.maps_api_key))
                    .build();
        }
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState)
    {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null)
        {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        mMap = map;
        mMap.setOnMapLongClickListener(this);

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        {
            //Location Permission already granted
            mMap.setMyLocationEnabled(true);
            mLocationProvider.getLastLocation().addOnSuccessListener(location ->
            {
                if(location != null)
                {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
                }
            });

        } else
        {
            //Request Location Permission
            checkLocationPermission();
            //Check if permission was granted
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)
            {
                mMap.setMyLocationEnabled(true);
                mLocationProvider.getLastLocation().addOnSuccessListener(location ->
                {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
                });
            }
        }
        if (!mMarkerList.isEmpty())
        {
            List<Marker> markers = new ArrayList<>();
            for (int i = 0; i < mMarkerList.size(); i++)
            {
                Marker marker = mMap.addMarker(new MarkerOptions().position(mMarkerList.get(i).getPosition())
                        .title(mMarkerList.get(i).getPosition().toString())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                markers.add(marker);
            }
            mMarkerList = markers;
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)
            {
                mLocationProvider.getLastLocation().addOnSuccessListener(this::calculateDirections);
            }
        }

    }

    @Override
    public void onMapLongClick(LatLng latLng)
    {
        Log.d("Google Maps", "Map was long pressed at cord: " + latLng);
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(latLng.toString())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mMarkerList.add(marker);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onPause()
    {
        mMapView.onPause();

        super.onPause();

    }

    @Override
    public void onDestroy()
    {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void checkLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION))
            {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission to be able to navigate")
                        .setPositiveButton("OK", (dialogInterface, i) ->
                        {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .create()
                        .show();
            } else
            {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }
}
