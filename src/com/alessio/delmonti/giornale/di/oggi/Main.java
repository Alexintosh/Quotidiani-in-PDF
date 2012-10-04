package com.alessio.delmonti.giornale.di.oggi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alessio.delmonti.giornale.di.oggi.R;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Main extends Activity {
	ListView lv;
	public static final String NEWSPAPERS = "newspapers";
	public static final String URL = "url";
	public static final String IMG = "img";
	public static final String TITLE = "title";
	JSONArray jNewspaper;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);       
        
//        List<Journal> test = new ArrayList<Journal>();
//        test.add( new Journal("Repubblica", "url1", "rep.jpg" ) );
//        test.add( new Journal("Quotidiano", "url2", "quo.jpg" ) );
//        test.add( new Journal("Sicilia", "url3", "sic.jpg" ) );
        
        
        try
        {          
          String myJ = "{ \"newspapers\": [ { \"title\": \"giornale\",\"img\": \"img\",\"url\": \"url\"}, {\"title\": \"rep\",\"img\": \"img\",\"url\": \"url\"} ] }";
                                       
          JSONObject jObj = new JSONObject(myJ);
          
          jNewspaper = jObj.getJSONArray(NEWSPAPERS);         

          List<String> listContents = new ArrayList<String>(jNewspaper.length());
          
          for (int i = 0; i < jNewspaper.length(); i++)
          {
        	JSONObject c = jNewspaper.getJSONObject(i);
        	
        	//Storing local vars 
        	String title = c.getString(TITLE);
//            String url = c.getString(URL);
//            String img = c.getString(IMG);
            listContents.add(title);
          }

          
        }
        catch (Exception e)
        {
          // this is just an example
        }
        
        lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				Log.i("position", ""+pos);
				JSONObject c;
				try {
					c = jNewspaper.getJSONObject(pos);
					Log.i("url", ""+c.getString(URL));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
        });
        

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
