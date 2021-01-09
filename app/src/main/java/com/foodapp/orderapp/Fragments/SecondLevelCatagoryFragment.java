package com.foodapp.orderapp.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.foodapp.orderapp.R;
import com.foodapp.orderapp.Utils.Api;
import com.foodapp.orderapp.Utils.AppController;
import com.foodapp.orderapp.Utils.Getseter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondLevelCatagoryFragment extends Fragment {

    public int counting;
    Button button1, button2, button3, button4, button5, button6;
    String cc;

    ArrayList<Getseter> DataList = new ArrayList<>();
    ArrayList<Getseter> DataList2 = new ArrayList<>();
    Fragment fragment;
    Adapter adapter;
    //    Adapter2 adapter2;
    Dialog dialog;
    GridView gridview;
    ImageView NoRecordFound;
    JSONObject jsonObject;
    JSONArray jsonArray;
    LinearLayout liner;

    public SecondLevelCatagoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_catagory, container, false);

        gridview = (GridView) view.findViewById(R.id.gridview);
        NoRecordFound = (ImageView) view.findViewById(R.id.NoRecordFound);


        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        Getseter.showdialog(dialog);
        adapter = new Adapter();
        getActivity().setTitle(""+getArguments().getString("name"));

        if (getArguments().getString("type").equalsIgnoreCase("search")) {

            searchApi();
        } else if (getArguments().getString("type").equalsIgnoreCase("normal")) {

            Log.d("fsdgdsfgdfgh", getArguments().getString("cat_id").toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Api.categoryList + "?category_id=" + getArguments().getString("cat_id").toString() + "&page_no=all", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.d("Okhttp","Second Level : "+response);

                    DataList.clear();
                    Getseter.exitdialog(dialog);
                    if (response.optString("status").equalsIgnoreCase("success")){
                        gridview.setVisibility(View.VISIBLE);
                        NoRecordFound.setVisibility(View.GONE);
                        jsonArray = response.optJSONArray("message");
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);

                        DataList.add(new Getseter(jsonObject.optString("id"), jsonObject.optString("category"), jsonObject.optString("photo"), jsonObject.optString("photo")));

                        gridview.setAdapter(adapter);
                    }
                    }else{
                        gridview.setVisibility(View.GONE);
                        NoRecordFound.setVisibility(View.VISIBLE);
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
        }

        return view;
    }

    private void searchApi() {
        Log.d("Sgdfgdfgdfgdfg", getArguments().getString("query"));
        Getseter.showdialog(dialog);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Api.searchSubCat + "?keyword=" + getArguments().getString("query").toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                DataList.clear();
                Getseter.exitdialog(dialog);
                if (response.optString("status").equalsIgnoreCase("success")) {
                    jsonArray = response.optJSONArray("message");

                    Log.d("dfgdfgsdfgsdfgds", String.valueOf(response));
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.optJSONObject(i);

                            JSONArray jsonArray1 = jsonObject.optJSONArray("sizes");
                            //DataList2.clear();
                            Log.d("fdgdgdfgd", jsonArray1.toString());
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                JSONObject jsonObject1 = jsonArray1.optJSONObject(j);

                                Log.d("gdfgdfgdfghdfgs", jsonArray1.toString());
                                Log.d("fdgdfdgdfggdfgd", jsonObject1.optString("sell_price"));

                                DataList.add(new Getseter(jsonObject.optString("id"), jsonObject.optString("product_name"), jsonObject.optString("photo"), jsonObject1.optString("id"), jsonObject1.optString("psize_id"), jsonObject1.optString("size"), jsonObject1.optString("mrp_price"), jsonObject1.optString("discount"), jsonObject1.optString("sell_price"), jsonObject1.optString("psize_image")));

                            }
                            gridview.setAdapter(adapter);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "No product found...", Toast.LENGTH_SHORT).show();
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

    }

    class Adapter extends BaseAdapter {

        NetworkImageView image;
        LayoutInflater inflater;
        GridView gridview2;


        Adapter() {
            inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater == null) {
                throw new AssertionError("LayoutInflater not found.");
            }
        }

        @Override
        public int getCount() {
            return DataList.size();
        }

        @Override
        public Object getItem(int position) {
            return DataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.custon_subcatagory_second_level_view, parent, false);
            image = (NetworkImageView) convertView.findViewById(R.id.imageView);
            TextView name = (TextView) convertView.findViewById(R.id.name);

            LinearLayout liner = (LinearLayout) convertView.findViewById(R.id.liner);


            liner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new SubCatagoryFragment();
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout, R.anim.frag_fade_right, R.anim.frag_fad_left);
                    ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                    Bundle bundle = new Bundle();
//                position=position-1;
                    bundle.putString("cat_id", DataList.get(position).getID().toString());
                    bundle.putString("type", "normal");
                    bundle.putString("name",""+DataList.get(position).getName());
                    bundle.putString("query", "");
                    Log.d("fsdgfsdgdf", DataList.get(position).getID().toString());
                    Log.d("fsdgfsdgdf", String.valueOf(position));
                    fragment.setArguments(bundle);
                }
            });

            ImageLoader imageLoader = AppController.getInstance().getImageLoader();

            name.setText(DataList.get(position).getName().toString());
            image.setImageUrl(DataList.get(position).getCount(),imageLoader);

            return convertView;
        }
    }

}
