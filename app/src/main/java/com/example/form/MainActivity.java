package com.example.form;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText mFirstName,mLastName,mUserName,mPassword,mContactNo;
    Button mSubmit;
    private static String URL_REGIST="http://192.168.137.1/Registraion/register.php";

    AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirstName = (EditText) findViewById(R.id.FirstName);
        mLastName = (EditText) findViewById(R.id.LastName);
        mUserName = (EditText) findViewById(R.id.UserName);
        mPassword = (EditText) findViewById(R.id.Password);
        mContactNo = (EditText) findViewById(R.id.ContactNo);
        mSubmit = (Button) findViewById(R.id.Submit);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.FirstName, RegexTemplate.NOT_EMPTY, R.string.invlid_name);
        awesomeValidation.addValidation(this, R.id.LastName, RegexTemplate.NOT_EMPTY, R.string.invlid_name);
        awesomeValidation.addValidation(this, R.id.UserName, RegexTemplate.NOT_EMPTY, R.string.invlid_name);
        String regexPassword = ".{8,}";
        awesomeValidation.addValidation(this, R.id.Password, regexPassword, R.string.invalid_password);

        awesomeValidation.addValidation(this, R.id.ContactNo, "^[+]?[0-9]{10,13}$", R.string.invalid_phone);

        // perform click event on the button
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Regist();
                if (awesomeValidation.validate()) {
                    Toast.makeText(getApplicationContext(), "Form Validation Successfully ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Form Validation Fail", Toast.LENGTH_SHORT).show();
                }
            }
        });
        }
    private void Regist(){

        final String FirstName = this.mFirstName.getText().toString().trim();
        final String LastName = this.mLastName.getText().toString().trim();
        final String UserName = this.mUserName.getText().toString().trim();
        final String Password = this.mPassword.getText().toString().trim();
        final String Contact  = this.mContactNo.getText().toString().trim();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String sucsess = jsonObject.getString("Success");

                            if(sucsess.equals("1"))
                            {
                                Toast.makeText(MainActivity.this,"Register Successflly",Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this,"Register Unsuccessflly",Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Register Error",Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> param=new HashMap<>();
                param.put("mFirstname",FirstName);
                param.put("mLastname",LastName);
                param.put("mUserName",UserName);
                param.put("mPassword",Password);
                param.put("mContactNo",Contact);

                return param;
                //return super.getParams();
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    }
