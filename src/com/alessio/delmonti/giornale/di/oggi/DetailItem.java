package com.alessio.delmonti.giornale.di.oggi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.alessio.delmonti.giornale.di.oggi.R;

public class DetailItem extends Activity{
	private AQuery aq;
	private static final int DIALOG_IMG = 1;
	private String img;
	private String downloadLink;
	private ImageView ivP;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_item);
		
		//final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);		
		
		Bundle bundle = getIntent().getExtras();
		aq = new AQuery(this);		
		
		
		if( bundle != null){
			String testata= bundle.getString("testata");
			String url = bundle.getString("url");
			
			ivP = (ImageView) findViewById(R.id.ivPreview);
			aq.id(R.id.ivPreview).image(R.drawable.ic_loader);
			
			ivP.startAnimation( AnimationUtils.loadAnimation(this, R.anim.infinite_rotation) );
			aq.id(R.id.tvTestata).text(testata);	
			setTitle(testata);
			

			aq.ajax(url, String.class, new AjaxCallback<String>() {

			        @Override
			        public void callback(String url, String html, AjaxStatus status) {
			        	Document doc = Jsoup.parse(html);
			        	
			        	Elements immagine = doc.select(".image img[src]");
			        	String src = immagine.attr("src");			        				        			        	
			        	
			        	img = src.replace("_medium", "");		        	
			        	aq.id(R.id.ivPreview).image(img);
			        	
			        	//Cambio parametri di layout_weight
						
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
						params.weight = 1.0f;

						ivP.setLayoutParams(params);
			        	
			        	Elements links = doc.select("a[href]");
			        	for(Element link : links){
			        		String href = link.attr("href"); 
			        		if( href.contains("filepost.com") || href.contains("depositfile.com") ){
			        			Log.i("LINK", href );
			        			downloadLink = href;
			        			Button bDownload = (Button) findViewById(R.id.bDownload);
			        			bDownload.setOnClickListener( new View.OnClickListener() {
									
									@Override
									public void onClick(View v) {
										Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadLink));
								    	startActivity(browserIntent);
									}
								});
			        		}
			        	}
			        				        	
			        				        	
			        }
			        
			});
		}
				
	}
	
    public void buttonClicked(View button){    	
    }

}
