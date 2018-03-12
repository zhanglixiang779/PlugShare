package com.financial.gavin.plugshare.retrofit;

import com.financial.gavin.plugshare.model.ChargingLocation;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by gavin on 3/11/18.
 */

public interface ApiEndpointInterface {
	
	@GET("nearby")
	Call<ChargingLocation[]> getChargingLocations(@QueryMap Map<String, Object> params);
}
