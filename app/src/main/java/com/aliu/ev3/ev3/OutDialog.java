package com.aliu.ev3.ev3;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliu.ev3.R;
import com.aliu.ev3.ev3.bluetooth.Ev3BtManager;
import com.aliu.ev3.ev3.main.BoxBean;
import com.aliu.ev3.ev3.main.VpAdapter;
import com.bumptech.glide.Glide;

/**
 * 下载条
 * <p>
 * Created by liudong on 2017/8/2.
 */

public class OutDialog extends Dialog {

    protected Context mContext;

    private static final String TAG = "DownLoadDialog";
    // 主View
    private View contentView;

    //标题
    private ImageView            mIv;
    private TextView               mBtCancel;
    private TextView               mBtSure;
    //监听
    private UnLockDialogListener listener;

    private VpAdapter thisAdapter;

    private BoxBean          boxBean;
    private TextView         mTvTittle;
    private BoxBean.ItemBean thisBean;
    private String boxName = "abc";

    public OutDialog(Context context, BoxBean boxBean) {
        super(context, R.style.FullDialogTheme);
        mContext = context;
        this.boxBean = boxBean;
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


    public void show(int id, VpAdapter thisAdapter) {
        this.thisAdapter = thisAdapter;
        thisBean = boxBean.get(id);
        if (!TextUtils.isEmpty(thisBean.photourl)) {
            Glide.with(CoreApplication.instance)
                    .load(thisBean.photourl)
                    .into(mIv);
        } else {
            Glide.with(CoreApplication.instance)
                    .load(thisBean.res)
                    .into(mIv);
        }

        mTvTittle.setText("是否取出这件物品?");
        mBtSure.setText("取出");
        mBtSure.setOnClickListener(v -> {
            toOut();
        });
        mBtCancel.setText("取消");
        mBtCancel.setOnClickListener(v -> {
            dismiss();
        });
        show();

    }

    private void toOut() {
        Ev3BtManager.getInstance().sendMsg(boxName, thisBean.i + "");
        mTvTittle.setText("请在取出后点击确认");
        mBtSure.setText("确认");
        mBtSure.setOnClickListener(v -> {
            boxBean.remove(thisBean.i);
            thisAdapter.notifyDataSetChanged();
            Ev3BtManager.getInstance().sendMsg(boxName,  "-1");
            dismiss();
        });
        mBtCancel.setOnClickListener(v -> {
            Ev3BtManager.getInstance().sendMsg(boxName,  "-1");
            dismiss();
        });
        mBtCancel.setText("放弃");
    }

    /**
     * 初始化监听
     */
    private void initListener() {

    }


    public interface UnLockDialogListener {
        void onUnLock(String id);

        void onCancel();
    }
}
