package com.example.cemilanku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.cemilanku.Adapter.AdapterData;
import com.example.cemilanku.Model.ModelData;
import com.example.cemilanku.Util.AppController;
import com.example.cemilanku.Util.ServerAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class MainActivity extends AppCompatActivity implements AdapterData.ItemClickListener {
    RecyclerView mRecyclerview;
    AdapterData mAdapter;
    List<ModelData> mItems;
    ProgressDialog pd;
    Toast toast;
    int total = 0;

    String username;
    SharedPreferences sharedpreferences;

    public static final String TAG_USERNAME = "username";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        username = getIntent().getStringExtra(TAG_USERNAME);

        toast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
        mRecyclerview = (RecyclerView) findViewById(R.id.recycle);
        pd = new ProgressDialog(MainActivity.this);
        mItems = new ArrayList<>();
        loadJson();
        mRecyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new AdapterData(MainActivity.this, mItems);
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if (username != null) {
            MenuItem info = menu.findItem(R.id.action_info);
            info.setTitle("Hai @" + username);
            info.setEnabled(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent = new Intent();
        String data;

        String phoneNo = "089654445555";
        //noinspection SimplifiableIfStatement
        switch (id) {

            case R.id.action_refresh:
                total = 0;
                TextView tvtotal = (TextView) findViewById(R.id.total);
                tvtotal.setText("0.00");
                for (int i = 0; i < mItems.size(); i++) {
                    ModelData md = mItems.get(i);
                    md.setJumlah(0);
                }
                intent.setClass(this, MainActivity.class);
                intent.putExtra(TAG_USERNAME, username);
                startActivity(intent);
                finish();
                return true;

            case R.id.action_callcenter:
                if(!TextUtils.isEmpty(phoneNo)) {
                    String dial = "tel:" + phoneNo;
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
                }else {
                    Toast.makeText(MainActivity.this, "Enter a phone number", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.action_sms:
                if(!TextUtils.isEmpty(phoneNo)) {
                    Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNo));
                    startActivity(smsIntent);
                }
                return true;

            case R.id.action_lokasi:
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=-6.982389, 110.409000(Semarang)");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                return true;

                /*
            case R.id.action_update:

                intent.setClass(MainActivity.this, UpdateActivity.class);
                intent.putExtra(TAG_USERNAME, username);
                startActivity(intent);
                return true;

                 */
            case R.id.logout:
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(LoginActivity.session_status, false);
                editor.putString(TAG_USERNAME, null);
                editor.commit();

                intent.setClass(MainActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadJson() {
        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        pd.show();
        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.POST, ServerAPI.URL_DASHBOARD,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pd.cancel();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject data = response.getJSONObject(i);

                        ModelData md = new ModelData();
                        md.setKode(data.getInt("id_produk"));
                        md.setNama(data.getString("nama"));
                        md.setHarga(data.getInt("harga"));
                        md.setGambar(ServerAPI.URL_IMAGE + data.getString("gambar"));
                        md.setDeskripsi(data.getString("deskripsi"));
                        md.setJumlah(0);
                        mItems.add(md);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();

                        Log.d("volley", "error : " + error.getMessage());
                    }
                });

        AppController.getInstance().addToRequestQueue(reqData);
    }

    public void onClick(View view, int position) {
        //final Produk mhs = produkArrayList.get(position);
        final ModelData md = mItems.get(position);
        switch (view.getId()) {
            case R.id.nama:
                Intent detail = new Intent(this, DetailActivity.class);
                detail.putExtra("nama", md.getNama());
                detail.putExtra("harga", md.getHarga());
                detail.putExtra("gambar", md.getGambar());
                detail.putExtra("deskripsi", md.getDeskripsi());
                startActivity(detail);
                return;
            case R.id.gambar:
                total = total + md.getHarga();
                toast.setText("Pesanan....." + md.getNama());
                toast.show();
                md.setJumlah(md.getJumlah() + 1);
                postTotals();
                return;
            default:
                toast.setText("Info..... -> " + md.getNama() + " Rp. " + md.getHarga());
                toast.show();
                break;
        }
    }

    public void postTotals() {
        TextView tvtotal = (TextView) findViewById(R.id.total);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        tvtotal.setText(decimalFormat.format(total));
    }

    public void checkout(View view) {
        if (total > 0) {
            int[] kode = new int[mItems.size()];
            int[] qty = new int[mItems.size()];
            Intent checkout = new Intent(this, CheckoutActivity.class);
            for (int i = 0; i < mItems.size(); i++) {
                ModelData md = mItems.get(i);
                kode[i] = md.getKode();
                qty[i] = md.getJumlah();
            }
            checkout.putExtra("total", total);
            checkout.putExtra("kode", kode);
            checkout.putExtra("qty", qty);
            checkout.putExtra(TAG_USERNAME, username);
            startActivity(checkout);
        } else {
            toast.setText("Belanja Dulu");
            toast.show();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Apakah kamu ingin keluar?");
        builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
