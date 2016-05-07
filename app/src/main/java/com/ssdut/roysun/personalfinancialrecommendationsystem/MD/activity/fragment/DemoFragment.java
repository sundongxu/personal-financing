package com.ssdut.roysun.personalfinancialrecommendationsystem.MD.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.ssdut.roysun.personalfinancialrecommendationsystem.MD.activity.DemoActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.MD.adapter.DemoAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.R;

import java.util.ArrayList;

/**
 * Created by roysun on 16/4/1
 * MD风格的Fragment，带RecycleView，可用于OthersFragment
 */

public class DemoFragment extends BaseFragmentMD {

	private RecyclerView recyclerView;
	private RecyclerView.LayoutManager layoutManager;

	/**
	 * Create a new instance of the fragment
	 */
	public static DemoFragment newInstance(int index) {
		DemoFragment fragment = new DemoFragment();
		Bundle b = new Bundle();
		b.putInt("index", index);
		fragment.setArguments(b);
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (getArguments().getInt("index", -1) == 0) {
			View view = inflater.inflate(R.layout.fragment_demo_settings, container, false);
			initDemoSettings(view);
			return view;
		} else {
			View view = inflater.inflate(R.layout.fragment_others, container, false);
			initDemoList(view);
			return view;
		}
	}

	/**
	 * Init demo settings
	 */
	private void initDemoSettings(View view) {
		final DemoActivity demoActivity = (DemoActivity) getActivity();
		final SwitchCompat switchColored = (SwitchCompat) view.findViewById(R.id.fragment_demo_switch_colored);
		final SwitchCompat switchFiveItems = (SwitchCompat) view.findViewById(R.id.fragment_demo_switch_five_items);

		switchColored.setChecked(demoActivity.isBottomNavigationColored());
		switchFiveItems.setChecked(demoActivity.getBottomNavigationNbItems() == 5);

		switchColored.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				demoActivity.updateBottomNavigationColor(isChecked);
			}
		});
		switchFiveItems.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				demoActivity.updateBottomNavigationItems(isChecked);
			}
		});
	}

	/**
	 * Init the fragment
	 */
	private void initDemoList(View view) {

		recyclerView = (RecyclerView) view.findViewById(R.id.fragment_demo_recycler_view);
		recyclerView.setHasFixedSize(true);
		layoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(layoutManager);

		ArrayList<String> itemsData = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			itemsData.add("Fragment " + getArguments().getInt("index", -1) + " / Item " + i);
		}

		DemoAdapter adapter = new DemoAdapter(itemsData);
		recyclerView.setAdapter(adapter);
	}

	/**
	 * Refresh
	 */
	public void refresh() {
		recyclerView.smoothScrollToPosition(0);
	}
}
