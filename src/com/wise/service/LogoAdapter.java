package com.wise.service;

import java.util.ArrayList;
import java.util.List;

import com.wise.data.CarData;
import com.wise.wawc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class LogoAdapter extends BaseAdapter {
	private Context context;
	private List<CarData> carDataList;
	private LayoutInflater layoutInflater;
	
	private List<View> viewList = new ArrayList<View>();
	public LogoAdapter(Context context,List<CarData> carDataList){
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
		this.carDataList = carDataList;
	}
	public int getCount() {
		return carDataList.size();
	}
	public Object getItem(int position) {
		return carDataList.get(position);
	}
	public long getItemId(int position) {
		return 0;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = layoutInflater.inflate(R.layout.logo_adapter, null);
		ViewHodler hodler = new ViewHodler();
		hodler.linearLayout = (LinearLayout) convertView.findViewById(R.id.image_layout);
		hodler.imageView = (ImageView) convertView.findViewById(R.id.image_view);
//		hodler.imageView.setBackgroundResource(carDataList.get(position).getCarLogo());
		hodler.imageView.setBackgroundResource(R.drawable.image);
		if(position == carDataList.size()){
			hodler.imageView = (ImageView) convertView.findViewById(R.id.image_view);
			hodler.imageView.setBackgroundResource(R.drawable.new_vehicle);
		}
		
		 if(carDataList.get(position).isCheck()){
			 hodler.linearLayout.setBackgroundResource(R.color.bkg1);
         }else{
        	 hodler.linearLayout.setBackgroundDrawable(null);
         }
		return convertView;
	}
	private class ViewHodler{
		LinearLayout linearLayout;
		ImageView imageView;
	}
	
	public void updataDatas(List<CarData> carDataList){
		this.carDataList = carDataList;
		this.notifyDataSetChanged();
	}
}
