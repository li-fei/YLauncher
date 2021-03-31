package com.yuneec.ylauncher.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yuneec.ylauncher.R;


public class ToastUtil {
    private boolean showToast = true;
    public static  ToastUtil toastUtil;
    private Toast toast;
    private Toast mToast;

    private ToastUtil() {
    }

    public static  ToastUtil getInstance() {
        if (toastUtil == null) {
            toastUtil = new  ToastUtil();
        }
        return toastUtil;
    }

    public void toastShow(Context context, String str) {
        if (!showToast) {
            return;
        }
        toastCancel();
        View view = LayoutInflater.from(context).inflate(R.layout.item_toast_view, null);
        TextView text = (TextView) view.findViewById(R.id.tv_toast_name);
        text.setText(str);
        toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 300);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    public void toastCancel() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
