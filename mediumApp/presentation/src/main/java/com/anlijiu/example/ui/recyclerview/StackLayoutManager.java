package com.anlijiu.example.ui.recyclerview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewConfiguration;

import timber.log.Timber;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static com.anlijiu.example.ui.recyclerview.StackLayoutManager.Align.BOTTOM;
import static com.anlijiu.example.ui.recyclerview.StackLayoutManager.Align.LEFT;
import static com.anlijiu.example.ui.recyclerview.StackLayoutManager.Align.RIGHT;
import static com.anlijiu.example.ui.recyclerview.StackLayoutManager.Align.TOP;

/**
 * Created by CJJ on 2017/5/17.
 * my thought is simple：we assume the first item in the initial state is the base position ，
 * we only need to calculate the appropriate position{@link #left(int index)}for the given item
 * index with the given offset{@link #mTotalOffset}.After solve this thinking confusion ,this
 * layoutManager is easy to implement
 *
 * @author CJJ
 */

public class StackLayoutManager extends RecyclerView.LayoutManager {

    private static final String TAG = "StackLayoutManager";

    //the space unit for the stacked item
    private int mMarginStart = 195;
    private int mSpace = 40;
    private int mItemSpace = 32;
    /**
     * the offset unit,deciding current position(the sum of  {@link #mItemWidth} and {@link #mSpace})
     */
    private int mUnit = 1;
    //item width
    private int mItemWidth;
    private int mItemHeight;
    //the counting variable ,record the total offset including parallex
    private int mTotalOffset;
    //record the total offset without parallex
    private int mRealOffset;
    private ObjectAnimator animator;
    private int animateValue;
    private RecyclerView.Recycler recycler;
    private int lastAnimateValue;
    //the max stacked item count;
    private int maxStackCount = 3;
    //initial stacked item
    private int initialStackCount = 0;
    private float scaleRatio = 0.4f;
    private float parallex = 1f;
    private int initialOffset;
    private boolean initial;
    private int mMinVelocityX;
    private Align direction = LEFT;
    //当前放大的item position
    private int currentIndex = RecyclerView.NO_POSITION;

    enum Align {

        LEFT(1),
        RIGHT(-1),
        TOP(1),
        BOTTOM(-1);

        int layoutDirection;

        Align(int sign) {
            this.layoutDirection = sign;
        }
    }


    @SuppressWarnings("unused")
    public StackLayoutManager() {
        setAutoMeasureEnabled(true);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        log("onLayoutChildren is run.");
        this.recycler = recycler;
        detachAndScrapAttachedViews(recycler);

        if (state.getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }

        //got the mUnit basing on the first child,of course we assume that  all the item has the same size
        if (mUnit <= 1) {
            View anchorView = recycler.getViewForPosition(0);
            measureChildWithMargins(anchorView, 0, 0);
            mItemWidth = anchorView.getMeasuredWidth();
            mItemHeight = anchorView.getMeasuredHeight();

            if (canScrollHorizontally()) {
                mUnit = mItemWidth + mSpace + mItemSpace;
            } else {
                mUnit = mItemHeight + mSpace + mItemSpace;
            }
            mMinVelocityX = ViewConfiguration.get(anchorView.getContext()).getScaledMinimumFlingVelocity();
        }

        //because this method will be called twice
        initialOffset = resolveInitialOffset();

        fill(recycler, 0);
    }

