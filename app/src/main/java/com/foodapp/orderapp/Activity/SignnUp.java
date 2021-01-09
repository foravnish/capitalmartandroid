package com.foodapp.orderapp.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.foodapp.orderapp.R;
import com.foodapp.orderapp.Utils.Api;
import com.foodapp.orderapp.Utils.AppController;
import com.foodapp.orderapp.Utils.Getseter;
import com.foodapp.orderapp.Utils.MyPrefrences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignnUp extends AppCompatActivity {

    Button signup;
    EditText re_password,password,mobile,email,name,gst,company,txtCity;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Dialog dialog;
    JSONObject jsonObject ;
    String refValue;
    TextView txtTerms;
    ArrayList<Getseter> DataList = new ArrayList<>();
    List<String> City_Names = new ArrayList<String>();
    Spinner city;
    String cityId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signn_up);

        name=(EditText)findViewById(R.id.name);
        email=(EditText)findViewById(R.id.email);
        mobile=(EditText)findViewById(R.id.mobile);
        password=(EditText)findViewById(R.id.password);
        re_password=(EditText)findViewById(R.id.re_password);
        gst=(EditText)findViewById(R.id.txtGst);
        company=(EditText)findViewById(R.id.txtCompany);
//        txtCity=(EditText)findViewById(R.id.txtCity);
        signup=(Button)findViewById(R.id.signup);
        txtTerms=(TextView)findViewById(R.id.txtTerms);
        city = (Spinner) findViewById(R.id.city);
        dialog=new Dialog(SignnUp.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        Getseter.preferences = getApplicationContext().getSharedPreferences("My_prefence", MODE_PRIVATE);
        Getseter.editor =Getseter.preferences.edit();

//        mobile.setText(getIntent().getStringExtra("mobile"));


        getCityList();

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("sdfvsdgvfdsgdf", DataList.get(position).getID());
                cityId=DataList.get(position).getID();
                Getseter.editor.putString("city_id", DataList.get(position).getID());
                Getseter.editor.putString("city_name", DataList.get(position).getName());
                Getseter.editor.commit();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (MyPrefrences.getRefer(getApplicationContext()).equals("")){
            refValue="no-referal";
        }
        else{
            refValue=MyPrefrences.getRefer(getApplicationContext());
        }

        txtTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!name.getText().toString().equals("")){

//                    if (!email.getText().toString().equals("")){

//                        if (email.getText().toString().trim().matches(emailPattern)){
                            if (!mobile.getText().toString().equals("")){

                                if (!password.getText().toString().equals("")){

                                    if (password.getText().toString().equalsIgnoreCase(re_password.getText().toString())) {
//                                        if (!gst.getText().toString().equals("")){

                                            if (!company.getText().toString().equals("")){
                                                register();

                                            } else{
                                                Toast.makeText(SignnUp.this, "Please enter Company Name.", Toast.LENGTH_SHORT).show();
                                            }
//                                        } else{
//                                            Toast.makeText(SignnUp.this, "Please enter GST No.", Toast.LENGTH_SHORT).show();
//                                        }

                                    }
                                    else{
                                        Toast.makeText(SignnUp.this, "Please enter same password", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else
                                {
                                    Toast.makeText(SignnUp.this, "Please enter password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(SignnUp.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
                            }
//                        }
//                        else
//                        {
//                            Toast.makeText(SignnUp.this, "Please enter valid Email", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                    else
//                    {
//                        Toast.makeText(SignnUp.this, "Please enter Email", Toast.LENGTH_SHORT).show();
//
//                    }
                }
                else{
                    Toast.makeText(SignnUp.this, "Please enter name", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getCityList() {

        JsonObjectRequest jsonObjectRequest22 = new JsonObjectRequest(Request.Method.GET, Api.cityList, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Getseter.exitdialog(dialog);

                if (response.optString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = response.optJSONArray("message");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        DataList.add(new Getseter(jsonObject.optString("id"), jsonObject.optString("city"), null, null));
                        City_Names.add(jsonObject.optString("city"));
                    }
                    ArrayAdapter<String> adapter_state1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, City_Names);
                    adapter_state1.setDropDownViewResource(R.layout.spinnertext);
                    city.setAdapter(adapter_state1);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Please connect to the internet.", Toast.LENGTH_SHORT).show();
                Getseter.exitdialog(dialog);
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest22);

    }

    private void register() {


        Getseter.showdialog(dialog);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Api.userRegistration  , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Getseter.exitdialog(dialog);

                Log.d("sdfdsgdfsgdsf",response);

                try {
                    jsonObject = new JSONObject(response);

                    if (jsonObject.optString("status").equals("success")) {

                    Toast.makeText(getApplicationContext(), jsonObject.optString("message").toString(), Toast.LENGTH_SHORT).show();

                    Getseter.editor.putString("fname",name.getText().toString());
                    Getseter.editor.putString("mobile",mobile.getText().toString());
                    Getseter.editor.putString("emailid",email.getText().toString());
                    Getseter.editor.commit();

                    startActivity(new Intent(SignnUp.this,Login.class));

                    }
                    else {
                        Getseter.exitdialog(dialog);
                        Toast.makeText(getApplicationContext(), jsonObject.optString("message").toString(), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Please connect to the internet...", Toast.LENGTH_SHORT).show();
                Getseter.exitdialog(dialog);

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

               // params.put("register", "1");
                params.put("fname", name.getText().toString());
                params.put("email", email.getText().toString());
                params.put("mobile",  mobile.getText().toString());
                params.put("password", password.getText().toString());
                params.put("referer", refValue.toString());
                params.put("password", password.getText().toString());
                params.put("gst", gst.getText().toString());
                params.put("company", company.getText().toString());
                params.put("city_id", cityId.toString());


                return params;
            }
        };

//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(27000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);

    }
}
