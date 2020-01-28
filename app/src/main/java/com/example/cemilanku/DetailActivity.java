package com.example.cemilanku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.DecimalFormat;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class DetailActivity extends AppCompatActivity {
    TextView tvnama, tvharga, tvdeskripsi;
    ImageView ivgambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);



        tvnama = (TextView) findViewById(R.id.judulDetail);
        tvharga = (TextView) findViewById(R.id.hargaDetail);
        ivgambar = (ImageView) findViewById(R.id.gambarDetail);
        tvdeskripsi = (TextView) findViewById(R.id.deskripsiDetail);

        tvnama.setText(getIntent().getStringExtra("nama"));
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        tvharga.setText(decimalFormat.format(getIntent().getIntExtra("harga", 0)));
        Glide.with(getApplicationContext()).load(getIntent().getStringExtra("gambar"))
                .thumbnail(0.5f).transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(ivgambar);
        tvdeskripsi.setText(getIntent().getStringExtra("deskripsi"));
    }
}
