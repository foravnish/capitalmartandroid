package com.foodapp.orderapp.Fragments;


import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProfile extends Fragment {


    EditText txtName, house, street, landmark, pincode, area, phone;
    TextView company,gstNo,city,mobileNo,addresses;
    Dialog dialog;
    Button btnEdit;
    ArrayList<Getseter> DataList = new ArrayList<>();
    List<String> City_Names = new ArrayList<String>();
    String regexStr = "^[0-9]$";

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_profile3, container, false);

        txtName = (EditText) view.findViewById(R.id.txtName);
        company = (TextView) view.findViewById(R.id.company);
        house = (EditText) view.findViewById(R.id.house);
        street = (EditText) view.findViewById(R.id.street);
        gstNo = (TextView) view.findViewById(R.id.gstNo);
        landmark = (EditText) view.findViewById(R.id.landmark);
        city = (TextView) view.findViewById(R.id.city);
        pincode = (EditText) view.findViewById(R.id.pincode);
//          area=(EditText)view.findViewById(R.id.area);
        mobileNo = (TextView) view.findViewById(R.id.mobileNo);
        addresses=(TextView) view.findViewById(R.id.addresses);
        btnEdit = (Button) view.findViewById(R.id.btnEdit);

        getActivity().setTitle("Profile");


        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        Getseter.showdialog(dialog);

        Getseter.preferences = getActivity().getSharedPreferences("My_prefence", MODE_PRIVATE);
        Getseter.editor = Getseter.preferences.edit();

        Getseter.showdialog(dialog);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Api.getUserDetails + "?userId=" + Getseter.preferences.getString("user_id", ""), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Getseter.exitdialog(dialog);

                Log.d("sdfsdfdgdgfg", String.valueOf(response));

                try {
                    if (response.optString("status").equals("success")) {
                        JSONArray jsonArray = response.getJSONArray("message");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);


                            txtName.setText(jsonObject.optString("fname").toString());
                            company.setText(jsonObject.optString("company").toString());
                            addresses.setText(""+jsonObject.optString("house_no")+" "+ jsonObject.optString("street")+" "+ jsonObject.optString("pincode"));
                            gstNo.setText(jsonObject.optString("gst").toString());
                            city.setText(jsonObject.optString("city_id").toString());
//                            complex.setText(jsonObject.optString("complex").toString());
                            mobileNo.setText(jsonObject.optString("mobile").toString());
                            //                phone.setText(jsonObject.optString("phone").toString());




                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Please connect to the internet.", Toast.LENGTH_SHORT).show();
                Getseter.exitdialog(dialog);
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);


        /////  TODO Without Validation

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment=new UpdateProfile();
                FragmentManager manager=getFragmentManager();
                Bundle bundle=new Bundle();
                bundle.putString("type","none");
                bundle.putString("orderitem", "");
                bundle.putString("cal_price","");
                bundle.putString("totalItem","");
                bundle.putInt("length",0);
                FragmentTransaction ft=manager.beginTransaction();
                fragment.setArguments(bundle);
                ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();

            }
        });


//
//            private void sendData() {
//
//                Getseter.showdialog(dialog);
//                //StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://hoomiehome.com/appcredentials/jsondata.php?", new Response.Listener<String>() {
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.updateUserDetails, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Getseter.exitdialog(dialog);
//
//                        Log.d("sdfdsgdfsgdsf", response);
//                        JSONObject jsonObject = null;
//                        try {
//                            jsonObject = new JSONObject(response);
//
//                            if (jsonObject.optString("status").equals("success")) {
//
//                                Toast.makeText(getActivity(), jsonObject.optString("message").toString(), Toast.LENGTH_SHORT).show();
//
//                                if (getArguments().getString("type").equalsIgnoreCase("none")) {
//
//                                    Toast.makeText(getActivity(), "Submit successfully.", Toast.LENGTH_SHORT).show();
////                                    Fragment fragment = new AccountDetails();
////                                    FragmentManager manager = getFragmentManager();
////                                    FragmentTransaction ft = manager.beginTransaction();
////                                    ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout, R.anim.frag_fade_right, R.anim.frag_fad_left);
////                                    ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
//                                } else if (getArguments().getString("type").equalsIgnoreCase("prod")) {
//
//                                    Fragment fragment = new Delivery();
//                                    FragmentManager manager = getFragmentManager();
//                                    FragmentTransaction ft = manager.beginTransaction();
//                                    ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout, R.anim.frag_fade_right, R.anim.frag_fad_left);
//                                    ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("orderitem", getArguments().get("orderitem").toString());
//                                    bundle.putString("cal_price", getArguments().get("cal_price").toString());
//                                    bundle.putString("totalItem", getArguments().get("totalItem").toString());
//                                    bundle.putInt("length", getArguments().getInt("length"));
//                                    fragment.setArguments(bundle);
//
//                                }
//                            } else {
//                                Toast.makeText(getActivity(), jsonObject.optString("message").toString(), Toast.LENGTH_SHORT).show();
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getActivity(), "Please connect to the internet.", Toast.LENGTH_SHORT).show();
//                        Getseter.exitdialog(dialog);
//
//                    }
//                }) {
//
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<String, String>();
//
//                        // params.put("update_profile", "1");
//                        params.put("userId", Getseter.preferences.getString("user_id", ""));
//                        params.put("fname", txtName.getText().toString());
//                        params.put("lname", "");
//                        params.put("house_no", house.getText().toString());
//                        params.put("street", street.getText().toString());
////                        params.put("complex", complex.getText().toString());
//                        params.put("landmark", landmark.getText().toString());
//                        params.put("pincode", pincode.getText().toString());
//                        params.put("mobile", mobileNo.getText().toString());
//                        params.put("gst", gstNo.getText().toString());
//                        params.put("company", company.getText().toString());
//
//                        //params.put("dob", "25/07/17");
//                        params.put("city_id", Getseter.preferences.getString("city_id", ""));
//
//                        Getseter.editor.putString("fname", txtName.getText().toString());
//                        Getseter.editor.putString("lname", company.getText().toString());
//                        Getseter.editor.putString("house_no", house.getText().toString());
//                        Getseter.editor.putString("street", street.getText().toString());
//                        Getseter.editor.putString("area", gstNo.getText().toString());
//                        Getseter.editor.putString("landmark", landmark.getText().toString());
//                        Getseter.editor.putString("pincode", pincode.getText().toString());
//                        Getseter.editor.putString("mobile", mobileNo.getText().toString());
//                        Getseter.editor.putString("city_name", city.getText().toString());
//                        Getseter.editor.commit();
//
//                        Log.d("fgdfgdfgdfdf", Getseter.preferences.getString("city_id", ""));
//                        Log.d("fgdfgdfgdfdf", Getseter.preferences.getString("user_id", ""));
//                        return params;
//                    }
//                };
//
//                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                        0,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//                AppController.getInstance().addToRequestQueue(stringRequest);
//            }
//        });


        return view;
    }

}
