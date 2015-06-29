package com.example.kaoru.fragmentinlistview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * Created by kaoru on 2015/06/28.
 */
public class TopFragment extends Fragment {
    private final ListViewEmbeddedFragmentController mController = new ListViewEmbeddedFragmentController(this);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mController.onCreateView(inflater, container, savedInstanceState);
        View containerView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView) containerView.findViewById(R.id.list_view);
        listView.setAdapter(new MyAdapter());
        listView.setRecyclerListener(mRecyclerListener);
        return containerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mController.setResumed(true);
    }

    @Override
    public void onPause() {
        mController.setResumed(false);
        super.onPause();
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? 0 : 1;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(position == 0 ? R.layout.list_item : R.layout.list_item2, parent, false);
                convertView.setTag(getItemViewType(position));
            }

            if (position == 0) {
                mController.getView(convertView);
            }

            return convertView;
        }
    }

    private AbsListView.RecyclerListener mRecyclerListener = new AbsListView.RecyclerListener() {
        @Override
        public void onMovedToScrapHeap(View view) {
            int viewType = (int) view.getTag();
            if (viewType == 0) {
                mController.onMovedToScrapHeap(view);
            }
        }
    };
}
