/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity {


 SharedPreferences sharedPreferences;
 static String phoneno;
    public void SignUp (View view){

   EditText usernameEditText= (EditText) findViewById(R.id.usernameEditText);

   EditText passwordEditText= (EditText) findViewById(R.id.passwordEditText);
  if(usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")){

      Toast.makeText( this,"A username and mobile number are required",Toast.LENGTH_SHORT).show();
  }
  else
      {
      ParseUser user = new ParseUser();

      user.setUsername(usernameEditText.getText().toString());
          phoneno = passwordEditText.getText().toString();

          user.setPassword(phoneno);
      user.put("PhoneNumber",passwordEditText.getText().toString());


      user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e)
          {

              if(e== null){

                  Log.i("Signup", "Successful");
              } else{
                  Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
              }
          }
      });
          Intent launchNextActivity;
          launchNextActivity = new Intent(MainActivity.this, VictimActivity.class);
          launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
          launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
          startActivity(launchNextActivity);

     }
 }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}