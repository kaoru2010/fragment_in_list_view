package com.example.kaoru.fragmentinlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kaoru on 2015/06/28.
 */
public class ListItemView extends ViewGroup {
    private static final String TAG = "ListItemView";

    public ListItemView(Context context) {
        super(context);
    }

    public ListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = w / 4;
        setMeasuredDimension(w, h);

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.getVisibility() == GONE) {
                continue;
            }

            view.measure(
                    MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.getVisibility() == GONE) {
                continue;
            }

            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        }
    }

    private boolean mCustomAttachedToWindow = false;

    public boolean isCustomAttachedToWindow() {
        return mCustomAttachedToWindow;
    }

    public void setCustomAttachedToWindow(boolean customAttachedToWindow) {
        if (mCustomAttachedToWindow == customAttachedToWindow) {
            return;
        }

        mCustomAttachedToWindow = customAttachedToWindow;

        Integer viewType = (Integer) getTag();
        if (viewType != null && viewType == 0) {
            Log.d(TAG, "setCustomAttachedToWindow customAttachedToWindow=" + customAttachedToWindow);

            if (mOnCustomAttachedToWindowChangedListener != null) {
                if (customAttachedToWindow) {
                    mOnCustomAttachedToWindowChangedListener.onAttachedToWindow(this);
                } else {
                    mOnCustomAttachedToWindowChangedListener.onDetachedToWindow(this);
                }
            }
        }
    }

    public OnCustomAttachedToWindowChangedListener getOnCustomAttachedToWindowChangedListener() {
        return mOnCustomAttachedToWindowChangedListener;
    }

    public void setOnCustomAttachedToWindowChangedListener(OnCustomAttachedToWindowChangedListener onCustomAttachedToWindowChangedListener) {
        mOnCustomAttachedToWindowChangedListener = onCustomAttachedToWindowChangedListener;
    }

    private OnCustomAttachedToWindowChangedListener mOnCustomAttachedToWindowChangedListener;

    public interface OnCustomAttachedToWindowChangedListener {
        void onAttachedToWindow(View view);
        void onDetachedToWindow(View view);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setCustomAttachedToWindow(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        setCustomAttachedToWindow(false);
        super.onDetachedFromWindow();
    }
}
