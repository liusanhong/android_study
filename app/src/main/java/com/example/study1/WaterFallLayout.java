package com.example.study1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * file_name:WaterFallLayout
 * date:2020-02-02 10:01
 * author:LQ
 * describe:
 */
public class WaterFallLayout extends ViewGroup {
    //    保存行高的列表
    private List<Integer> lstLineHeight = new ArrayList<>();

    //    保存所有组件的列表
    private List<List<View>> lstLineView = new ArrayList<>();
    private boolean isFlag = false;

    public WaterFallLayout(Context context) {
        super(context);
    }

    public WaterFallLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WaterFallLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return super.generateLayoutParams(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (isFlag) {
            return;
        }
        isFlag = true;
//      得到父容器宽高
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

//      得到父容器模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

//      当前容器宽高
        int measureWidth = 0;
        int measureHeight = 0;

        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            measureWidth = widthSize;
            measureHeight = heightSize;
        } else {
//            子空间宽高
            int iChildWidth = 0;
            int iChildHeight = 0;

//            当前行宽，高
            int iCurLineW = 0;
            int iCurLineH = 0;
//            每一行的view
            List<View> viewlist = new ArrayList<>();

            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = getChildAt(i);

//                测量自己
                measureChild(childAt, widthMeasureSpec, heightMeasureSpec);

                MarginLayoutParams layoutParams = (MarginLayoutParams) childAt.getLayoutParams();
//                每个自组件的宽高
                iChildWidth = childAt.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
                iChildHeight = childAt.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;

//                是否需要换行
                if (iChildWidth + iCurLineW > widthSize) {
                    measureWidth = Math.max(measureWidth, iCurLineW);
                    measureHeight += iCurLineH;

                    lstLineHeight.add(iCurLineH);
                    lstLineView.add(viewlist);

//                    重新行宽
                    iCurLineW = iChildWidth;
                    iCurLineH = iChildHeight;
//
                    viewlist = new ArrayList<>();
                    viewlist.add(childAt);

                } else {
                    iCurLineW += iChildWidth;
                    iCurLineH = Math.max(iCurLineH, iChildHeight);

                    viewlist.add(childAt);
                }

//                最后一行
                if (i == childCount - 1) {
                    measureWidth = Math.max(measureWidth, iCurLineW);
                    measureHeight += iCurLineH;

                    lstLineHeight.add(iCurLineH);
                    lstLineView.add(viewlist);
                }
            }
        }


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int left, right, top, bottom;

        int curTop = 0;
        int curLeft = 0;

//        迭代
        int lineCount = lstLineView.size();
        for (int i = 0; i < lineCount; i++) {
            List<View> viewList = lstLineView.get(i);

            int lineViewSize = viewList.size();
            for (int j = 0; j < lineViewSize; j++) {
                View childView = viewList.get(j);
                MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();

                left = curLeft + layoutParams.leftMargin;
                top = curTop + layoutParams.topMargin;

                right = left + childView.getMeasuredWidth();
                bottom = top = childView.getMeasuredHeight();

                childView.layout(left, top, right, bottom);

                curLeft += childView.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;

            }

            curLeft = 0;
            curTop += lstLineHeight.get(i);
        }

        lstLineView.clear();
        lstLineView.clear();

    }

}