    //we need take direction into account when calc initialOffset
    private int resolveInitialOffset() {
        int offset = initialStackCount * mUnit;
        if (direction == LEFT)
            return offset;
        if (direction == RIGHT)
            return -offset;
        if (direction == TOP)
            return offset;
        else return offset;
    }

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        if (!initial) {
            fill(recycler, initialOffset, false);
            initial = true;
        }
    }

    @Override
    public void onAdapterChanged(RecyclerView.Adapter oldAdapter, RecyclerView.Adapter newAdapter) {
        initial = false;
        mTotalOffset = mRealOffset = 0;
    }

    public int fill(RecyclerView.Recycler recycler, int dy) {
        return fill(recycler, dy, true);
    }

    /**
     * the magic function :).all the work including computing ,recycling,and layout is done here
     *
     * @param recycler {@link android.support.v7.widget.RecyclerView.Recycler}
     * @param dy       scroll distance
     * @param apply    true if apply parallex,but we don't when first layout process
     */
    private int fill(RecyclerView.Recycler recycler, int dy, boolean apply) {
        int delta = direction.layoutDirection * dy;
        // multiply the parallex factor
        if (apply)
            delta = (int) (delta * parallex);
        if (direction == LEFT)
            //if slices are processed at the entrance, the standard of different modes should be converted if mode is switched
            switch (mManagerModel) {
                case MANAGER_MODEL_DISPLAY:
                    return fillFromLeft(recycler, delta);
                case MANAGER_MODEL_EDIT:
                    return fillFromLeftInEdit(recycler, delta);
            }
        if (direction == RIGHT)
            return fillFromRight(recycler, delta);
        if (direction == TOP)
            return fillFromTop(recycler, delta);
        else return dy;
    }

    private int fillFromTop(RecyclerView.Recycler recycler, int dy) {
        if (mTotalOffset + dy < 0 || (mTotalOffset + dy + 0f) / mUnit > getItemCount() - 1)
            return 0;
        detachAndScrapAttachedViews(recycler);
        mTotalOffset += direction.layoutDirection * dy;
        int count = getChildCount();
        //removeAndRecycle  views
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (recycleVertically(child, dy))
                removeAndRecycleView(child, recycler);
        }
        int curPos = mTotalOffset / mUnit;
        int leavingSpace = getHeight() - (left(curPos) + mUnit);
        int itemCountAfterBaseItem = leavingSpace / mUnit + 2;
        int e = curPos + itemCountAfterBaseItem;

        int start = curPos - maxStackCount >= 0 ? curPos - maxStackCount : 0;
        int end = e >= getItemCount() ? getItemCount() - 1 : e;

        //layout views
        for (int i = start; i <= end; i++) {
            View view = recycler.getViewForPosition(i);

            float scale = scale(i);
            float alpha = alpha(i);

            addView(view);
            measureChildWithMargins(view, 0, 0);
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
            int left = getWidth() / 2 - mItemWidth / 2 + lp.leftMargin;
            int top = (int) (left(i) - (1 - scale) * view.getMeasuredHeight() / 2) + lp.topMargin;
            int right = view.getMeasuredWidth() + left - lp.rightMargin;
            int bottom = view.getMeasuredHeight() + top - lp.bottomMargin;

            layoutDecorated(view, left, top, right, bottom);
            view.setAlpha(alpha);
            view.setScaleY(scale);
            view.setScaleX(scale);
        }

        return dy;
    }

    private int fillFromRight(RecyclerView.Recycler recycler, int dy) {

        if (mTotalOffset + dy < 0 || (mTotalOffset + dy + 0f) / mUnit > getItemCount() - 1)
            return 0;
        detachAndScrapAttachedViews(recycler);
        mTotalOffset += dy;
        int count = getChildCount();
        //removeAndRecycle  views
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (recycleHorizontally(child, dy))
                removeAndRecycleView(child, recycler);
        }


        int curPos = mTotalOffset / mUnit;
        int leavingSpace = left(curPos);
        int itemCountAfterBaseItem = leavingSpace / mUnit + 2;
        int e = curPos + itemCountAfterBaseItem;

        int start = curPos - maxStackCount <= 0 ? 0 : curPos - maxStackCount;
        int end = e >= getItemCount() ? getItemCount() - 1 : e;

        //layout view
        for (int i = start; i <= end; i++) {
            View view = recycler.getViewForPosition(i);

            float scale = scale(i);
            float alpha = alpha(i);

            addView(view);
            measureChildWithMargins(view, 0, 0);

            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();

            int left = (int) (left(i) - (1 - scale) * view.getMeasuredWidth() / 2) + lp.leftMargin;
            int top = lp.topMargin;
            int right = left + view.getMeasuredWidth() - lp.rightMargin;
            int bottom = view.getMeasuredHeight() - lp.bottomMargin;

            layoutDecorated(view, left, top, right, bottom);
            view.setAlpha(alpha);
            view.setScaleY(scale);
            view.setScaleX(scale);
        }

        return dy;
    }

    private SparseArray<ViewEntry> sparseArray = new SparseArray<>();

    private int fillFromLeft(RecyclerView.Recycler recycler, int dy) {
        if (mTotalOffset + dy < 0) {
            scrollLeftToRight(recycler, dy);
            return dy;
        }
        if ((mTotalOffset + dy + 0f) / mUnit > getItemCount() - 1) {
            return 0;
        }
        detachAndScrapAttachedViews(recycler);
        mTotalOffset += direction.layoutDirection * dy;
        int count = getChildCount();
        //removeAndRecycle  views
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (recycleHorizontally(child, dy)) {
                removeAndRecycleView(child, recycler);
            }
        }

        int curPos = mTotalOffset / mUnit;
        log("fillFromLeft0. count:" + count + ",mTotalOffset:" + mTotalOffset + ",mUnit:" + mUnit + ",curPos:" + curPos);
        int leavingSpace = getWidth() - (left(curPos) + mUnit);
        int itemCountAfterBaseItem = leavingSpace / mUnit + 2;
        int e = curPos + itemCountAfterBaseItem;

        int start = curPos - maxStackCount >= 0 ? curPos - maxStackCount : 0;
        int end = e >= getItemCount() ? getItemCount() - 1 : e;
        mRealOffset = 0;
        //layout view
        for (int i = start; i <= end; i++) {
            View view = recycler.getViewForPosition(i);

            float scale = scale(i);
            float alpha = alpha(i);

            addView(view);
            measureChildWithMargins(view, 0, 0);

            final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();

            float scaleX = scale;
            view.setPivotX(0);
            view.setPivotY(mItemHeight / 2);

            {
                ViewEntry entry = new ViewEntry();
                entry.index = i;
                entry.width = view.getMeasuredWidth();
                entry.height = view.getMeasuredHeight();
                sparseArray.put(i, entry);
            }

            if (view.getMeasuredWidth() > mItemWidth) {
                scaleX = scaleX4BigItem(view, i, view.getMeasuredWidth());
            }

            int left = left(i) + lp.leftMargin;
            if (i > curPos + 1) {
                left = left + mRealOffset;
            }
            int top = lp.topMargin;
            int right = left + view.getMeasuredWidth() - lp.rightMargin;
            int bottom = top + view.getMeasuredHeight() - lp.bottomMargin;

            layoutDecorated(view, left, top, right, bottom);

            view.setAlpha(alpha);
            view.setScaleY(scale);
            view.setScaleX(scaleX);

            if (i >= curPos)
                mRealOffset += view.getMeasuredWidth() - mItemWidth;
        }

        return dy;
    }

    private void scrollLeftToRight(RecyclerView.Recycler recycler, int dy) {
        int curPos = mTotalOffset / mUnit;
        log("scrollLeftToRight. dy:" + dy + ",mTotalOffset:" + mTotalOffset + ",mUnit:" + mUnit + ",curPos:" + curPos);
        int leavingSpace = getWidth() - (left(curPos) + mUnit);
        int itemCountAfterBaseItem = leavingSpace / mUnit + 2;
        int e = curPos + itemCountAfterBaseItem;

        int start = curPos - maxStackCount >= 0 ? curPos - maxStackCount : 0;
        int end = e >= getItemCount() ? getItemCount() - 1 : e;

        if (end < 3 && dy < 0) {
            return;
        }
        if (end < 3) {
            end = 2;
        }

        detachAndScrapAttachedViews(recycler);
        mTotalOffset += direction.layoutDirection * dy;
        int count = getChildCount();
        //removeAndRecycle  views
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (recycleHorizontally(child, dy))
                removeAndRecycleView(child, recycler);
        }
        mRealOffset = 0;
        //layout view
        for (int i = start; i <= end; i++) {
            View view = recycler.getViewForPosition(i);

            addView(view);
            measureChildWithMargins(view, 0, 0);

            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();

            int left = left2(i) + lp.leftMargin;
            if (i > curPos) {
                left = left + mRealOffset;
            }
            int top = lp.topMargin;
            int right = left + view.getMeasuredWidth() - lp.rightMargin;
            int bottom = top + view.getMeasuredHeight() - lp.bottomMargin;

            layoutDecorated(view, left, top, right, bottom);
            view.setAlpha(1);
            view.setScaleY(1);
            view.setScaleX(1);
            if (i >= curPos)
                mRealOffset += view.getMeasuredWidth() - mItemWidth;
        }
    }

    private static final int VIEW_SCALE_DURATION = 500;
    float itemViewWidth, originLeft;

    public Animator itemSelf2FullWidth(int position) {
        currentIndex = position;
        View itemView = recycler.getViewForPosition(position);
        itemViewWidth = itemView.getLayoutParams().width;
        originLeft = left(position);

        log("itemSelf2FullWidth.itemViewWidth:" + itemViewWidth + ",originLeft:" + originLeft);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.addUpdateListener(animation -> {
            float rate = (float) animation.getAnimatedValue();

            int tmpWidth = (int) ((getWidth() - itemViewWidth) * rate + itemViewWidth);
            int offset = (int) (originLeft * rate);
            int nextOffset = (int) ((getWidth() - originLeft - itemViewWidth) * rate);

            log("itemSelf2FullWidth. itemSelf2FullWidth rate:" + rate + ",tmpWidth:" + tmpWidth + ",offset:" + offset + ",nextOffset:" + nextOffset);
            formatChildren(recycler, position, tmpWidth, offset, nextOffset);
        });
        valueAnimator.setDuration(VIEW_SCALE_DURATION);
        return valueAnimator;
    }

    public ValueAnimator itemFull2SelfWidth(int position) {
        currentIndex = position;
        float startScale = lastScale > 0 ? lastScale : 1f;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(startScale, 0f);
        valueAnimator.addUpdateListener(animation -> setScale((float) animation.getAnimatedValue()));
        valueAnimator.setDuration(VIEW_SCALE_DURATION);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                lastScale = 0f;
                int left = left(position);
                int offset = Math.round(left - originLeft);
                log("onAnimationEnd. left:" + left + ",originLeft:" + originLeft);
                int dur = (int) (Math.abs((offset + 0f) / mUnit) * VIEW_SCALE_DURATION);
                brewAndStartAnimator(dur, offset);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return valueAnimator;
    }

    private float lastScale = 0f;
    private int mWidth;

    public void setScale(float scale) {
        if (scale > 1) return;
        lastScale = scale;

        mWidth = getWidth();
        //本次缩放等级下，临时的宽度为
        float tmpWidth = (mWidth - itemViewWidth) * scale + itemViewWidth;
        //改变的宽度
        float w = tmpWidth - itemViewWidth;
        //只需要计算出每次item的左右增加的宽度即可
        float lw = w * (originLeft / (mWidth - itemViewWidth));
        int offset = Math.round(lw + 0.5f);
        int offset1 = Math.round(w - lw + 0.5f);

//        int offset = (int) (originLeft * scale);
//        int nextOffset = (int) ((getWidth() - originLeft - itemViewWidth) * scale);

        log("setScale. rate:" + scale + ",tmpWidth:" + tmpWidth + ",offset:" + offset + ",offset1:" + offset1 + ",itemViewWidth:" + itemViewWidth);
        formatChildren(recycler, currentIndex, (int) tmpWidth, offset, offset1);
    }

    private void formatChildren(RecyclerView.Recycler recycler, int pos, int width, int offset, int nextOffset) {
        detachAndScrapAttachedViews(recycler);
        mTotalOffset += direction.layoutDirection;
        int count = getChildCount();
        //removeAndRecycle  views
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (recycleHorizontally(child, 0))
                removeAndRecycleView(child, recycler);
        }

        int curPos = mTotalOffset / mUnit;
        log("formatChildren. count:" + count + ",mTotalOffset:" + mTotalOffset + ",mUnit:" + mUnit);
        int leavingSpace = getWidth() - (left(curPos) + mUnit);
        int itemCountAfterBaseItem = leavingSpace / mUnit + 2;
        int e = curPos + itemCountAfterBaseItem;

        int start = curPos - maxStackCount >= 0 ? curPos - maxStackCount : 0;
        int end = e >= getItemCount() ? getItemCount() - 1 : e;

        //layout view
        for (int i = start; i <= end; i++) {
            View view = recycler.getViewForPosition(i);
            float scale = scale(i);
            float alpha = alpha(i);

            addView(view);
            measureChildWithMargins(view, 0, 0);
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
            log("formatChildren. 0 left(" + i + "):" + left(i) + ",curPos:" + curPos);
            int left;
            if (i >= curPos) {
                scale = 1;
                alpha = 1;
                left = left(i);
            } else {
                left = Math.round(left(i) - (1 - scale) * view.getMeasuredWidth() / 2);
            }
            log("formatChildren. 1 left:" + left + ",offset:" + offset + ",width:" + width + ",view.left:" + view.getLeft() + ",scale:" + scale + ",alpha:" + alpha);
            if (i <= pos) {
                left -= offset;
            } else if (i > pos) {
                left += nextOffset;
            }

            left += lp.leftMargin;
            int top = lp.topMargin;
            int right = left + view.getMeasuredWidth() - lp.rightMargin;
            int bottom = top + view.getMeasuredHeight() - lp.bottomMargin;
            if (i == pos) {
                right = left + width;
            }
            layoutDecorated(view, left, top, right, bottom);
            log("formatChildren. 2 index:" + i + ",pos:" + pos
                    + ",view.left:" + view.getLeft() + ",view.top:" + view.getTop() + ",view.right:" + view.getRight() + ",view.bottom:" + view.getBottom()
                    + ",width:" + view.getWidth() + ",height:" + view.getHeight());
            view.setAlpha(alpha);
            view.setScaleY(scale);
            view.setScaleX(scale);
        }
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);

        view.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                log("onScrollStateChanged,newState:" + newState);
                if (newState == SCROLL_STATE_IDLE) {
                    //differentiated mode
                    switch (mManagerModel) {
                        default:
                        case MANAGER_MODEL_DISPLAY:
                            scrollIdleDisplay();
                            break;
                        case MANAGER_MODEL_EDIT:
//                            scrollIdleEdit();
                            break;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });
    }

    private void scrollIdleDisplay() {
        int o = mTotalOffset % mUnit;
        int scrollX;

        if (o >= mUnit / 2) {
            scrollX = mUnit - o;
        } else {
            scrollX = -o;
        }
        int dur = (int) (Math.abs((scrollX + 0f) / mUnit) * VIEW_SCALE_DURATION);
        brewAndStartAnimator(dur, scrollX);
    }

    private void scrollIdleEdit() {
        int unit = getScaleUnit(EDIT_STATE_SCALE);
        int o = mEditTotalOffset % unit;
        int scrollX;

        if (o >= unit / 2) {
            scrollX = unit - o;
        } else {
            scrollX = -o;
        }
        int dur = (int) (Math.abs((scrollX + 0f) / unit) * VIEW_SCALE_DURATION);
        brewAndStartAnimator(dur, scrollX);
    }

    private void brewAndStartAnimator(int dur, int finalX) {
        log("brewAndStartAnimator finalX:" + finalX);
        animator = ObjectAnimator.ofInt(StackLayoutManager.this, "animateValue", 0, finalX);
        animator.setDuration(dur);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                lastAnimateValue = 0;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                lastAnimateValue = 0;
            }
        });
    }

    /******************************precise math method*******************************/
    private float alpha(int position) {
        float alpha;
        int curPos = mTotalOffset / mUnit;
        float n = (mTotalOffset + .0f) / mUnit;
        if (position >= curPos - 1)
            alpha = 1.0f;
        else {
            //temporary linear map,barely ok
            alpha = 1 - (n - position) / maxStackCount;
        }
        //for precise checking,oh may be kind of dummy
        return alpha <= 0.001f ? 0 : alpha;
    }

    private float scale(int position) {
        switch (direction) {
            default:
            case LEFT:
            case RIGHT:
                return scaleDefault(position);
        }
    }

    private float scaleDefault(int position) {
        float scale;
        int curPos = this.mTotalOffset / mUnit;
        float n = (mTotalOffset + .0f) / mUnit;
        if (position > curPos) {
            scale = 1;
        } else if (position < curPos - maxStackCount) {
            scale = 0;
        } else {
            scale = 1 - scaleRatio * (n - position) / maxStackCount;
        }
        return scale;
    }


    private float scaleX4BigItem(View view, int position, int measuredWidth) {
        float scale;
        int curPos = this.mTotalOffset / mUnit;
        float n = (mTotalOffset + .0f) / mUnit;
        if (position > curPos) {
            scale = 1;
        } else if (position < curPos - maxStackCount) {
            scale = 0;
        } else if (position == curPos) {
            float s1 = 1f - scaleRatio * 1 / maxStackCount;
            scale = 1f - (n - position) * (1 - s1 * mItemWidth / measuredWidth);
        } else {
            //大卡在堆叠中间时缩放
            float startScale = (1f - scaleRatio * 1 / maxStackCount) * mItemWidth / measuredWidth;
            //小卡在堆叠最下面时缩放
            float s1 = 1f - scaleRatio * (1 + curPos - position) / maxStackCount;

            scale = startScale - (n - position - 1) * (startScale - s1 * mItemWidth / measuredWidth);
        }
        log("scale:" + scale + ",n-position:" + (n - position) + ",curPos:" + curPos + ",measuredWidth:" + measuredWidth);
        return scale;
    }

    private int getItemViewWidth(int position) {
        ViewEntry entry = sparseArray.get(position);
        if (sparseArray != null && entry != null) {
            return entry.width;
        }
        return 0;
    }

    /**
     * @param position the index of the item in the adapter
     * @return the accurate left position for the given item
     */
    private int left(int position) {
        int curPos = mTotalOffset / mUnit;
        int tail = mTotalOffset % mUnit;
        float n = (mTotalOffset + .0f) / mUnit;
        float x = n - curPos;

        switch (direction) {
            default:
            case LEFT:
            case TOP:
                //from left to right or top to bottom
                //these two cases are actually same
                return ltr(position, curPos, tail, x);
            case RIGHT:
                return rtl(position, curPos, tail, x);
        }
    }

    private int left2(int position) {
        int curPos = mTotalOffset / mUnit;
        int tail = mTotalOffset % mUnit;
        float n = (mTotalOffset + .0f) / mUnit;
        float x = n - curPos;

        switch (direction) {
            default:
            case LEFT:
            case TOP:
                //from left to right or top to bottom
                //these two cases are actually same
                return ltr2(position, curPos, tail, x);
            case RIGHT:
                return rtl(position, curPos, tail, x);
        }
    }

    /**
     * @param position ..
     * @param curPos   ..
     * @param tail     .. change
     * @param x        ..
     * @return the left position for given item
     */
    private int rtl(int position, int curPos, int tail, float x) {
        //虽然是做对称变换，但是必须考虑到scale给 对称变换带来的影响
        float scale = scale(position);
        int ltr = ltr(position, curPos, tail, x);
        return (int) (getWidth() - ltr - (mItemWidth) * scale);
    }

    private int ltr(int position, int curPos, int tail, float x) {
        float left;

        if (position == curPos) {
            left = mSpace * (maxStackCount - x);
        } else if (position < curPos) {
            left = mSpace * (maxStackCount - (curPos - position) - x);
        } else {
            float offset = 0;
            float offset1 = 0;
            if (getItemViewWidth(curPos) > mItemWidth) {
                int temp = mUnit - mItemWidth + getItemViewWidth(curPos);
                float v = (tail + 0f) / mUnit;
                offset = (mUnit - tail) - temp * (1 - v);
                offset1 = temp * v - tail;
            }
            if (position == curPos + 1) {
                left = mSpace * maxStackCount + mUnit - tail;
                left -= offset;
            } else {
                left = mSpace * maxStackCount + (position - curPos) * mUnit - tail;
                left -= offset1;
            }
        }
        left = left <= 0 ? 0 : left;
        return Math.round(left + 0.5f) + mMarginStart;
    }

    private int ltr2(int position, int curPos, int tail, float x) {
        int left;
        if (position == curPos + 1)
            left = mSpace * maxStackCount + mUnit - tail;
        else {
            left = mSpace * maxStackCount + (position - curPos) * mUnit - tail;
        }
        left = left <= 0 ? 0 : left;
        return left + mMarginStart;
    }


    @SuppressWarnings("unused")
    public void setAnimateValue(int animateValue) {
        this.animateValue = animateValue;
        int dy = this.animateValue - lastAnimateValue;
        log("setAnimateValue. animateValue:" + animateValue + ",lastAnimateValue:" + lastAnimateValue + ",dy:" + dy);
        fill(recycler, direction.layoutDirection * dy, false);
        lastAnimateValue = animateValue;
    }

    @SuppressWarnings("unused")
    public int getAnimateValue() {
        return animateValue;
    }

    /**
     * should recycle view with the given dy or say check if the
     * view is out of the bound after the dy is applied
     *
     * @param view ..
     * @param dy   ..
     * @return ..
     */
    private boolean recycleHorizontally(View view/*int position*/, int dy) {
        return view != null && (view.getLeft() - dy < 0 || view.getRight() - dy > getWidth());
    }

    private boolean recycleVertically(View view, int dy) {
        return view != null && (view.getTop() - dy < 0 || view.getBottom() - dy > getHeight());
    }


    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return fill(recycler, dx);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return fill(recycler, dy);
    }

    @Override
    public boolean canScrollHorizontally() {
        return direction == LEFT || direction == RIGHT;
    }

    @Override
    public boolean canScrollVertically() {
        return direction == TOP || direction == BOTTOM;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    //-------------- Edit model start --------------------

    private static final long EDIT_STATE_SCALE_DURATION = 500;
    private static final String EDIT_STATE_PROPERTY_NAME = "edit_scale";
    private static final float EDIT_STATE_SCALE = 0.8f;
    private static final int EDIT_STATE_LEFT_MARGIN = 150;
    private static final int EDIT_STATE_RIGHT_MARGIN = 150;
    private static final int EDIT_STATE_ITEM_SPACE = 56;
    private static final int MANAGER_MODEL_DISPLAY = 1;
    private static final int MANAGER_MODEL_EDIT = 2;
    private int mManagerModel = MANAGER_MODEL_DISPLAY;
    private boolean mEditState = false;
    private boolean mEditZooming = false;
    private float mEditScale = 1f;
    private int mDragItemPosition = -1;
    private int mMarkFirstId = 0;
    private int mMoveOffset = 0;
    private int mEditTotalOffset = 0;

    private int fillFromLeftInEdit(RecyclerView.Recycler recycler, int dy) {
//        Log.d(TAG, "fillFromLeftInEdit() called with: recycler = [" + recycler + "], dy = [" + dy + "]");
        int editDy = dy;
        if (mEditZooming) {
            editDy = 0;
        }

        if (tryHandleLeftToEdge(recycler, editDy)) {
            return 0;
        }

        if (tryHandleRightToEdge(recycler, editDy)) {
            return 0;
        }

        Log.d(TAG, "fillFromLeftInEdit: scroll_type : handle normal");
        handleDrawLeft2Right(recycler, editDy);

        return editDy;
    }

    /**
     * left slip to edge handle
     */
    private boolean tryHandleLeftToEdge(RecyclerView.Recycler recycler, int dy) {
        //it is on the left side of the edge position and is currently sliding to the right side
        if (dy < 0 && leftInEdit(0, mEditTotalOffset, mMarkFirstId, mMoveOffset) - getEditLeftMargin(EDIT_STATE_SCALE) >= 0) {
            Log.d(TAG, "fillFromLeftInEdit: scroll_type : left_edge & right_move");
            return true;
        }

        //edit zooming state
        if (dy == 0 && leftInEdit(0, mEditTotalOffset, mMarkFirstId, mMoveOffset) - getEditLeftMargin(EDIT_STATE_SCALE) >= 0) {
            Log.d(TAG, "fillFromLeftInEdit: scroll_type : left_edge & zooming");
            handleDrawLeft2Right(recycler, dy);
            return true;
        }

        //it is not allowed to exceed the left limit position
        int targetTotalOffset = mEditTotalOffset + direction.layoutDirection * dy;
        if (leftInEdit(0, targetTotalOffset, mMarkFirstId, mMoveOffset) - getEditLeftMargin(EDIT_STATE_SCALE) >= 0) {
            Log.d(TAG, "fillFromLeftInEdit: scroll_type : trend is left_edge & move to left_edge by compensation strategy");
            handleDrawLeft2Right(recycler, mEditTotalOffset * direction.layoutDirection * -1);
            return true;
        }

        return false;
    }

    /**
     * right slip to edge handle
     */
    private boolean tryHandleRightToEdge(RecyclerView.Recycler recycler, int dy) {
        //it is on the right side of the edge position and is currently sliding to the left side
        if (dy > 0 && isRightEdge(mEditTotalOffset, mMarkFirstId, 0)) {
            Log.d(TAG, "fillFromLeftInEdit: scroll_type : right_edge & left_move");
            return true;
        }

        //edit zooming state
        if (dy == 0 && isRightEdge(mEditTotalOffset, mMarkFirstId, dy)) {
            if (isRightEdgeReached(mEditTotalOffset, mMarkFirstId, dy)) {
                Log.d(TAG, "fillFromLeftInEdit: scroll_type : right_edge & zooming edit to display");
                handleDrawLeft2Right(recycler, dy);
            } else {
                Log.d(TAG, "fillFromLeftInEdit: scroll_type : right_edge & zooming display to edit");
                handleDrawRight2Left(recycler, dy);
            }
            return true;
        }

        //it is not allowed to exceed the right limit position
        if (isRightEdge(mEditTotalOffset, mMarkFirstId, dy)) {
            Log.d(TAG, "fillFromLeftInEdit: scroll_type : trend is right_edge & move to right_edge by compensation strategy");
            int targetDy = (getRightEdgeFullTotalOffset() - mEditTotalOffset) / direction.layoutDirection;
            handleDrawLeft2Right(recycler, targetDy);
            return true;
        }

        return false;
    }

    /**
     * draw left to right
     */
    private void handleDrawLeft2Right(RecyclerView.Recycler recycler, int dy) {
//        Log.d(TAG, "handleDrawLeft2Right() called with: recycler = [" + recycler + "], dy = [" + dy + "]");
        mEditTotalOffset = calculateTotalOffset(mEditTotalOffset, dy);
        removeAndRecycleViews(recycler, dy);
        drawId2LastRight2Left(recycler, mEditTotalOffset, mMarkFirstId, mMoveOffset);
    }

    /**
     * draw left to right
     */
    private void handleDrawRight2Left(RecyclerView.Recycler recycler, int dy) {
//        Log.d(TAG, "handleDrawRight2Left() called with: recycler = [" + recycler + "], dy = [" + dy + "]");
        mEditTotalOffset = calculateTotalOffset(mEditTotalOffset, dy);
        removeAndRecycleViews(recycler, dy);
        drawLast2IdLeft2Right(recycler, mEditTotalOffset, mMarkFirstId, mMoveOffset);
    }

    /**
     * starting from the first element, the right coordinate value of the last element in the edit state
     */
    private int getLastRightByFirstSort() {
        int lastIndex = getItemCount() - 1;
        int lastLeftOfFirstSort = leftInEdit(lastIndex, 0, 0, 0, EDIT_STATE_SCALE);
        int lastRightOfFirstSort = lastLeftOfFirstSort + getScaleItemWidth(EDIT_STATE_SCALE);
        return lastRightOfFirstSort;
    }

    /**
     * is the right limit filled? (for example, full screen can display 5 elements, while only 3 at present, the right edge is not filled).
     */
    private boolean isRightEdgeFull() {
        if (getLastRightByFirstSort() > getWidth() - getEditRightMargin(EDIT_STATE_SCALE)) {
            //order from right
            return true;
        } else {
            //from the first one, the last one is not reached to right edge
            return false;
        }
    }

    /**
     * whether reached the right edge
     */
    private boolean isRightEdgeReached(int totalOffset, int markId, int dy) {
        int targetTotalOffset = totalOffset + direction.layoutDirection * dy;
        int lastLeft = leftInEdit(getItemCount() - 1, targetTotalOffset, markId, 0, EDIT_STATE_SCALE);
        int layoutDecorateEdgeSpace = (EDIT_STATE_RIGHT_MARGIN - getEditRightMargin(EDIT_STATE_SCALE)) * 2;
        int lastRight = lastLeft + getScaleItemWidth(EDIT_STATE_SCALE) + layoutDecorateEdgeSpace;
        int rightEdge = getWidth() - getEditRightMargin(EDIT_STATE_SCALE);
        int errorCompensation = 1;
        if (lastRight >= rightEdge + errorCompensation) {
            return true;
        }
        return false;
    }

    /**
     * whether at the right edge
     */
    private boolean isRightEdge(int totalOffset, int markId, int dy) {
        int targetTotalOffset = totalOffset + direction.layoutDirection * dy;
        int lastLeft = leftInEdit(getItemCount() - 1, targetTotalOffset, markId, 0, EDIT_STATE_SCALE);
        int layoutDecorateEdgeSpace = (EDIT_STATE_RIGHT_MARGIN - getEditRightMargin(EDIT_STATE_SCALE)) * 2;
        int lastRight = lastLeft + getScaleItemWidth(EDIT_STATE_SCALE) + layoutDecorateEdgeSpace;
        int rightEdge = getWidth() - getEditRightMargin(EDIT_STATE_SCALE);
        int errorCompensation = 1;
        if (lastRight <= rightEdge + errorCompensation) {
            return true;
        }
        return false;
    }

    private void entryEditTransform() {
        int curPos = mTotalOffset / mUnit;
        if (mTotalOffset > 0) {
            if (isRightEdgeLack(0, curPos, 0)) {
                if (isRightEdgeFull()) {
                    mMarkFirstId = getRightEdgeFullMarkId();
                    mEditTotalOffset = getRightEdgeFullTotalOffset();
                } else {
                    mMarkFirstId = 0;
                    mEditTotalOffset = 0;
                }
            }
        }
    }

    private int getRightEdgeFullMarkId() {
        int leftLeave = getWidth() - getEditRightMargin(EDIT_STATE_SCALE);
        int unit = getScaleUnit(EDIT_STATE_SCALE);
        int itemCount = leftLeave / unit + 1;
        int startId = (getItemCount() - 1) - itemCount;
        return startId > 0 ? startId : 0;
    }

    private int getRightEdgeFullTotalOffset() {
        int leftLeave = getWidth() - getEditRightMargin(EDIT_STATE_SCALE);
        int unit = getScaleUnit(EDIT_STATE_SCALE);
        int itemCount = leftLeave / unit + 1;
        int startId = (getItemCount() - 1) - itemCount;
        int markId = startId > 0 ? startId : 0;

        int layoutDecorateEdgeSpace = (EDIT_STATE_RIGHT_MARGIN - getEditRightMargin(EDIT_STATE_SCALE)) * 2;
        int markIdLeft = leftLeave - getScaleItemWidth(EDIT_STATE_SCALE) - (getItemCount() - 1 - markId) * unit;
        int totalOffset = getEditLeftMargin(EDIT_STATE_SCALE) - markIdLeft + layoutDecorateEdgeSpace;
        return totalOffset;
    }

    /**
     * whether the right edge is exist lack
     */
    private boolean isRightEdgeLack(int totalOffset, int markId, int dy) {
        int unit = getScaleUnit(EDIT_STATE_SCALE);
        int targetTotalOffset = totalOffset + direction.layoutDirection * dy;
        int curPos = targetTotalOffset / unit + markId;

        int leavingSpace = getWidth() - (leftInEdit(curPos, targetTotalOffset, markId, 0, EDIT_STATE_SCALE) + unit);
        int itemCountAfterBaseItem = leavingSpace / unit + 1;
        int e = curPos + itemCountAfterBaseItem;
        int end = e >= getItemCount() - 1 ? getItemCount() - 1 : e;

        int lastIndex = getItemCount() - 1;
        if (end >= lastIndex) {
            int lastLeft = leftInEdit(lastIndex, targetTotalOffset, markId, 0, EDIT_STATE_SCALE);
            if (lastLeft + getScaleItemWidth(EDIT_STATE_SCALE) <= getWidth() - getEditRightMargin(EDIT_STATE_SCALE)) {
                return true;
            }
        }

        return false;
    }

    /**
     * display to edit standard conversion (mTotalOffset and mMoveOffset)
     *
     */
    private void saveDisplayData() {
        int curPos = mTotalOffset / mUnit;

        if (mTotalOffset <= 0) {
            mMoveOffset = left2(0) - EDIT_STATE_LEFT_MARGIN;
            mMarkFirstId = 0;
            mEditTotalOffset = 0;
//            Log.d(TAG, "saveDisplayData() offset <= 0 : mMarkFirstId = [" + mMarkFirstId + "], mMoveOffset = ["  + mMoveOffset + "]");
        } else {
            if (isRightEdgeLack(0, curPos, 0)) {
                //when the current position execution enters the edit animation, the right side alignment is required
                int curLastRight = left(getItemCount() - 1) + getDisplayItemWidth();
                if (isRightEdgeFull()) {
                    mMoveOffset = getWidth() - EDIT_STATE_RIGHT_MARGIN - curLastRight;
                } else {
                    mMoveOffset = getLastRightByFirstSort() - curLastRight;
                }
            } else {
                //the current location execution into the edit animation requires the left alignment of the first marker id
                mMoveOffset = left(curPos) - EDIT_STATE_LEFT_MARGIN;
            }
            mMarkFirstId = curPos;
            mEditTotalOffset = 0;
//            Log.d(TAG, "saveDisplayData() offset > 0 : mMarkFirstId = [" + mMarkFirstId + "], mMoveOffset = ["  + mMoveOffset + "]");
        }
    }

    /**
     * edit to display standard conversion (mEditTotalOffset & mMarkFirstId and mMoveOffset)
     */
    private void saveEditData() {
        if (mTotalOffset <= 0) {
            int editUnit = getScaleUnit(EDIT_STATE_SCALE);
            int startPreCount = mEditTotalOffset / editUnit;

            //after mode switching,coordinates of the starting bar
            int objStartLeft = left(mTotalOffset / mUnit);
            int offsetChange = left2(0) - objStartLeft;

            //change mMoveOffset,the process animation is normal in order to exit the edit mode. The actual value of migration is described below
            int layoutDec0rateDiff = EDIT_STATE_LEFT_MARGIN - getEditLeftMargin(EDIT_STATE_SCALE);
            mMoveOffset = objStartLeft - leftInEdit(startPreCount, mEditTotalOffset, mMarkFirstId, mMoveOffset) - layoutDec0rateDiff;

            //must to update mMarkFirstId, because there will be a different compensation strategy on the left and right side about mMarkFirstId
            mMarkFirstId = startPreCount + mMarkFirstId;
            mEditTotalOffset = mEditTotalOffset - startPreCount * editUnit;

            //change mTotalOffset，to exit to the display mode, the display mode slid normally, and the standard switch completes
            //the deviation of the difference needs to be compensated to the display mode
            mTotalOffset += offsetChange;
        } else {
            int editUnit = getScaleUnit(EDIT_STATE_SCALE);
            int startPreCount = mEditTotalOffset / editUnit;

            //after mode switching,coordinates of the starting bar
            int objStartLeft = left(mTotalOffset / mUnit);
            int offsetChange = (mMarkFirstId - mTotalOffset / mUnit) * mUnit;

            //change mMoveOffset,the process animation is normal in order to exit the edit mode. The actual value of migration is described below
            int layoutDec0rateDiff = EDIT_STATE_LEFT_MARGIN - getEditLeftMargin(EDIT_STATE_SCALE);
            mMoveOffset = objStartLeft - leftInEdit(startPreCount, mEditTotalOffset, mMarkFirstId, mMoveOffset) - layoutDec0rateDiff;

            //must to update mMarkFirstId, because there will be a different compensation strategy on the left and right side about mMarkFirstId
            mMarkFirstId = startPreCount + mMarkFirstId;
            mEditTotalOffset = mEditTotalOffset - startPreCount * editUnit;

            //change mTotalOffset，to exit to the display mode, the display mode slid normally, and the standard switch completes
            //the deviation of the difference needs to be compensated to the display mode
            mTotalOffset += offsetChange;
        }
    }

    private void drawLast2IdLeft2Right(RecyclerView.Recycler recycler, int totalOffset, int markId, int moveOffset) {
        log("drawLast2IdLeft2Right: edit_draw_model right -> left");
        DisplayItemInfo displayItemInfo = getRightItemInfo(totalOffset, markId, moveOffset);

        for (int i = displayItemInfo.end; i >= displayItemInfo.start; i--) {
            View view = recycler.getViewForPosition(i);

            float scale = scaleInEdit(i);
            float alpha = alphaInEdit(i);

            addView(view);

            final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
            int right = rightInEdit(i, totalOffset, markId, moveOffset) - lp.rightMargin;
            int left = right - mItemWidth + lp.leftMargin;
            int top = lp.topMargin;
            int bottom = top + mItemHeight - lp.bottomMargin;

            layoutDecorated(view, left, top, right, bottom);

            view.setAlpha(alpha);
            view.setScaleY(scale);
            view.setScaleX(scale);
        }
    }

    private DisplayItemInfo getRightItemInfo(int totalOffset, int markId, int moveOffset) {
        int unit = getScaleUnit(scaleCommonInEdit());
        int curPos = totalOffset / unit + markId;

        //left start item,the display item on the right side
        int end = getItemCount() - 1;

        //left start item,the display item on the left side
        int start = 0;
        int lastPosition = curPos - 1;
        if (lastPosition >= 0) {
            for (int i = lastPosition; i >= 0; i--) {
                int positionRight = rightInEdit(i, totalOffset, markId, moveOffset);
                if (positionRight - getScaleItemWidth(scaleCommonInEdit()) <= 0) {
                    start = i;
                    break;
                }
            }
        }

        return new DisplayItemInfo(start, end);
    }

    private int rightInEdit(int position, int totalOffset, int markFirstId, int moveOffset, float scale) {
        switch (direction) {
            default:
            case LEFT:
                return rtlInEdit(position, totalOffset, markFirstId, moveOffset, scale);
        }
    }

    private int rightInEdit(int position, int totalOffset, int markFirstId, int moveOffset) {
        return rightInEdit(position, totalOffset, markFirstId, moveOffset, scaleInEdit(position));
    }

    private int rtlInEdit(int position, int totalOffset, int markFirstId, int moveOffset, float scale) {
        int unit = getScaleUnit(scale);
        int tail = totalOffset % unit;

        float rightEdgeRelative = getWidth();
        int end = getItemCount() - 1;

        if (!isRightEdgeFull()) {
            //result = last right + fix margin - layout decorated adjust
            rightEdgeRelative = getLastRightByFirstSort() + EDIT_STATE_RIGHT_MARGIN + (EDIT_STATE_RIGHT_MARGIN - getEditRightMargin(EDIT_STATE_SCALE));
        }

        float cardRight = rightEdgeRelative - ((end - position) * unit - tail);

        float scaleSlope = getScaleSlope(scale);

        float offsetAdjust = scaleSlope * moveOffset;

        //Scale compensation strategy for the display bar on the left side
//        float scaleAdjust = scaleSlope * calScaleInEdit(position, totalOffset, markFirstId);
        float scaleAdjust = 0;

        //Stack compensation strategy for the display bar on the left side
//        float stackAdjust = scaleSlope * calStackInEdit(position, totalOffset, markFirstId);
        float stackAdjust = 0;

//        Log.d(TAG, "ltrInEdit: position = [" + position + "], cardLeft = [" + cardRight + "], offsetAdjust = [" + offsetAdjust + "]");
        float right = cardRight - offsetAdjust - scaleAdjust - stackAdjust;

        return Math.round(right + 0.5f) - getEditRightMargin(scale);
    }

    private int getEditRightMargin(float scale) {
        //when layout decorated, the visual item will move the shift of the following parameter factors to the right
        float viewLayoutDecoratedAdjust = ((1 - scale) / 2f) * mItemWidth;
        return EDIT_STATE_RIGHT_MARGIN - Math.round(viewLayoutDecoratedAdjust);
    }

    private void drawId2LastRight2Left(RecyclerView.Recycler recycler, int totalOffset, int markId, int moveOffset) {
        log("drawId2LastRight2Left: edit_draw_model left -> right");
        DisplayItemInfo displayItemInfo = getLeftItemInfo(totalOffset, markId, moveOffset);

        for (int i = displayItemInfo.start; i <= displayItemInfo.end; i++) {
            View view = recycler.getViewForPosition(i);

            float scale = scaleInEdit(i);
            float alpha = alphaInEdit(i);

            addView(view);
            measureChildWithMargins(view, 0, 0);

            final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
            int left = leftInEdit(i, totalOffset, markId, moveOffset) + lp.leftMargin;
            int top = lp.topMargin;
            int right = left + mItemWidth - lp.rightMargin;
            int bottom = top + mItemHeight - lp.bottomMargin;

//            Log.d(TAG, "drawId2LastRight2Left() called with: left = [" + left + "], top = [" + top + "], right = [" + right + "], bottom = [" + bottom + "]");
            layoutDecorated(view, left, top, right, bottom);

            view.setAlpha(alpha);
            view.setScaleY(scale);
            view.setScaleX(scale);
        }
    }

    private DisplayItemInfo getLeftItemInfo(int totalOffset, int markId, int moveOffset) {
        int unit = getScaleUnit(scaleCommonInEdit());
        int curPos = totalOffset / unit + markId;

        //left start item, the display item on the right side
        int leavingSpace = getWidth() - (leftInEdit(curPos, totalOffset, markId, moveOffset) + unit);
        int itemCountAfterBaseItem = leavingSpace / unit + 1;
        int e = curPos + itemCountAfterBaseItem;
        int end = e >= getItemCount() - 1 ? getItemCount() - 1 : e;

        //left start item, the display item on the left side
        int start = 0;
        int lastPosition = curPos - 1;
        if (lastPosition >= 0) {
            for (int i = lastPosition; i >= 0; i--) {
                int positionLeft = leftInEdit(i, totalOffset, markId, moveOffset);
                if (positionLeft <= 0) {
                    start = i;
                    break;
                }
            }
        }

        return new DisplayItemInfo(start, end);
    }

    private int leftInEdit(int position, int totalOffset, int markFirstId, int moveOffset, float scale) {
        switch (direction) {
            default:
            case LEFT:
                return ltrInEdit(position, totalOffset, markFirstId, moveOffset, scale);
        }
    }

    private int leftInEdit(int position, int totalOffset, int markFirstId, int moveOffset) {
        return leftInEdit(position, totalOffset, markFirstId, moveOffset, scaleInEdit(position));
    }

    private int ltrInEdit(int position, int totalOffset, int markFirstId, int moveOffset, float scale) {
        int unit = getScaleUnit(scale);
        int curPos = totalOffset / unit + markFirstId;
        int tail = totalOffset % unit;

        float cardLeft = (position - curPos) * unit - tail;

        float scaleSlope = getScaleSlope(scale);

        float offsetAdjust = scaleSlope * moveOffset;

        //Scale compensation strategy for the display bar on the left side
//        float scaleAdjust = scaleSlope * calScaleInEdit(position, totalOffset, markFirstId);
        float scaleAdjust = 0;

        //Stack compensation strategy for the display bar on the left side
//        float stackAdjust = scaleSlope * calStackInEdit(position, totalOffset, markFirstId);
        float stackAdjust = 0;

//        Log.d(TAG, "ltrInEdit: position = [" + position + "], cardLeft = [" + cardLeft + "], offsetAdjust = [" + offsetAdjust + "]");
        float left = cardLeft + offsetAdjust + scaleAdjust + stackAdjust;

        return Math.round(left + 0.5f) + getEditLeftMargin(scale);
    }

    private int getEditLeftMargin(float scale) {
        //when layout decorated, the visual item will move the shift of the following parameter factors to the left
        float viewLayoutDecoratedAdjust = ((1 - scale) / 2f) * mItemWidth;
        return EDIT_STATE_LEFT_MARGIN - Math.round(viewLayoutDecoratedAdjust);
    }

    private int calRightTotalOffset(int totalOffset, int dy) {
        return totalOffset + direction.layoutDirection * dy;
    }

    private int calLeftTotalOffset(int totalOffset, int dy) {
        return totalOffset + direction.layoutDirection * dy;
    }

    private float calStackInEdit(int position, int totalOffset, int markFirstId) {
        float scale = scaleInEdit(position);
        int unit = getScaleUnit(scale);
        int curPos = totalOffset / unit + markFirstId;
        int tail = totalOffset % unit;
        float n = (totalOffset + .0f) / unit + markFirstId;
        float x = n - curPos;

        return ltr(position, curPos, tail, x);
    }

    private float calScaleInEdit(int position, int totalOffset, int markFirstId) {
        switch (direction) {
            default:
            case LEFT:
            case RIGHT:
                return calScaleInEditDefault(position, totalOffset, markFirstId);
        }
    }

    private float calScaleInEditDefault(int position, int totalOffset, int markFirstId) {
        float scale = scaleInEdit(position);
        int unit = getScaleUnit(scale);
        int curPos = totalOffset / unit + markFirstId;
        int tail = totalOffset % unit;
        float n = (totalOffset + .0f) / unit + markFirstId;
        float x = n - curPos;

        float calScale = 0;
        if (position >= curPos) {
            if (position == curPos) {
                calScale = 1 - scaleRatio * (x) / maxStackCount;
            } else
                calScale = 1;
        } else {
            if (position < curPos - maxStackCount)
                calScale = 0f;
            else
                calScale = 1f - scaleRatio * (x + curPos - position) / maxStackCount;
        }

        return calScale;
    }

    private float calAlphaInEdit(int position, int totalOffset, int markFirstId) {
        float scale = scaleInEdit(position);
        int unit = getScaleUnit(scale);
        int curPos = totalOffset / unit + markFirstId;
        int tail = totalOffset % unit;
        float n = (totalOffset + .0f) / unit + markFirstId;
        float x = n - curPos;

        float alpha = 1.0f;
        if (position >= curPos - 1)
            alpha = 1.0f;
        else {
            //temporary linear map,barely ok
            alpha = 1 - (n - position) / maxStackCount;
        }
        //for precise checking,oh may be kind of dummy
        return alpha <= 0.001f ? 0 : alpha;
    }

    private int calculateTotalOffset(int totalOffset, int dy) {
        return totalOffset + direction.layoutDirection * dy;
    }

    private void removeAndRecycleViews(RecyclerView.Recycler recycler, int dy) {
        detachAndScrapAttachedViews(recycler);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (recycleHorizontally(child, dy)) {
                removeAndRecycleView(child, recycler);
            }
        }
    }

    private int getDisplayItemWidth() {
        return mItemWidth;
    }

    private float getScaleSlope(float scale) {
        return (scale - EDIT_STATE_SCALE) * (1 / (1 - EDIT_STATE_SCALE));
    }

    private int getScaleUnit(float scale) {
        float spaceChange = getScaleSlope(scale) * (mSpace + mItemSpace - EDIT_STATE_ITEM_SPACE);
        return (int) (mItemWidth * scale + EDIT_STATE_ITEM_SPACE + spaceChange);
    }

    private int getScaleItemWidth(float scale) {
        return (int) (mItemWidth * scale);
    }

    private static class DisplayItemInfo {
        public int start;
        public int end;

        public DisplayItemInfo(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    private float alphaInEdit(int position) {
        return 1;
    }

    private float scaleCommonInEdit() {
        switch (direction) {
            default:
            case LEFT:
                return scaleDefaultInEdit();
        }
    }

    private float scaleInEdit(int position) {
        switch (direction) {
            default:
            case LEFT:
                return scaleDefaultInEdit(position);
        }
    }

    private float scaleDefaultInEdit(int position) {
        return mEditScale;
    }

    private float scaleDefaultInEdit() {
        return mEditScale;
    }

    private void setManagerModel(int managerModel) {
        mManagerModel = managerModel;
    }

    public void setDragItem(int position) {
        mDragItemPosition = position;
    }

    public void entryEditState() {
        if (mEditState) {
            return;
        }

        mEditState = true;
        setManagerModel(MANAGER_MODEL_EDIT);
        saveDisplayData();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, EDIT_STATE_PROPERTY_NAME, 1f, EDIT_STATE_SCALE);
        objectAnimator.addUpdateListener(mEditScaleUpdateListener);
        objectAnimator.addListener(mEditScaleAnimatorListener);
        objectAnimator.setDuration(EDIT_STATE_SCALE_DURATION);
        objectAnimator.start();
    }

    public void exitEditState() {
        if (!mEditState) {
            return;
        }

        mEditState = false;
        saveEditData();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, EDIT_STATE_PROPERTY_NAME, EDIT_STATE_SCALE, 1f);
        objectAnimator.addUpdateListener(mEditScaleUpdateListener);
        objectAnimator.addListener(mEditScaleAnimatorListener);
        objectAnimator.setDuration(EDIT_STATE_SCALE_DURATION);
        objectAnimator.start();
    }

    private ValueAnimator.AnimatorUpdateListener mEditScaleUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float scale = (float) animation.getAnimatedValue(EDIT_STATE_PROPERTY_NAME);
            updateEditScale(scale);
        }
    };

    private SimpleAnimatorListener mEditScaleAnimatorListener = new SimpleAnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            mEditZooming = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            mEditZooming = false;
            if (!mEditState) {
                setManagerModel(MANAGER_MODEL_DISPLAY);
                fill(recycler, 0);
            } else {
                entryEditTransform();
            }
        }
    };

    private void updateEditScale(float scale) {
//        Log.d(TAG, "updateEditScale() called with: scale = [" + scale + "]");
        mEditScale = scale;
        fill(recycler, 0);
    }

    //-------------- Edit model end --------------------
    private void log(String msg) {
        Timber.d(msg);
    }

    private class ViewEntry{
        int width;
        int height;
        int index;
    }

}
