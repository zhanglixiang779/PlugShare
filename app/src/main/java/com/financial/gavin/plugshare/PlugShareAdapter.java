package com.financial.gavin.plugshare;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.financial.gavin.plugshare.model.ChargingLocation;

/**
 * Created by gavin on 3/11/18.
 */

public class PlugShareAdapter extends RecyclerView.Adapter<PlugShareAdapter.ViewHolder> {
	
	private ChargingLocation[] mChargingLocations;
	private Context mContext;
	private ItemListener mItemListener;
	
	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		
		TextView name;
		TextView score;
		TextView latitude;
		TextView longitude;
		ItemListener mItemListener;
		
		public ViewHolder(View itemView, ItemListener itemListener) {
			super(itemView);
			name = itemView.findViewById(R.id.name);
			score = itemView.findViewById(R.id.score);
			latitude = itemView.findViewById(R.id.latitude);
			longitude = itemView.findViewById(R.id.longitude);
			
			this.mItemListener = itemListener;
			itemView.setOnClickListener(this);
		}
		
		@Override
		public void onClick(View view) {
			ChargingLocation chargingLocation = getItem(getAdapterPosition());
			this.mItemListener.onItemClick(chargingLocation);
		}
	}
	
	public PlugShareAdapter(Context context, ChargingLocation[] chargingLocations, ItemListener itemListener) {
		mChargingLocations = chargingLocations;
		mContext = context;
		mItemListener = itemListener;
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		
		Context context = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);
		
		View view = inflater.inflate(R.layout.list_view, parent, false);
		
		return new ViewHolder(view, this.mItemListener);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		
		ChargingLocation chargingLocation = mChargingLocations[position];
		holder.name.setText(chargingLocation.getName());
		holder.score.setText(String.valueOf(chargingLocation.getScore()));
		holder.latitude.setText(String.valueOf(chargingLocation.getLatitude()));
		holder.longitude.setText(String.valueOf(chargingLocation.getLongitude()));
	}
	
	@Override
	public int getItemCount() {
		return mChargingLocations.length;
	}
	
	public void updateChargingLocations(ChargingLocation[] chargingLocations) {
		if (chargingLocations.length != 0) {
			mChargingLocations = chargingLocations;
			notifyDataSetChanged();
		} else {
			Toast.makeText(mContext, "There is no more charging location!", Toast.LENGTH_SHORT).show();
		}
	}
	
	private ChargingLocation getItem(int adapterPosition) {
		return mChargingLocations[adapterPosition];
	}
	
	public interface ItemListener {
		void onItemClick(ChargingLocation chargingLocation);
	}
}
