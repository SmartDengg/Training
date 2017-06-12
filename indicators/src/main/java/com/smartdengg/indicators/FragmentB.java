package com.smartdengg.indicators;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static android.widget.LinearLayout.VERTICAL;

/**
 * 创建时间:  2017/02/28 11:11 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class FragmentB extends Fragment {

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    RecyclerView recyclerView = (RecyclerView) view;
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    layoutManager.setOrientation(VERTICAL);
    recyclerView.setLayoutManager(layoutManager);

    RecyclerView.Adapter adapter = new RecyclerViewAdapter(getActivity());
    recyclerView.setAdapter(adapter);
  }
}
