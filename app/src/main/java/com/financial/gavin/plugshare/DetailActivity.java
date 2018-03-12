package com.financial.gavin.plugshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.financial.gavin.plugshare.model.ChargingLocation;

public class DetailActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		TextView name = findViewById(R.id.name);
		TextView score = findViewById(R.id.score);
		TextView latitude = findViewById(R.id.latitude);
		TextView longitude = findViewById(R.id.longitude);
		ImageView image = findViewById(R.id.image);
		
		Intent intent = getIntent();
		ChargingLocation chargingLocation = intent.getParcelableExtra("ChargingLocation");
		
		name.setText(chargingLocation.getName());
		score.setText(String.valueOf(chargingLocation.getScore()));
		latitude.setText(String.valueOf(chargingLocation.getLatitude()));
		longitude.setText(String.valueOf(chargingLocation.getLongitude()));
		
		int num = chargingLocation.getPhotos().length;
		if (num != 0) {
			Glide.with(this).load(chargingLocation.getPhotos()[0].getUrl()).into(image);
		} else {
			Toast.makeText(this, "There is no photo for this station!", Toast.LENGTH_SHORT).show();
		}
	}
}
