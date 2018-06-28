package com.anlijiu.example.ui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.List;

/**
 * the default effect of android recyclerView provide about moving item
 * StackLayoutManager is not compatible for recyclerView about item move(item_0 element is flashing when moved)
 * VerticalDividerItemDecoration effect conflict with StackLayoutManager
 */
public class VerticalItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private OnItemTouchCallbackListener onItemTouchCallbackListener;

    public VerticalItemTouchHelperCallback(OnItemTouchCallbackListener onItemTouchCallbackListener) {
        this.onItemTouchCallbackListener = onItemTouchCallbackListener;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        if (null != onItemTouchCallbackListener) {
            return onItemTouchCallbackListener.getLongPressEditEnabled();
        }
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int swipeFlags = ItemTouchHelper.ACTION_STATE_IDLE;//temp no handle
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder srcViewHolder, RecyclerView.ViewHolder targetViewHolder) {
        if (onItemTouchCallbackListener != null) {
            return onItemTouchCallbackListener.onMove(srcViewHolder.getAdapterPosition(), targetViewHolder.getAdapterPosition());
        }
        return false;
    }

    @Override
    public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
        //fraction = (item_width / 2 + divider_width) / item_width
        float fraction = 0.5f; //temp no define, default is 0.5f
        return fraction;
    }

    @Override
    public RecyclerView.ViewHolder chooseDropTarget(RecyclerView.ViewHolder selected, List<RecyclerView.ViewHolder> dropTargets, int curX, int curY) {
//        return super.chooseDropTarget(selected, dropTargets, curX, curY);

        int right = curX + selected.itemView.getWidth();
        int bottom = curY + selected.itemView.getHeight();
        RecyclerView.ViewHolder winner = null;
        int winnerScore = -1;
        final int dx = curX - selected.itemView.getLeft();
        final int dy = curY - selected.itemView.getTop();
        final int targetsSize = dropTargets.size();
        for (int i = 0; i < targetsSize; i++) {
            final RecyclerView.ViewHolder target = dropTargets.get(i);
            if (dx > 0) {
//                int diff = target.itemView.getRight() - right;
                int diff = target.itemView.getRight() - right - target.itemView.getWidth() / 2;
                if (diff < 0 && target.itemView.getRight() > selected.itemView.getRight()) {
                    final int score = Math.abs(diff);
                    if (score > winnerScore) {
                        winnerScore = score;
                        winner = target;
                    }
                }
            }
            if (dx < 0) {
//                int diff = target.itemView.getLeft() - curX;
                int diff = target.itemView.getLeft() - curX + target.itemView.getWidth() / 2;
                if (diff > 0 && target.itemView.getLeft() < selected.itemView.getLeft()) {
                    final int score = Math.abs(diff);
                    if (score > winnerScore) {
                        winnerScore = score;
                        winner = target;
                    }
                }
            }
            if (dy < 0) {
                int diff = target.itemView.getTop() - curY;
                if (diff > 0 && target.itemView.getTop() < selected.itemView.getTop()) {
                    final int score = Math.abs(diff);
                    if (score > winnerScore) {
                        winnerScore = score;
                        winner = target;
                    }
                }
            }

            if (dy > 0) {
                int diff = target.itemView.getBottom() - bottom;
                if (diff < 0 && target.itemView.getBottom() > selected.itemView.getBottom()) {
                    final int score = Math.abs(diff);
                    if (score > winnerScore) {
                        winnerScore = score;
                        winner = target;
                    }
                }
            }
        }
        return winner;
    }


    //temp no handle function, add optimization function in the future

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    //provide listener

    public interface OnItemTouchCallbackListener {
        boolean onMove(int srcPosition, int targetPosition);

        boolean getLongPressEditEnabled();
    }
}
