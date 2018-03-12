package com.financial.gavin.plugshare.retrofit;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gavin on 3/11/18.
 */

public class RetrofitClient {
	
	private static Retrofit retrofit = null;
	private static final String USER_NAME = "code_eval2";
	private static final String PASS_WORD = "automaticmothrun";
	private static final String AUTH = "Authorization";
	
	public static Retrofit getClient(String baseUrl) {
		if (retrofit==null) {
			retrofit = new Retrofit.Builder()
					.baseUrl(baseUrl)
					.addConverterFactory(GsonConverterFactory.create())
					.client(getOkHttpClient())
					.build();
		}
		return retrofit;
	}
	
	private static OkHttpClient getOkHttpClient() {
		return new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
			@Override
			public okhttp3.Response intercept(Chain chain) throws IOException {
				Request originalRequest = chain.request();
				
				Request.Builder builder = originalRequest.newBuilder().header(AUTH,
						Credentials.basic(USER_NAME, PASS_WORD));
				
				Request newRequest = builder.build();
				return chain.proceed(newRequest);
			}
		}).build();
	}
}
