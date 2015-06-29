package com.example.kaoru.fragmentinlistview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kaoru on 2015/06/28.
 */
public class ListViewEmbeddedFragmentController {
    private static final String TAG = "Controller";
    private static final String FRAGMENT_TAG = "CHILD_FRAGMENT";

    private final Fragment mParentFragment;

    private boolean mResumed = false;
    private boolean mActiveInListView = false;
    private boolean mItemViewAttachedToWindow = false;
    private boolean mFragmentActive = false;

    public ListViewEmbeddedFragmentController(Fragment parentFragment) {
        mParentFragment = parentFragment;
    }

    public void getView(View view) {
        if (view instanceof ListItemView) {
            Log.d(TAG, "getView attachedToWindow=" + ((ListItemView) view).isCustomAttachedToWindow());
            ((ListItemView) view).setOnCustomAttachedToWindowChangedListener(mOnCustomAttachedToWindowChangedListener);
            setActiveInListView(true);
            setItemViewAttachedToWindow(((ListItemView) view).isCustomAttachedToWindow());
        }
    }

    public void onMovedToScrapHeap(View view) {
        if (view instanceof ListItemView) {
            Log.d(TAG, "getView onMovedToScrapHeap=" + ((ListItemView) view).isCustomAttachedToWindow());
            ((ListItemView) view).setOnCustomAttachedToWindowChangedListener(null);
            setActiveInListView(false);
        }
    }

    private ListItemView.OnCustomAttachedToWindowChangedListener mOnCustomAttachedToWindowChangedListener = new ListItemView.OnCustomAttachedToWindowChangedListener() {
        @Override
        public void onAttachedToWindow(View view) {
            Log.d(TAG, "onAttachedToWindow");
            setItemViewAttachedToWindow(true);
        }

        @Override
        public void onDetachedToWindow(View view) {
            Log.d(TAG, "onDetachedToWindow");
            setItemViewAttachedToWindow(false);
        }
    };

    public boolean isActiveInListView() {
        return mActiveInListView;
    }

    public void setActiveInListView(boolean activeInListView) {
        mActiveInListView = activeInListView;
        check();
    }

    public boolean isItemViewAttachedToWindow() {
        return mItemViewAttachedToWindow;
    }

    public void setItemViewAttachedToWindow(boolean itemViewAttachedToWindow) {
        mItemViewAttachedToWindow = itemViewAttachedToWindow;
        check();
    }

    public boolean isResumed() {
        return mResumed;
    }

    public void setResumed(boolean resumed) {
        mResumed = resumed;
        check();
    }

    private void check() {
        if ( !isResumed()) {
            return;
        }

        setFragmentActive(isActiveInListView() && isItemViewAttachedToWindow());
    }

    public boolean isFragmentActive() {
        return mFragmentActive;
    }

    public void setFragmentActive(boolean fragmentActive) {
        if (mFragmentActive == fragmentActive) {
            return;
        }

        mFragmentActive = fragmentActive;

        Fragment fragment = mParentFragment.getChildFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        FragmentTransaction transaction = mParentFragment.getChildFragmentManager().beginTransaction();
        if (fragmentActive) {
            if (fragment == null) {
                fragment = new ChildFragment();
                transaction.add(R.id.list_item_fragment_container, fragment, FRAGMENT_TAG);
            } else {
                transaction.attach(fragment);
            }
        } else {
            if (fragment != null) {
                transaction.detach(fragment);
            }
        }
        transaction.commit();
    }

    public void onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }

        Fragment fragment = mParentFragment.getChildFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (fragment != null) {
            FragmentTransaction transaction = mParentFragment.getChildFragmentManager().beginTransaction();
            transaction.detach(fragment);
            transaction.commit();
        }
    }
}
