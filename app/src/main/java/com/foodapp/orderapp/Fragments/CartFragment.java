package com.foodapp.orderapp.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.foodapp.orderapp.Activity.Login;
import com.foodapp.orderapp.Activity.Navigation;
import com.foodapp.orderapp.R;
import com.foodapp.orderapp.Utils.AppController;
import com.foodapp.orderapp.Utils.DatabaseHandler;
import com.foodapp.orderapp.Utils.Getseter;
import com.foodapp.orderapp.Utils.MyPrefrences;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends AppCompatActivity {

    Fragment fragment;
    GridView gridview;
    String image;
    DatabaseHandler db;
    List<Getseter> DataList=new ArrayList<Getseter>();
    Button checkout;
    Adapter adapter;
    Dialog dialog;
    ImageView remove;
    double total;
    int totalItem;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cart);

        db = new DatabaseHandler(getApplicationContext());
        DataList=db.getAllCatagory();

        gridview=(GridView)findViewById(R.id.gridview);
        checkout=(Button)findViewById(R.id.checkout);
        remove=(ImageView)findViewById(R.id.remove);

        dialog=new Dialog(getApplicationContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        adapter=new Adapter();

        setTitle("Cart");



        // Toast.makeText(getActivity(), DataList.size()+"", Toast.LENGTH_SHORT).show();

        if (DataList.size()==0){
            checkout.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Your Cart is empty", Toast.LENGTH_SHORT).show();
        }
        else{
            checkout.setVisibility(View.VISIBLE);

        }

        gridview.setAdapter(adapter);
        Log.d("dfgdfghdfhdfh",DataList.toString());

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteCatogory();
                Navigation.textOne.setText("0");
                Intent intent = new Intent(getApplicationContext(), CartFragment.class);
                startActivity(intent);
                finish();

            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MyPrefrences.getUserLogin(getApplicationContext())==false){
                    popForLogin();
                }
                else {

                    Intent intent = new Intent(getApplicationContext(), Delivery.class);
                    intent.putExtra("orderitem", String.valueOf(DataList.size()));
                    intent.putExtra("cal_price", String.valueOf(total));
                    intent.putExtra("totalItem", String.valueOf(totalItem));
                    intent.putExtra("length",""+ DataList.size());
                    startActivity(intent);


//                    Fragment fragment = new Delivery();
//                    FragmentManager manager = getFragmentManager();
//                    FragmentTransaction ft = manager.beginTransaction();
//                    ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout, R.anim.frag_fade_right, R.anim.frag_fad_left);
//                    ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("orderitem", String.valueOf(DataList.size()));
//                    bundle.putString("cal_price", String.valueOf(total));
//                    bundle.putString("totalItem", String.valueOf(totalItem));
//                    bundle.putInt("length", DataList.size());
//                    fragment.setArguments(bundle);
                }
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d("gdfgdfgdfgdfgd",DataList.get(i).getProductId().toString());

//                Fragment fragment=new CatagoryViewFragment();
//                FragmentManager manager=getFragmentManager();
//                FragmentTransaction ft=manager.beginTransaction();
//                ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
//                Bundle bundle=new Bundle();
//                bundle.putString("product_id",DataList.get(i).getProductId().toString());
//                //bundle.putString("product_image",DataList.get(position).getDesc().toString());
//                fragment.setArguments(bundle);
            }
        });


    }

    private void popForLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setMessage("Please Login to Checkout")
                .setCancelable(false)
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent=new Intent(getApplicationContext(),Login.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        finish();
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
        alert.setTitle(""+MyPrefrences.getUSENAME(getApplicationContext()));
        alert.show();
    }

    class  Adapter extends BaseAdapter {

        NetworkImageView image;
        LayoutInflater inflater;
        TextView id,name,price,newprice,date,qty,close;
        int bal;
        int np1;
        Adapter(){
            inflater=(LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            convertView=inflater.inflate(R.layout.custonlistview_cart,parent,false);

            id=(TextView)convertView.findViewById(R.id.id);
            name=(TextView)convertView.findViewById(R.id.name);
            qty=(TextView)convertView.findViewById(R.id.qty);
            price=(TextView)convertView.findViewById(R.id.price);
            newprice=(TextView)convertView.findViewById(R.id.newprice);
            close=(TextView)convertView.findViewById(R.id.close);
//            method=(TextView)convertView.findViewById(R.id.method);
//            amount=(TextView)convertView.findViewById(R.id.amount);
//            status=(TextView)convertView.findViewById(R.id.status);
            image=(NetworkImageView)convertView.findViewById(R.id.image);

            id.setText(DataList.get(position).getName().toString());
           // name.setText(DataList.get(position).getName().toString());
            qty.setText(DataList.get(position).getUdate().toString());
            price.setText("Price ₹: "+DataList.get(position).getCdate());

//            int m1=Integer.parseInt(qty.getText().toString());
//            int m2=Integer.parseInt(price.getText().toString());
//            int m=m1*m2;
            Log.d("sdfdsgfdgdf",DataList.get(position).getTime());


            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.removeSingleContact(new Getseter(DataList.get(position).getTime().toString(),null,null,null));

                    Intent intent = new Intent(getApplicationContext(), CartFragment.class);
                    startActivity(intent);
                    finish();
//                    fragment=new CartFragment();
//                    FragmentManager fm=getFragmentManager();
//                    FragmentTransaction ft = fm.beginTransaction();
//                    ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
////                    ft.remove(fragment);
//                    ft.replace(R.id.content_frame, fragment).addToBackStack(null);
//                    ft.commit();

                    int num=Integer.valueOf(Navigation.textOne.getText().toString());
                    num=num-1;
                    Navigation.textOne.setText(num+"");


                }
            });
            newprice.setText("Total Amount ₹: "+DataList.get(position).getUdate3());

            total=0.0;
            totalItem=0;

            for(int i=0;i<DataList.size();i++){
                total=total+Double.parseDouble(DataList.get(i).getUdate3());
                totalItem=totalItem+Integer.parseInt(DataList.get(i).getUdate());


            }


//            int np=Integer.parseInt(newprice.getText().toString());

//            np1=np1+np;
//            checkout.setText(""+np1);

            Log.d("fgdfgdfhdfh",total+"");
            Log.d("dfdfdfdfgetgs",totalItem+"");
            Log.d("fgdfgdfhgdfh",DataList.size()+"");
//            Toast.makeText(getActivity(), ""+m, Toast.LENGTH_SHORT).show();
//            amount.setText(DataList.get(position).getCount().toString());
//            method.setText(DataList.get(position).getImg().toString());
//            status.setText(DataList.get(position).getCdate().toString());
            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            image.setImageUrl(DataList.get(position).getCount().toLowerCase(),imageLoader);
            Log.d("dsfsdgfsdgfsdg",DataList.get(position).toString());

//            bal=bal+Integer.parseInt(DataList.get(position).getCdate());
//            Toast.makeText(getActivity(), ""+bal, Toast.LENGTH_SHORT).show();
            return convertView;
        }
    }




}
