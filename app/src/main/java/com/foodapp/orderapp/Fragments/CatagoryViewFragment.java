package com.foodapp.orderapp.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.foodapp.orderapp.Activity.Login;
import com.foodapp.orderapp.Activity.Navigation;
import com.foodapp.orderapp.R;
import com.foodapp.orderapp.Utils.Api;
import com.foodapp.orderapp.Utils.AppController;
import com.foodapp.orderapp.Utils.DatabaseHandler;
import com.foodapp.orderapp.Utils.Getseter;
import com.foodapp.orderapp.Utils.MyPrefrences;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class CatagoryViewFragment extends Fragment {

    String image;
    NetworkImageView parallax_header_imageview;
    TextView mrp, disount, selling, name, desc, button1, size, btnNumber;
    EditText integer_number;
    Dialog dialog;
    Button decrease, increase;
    static int number = 1;
    DatabaseHandler db;
    List<Getseter> DataList = new ArrayList<Getseter>();
    JSONObject jsonObject;
    JSONObject jsonObject1;
    List<Getseter> Catag = new ArrayList<Getseter>();
    boolean flag = false;
    String productId;
    TextView txtIsInStock;
    Boolean flagInStock=false;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cat_view, container, false);
        parallax_header_imageview = (NetworkImageView) view.findViewById(R.id.parallax_header_imageview);

        productId = getArguments().getString("product_id");
        txtIsInStock = (TextView) view.findViewById(R.id.txtIsInStock);

        mrp = (TextView) view.findViewById(R.id.mrp);
        disount = (TextView) view.findViewById(R.id.disount);
        selling = (TextView) view.findViewById(R.id.selling);
        name = (TextView) view.findViewById(R.id.name);
        desc = (TextView) view.findViewById(R.id.desc);
        size = (TextView) view.findViewById(R.id.size);
        integer_number = (EditText) view.findViewById(R.id.integer_number);
        db = new DatabaseHandler(getActivity());
//        decrease = (Button) view.findViewById(R.id.decrease);
        increase = (Button) view.findViewById(R.id.increase);
        button1 = (TextView) view.findViewById(R.id.button1);
        btnNumber = (TextView) view.findViewById(R.id.btnNumber);

        number = 1;
//        DataList=db.getAllCatagory();

//        Date currentTime = Calendar.getInstance().getTime();
//        Log.d("sdgdfgdfgdfgf", String.valueOf(currentTime));


        getActivity().setTitle("Product Detail");

