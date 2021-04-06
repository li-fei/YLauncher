package com.yuneec.ylauncher.views;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.yuneec.ylauncher.utils.AppsPopMenuManager;
import com.yuneec.ylauncher.utils.LauncherAppState;
import com.yuneec.ylauncher.R;
import com.yuneec.ylauncher.utils.MessageEvent;
import com.yuneec.ylauncher.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

public class MenuPopupWindow extends PopupWindow {

    private Context context;
    private View mRootView;
    private Animation showAnim, hideAnim;
    private String packageName;
    private String menu1;
    private String menu2;
    private TextView menu_item_remove, menu_item_detail, menu_item1, menu_item2;

    public MenuPopupWindow(Context context) {
        super(context);
        this.context = context;
        initialize();
    }

    public MenuPopupWindow(Context context, String packageName, String... parms) {
        super(context);
        this.context = context;
        this.packageName = packageName;
        if (parms.length == 1) {
            menu1 = parms[0];
        } else if (parms.length == 2) {
            menu1 = parms[0];
            menu2 = parms[1];
        }
        initialize();
    }

    private void initialize() {
        mRootView = LayoutInflater.from(context).inflate(R.layout.popup_content_layout, null);
        menu_item_remove = mRootView.findViewById(R.id.menu_item_remove);
        menu_item_detail = mRootView.findViewById(R.id.menu_item_detail);
        menu_item1 = mRootView.findViewById(R.id.menu_item1);
        menu_item2 = mRootView.findViewById(R.id.menu_item2);
        this.setContentView(mRootView);
        this.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)));
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        View.OnClickListener menuItemOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtil.getInstance().toastShow(context, "Click " + ((TextView) v).getText());
                dismiss();
                switch (v.getId()) {
                    case R.id.menu_item_remove:
                        if (LauncherAppState.getInstance().isSystemApp(packageName)) {
                            ToastUtil.getInstance().toastShow(context, "No delete permission ...");
                        } else {
                            deleteApp(packageName);
                        }
                        break;
                    case R.id.menu_item_detail:
                        gotoAppDetailIntent(packageName);
                        break;
                    case R.id.menu_item1:
                        menuItemAction(menu1);
                        break;
                    case R.id.menu_item2:
                        menuItemAction(menu2);
                        break;
                }
            }
        };
        if (menu1 == null) {
            menu_item1.setVisibility(View.GONE);
        } else {
            menu_item1.setVisibility(View.VISIBLE);
            menu_item1.setText(menu1);
        }
        if (menu2 == null) {
            menu_item2.setVisibility(View.GONE);
        } else {
            menu_item2.setVisibility(View.VISIBLE);
            menu_item2.setText(menu2);
        }
        menu_item_remove.setOnClickListener(menuItemOnClickListener);
        menu_item_detail.setOnClickListener(menuItemOnClickListener);
        menu_item1.setOnClickListener(menuItemOnClickListener);
        menu_item2.setOnClickListener(menuItemOnClickListener);
    }

    private int[] location = new int[2];

    public void showPopupWindow(View v) {
        v.getLocationOnScreen(location);
        mRootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int measuredHeight = mRootView.getMeasuredHeight();
        int measuredWidth = mRootView.getMeasuredWidth();
        int width = v.getMeasuredWidth();
        int position_x = location[0] + 50;
        int position_y = location[1] - measuredHeight;
        showAnim = createVerticalAnimation(1, 0);
        hideAnim = createVerticalAnimation(0, 1);
        mRootView.startAnimation(showAnim);
        this.showAtLocation(v, Gravity.NO_GRAVITY, position_x, position_y);
        hideAnim.setAnimationListener(new AnimListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                MenuPopupWindow.super.dismiss();
            }
        });
    }

    @Override
    public void dismiss() {
        if (hideAnim != null) {
            mRootView.startAnimation(hideAnim);
        } else {
            super.dismiss();
        }
    }

    private Animation createVerticalAnimation(float fromY, float toY) {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                fromY, Animation.RELATIVE_TO_SELF, toY);
        animation.setDuration(200);
        animation.setInterpolator(new DecelerateInterpolator());
        return animation;
    }

    public class AnimListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

    private void deleteApp(String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void gotoAppDetailIntent(String packageName) {
        EventBus.getDefault().post(new MessageEvent(packageName, MessageEvent.AppDetail));
    }

    private void menuItemAction(String menu) {
        AppsPopMenuManager.getInstance().setAppsMenu(context, packageName, menu);
    }
}
