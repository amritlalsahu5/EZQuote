package com.example.finalproject;

/*
 * Author: Alexander Pinkerton, Udeep Manchanda, Tianyi Xie
 */

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adapter.AboutUsAdapter;
import com.example.pojo.AboutUs;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


/*
 * Author: Alexander Pinkerton, Udeep Manchanda, Tianyi Xie
 */


public class LoginActivity extends Activity implements OnClickListener{

	EditText email, pass;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getActionBar().setTitle("Login");
        
        email = (EditText)findViewById(R.id.editTextEmail);
        pass = (EditText)findViewById(R.id.editTextPassword);
        
        Button b = (Button)findViewById(R.id.buttonCreateNewAccount);
        b.setOnClickListener(this);
        b = (Button)findViewById(R.id.buttonLogin);
        b.setOnClickListener(this);
        
        
        if (ParseUser.getCurrentUser() != null){
        	Intent I = new Intent(LoginActivity.this, MarketSummaryActivity.class);
			startActivity(I);
			finish();
        }
        
  
    }

    
    
    
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.buttonCreateNewAccount:
			
			Intent i = new Intent(this, SignUpActivity.class);
			startActivity(i);
			break;
			
		case R.id.buttonLogin:

			if(isConnected()){
		
			if(email.getText() != null && email.getText().toString().trim().length() == 0){
				Toast.makeText(LoginActivity.this, "Email cannot be empty.", Toast.LENGTH_SHORT).show();
			}else if(pass.getText() != null && pass.getText().toString().trim().length() == 0){
				Toast.makeText(LoginActivity.this, "Password cannot be empty.", Toast.LENGTH_SHORT).show();
			}else{
				
				ParseUser.logInInBackground(email.getText().toString(), pass.getText().toString(), new LogInCallback() {
					
					@Override
					public void done(ParseUser user, ParseException e) {
						// TODO Auto-generated method stub
						if (user != null && e == null) {
						    // Hooray! The user is logged in.
							Intent I = new Intent(LoginActivity.this, MarketSummaryActivity.class);
							//I.putExtra(name, value)
							startActivity(I);
							finish();
						    } else {
						      // Signup failed. Look at the ParseException to see what happened.
						    	Log.d("Loginerror", e.getLocalizedMessage());
						    	Toast.makeText(LoginActivity.this, "Login Failed.", Toast.LENGTH_SHORT).show();
						    }
					}
					});
			}
			}
			
			
			break;
		
		}
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		 	MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.about_us, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.aboutUs) {
			
			AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                    LoginActivity.this,AlertDialog.THEME_HOLO_DARK);
            builderSingle.setTitle("About Us");
            LinearLayout LL = new LinearLayout(this);
    	    LL.setOrientation(LinearLayout.VERTICAL);

    	    LayoutParams LLParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
    	    
    	    LL.setLayoutParams(LLParams);

            ListView lv = new ListView(this);
            ArrayList<AboutUs> aList = new ArrayList<AboutUs>();
            AboutUs udeepObj = new AboutUs();
            udeepObj.setName("Udeep Manchanda");
            udeepObj.setEmail("udeep.m@gmail.com | umanchan@uncc.edu");
            udeepObj.setPic(R.drawable.udeep);
            AboutUs alexObj = new AboutUs();
            alexObj.setName("Alexander Pinkerton");
            alexObj.setEmail("alexpinkerton88@gmail.com | apinkert@uncc.edu");
            alexObj.setPic(R.drawable.alex);
            AboutUs tianyiObj = new AboutUs();
            tianyiObj.setName("Tianyi Xie");
            tianyiObj.setEmail("tianyi.xie11@gmail.com | txie@uncc.edu");
            tianyiObj.setPic(R.drawable.tianyii);
            
            aList.add(udeepObj);
            aList.add(alexObj);
            aList.add(tianyiObj);
            
            final ArrayAdapter<AboutUs> arrayAdapter = new AboutUsAdapter(this, R.layout.aboutus_listview, aList);
            lv.setAdapter(arrayAdapter);
            LL.addView(lv);
            builderSingle.setView(LL);
            builderSingle.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
			builderSingle.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean isConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(LoginActivity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			// Toast.makeText(MainActivity.this, "Internet is connected",
			// Toast.LENGTH_SHORT).show();
			return true;
		} else {
			Toast.makeText(LoginActivity.this, "No Internet Connection",
					Toast.LENGTH_SHORT).show();
			return false;
		}
	}
	
	
	
	
}