//        decrease.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (integer_number.getText().toString().equals("1")) {
//
//                } else {
//                    number = number - 1;
//                    integer_number.setText("" + number);
//                }
//            }
//        });
//
//        increase.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                if (integer_number.getText().toString().equals("5")) {
////                    Toast.makeText(getActivity(), "No more...", Toast.LENGTH_SHORT).show();
////                } else {
//                    number = number + 1;
//                    integer_number.setText("" + number);
////                }
//
//            }
//        });

        parallax_header_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFullImageDialog(getActivity(), jsonObject.optString("photo"), 0, "Photo");

            }
        });


        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        Getseter.preferences = getActivity().getSharedPreferences("My_prefence", MODE_PRIVATE);
        Getseter.editor = Getseter.preferences.edit();

        Getseter.showdialog(dialog);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Api.productDetails + "?productId=" + getArguments().getString("product_id"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("sdgsdgdf", getArguments().getString("product_id"));

                Log.d("fsddgdgdfgdfgd", String.valueOf(response));

                Getseter.exitdialog(dialog);
                if (response.optString("status").equalsIgnoreCase("success")) {


                    JSONArray jsonArray = response.optJSONArray("message");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.optJSONObject(i);

                        JSONArray jsonArray1 = jsonObject.optJSONArray("sizes");
                        for (int j = 0; j < jsonArray1.length(); j++) {

                            jsonObject1 = jsonArray1.optJSONObject(j);
                            if (jsonObject1.optString("in_stock").equals("0")){
                                flagInStock=false;
                                txtIsInStock.setVisibility(View.VISIBLE);
                            }else{
                                flagInStock=true;
                                txtIsInStock.setVisibility(View.GONE);

                            }
                            mrp.setText("₹ " + jsonObject1.optString("mrp_price"));
                            mrp.setPaintFlags(mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                            size.setText(jsonObject1.optString("size"));
                            disount.setText(jsonObject1.optString("discount") + " OFF");
                            selling.setText(jsonObject1.optString("sell_price"));
                        }
                        //  DataList.add(new Getseter(jsonObject.optString("product_id"),jsonObject.optString("product_name"),jsonObject.optString("product_image"),null));

//                        name.setText(jsonObject.optString("product_name") + " (" + jsonObject1.optString("size") + ")");
                        name.setText(jsonObject.optString("product_name"));

//                        desc.setText(jsonObject.optString("description"));
                        desc.setText(Html.fromHtml(jsonObject.optString("description")));
                        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                        parallax_header_imageview.setImageUrl(jsonObject.optString("photo"), imageLoader);
                    }


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

        btnNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flagInStock) {
                    if (Integer.parseInt(integer_number.getText().toString()) > 0) {
                        callApiForEnquiry();

                    } else {
                        Toast.makeText(getActivity(), "Please fill Quantity!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "The Product has been Out of Stock", Toast.LENGTH_LONG).show();

                }

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagInStock) {
                    if (Integer.parseInt(integer_number.getText().toString()) > 0) {
//                        popForLogin();
                        continueCart();
                    } else {
                        Toast.makeText(getActivity(), "Please fill Quantity!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "The Product has been Out of Stock", Toast.LENGTH_LONG).show();
                }

//                    continueCart();

//                    if (!Getseter.preferences.getString("fname", "").toString().equals("") &&
//                            //   !Getseter.preferences.getString("lname","").toString().equals("") &&
//                            !Getseter.preferences.getString("house_no", "").toString().equals("") &&
//                            !Getseter.preferences.getString("street", "").toString().equals("") &&
//                            // !Getseter.preferences.getString("area","").toString().equals("") &&
//                            !Getseter.preferences.getString("pincode", "").toString().equals("") &&
//                            !Getseter.preferences.getString("city_name", "").toString().equals("") &&
//                            !Getseter.preferences.getString("mobile", "").toString().equals("")) {
//
//                        if (Getseter.preferences.getString("pincode", "").equals("141001") ||
//                                Getseter.preferences.getString("pincode", "").equals("141002") ||
//                                Getseter.preferences.getString("pincode", "").equals("141003") ||
//                                Getseter.preferences.getString("pincode", "").equals("141004") ||
//                                Getseter.preferences.getString("pincode", "").equals("141005") ||
//                                Getseter.preferences.getString("pincode", "").equals("141006") ||
//                                Getseter.preferences.getString("pincode", "").equals("141007") ||
//                                Getseter.preferences.getString("pincode", "").equals("141008")) {
//
//
//                            continueCart();
//
//                        } else {
//
//                            if (jsonObject.optString("product_name").contains("Dry") || jsonObject.optString("product_name").contains("Giftpack")) {
//                                Toast.makeText(getActivity(), "Sorry! this product not available on this pincode.", Toast.LENGTH_LONG).show();
//                            } else {
//                                continueCart();
//                            }
//                        }
//
//
//                    } else {
//                        Fragment fragment = new ViewProfile();
//                        FragmentManager manager = getFragmentManager();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("type", "none");
//                        bundle.putString("orderitem", "");
//                        bundle.putString("cal_price", "");
//                        bundle.putString("totalItem", "");
//                        bundle.putInt("length", 0);
//                        FragmentTransaction ft = manager.beginTransaction();
//                        fragment.setArguments(bundle);
//                        ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout, R.anim.frag_fade_right, R.anim.frag_fad_left);
//                        ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
//
//                    }
            }
        });


        return view;
    }

    private void callApiForEnquiry() {
        Getseter.showdialog(dialog);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.Enquiry, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Getseter.exitdialog(dialog);

                JSONObject jsonObject;
                Log.d("Okhttp", "Enquiry" + response);

                try {
                    jsonObject = new JSONObject(response);

                    if (jsonObject.optString("status").equals("success")) {

                        Getseter.exitdialog(dialog);
                        Toast.makeText(getActivity(), jsonObject.optString("message").toString(), Toast.LENGTH_LONG).show();

                    } else {
                        Getseter.exitdialog(dialog);
                        Toast.makeText(getActivity(), jsonObject.optString("message").toString(), Toast.LENGTH_LONG).show();


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
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                //  params.put("change_pass", "1");
                params.put("user_id", Getseter.preferences.getString("user_id", ""));
                params.put("product_id", productId);
                params.put("product_qty", integer_number.getText().toString());
                params.put("message", "");

                return params;
            }
        };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest);


    }

    private void continueCart() {

        DataList = db.getAllCatagory();
        int num = DataList.size();
        num = num + 1;
        Navigation.textOne.setText(num + "");


        Getseter.editor.putString("ordername", name.getText().toString());
        Getseter.editor.commit();
        double m1 = Double.parseDouble(selling.getText().toString());
        double m2 = Double.parseDouble(integer_number.getText().toString());
        double m = m1 * m2;


        Date currentTime = Calendar.getInstance().getTime();
        String time = String.valueOf(currentTime);

        db.addCatagory(new Getseter(jsonObject1.optString("id"), jsonObject.optString("product_name"), jsonObject.optString("description"), jsonObject.optString("photo"), jsonObject1.optString("mrp_price"), jsonObject1.optString("sell_price"), integer_number.getText().toString(), String.valueOf(m), jsonObject.optString("id"), time, "time2"));

        Toast.makeText(getActivity(), "Added in Cart", Toast.LENGTH_SHORT).show();


    }

    private void popForLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Please Login to Checkout")
                .setCancelable(false)
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(getActivity(), Login.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        getActivity().finish();
                    }
                })
                .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();


                    }
                });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("" + MyPrefrences.getUSENAME(getActivity()));
        alert.show();
    }


    public static void showFullImageDialog(Context context, String url, int pos, String titlename) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        //dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.showfullimage);
        ImageView back_img = (ImageView) dialog.findViewById(R.id.back_img);
//          ImageView fact_image = (ImageView) dialog.findViewById(R.id.fact_image);


        Log.d("fgdfgdfghsg", url);

        ZoomageView networkImageView = (ZoomageView) dialog.findViewById(R.id.networkImageView);

        Picasso.with(context).load(url).into(networkImageView);

        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setText(titlename);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }


}
