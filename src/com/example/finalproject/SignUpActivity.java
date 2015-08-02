package com.example.finalproject;

/*
 * Author: Alexander Pinkerton, Udeep Manchanda, Tianyi Xie
 */

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/*
 * Author: Alexander Pinkerton, Udeep Manchanda, Tianyi Xie
 */


public class SignUpActivity extends Activity implements OnClickListener{

	EditText editTextUserName,editTextEmail,editTextPassword,editTextPasswordConfirm;
	Button b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		
		b = (Button)findViewById(R.id.buttonSignup);
		b.setOnClickListener(this);
		b = (Button)findViewById(R.id.buttonCancel);
		b.setOnClickListener(this);
		
		
		editTextUserName = (EditText)findViewById(R.id.editTextUserName);
		editTextEmail = (EditText)findViewById(R.id.editTextEmail);
		editTextPassword = (EditText)findViewById(R.id.editTextPassword);
		editTextPasswordConfirm = (EditText)findViewById(R.id.editTextPasswordConfirm);
		
		
		
				
	}

	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.buttonSignup:
			if(isConnected()){

			if(editTextEmail.getText() != null && editTextEmail.getText().toString().trim().length() == 0){
				Toast.makeText(SignUpActivity.this, "Email cannot be empty.", Toast.LENGTH_SHORT).show();
			}else if(editTextPassword.getText() != null && editTextPassword.getText().toString().trim().length() == 0){
				Toast.makeText(SignUpActivity.this, "Password cannot be empty.", Toast.LENGTH_SHORT).show();
			}else if(editTextUserName.getText() != null && editTextUserName.getText().toString().trim().length() == 0){
				Toast.makeText(SignUpActivity.this, "Name cannot be empty.", Toast.LENGTH_SHORT).show();
			}else{
				
				ParseUser user = new ParseUser();
				
				if(editTextPassword.getText().toString().equals(editTextPasswordConfirm.getText().toString())){
					user.setUsername(editTextEmail.getText().toString());
					user.setEmail(editTextEmail.getText().toString());
					user.setPassword(editTextPassword.getText().toString());
					user.put("name", editTextUserName.getText().toString());
					
					
					user.signUpInBackground(new SignUpCallback() {
						  public void done(ParseException e) {
						    if (e == null) {
						    	Toast.makeText(SignUpActivity.this, "Account Created.", Toast.LENGTH_SHORT).show();
						    	saveInFavorites("GOOG");
						    	saveInFavorites("AAPL");
						    	
						    	Intent i = new Intent(SignUpActivity.this,MarketSummaryActivity.class);
						    	startActivity(i);
								finish();
						    } else {
						    	//Toast.makeText(SignUp.this, "Invalid Email.", Toast.LENGTH_SHORT).show();
						    	Toast.makeText(SignUpActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
						    }
						  }
						});
					
					
					
				}else{
					Toast.makeText(this, "Passwords must match.", Toast.LENGTH_SHORT).show();
				}
				
			}
			
			}
			
	
			break;
		case R.id.buttonCancel:
			finish();
			break;
		
		}

	}
	private void saveInFavorites(String stockSymbol) {
		ParseObject favObj = new ParseObject("Favorites");
		favObj.put("UserName", ParseUser.getCurrentUser().getUsername());
		favObj.put("StockName", stockSymbol);

		favObj.saveInBackground();

	}
	
	
	public boolean isConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(SignUpActivity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			// Toast.makeText(MainActivity.this, "Internet is connected",
			// Toast.LENGTH_SHORT).show();
			return true;
		} else {
			Toast.makeText(SignUpActivity.this, "No Internet Connection",
					Toast.LENGTH_SHORT).show();
			return false;
		}
	}
	

}
