package com.yuneec.ylauncher;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.yuneec.ylauncher.views.MenuPopupWindow;

import java.util.List;

class GridAdapter extends RecyclerView.Adapter<GridAdapter.Holder> {
    private static final String TAG = "GridAdapter";
    private Context context;
    private List<ResolveInfo> apps;
    private PackageManager packageManager;
    private MenuPopupWindow menuPopupWindow;

    private AppListener appListener;
    public void setAppListener(AppListener appListener) {
        this.appListener = appListener;
    }

    public interface AppListener {
        void appDelete(String packageName);
    }

    GridAdapter(Context c, List<ResolveInfo> apps) {
        this.context = c;
        this.apps = apps;
        packageManager = context.getPackageManager();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_apps_view, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ActivityInfo info = apps.get(position).activityInfo;
        Drawable icon = info.loadIcon(packageManager);
        String name = (String) info.loadLabel(packageManager);
        holder.appIcon.setImageDrawable(icon);
        holder.appName.setText(name);
        String pkg = info.packageName;
        String cls = info.name;

        holder.itemView.setOnClickListener(v -> {
            ComponentName componentName = new ComponentName(pkg, cls);
            Intent intent = new Intent();
            intent.setComponent(componentName);
            context.startActivity(intent);
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showAppMenu(holder.itemView,pkg);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        private ImageView appIcon;
        private TextView appName;

        public Holder(View itemView) {
            super(itemView);
            appIcon = itemView.findViewById(R.id.app_icon);
            appName = itemView.findViewById(R.id.app_name);
        }
    }

    private void showAppMenu(View itemView, String pkg) {
        String menu1 = null;
        String menu2 = null;
        for (String[] app : AppsConfigs.AppsList) {
            if (pkg.equals(app[0])) {
                if (app.length == 2) {
                    menu1 = app[1];
                } else if (app.length == 3) {
                    menu1 = app[1];
                    menu2 = app[2];
                }
            }
        }
        menuPopupWindow = new MenuPopupWindow(context, pkg, menu1, menu2);
        menuPopupWindow.showPopupWindow(itemView);
    }

}
