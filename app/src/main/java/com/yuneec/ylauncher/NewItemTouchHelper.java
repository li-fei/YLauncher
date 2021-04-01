package com.yuneec.ylauncher;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.graphics.Canvas;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import androidx.recyclerview.widget.RecyclerView;
import com.yuneec.ylauncher.utils.Logg;
import java.util.ArrayList;
import java.util.Collections;

public class NewItemTouchHelper extends Callback {
    private GridAdapter adapter;
    private ArrayList<ResolveInfo> showApps;
    private final Vibrator mVibrator;
    private Context context;

    public NewItemTouchHelper(Context context, GridAdapter adapter, ArrayList<ResolveInfo> list) {
        super();
        this.adapter = adapter;
        this.showApps = list;
        this.context = context;
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags;
        int swipFlags;
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
//            Logg.loge("getMovementFlags ...");
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            swipFlags = 0;
        } else {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            swipFlags = 0;
        }
        return makeMovementFlags(dragFlags, swipFlags);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//        Logg.loge("onChildDraw ..." + " dX:" + dX + " dY:" + dY + " actionState:" + actionState + " isCurrentlyActive:" + isCurrentlyActive);
        if (dX != 0 && dY != 0) {
            adapter.dismissAppMenu();
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
//        Logg.loge("onMove ... fromPosition:" + fromPosition + " ,toPosition:" + toPosition);
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(showApps, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(showApps, i, i - 1);
            }
        }
        adapter.notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//        Logg.loge("onSwiped ...");
    }


    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            mVibrator.vibrate(60);
//            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.setBackgroundColor(0);
        super.clearView(recyclerView, viewHolder);
    }

}

