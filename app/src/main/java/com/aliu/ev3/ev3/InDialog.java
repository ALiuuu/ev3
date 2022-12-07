package com.aliu.ev3.ev3;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliu.ev3.R;
import com.bumptech.glide.Glide;

/**
 * 下载条
 * <p>
 * Created by liudong on 2017/8/2.
 */

public class InDialog extends Dialog {

    protected Context mContext;

    private static final String TAG = "DownLoadDialog";
    // 主View
    private View contentView;

    //标题
    private ImageView mIv;
    private TextView  mBtCancel;
    private TextView  mBtSure;


    private TextView mTvTittle;
    private String boxName = "abc";

    private ClickListener listener;

    public InDialog(Context context, ClickListener listener) {
        super(context, R.style.FullDialogTheme);
        mContext = context;
        this.listener = listener;
        init(context);
    }

    // ========= 私有方法 =========

    /**
     * 初始化
     */
    private void init(Context context) {
        this.mContext = context;
        initConfig();
        initView();
        initListener();
    }

    /**
     * 初始化配置
     */
    private void initConfig() {
        Window window = getWindow();  //获得当前窗体
        if (window != null) {
            window.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT; //设置宽度
            lp.height = WindowManager.LayoutParams.MATCH_PARENT; //设置高度
            window.setAttributes(lp);
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_unlock, null);
        setContentView(contentView);
        mIv = findViewById(R.id.iv_show);
        mBtCancel = findViewById(R.id.bt_cancel);
        mBtSure = findViewById(R.id.bt_true);

        mTvTittle = findViewById(R.id.tv_tittle);

        mBtCancel.setOnClickListener(v -> {
            dismiss();
        });

    }

    private int    thisRes;
    private String thisUrl;


    public void show(int res, String url) {
        thisRes = res;
        thisUrl = url;
        if (!TextUtils.isEmpty(url)) {
            Glide.with(CoreApplication.instance)
                    .load(url)
                    .into(mIv);
        } else {
            Glide.with(CoreApplication.instance)
                    .load(res)
                    .into(mIv);
        }

        mTvTittle.setText("请在放入后点击确认?");
        mBtSure.setText("确认");
        mBtSure.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSure(thisRes, thisUrl);
            }
            dismiss();
        });
        mBtCancel.setText("取消");

        show();

    }


    /**
     * 初始化监听
     */
    private void initListener() {

    }


    public interface ClickListener {
        void onSure(int res, String url);

    }
}
