package com.alessio.delmonti.giornale.di.oggi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.XmlDom;
import com.alessio.delmonti.giornale.di.oggi.R;

public class RssReader extends Activity{

	 private AQuery aq;
	 ListView lv;
	 List<Journal> giornali = new ArrayList<Journal>();
	 List<String> blogs = new ArrayList<String>();
	 Integer numBlogs, index = 0;
	 ImageView loading;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//controllo connessione a internet + gif
		
		loading = (ImageView) findViewById(R.id.imageView1);
		loading.startAnimation( AnimationUtils.loadAnimation(this, R.anim.infinite_rotation) );
		
		
		blogs.add("http://avaxhome.ws/blogs/enmy/rss.xml");
		blogs.add("http://avaxhome.ws/blogs/tenovis/rss.xml");
		
		numBlogs = blogs.size();		
		
		aq = new AQuery(this);

		if( isOnline() == true ){
			for(String blog : blogs){
				Log.i("blog", blog);
				aq.ajax(blog, XmlDom.class, this, "xmlHandler");
			}	
		} else {
			aq.id(R.id.textView1).text("Non sei connesso a internet!");
		}
		

	}
	
	public void xmlHandler(String url, XmlDom xml, AjaxStatus status){
		List<XmlDom> entries = xml.tags("item");        
	    List<String> titles = new ArrayList<String>();
	    
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	    Date date = new Date();
	    
	    
	    
	    for(XmlDom entry: entries){
	    	String title = entry.text("title");
	    	String url_detail = entry.text("guid");
	        //titles.add(entry.text("title"));
	    	if( title.contains(dateFormat.format(date)) ){
	    		Log.i("title:", entry.text("title"));
	    		giornali.add( new Journal(title, url_detail, "rep.jpg" ) );
		        	
	    	}
	        
	    }
	    
    	index++;
	    
	    if(index == (numBlogs) ){

	    	//Order by name
	    	Collections.sort(giornali, new Comparator<Journal>() {
	            @Override
	            public int compare(final Journal object1, final Journal object2) {
	                return object1.name.compareTo(object2.name);
	            }

	         } );
	    	
	    	//Create list adapter
	    	List<String> listContents = new ArrayList<String>(giornali.size());
	    	
	    	for(Journal g : giornali){
	    		listContents.add(g.name);
	    	}	    	
	    	
	    	//Stopping animation
	    	LinearLayout lp=new LinearLayout(this);
	    	
	    	loading.clearAnimation();
	    	loading.setVisibility(View.GONE);
	    	lp.removeView(loading);
	    	
	    	//Dinamically create the listview
	    	lv = new ListView(this);	    	
	        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listContents));
	        
	        setContentView(lv);
	        
	        lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
					Intent intent = new Intent(RssReader.this, DetailItem.class);
					intent.putExtra("testata", giornali.get(pos).name);
					intent.putExtra("url", giornali.get(pos).url);
					startActivity(intent);
				}
	        });
	    }
	   	   
	}		
	
	private Boolean isOnline()	{
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if(ni != null && ni.isConnected())
			return true;

		return false;
	}
	
}

