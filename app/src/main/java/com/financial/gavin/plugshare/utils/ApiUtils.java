package com.financial.gavin.plugshare.utils;

import com.financial.gavin.plugshare.retrofit.ApiEndpointInterface;
import com.financial.gavin.plugshare.retrofit.RetrofitClient;

/**
 * Created by gavin on 3/11/18.
 */

public class ApiUtils {
	
	public static final String BASE_URL = "https://staging.plugshare.com/api/locations/";
	
	public static ApiEndpointInterface getEndpointInterface() {
		return RetrofitClient.getClient(BASE_URL).create(ApiEndpointInterface.class);
	}
}
