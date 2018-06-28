package com.anlijiu.example.ui.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class MyLayoutManager extends RecyclerView.LayoutManager {

    private Context context;

    private int horizontallyOffset=0;

    private int totalWidth=0;//控件的总宽度

    //保存所有的Item的上下左右的偏移量信息

    private SparseArray<Rect> allItemFrames = new SparseArray<>();

    //记录Item是否出现过屏幕且还没有回收。true表示出现过屏幕上，并且还没被回收

    private SparseBooleanArray hasAttachedItems = new SparseBooleanArray();

    public MyLayoutManager(Context activity) {

        this.context= activity;

    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler,RecyclerView.State state) {

        //如果没有item，直接返回

        if(getItemCount() <=0)return;

        //跳过preLayout，preLayout主要用于支持动画

        if(state.isPreLayout()) {

            return;

        }

        //在布局之前，将所有的子View先Detach掉，放入到Scrap缓存中

        detachAndScrapAttachedViews(recycler);

        /**

         *定义横、竖方向的偏移量

*/

        int offsetX =0;

        int offsetY =0;

        totalWidth=0;//初始化宽度

        int lins =1;//行

        int column =0;//页

        int linNumber =0;

        int number =0;

        DisplayMetrics metric =new DisplayMetrics();

        ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metric);

        int maxWidth = metric.widthPixels;//屏幕宽度（像素）

        Log.d("[MyLayoutManager]","maxWidth:"+ maxWidth);

        Log.d("[MyLayoutManager]","ItemCount:"+ getItemCount());

        for(int i =0;i < getItemCount();i++) {

            View scrap = recycler.getViewForPosition(i);//根据position获取一个碎片view，可以从回收的view中获取，也可能新构造一个

            addView(scrap);

            measureChildWithMargins(scrap,0,0);//计算此碎片view包含边距的尺寸

            int width = getDecoratedMeasuredWidth(scrap);//获取此碎片view包含边距和装饰的宽度width

            int height = getDecoratedMeasuredHeight(scrap);//获取此碎片view包含边距和装饰的高度height

            linNumber = maxWidth / width;

            /**
             * view复用
             */

            Rect frame =allItemFrames.get(i);

            if(frame ==null) {

                frame =new Rect();

            }

            if(number >= linNumber) {//第二行

                if(number == linNumber) offsetX = offsetX - linNumber * width;

                //最后，将View布局到RecyclerView容器中

                //                layoutDecorated(scrap, offsetX, height, offsetX + width, height * 2);

                frame.set(offsetX,height,offsetX + width,height *2);

                offsetX += width;

                if(number == (linNumber *2-1)) {

                    number =0;

                    column++;

                }else{

                    number++;

                }

            }else{//第一行

                //最后，将View布局到RecyclerView容器中

                //                layoutDecorated(scrap, offsetX, offsetY, offsetX + width, height);

                frame.set(offsetX,offsetY,offsetX + width,height);

                offsetX += width;

                number++;

            }

            /**

             * view复用

*/

            //将当前的Item的Rect边界数据保存

            allItemFrames.put(i,frame);

            //由于已经调用了detachAndScrapAttachedViews，因此需要将当前的Item设置为未出现过

            hasAttachedItems.put(i, false);

            //第一行  第二行

            totalWidth= number < linNumber ? offsetX : column * width * linNumber - (maxWidth - linNumber * width);

        }

        Log.d("[MyLayoutManager]","totalWidth-1:"+totalWidth);

        //如果所有子View的高度和没有填满RecyclerView的高度，

        //则将高度设置为RecyclerView的高度

        totalWidth= Math.max(totalWidth,getHorizontallySpace());

        Log.d("[MyLayoutManager]","totalWidth-2:"+totalWidth);

        /**

         * view复用

*/

        recycleAndFillItems(recycler,state);

    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {

        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,

                ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @Override

    public boolean canScrollHorizontally() {

        return true;

    }

    @Override

    public int scrollHorizontallyBy(int dx,RecyclerView.Recycler recycler,RecyclerView.State state) {

        //先detach掉所有的子View

        detachAndScrapAttachedViews(recycler);

        //实际要滑动的距离

        int travel = dx;

        Log.d("[MyLayoutManager]","travel:"+ travel);

        //如果滑动到最顶部

        if(horizontallyOffset+ dx <0) {

            travel = -horizontallyOffset;

        }else if(horizontallyOffset+ dx >totalWidth) {//如果滑动到最底部horizontallyOffset + dx > totalWidth - getHorizontallySpace()

            //            travel = totalWidth - getHorizontallySpace() - horizontallyOffset;

            travel =totalWidth-horizontallyOffset;

        }

        //将横向的偏移量+travel

        horizontallyOffset+= travel;

        Log.d("[MyLayoutManager]","travel-1:"+ travel);

        //平移容器内的item

        offsetChildrenHorizontal(-travel);

        /**

         * view复用

*/

        recycleAndFillItems(recycler,state);

        return travel;

    }

    /**

     *回收不需要的Item，并且将需要显示的Item从缓存中取出

*/

    private void recycleAndFillItems(RecyclerView.Recycler recycler,RecyclerView.State state) {

        if(state.isPreLayout()) {//跳过preLayout，preLayout主要用于支持动画

            return;

        }

        //当前scroll offset状态下的显示区域

        Rect displayFrame =new Rect(horizontallyOffset,0,horizontallyOffset+ getHorizontallySpace(),getVerticalSpace());

        /**

         *将滑出屏幕的Items回收到Recycle缓存中

*/

        Rect childFrame =new Rect();

        for(int i =0;i < getChildCount();i++) {

            View child = getChildAt(i);

            childFrame.left= getDecoratedLeft(child);

            childFrame.top= getDecoratedTop(child);

            childFrame.right= getDecoratedRight(child);

            childFrame.bottom= getDecoratedBottom(child);

            //如果Item没有在显示区域，就说明需要回收

            if(!Rect.intersects(displayFrame,childFrame)) {

                //回收掉滑出屏幕的View

                removeAndRecycleView(child,recycler);

            }

        }

        //重新显示需要出现在屏幕的子View

        for(int i =0;i < getItemCount();i++) {

            if(Rect.intersects(displayFrame,allItemFrames.get(i))) {

                View scrap = recycler.getViewForPosition(i);

                measureChildWithMargins(scrap,0,0);

                addView(scrap);

                Rect frame =allItemFrames.get(i);// -horizontallyOffset

                //将这个item布局出来

                layoutDecorated(scrap,

                        frame.left-horizontallyOffset,

                        frame.top,

                        frame.right-horizontallyOffset,

                        frame.bottom);

            }

        }

    }

    /**

     *获取RecyclerView在水平方向上的可用空间，

     *即去除了padding后的高度

*/

    private int getHorizontallySpace() {

        return getWidth() - getPaddingLeft() - getPaddingRight();

    }

    /**

     *获取RecyclerView在竖直方向上的可用空间，

     *即去除了padding后的高度

*/

    private int getVerticalSpace() {

        return getHeight() - getPaddingBottom() - getPaddingTop();

    }

}
