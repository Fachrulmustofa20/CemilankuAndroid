package com.example.cemilanku;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.cemilanku.Util.AppController;
import com.example.cemilanku.Util.ServerAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {
    ProgressDialog pd;
    int total, bayar, kembali = -1;
    DecimalFormat decimalFormat;
    TextView totalCheckout, kembalian ,tanggal;
    EditText pembayaran;
    Toast toast;

    int[] qty;
    String[] kode;
    String username;
    SharedPreferences sharedpreferences;



    public static final String TAG_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        username = getIntent().getStringExtra(TAG_USERNAME);
        kode = getIntent().getStringArrayExtra("kode");
        qty = getIntent().getIntArrayExtra("qty");

        tanggal = (TextView) findViewById(R.id.time);
        String tanggal_sekarang = getCurrentDate();
        tanggal.setText(tanggal_sekarang);

        pd = new ProgressDialog(CheckoutActivity.this);
        toast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
        totalCheckout = (TextView) findViewById(R.id.total);
        total = getIntent().getIntExtra("total", 0);

        decimalFormat = new DecimalFormat("#,##0.00");
        totalCheckout.setText(decimalFormat.format(total));


        pembayaran = (EditText) findViewById(R.id.jumlah);
        kembalian = (TextView) findViewById(R.id.kembali);

        pembayaran.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (pembayaran.getText().toString().trim().length() != 0) {
                    bayar = Integer.parseInt(pembayaran.getText().toString());
                    kembali = bayar - total;

                    if (kembali < 0) {
                        kembalian.setText("0.00");
                    } else {
                        kembalian.setText(decimalFormat.format(kembali));
                    }
                } else {
                    kembalian.setText("0.00");
                }
            }
        });

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kembali < 0) {
                    toast.setText("Uang pembayarannya kurang");
                    toast.show();
                } else {
                    createJual();
                    Intent finish = new Intent(getApplicationContext(), NotaActivity.class);
                    finish.putExtra(TAG_USERNAME, username);
                    startActivity(finish);
                }
            }
        });
    }

    private void createJual() {
        pd.setMessage("Mencetak Nota");
        pd.setCancelable(false);
        pd.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, ServerAPI.URL_JUAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();

                        try {
                            JSONObject res = new JSONObject(response);
                            toast.setText(res.getString("message"));
                            toast.show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        toast.setText("Gagal Cetak Nota");
                        toast.show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to nota
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("subtotal", Integer.toString(total));
                params.put("pembayaran", Integer.toString(bayar));
                params.put("kembalian", Integer.toString(kembali));

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq);
    }
    public String getCurrentDate() {
        final Calendar c = Calendar.getInstance();
        int year, month, day;
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DATE);
        return day + "/" + (month + 1) + "/" + year;
    }

}
