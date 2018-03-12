package com.financial.gavin.plugshare;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.financial.gavin.plugshare.model.ChargingLocation;
import com.financial.gavin.plugshare.retrofit.ApiEndpointInterface;
import com.financial.gavin.plugshare.utils.ApiUtils;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PlugShareAdapter.ItemListener {
	
	private RecyclerView mRecyclerView;
	private ApiEndpointInterface mService;
	private PlugShareAdapter mAdapter;
	private FloatingActionButton mNext, mPrevious;
	private FusedLocationProviderClient mFusedLocationClient;
	private double mLatitude, mLongitude;
	private static final String FILE_NAME = "ChargingLocation";
	private int mPageNumber = 1;
	private static final int REQUEST_CHECK_SETTINGS = 100;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		locationRequestInitialWork();
		mService = ApiUtils.getEndpointInterface();
		mRecyclerView = findViewById(R.id.recycler_view);
		mNext = findViewById(R.id.next);
		mPrevious = findViewById(R.id.previous);
		mPrevious.setOnClickListener(this);
		mNext.setOnClickListener(this);
		mAdapter = new PlugShareAdapter(this, new ChargingLocation[0], this);
		mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
		RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
		mRecyclerView.setLayoutManager(layoutManager);
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.addItemDecoration(itemDecoration);
	}
	
	public void loadChargingLocations(Map<String, Object> params) {
		mService.getChargingLocations(params).enqueue(new Callback<ChargingLocation[]>() {
			@Override
			public void onResponse(@NonNull Call<ChargingLocation[]> call, @NonNull Response<ChargingLocation[]> response) {
				
				if (response.isSuccessful()) {
					mAdapter.updateChargingLocations(response.body());
				} else {
					//TODO: check status code
				}
			}
			
			@Override
			public void onFailure(@NonNull Call<ChargingLocation[]> call, @NonNull Throwable t) {
				//TODO: error handling
			}
		});
	}
	
	private void startDetailActivity(ChargingLocation chargingLocation) {
		Intent intent = new Intent(this, DetailActivity.class);
		intent.putExtra(FILE_NAME, chargingLocation);
		startActivity(intent);
	}
	
	private Map<String, Object> constructParams(int pageNumber, double latitude, double longitude) {
		Map<String, Object> params = new HashMap<>();
		params.put("latitude", latitude);
		params.put("longitude", longitude);
		params.put("include_extras", true);
		params.put("count", 15);
		params.put("page", pageNumber);
		return params;
	}
	
	@Override
	public void onClick(View view) {
		int viewId = view.getId();
		switch (viewId) {
			case R.id.previous:
				previousClicked();
				break;
			case R.id.next:
				nextClicked();
				break;
			default:
				break;
		}
	}
	
	@Override
	public void onItemClick(ChargingLocation chargingLocation) {
		startDetailActivity(chargingLocation);
	}
	
	private void requestLocationPermission() {
		Dexter.withActivity(this)
				.withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
				.withListener(new PermissionListener() {
					@Override
					public void onPermissionGranted(PermissionGrantedResponse response) {
						getCurrentLocation();
					}
					
					@Override
					public void onPermissionDenied(PermissionDeniedResponse response) {
						if (response.isPermanentlyDenied()) {
							//TODO: Show an alert dialog to direct users to the detail setting to turn on location
						}
					}
					
					@Override
					public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
						token.continuePermissionRequest();
					}
				}).check();
	}
	
	@SuppressLint("MissingPermission")
	private void getCurrentLocation() {
		mFusedLocationClient.getLastLocation()
				.addOnSuccessListener(this, new OnSuccessListener<Location>() {
					@Override
					public void onSuccess(Location location) {
						if (location != null) {
							mLatitude = location.getLatitude();
							mLongitude = location.getLongitude();
							loadChargingLocations(constructParams(mPageNumber, mLatitude, mLongitude));
						} else {
							getCurrentLocation();
						}
					}
				});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == RESULT_OK) {
			requestLocationPermission();
		}
	}
	
	private void locationRequestInitialWork() {
		LocationRequest mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(100);
		mLocationRequest.setFastestInterval(50);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		
		LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
				.addLocationRequest(mLocationRequest);
		SettingsClient client = LocationServices.getSettingsClient(this);
		Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
		
		task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
			@Override
			public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
				requestLocationPermission();
			}
		});
		
		task.addOnFailureListener(this, new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				if (e instanceof ResolvableApiException) {
					try {
						ResolvableApiException resolvable = (ResolvableApiException) e;
						resolvable.startResolutionForResult(MainActivity.this,
								REQUEST_CHECK_SETTINGS);
					} catch (IntentSender.SendIntentException sendEx) {
						//TODO: error handling
					}
				}
			}
		});
	}
	
	private void previousClicked() {
		if (mPageNumber > 0 && --mPageNumber > 0) {
			loadChargingLocations(constructParams(mPageNumber, mLatitude, mLongitude));
		} else {
			Toast.makeText(this, "This is the first page!", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void nextClicked() {
		if (mPageNumber == 0) {
			mPageNumber = 2;
		} else {
			++mPageNumber;
		}
		
		loadChargingLocations(constructParams(mPageNumber, mLatitude, mLongitude));
	}
}
