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
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;


public class MainActivity extends AppCompatActivity {


  // SharedPreferences sharedPreferences;



    public void SignIn (View view){

        EditText VehicleNumber = (EditText) findViewById(R.id.VehicleNumber);
        EditText DriverMobileNumber =(EditText) findViewById(R.id.DriverMobileNumber);

        if(VehicleNumber.getText().toString().matches("") || DriverMobileNumber.getText().toString().length()!=10){

            if(DriverMobileNumber.getText().toString().length()!=10)
            {    Toast.makeText(this, "Enter Valid Phone number", Toast.LENGTH_SHORT).show();
                 DriverMobileNumber.setText("");
            }
            Toast.makeText(this, "vehicle registration number and mobile number are cumpulsory!", Toast.LENGTH_SHORT).show();


        } else {
            //ParseUser Driver = new ParseUser();
            //Driver.setUsername(VehicleNumber.getText().toString());
            //Driver.setPassword(DriverMobileNumber.getText().toString());

            ParseObject userDriver = new ParseObject("userDriver");
            userDriver.put("saviourMobileNumber",DriverMobileNumber.getText().toString() );
            userDriver.put("vehicleNumber",VehicleNumber.getText().toString() );

            userDriver.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null)
                    {
                      //  sharedPreferences.edit().putString("drivername",ParseUser.getCurrentUser().getUsername().toString()).apply();
                        Toast.makeText(MainActivity.this,"successful",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,requestView.class);

                        startActivity(intent);
                    }else{
                        Toast.makeText(MainActivity.this,"check your network connection ",Toast.LENGTH_LONG).show();

                    }
                }
            });


/*
            Driver.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e==null){
                        Log.i("sign In", "successful!");
                    }else {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            */
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     //   sharedPreferences=this.getSharedPreferences("com.parse.starter", Context.MODE_PRIVATE);
       /* if(sharedPreferences.getString("drivername","").equals(ParseUser.getCurrentUser().getUsername().toString()));
        {
            Intent intent = new Intent(MainActivity.this,requestView.class);

            startActivity(intent);

        }
*/
        ParseAnalytics.trackAppOpenedInBackground(getIntent());




    }

}