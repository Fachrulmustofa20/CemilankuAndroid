package com.example.cemilanku.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.cemilanku.Model.ModelData;
import com.example.cemilanku.R;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData> {
    private List<ModelData> mItems;
    private Context context;
    private ItemClickListener clickListener;

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    public AdapterData(Context context, List<ModelData> items) {
        this.mItems = items;
        this.context = context;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,
                parent, false);
        HolderData holderData = new HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(HolderData holder, int position) {
        ModelData md = mItems.get(position);
        holder.tvnama.setText(md.getNama());
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        holder.tvharga.setText(decimalFormat.format(md.getHarga()));
        Glide.with(context).load(md.getGambar()).thumbnail(0.5f).transition(withCrossFade()).
                diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivgambar);
        holder.md = md;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class HolderData extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvnama, tvharga;
        ImageView ivgambar;
        ModelData md;

        public HolderData(View view) {
            super(view);
            tvnama = (TextView) view.findViewById(R.id.nama);
            tvharga = (TextView) view.findViewById(R.id.harga);
            ivgambar = (ImageView) view.findViewById(R.id.gambar);
            view.setTag(view);
            view.setOnClickListener(this);
            tvnama.setOnClickListener(this);
            ivgambar.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null)
                clickListener.onClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
