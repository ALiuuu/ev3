package com.aliu.ev3.ev3.main;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliu.ev3.R;
import com.aliu.ev3.ev3.CoreApplication;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/**
 * Created by ALiu on 2018/4/11.
 */
public class VpAdapter extends PagerAdapter {

    private Context context;

    public ArrayList<BoxBean.ItemBean> beans;


    private VpListener listener;

    public VpAdapter(Context context, ArrayList<BoxBean.ItemBean> beans, VpListener listener) {
        super();
        this.context = context;
        this.beans = beans;
        this.listener = listener;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);// 删除页卡
    }

    // 这个方法用来实例化页卡
    public Object instantiateItem(ViewGroup container, int position) {
        View inflate = View.inflate(context, R.layout.layout_item, null);
        ImageView imageView = inflate.findViewById(R.id.iv_photo_item);
        if (!TextUtils.isEmpty(beans.get(position).photourl)) {
            Glide.with(CoreApplication.instance)
                    .load(beans.get(position).photourl)
                    .into(imageView);
        } else {
            Glide.with(CoreApplication.instance)
                    .load(beans.get(position).res)
                    .into(imageView);
        }
        inflate.setOnClickListener(v -> {
            listener.onclick(beans.get(position).i);
        });
        TextView textView = inflate.findViewById(R.id.tv_box_item);
        textView.setText("格子：" + beans.get(position).i);
        container.addView(inflate);// 添加页卡
        return inflate;
    }

    // 返回页卡的数量
    public int getCount() {
        return beans.size();
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;// 官方提示这样写
    }


    public interface VpListener {
        void onclick(int id);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
