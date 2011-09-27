package jp.moo.snsk;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.ClientProtocolException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ResultViewActivity extends Activity {
    /** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        final LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
    	
        final LocationListener ll = new LocationListener() {		
		@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
		       	latitude = String.valueOf(location.getLatitude());
		       	longtude = String.valueOf(location.getLongitude());
		       	lm.removeUpdates(this);
			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				Log.v(tag, "onStatusChanged");
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				Log.v(tag, "onProviderEnabled");
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				Log.v(tag, "onProviderDisabled");
			}
		};

    	lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, ll);
    	lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, ll);
		
        //sandbox activityから飛んできたIntentの受け取り
        Intent intent = getIntent();
        int headcount = intent.getIntExtra("HEADCOUNT", 0);
        int offed_headcount = intent.getIntExtra("OFFED_HEADCOUNT", 0);
        int answer = intent.getIntExtra("ANSWER", 0);
        int answer_mod = intent.getIntExtra("ANSWER_MOD", 0);
        int offed_answer = intent.getIntExtra("OFFED_ANSWER", -1);
        int shortage = intent.getIntExtra("SHORTAGE", 0);
        boolean ischecked = intent.getBooleanExtra("ISCHECKED", false);
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        layout.setBackgroundColor(Color.GRAY);
        layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_img_repeat));
        //layout.setBackgroundResource(R.drawable.back_img_repeat); //↑とどう違うの？
        setContentView(layout);

        if(ischecked != false){
        	
        	TextView textViewStaticText1 = new TextView(this);
	        textViewStaticText1.setText("割引対象の人(" + String.valueOf(offed_headcount) +"名)ひとりあたり");
	        textViewStaticText1.setTextSize(16.0f);
	        textViewStaticText1.setTextColor(Color.BLACK);
	        textViewStaticText1.setPadding(10, 10, 0, 10);
	        layout.addView(textViewStaticText1);
	        
	        TextView textViewforOnePerson = new TextView(this);
	        textViewforOnePerson.setText("￥" + String.valueOf(offed_answer));
	        textViewforOnePerson.setTextSize(32.0f);
	        textViewforOnePerson.setTextColor(Color.BLACK);
	        textViewforOnePerson.setGravity(Gravity.RIGHT);
	        layout.addView(textViewforOnePerson);

	        TextView textViewStaticText2 = new TextView(this);
	        textViewStaticText2.setText("割引されない人(" + String.valueOf(headcount) +"名)ひとりあたり");
	        textViewStaticText2.setTextSize(16.0f);
	        textViewStaticText2.setTextColor(Color.BLACK);
	        textViewStaticText2.setPadding(10, 10, 0, 10);
	        layout.addView(textViewStaticText2);
	        
	        TextView textViewforOnePerson2 = new TextView(this);
	        textViewforOnePerson2.setText("￥" + String.valueOf(answer));
	        textViewforOnePerson2.setTextSize(32.0f);
	        textViewforOnePerson2.setTextColor(Color.BLACK);
	        textViewforOnePerson2.setGravity(Gravity.RIGHT);
	        layout.addView(textViewforOnePerson2);

            TextView textViewShortAge = new TextView(this);
            textViewShortAge.setText("不足額");
            textViewShortAge.setTextSize(16.0f);
            textViewShortAge.setTextColor(Color.BLACK);
            textViewShortAge.setPadding(10, 10, 0, 10);
            layout.addView(textViewShortAge);
            
            TextView textViewShortAge2 = new TextView(this);
            textViewShortAge2.setText("￥" + String.valueOf(shortage));
            textViewShortAge2.setTextSize(32.0f);
            textViewShortAge2.setTextColor(Color.BLACK);
            textViewShortAge2.setGravity(Gravity.RIGHT);
            layout.addView(textViewShortAge2);

	    	Button secondPartySearch = new Button(this);
        	secondPartySearch.setOnClickListener(
        			new OnClickListener(){
            		public void onClick(View v){
            	    	lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, ll);
            	    	lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, ll);
            			sendQuery();
            		}
            	}
            );
	    	
	    	secondPartySearch.setText("二次会のお店を検索");
        	secondPartySearch.setTextSize(16.0f);
        	LinearLayout.LayoutParams layoutParams = 
                new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        	layoutParams.setMargins(0, 50, 0, 0);
        	secondPartySearch.setLayoutParams(layoutParams);
        	layout.addView(secondPartySearch);
                        
        }else{
    	
	        TextView textViewStaticText1 = new TextView(this);
	        textViewStaticText1.setText("ひとりあたり");
	        textViewStaticText1.setTextSize(16.0f);
	        textViewStaticText1.setTextColor(Color.BLACK);
	        textViewStaticText1.setPadding(10, 10, 0, 10);
	        layout.addView(textViewStaticText1);
	        
	        TextView textViewforOnePerson = new TextView(this);
	        textViewforOnePerson.setText("￥" + String.valueOf(answer)+" 余り ￥" + String.valueOf(answer_mod));
	        textViewforOnePerson.setTextSize(32.0f);
	        textViewforOnePerson.setTextColor(Color.BLACK);
	        textViewforOnePerson.setGravity(Gravity.RIGHT);
	        layout.addView(textViewforOnePerson);

	    	Button secondPartySearch = new Button(this);
        	secondPartySearch.setOnClickListener(
        			new OnClickListener(){
            		public void onClick(View v){
            	    	lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, ll);
            	    	lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, ll);
            			sendQuery();
            		}
            	}
            );
        	secondPartySearch.setText("二次会のお店を検索");
        	secondPartySearch.setTextSize(16.0f);
        	LinearLayout.LayoutParams layoutParams = 
                new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        	layoutParams.setMargins(0, 50, 0, 0);
        	secondPartySearch.setLayoutParams(layoutParams);
        	layout.addView(secondPartySearch);
        }
    }
    
    private void sendQuery(){
		if(latitude == null){
			Toast.makeText(ResultViewActivity.this, "位置情報が取得できません。端末の設定をご確認ください。", Toast.LENGTH_LONG).show();
		}else{
			//Log.d("latitude",latitude);
			//Log.d("longtude",longtude);
			try {
				XmlPullParser parser = Xml.newPullParser();
				
	            URL url = new URL("http://www.finds.jp/ws/rgeocode.php?lat=" + latitude + "&lon=" + longtude);
	            URLConnection connect = url.openConnection();
	            parser.setInput(connect.getInputStream(), "UTF-8");

	            int eventType;
	            String addressText = "";
	            while((eventType = parser.next()) != XmlPullParser.END_DOCUMENT){
	                if(eventType == XmlPullParser.START_TAG && "pname".equals(parser.getName())){
	                	addressText = parser.nextText() + "　";
	                }
	                //TODO:for better serch word
	                //if(eventType == XmlPullParser.START_TAG && "mname".equals(parser.getName())){
	                //	addressText += parser.nextText();
	                //}
	                if(eventType == XmlPullParser.START_TAG && "section".equals(parser.getName())){
	                	addressText += parser.nextText() + "　";;
	                	Log.d("res", addressText);
	                }
	            }
	            
	            Toast.makeText(ResultViewActivity.this, addressText + " で検索します", Toast.LENGTH_LONG).show();
	            
	            Intent intent = new Intent();
	            intent.setAction(Intent.ACTION_VIEW);
	            intent.setData(Uri.parse("http://gsearch.gnavi.co.jp/rest/search.php?key=" + addressText + " 二次会")); 
	            //intent.setData(Uri.parse("http://r.tabelog.com/japan/0/0/lst/?sw=" + addressText + " 居酒屋"  + "&vs=1"));
	            startActivity(intent);
	            
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
    
	private final String tag = "GeoLocSampleGeocode";
	private String latitude;
	private String longtude;
}