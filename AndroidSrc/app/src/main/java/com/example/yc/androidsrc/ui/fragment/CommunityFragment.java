package com.example.yc.androidsrc.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yc.androidsrc.R;

/**
 * @author RebornC
 * Created on 2018/11/28.
 */

public class CommunityFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#e2e7d3"));
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        TextView title = (TextView) getActivity().findViewById(R.id.title);
        title.setText("");
        title.setTextColor(getResources().getColor(R.color.white));

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            // onPause()
        } else {
            // onResume()
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            toolbar.setBackgroundColor(Color.parseColor("#e2e7d3"));
            toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
            TextView title = (TextView) getActivity().findViewById(R.id.title);
            title.setText("");
            title.setTextColor(getResources().getColor(R.color.white));
        }
    }
}
