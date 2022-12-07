package com.aliu.ev3.ev3.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aliu.ev3.R;
import com.aliu.ev3.ev3.CoreApplication;
import com.bumptech.glide.Glide;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ALiu on 2018/4/12.
 */
public class RvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "MoreListAdapter";
    private Context mContext;

    private int[] res;

    private RvClick rvlick;

    /**
     * @param c
     */
    public RvAdapter(Context c, int[] res, RvClick rvlick) {
        this.mContext = c;
        this.res = res;
        this.rvlick = rvlick;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;

        if (CoreApplication.isPad) {
            holder = new PhotoHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_rv_pad, parent, false));

        } else {
            holder = new PhotoHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_rv, parent, false));

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        PhotoHolder tittleHolder = (PhotoHolder) holder;
        tittleHolder.bindItem(res[position]);

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return res.length;
    }

    //Tittle
    private class PhotoHolder extends RecyclerView.ViewHolder {

        ImageView mPhoto;

        PhotoHolder(View itemView) {
            super(itemView);
            mPhoto = itemView.findViewById(R.id.iv_photo_item);

        }

        void bindItem(int res) {
            mPhoto.setOnClickListener(v -> {
                rvlick.onClick(v, res);
            });
            Glide.with(CoreApplication.instance)
                    .load(res)
                    .into(mPhoto);
        }
    }


    public interface RvClick {
        void onClick(View v, int res);
    }
}
