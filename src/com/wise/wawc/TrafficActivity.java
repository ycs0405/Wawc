package com.wise.wawc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.wise.extend.CarAdapter;
import com.wise.list.XListView;
import com.wise.list.XListView.IXListViewListener;
import com.wise.pubclas.Constant;
import com.wise.pubclas.GetSystem;
import com.wise.pubclas.NetThread;
import com.wise.pubclas.Variable;
import com.wise.sql.DBExcute;
import com.wise.sql.DBHelper;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
/**
 * 车辆违章
 * @author honesty
 */
public class TrafficActivity extends Activity implements IXListViewListener{
    private static final String TAG = "TrafficActivity";
    private static final int refresh_traffic = 2;
    private static final int load_traffic = 3;
	
    HorizontalScrollView hsv_car;
    XListView lv_activity_traffic;
    RelativeLayout rl_Note;
    LinearLayout ll_info;
    DBExcute dbExcute = new DBExcute();
	CarAdapter carAdapter;
	List<TrafficData> trafficDatas = new ArrayList<TrafficData>();
	TrafficAdapter trafficAdapter;

    String Car_name = "";
	boolean isGetDB = true; //上拉是否继续读取数据库
    int Toal = 0; //从那条记录读起
    int pageSize = 10 ; //每次读取的记录数目
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traffic);
		rl_Note = (RelativeLayout)findViewById(R.id.rl_Note);
		ll_info = (LinearLayout)findViewById(R.id.ll_info);
		ImageView iv_activity_traffic_back = (ImageView)findViewById(R.id.iv_activity_traffic_back);
		iv_activity_traffic_back.setOnClickListener(onClickListener);
        lv_activity_traffic = (XListView)findViewById(R.id.lv_activity_traffic);
        lv_activity_traffic.setXListViewListener(this);
        trafficAdapter = new TrafficAdapter();
        lv_activity_traffic.setAdapter(trafficAdapter);
        
        hsv_car = (HorizontalScrollView)findViewById(R.id.hsv_car);
		GridView gv_activity_traffic = (GridView)findViewById(R.id.gv_activity_traffic);
        carAdapter = new CarAdapter(TrafficActivity.this,Variable.carDatas);
        gv_activity_traffic.setAdapter(carAdapter);
        
        int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
		LayoutParams params = new LayoutParams((Variable.carDatas.size() * (px + 10)+10),LayoutParams.WRAP_CONTENT);
		gv_activity_traffic.setLayoutParams(params);
		gv_activity_traffic.setColumnWidth(px);
		gv_activity_traffic.setHorizontalSpacing(10);
		gv_activity_traffic.setStretchMode(GridView.NO_STRETCH);
		gv_activity_traffic.setNumColumns(Variable.carDatas.size());
		gv_activity_traffic.setOnItemClickListener(onItemClickListener);	
		if (Variable.carDatas != null && Variable.carDatas.size() > 0) {
		    SharedPreferences preferences = getSharedPreferences(Constant.sharedPreferencesName, Context.MODE_PRIVATE);
	        int DefaultVehicleID = preferences.getInt(Constant.DefaultVehicleID, 0);
	        GetData(DefaultVehicleID);
            if(Variable.carDatas.size() == 1){
                hsv_car.setVisibility(View.GONE);
            }else{
                hsv_car.setVisibility(View.VISIBLE);
            }
        }
	}
	
	Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case refresh_traffic:
                trafficDatas.addAll(0, jsonTrafficData(msg.obj.toString()));
                trafficAdapter.notifyDataSetChanged();
                onLoad();
                if(trafficDatas.size() > 0){
                    isNothingNote(false);
                }
                break;
            case load_traffic:
                trafficDatas.addAll(jsonTrafficData(msg.obj.toString()));
                trafficAdapter.notifyDataSetChanged();
                onLoad();
                break;
            }
        }	    
	};
	OnClickListener onClickListener = new OnClickListener() {		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_activity_traffic_back:
				finish();
				break;
			}
		}
	};
	OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
		    GetData(arg2);
		}
	};
	private void GetData(int arg2){
	    for(int i = 0 ; i < Variable.carDatas.size() ; i++){
            Variable.carDatas.get(i).setCheck(false);
        }
        Variable.carDatas.get(arg2).setCheck(true);
        carAdapter.notifyDataSetChanged();
        trafficDatas.clear();
        Car_name = Variable.carDatas.get(arg2).getObj_name();
        Toal = 0;
        boolean isUrl = isGetDataUrl(Car_name);
        if(isUrl){
            isGetDB = false;
            //从服务器读取数据
            Log.d(TAG, "从服务器读取数据");
            try {
                String url = Constant.BaseUrl + "vehicle/" + URLEncoder.encode(Car_name, "UTF-8") + "/violation?auth_code=" + Variable.auth_code;
                new Thread(new NetThread.GetDataThread(handler, url, refresh_traffic)).start();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            isNothingNote(false);
            isGetDB = true;
            Log.d(TAG, "从本地读取数据");
            trafficDatas.addAll(getTrafficDatas(Toal, pageSize));
            trafficAdapter.notifyDataSetChanged();
        }
	}
	private void isNothingNote(boolean isNote){
	    if(isNote){
	        rl_Note.setVisibility(View.VISIBLE);
	        ll_info.setVisibility(View.GONE);
	    }else{
	        rl_Note.setVisibility(View.GONE);
	        ll_info.setVisibility(View.VISIBLE);
	    }
	}
	/**
	 * 判断本地是否有记录
	 * @param Car_name
	 * @return
	 */
	private boolean isGetDataUrl(String Car_name){
	    String sql = "select * from " + Constant.TB_Traffic + " where Car_name=?";
	    int Total = dbExcute.getTotalCount(getApplicationContext(), sql, new String[]{Car_name});
	    if(Total == 0){
	        return true;
	    }else{
	        return false;
	    }
	}
	/**
	 * 解析json数据
	 * @param result
	 */
	private List<TrafficData> jsonTrafficData(String result){
	    List<TrafficData> Datas = new ArrayList<TrafficData>();
	    try {
	        JSONObject jsonObject1 = new JSONObject(result);
	        JSONArray jsonArray = jsonObject1.getJSONArray("data");
            for(int i = 0 ; i < jsonArray.length() ; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String Time = jsonObject.getString("create_time").replace("T", " ").substring(0, 16);
                TrafficData trafficData = new TrafficData();
                trafficData.setObj_id(jsonObject.getString("vio_id"));
                trafficData.setAction(jsonObject.getString("action"));
                trafficData.setLocation(jsonObject.getString("location"));
                trafficData.setDate(Time);
                trafficData.setScore(jsonObject.getInt("score"));
                trafficData.setFine(jsonObject.getInt("fine"));
                Datas.add(trafficData);
                
                ContentValues values = new ContentValues();
                values.put("obj_id", Variable.cust_id);
                values.put("Car_name", jsonObject.getString("obj_name"));
                values.put("create_time", Time);
                values.put("action", trafficData.getAction());
                values.put("location", trafficData.getLocation());
                values.put("score", trafficData.getScore());
                values.put("fine", trafficData.getFine());
                dbExcute.InsertDB(TrafficActivity.this, values, Constant.TB_Traffic);                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	    return Datas;
	}
	
	private List<TrafficData> getTrafficDatas(int start,int pageSize) {
        System.out.println("start = " + start);
        List<TrafficData> datas = getPageDatas(TrafficActivity.this, "select * from " + Constant.TB_Traffic + " where Car_name=? order by obj_id desc limit ?,?", new String[]{Car_name,String.valueOf(start),String.valueOf(pageSize)});
        Toal += datas.size();//记录位置
        if(datas.size() == pageSize){
            //继续读取数据库
        }else{
            //数据库读取完毕
            isGetDB = false;
        }
        return datas;
    }
	
	public List<TrafficData> getPageDatas(Context context,String sql,String[] whereClause){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, whereClause);
        List<TrafficData> Datas = new ArrayList<TrafficData>();
        while(cursor.moveToNext()){
            TrafficData trafficData = new TrafficData();
            trafficData.setObj_id(cursor.getString(cursor.getColumnIndex("obj_id")));
            trafficData.setAction(cursor.getString(cursor.getColumnIndex("action")));
            trafficData.setLocation(cursor.getString(cursor.getColumnIndex("location")));
            trafficData.setDate(cursor.getString(cursor.getColumnIndex("create_time")));
            trafficData.setScore(cursor.getInt(cursor.getColumnIndex("score")));
            trafficData.setFine(cursor.getInt(cursor.getColumnIndex("fine")));
            Datas.add(trafficData);
        }
        cursor.close();
        db.close();
        return Datas;
    }
	
	private class TrafficAdapter extends BaseAdapter{
		LayoutInflater mInflater = LayoutInflater.from(TrafficActivity.this);
		@Override
		public int getCount() {
			return trafficDatas.size();
		}
		@Override
		public Object getItem(int position) {
			return trafficDatas.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_traffic, null);
				holder = new ViewHolder();
				holder.tv_item_traffic_data = (TextView) convertView.findViewById(R.id.tv_item_traffic_data);
				holder.tv_item_traffic_adress = (TextView)convertView.findViewById(R.id.tv_item_traffic_adress);
				holder.tv_item_traffic_content = (TextView)convertView.findViewById(R.id.tv_item_traffic_content);
				holder.tv_item_traffic_fraction = (TextView)convertView.findViewById(R.id.tv_item_traffic_fraction);
				holder.tv_item_traffic_money = (TextView)convertView.findViewById(R.id.tv_item_traffic_money);
				holder.iv_traffic_share = (ImageView)convertView.findViewById(R.id.iv_traffic_share);
				holder.iv_traffic_help = (ImageView)convertView.findViewById(R.id.iv_traffic_help);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final TrafficData trafficData = trafficDatas.get(position);
			holder.tv_item_traffic_data.setText(trafficData.getDate());
			holder.tv_item_traffic_adress.setText(trafficData.getLocation());
			holder.tv_item_traffic_content.setText(trafficData.getAction());
			holder.tv_item_traffic_fraction.setText("扣分："+trafficData.getScore());
			holder.tv_item_traffic_money.setText("罚款："+trafficData.getFine());
			holder.iv_traffic_share.setOnClickListener(new OnClickListener() {                
                @Override
                public void onClick(View v) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("【违章】");
                    sb.append(trafficData.getDate());
                    sb.append("," + trafficData.getLocation());
                    sb.append("," + trafficData.getAction());
                    sb.append("," + trafficData.getScore());
                    sb.append("," + trafficData.getFine());
                    GetSystem.share(TrafficActivity.this, sb.toString(), "",0,0);
                }
            });
			holder.iv_traffic_help.setOnClickListener(new OnClickListener() {                
                @Override
                public void onClick(View v) {
                    
                }
            });
			return convertView;
		}	
		private class ViewHolder {
			TextView tv_item_traffic_data,tv_item_traffic_adress,tv_item_traffic_content,
						tv_item_traffic_fraction,tv_item_traffic_money;
			ImageView iv_traffic_share,iv_traffic_help;
		}
	}
	
	private class TrafficData{
	    String obj_id;
		String date;
		String action;
		String location;
		int score;
		int fine;
		
        public String getObj_id() {
            return obj_id;
        }
        public void setObj_id(String obj_id) {
            this.obj_id = obj_id;
        }
        public String getDate() {
            return date;
        }
        public void setDate(String date) {
            this.date = date;
        }
        public String getAction() {
            return action;
        }
        public void setAction(String action) {
            this.action = action;
        }
        public String getLocation() {
            return location;
        }
        public void setLocation(String location) {
            this.location = location;
        }
        public int getScore() {
            return score;
        }
        public void setScore(int score) {
            this.score = score;
        }
        public int getFine() {
            return fine;
        }
        public void setFine(int fine) {
            this.fine = fine;
        }		
	}

    @Override
    public void onRefresh() {
        try {
            String url = Constant.BaseUrl + "vehicle/" + URLEncoder.encode(Car_name, "UTF-8") + "/violation?auth_code=" + Variable.auth_code + "&max_id=" + trafficDatas.get(0).getObj_id();
            new Thread(new NetThread.GetDataThread(handler, url, refresh_traffic)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
    @Override
    public void onLoadMore() {
        if(isGetDB){
            trafficDatas.addAll(getTrafficDatas(Toal, pageSize));
            trafficAdapter.notifyDataSetChanged();
        }else{
            try {
                String min_id = trafficDatas.get(trafficDatas.size() - 1).getObj_id();
                String url = Constant.BaseUrl + "vehicle/" + URLEncoder.encode(Car_name, "UTF-8") + "/violation?auth_code=" + Variable.auth_code + "&min_id=" + min_id;
                new Thread(new NetThread.GetDataThread(handler, url, load_traffic)).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }        
    }
    private void onLoad() {
        lv_activity_traffic.stopRefresh();
        lv_activity_traffic.stopLoadMore();
    }
}