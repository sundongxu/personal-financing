package com.ssdut.roysun.personalfinancialrecommendationsystem.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.TestAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.component.DividerItemDecoration;

/**
 * Created by roysun on 16/5/15.
 * ViewPager + Fragment布局 测试用
 */
public class TestFragment extends Fragment {

    private View rootView;
    private RecyclerView mRecyclerView;
    private TestAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
//    private WebView mWebView;

    public static TestFragment newInstance() {
        return new TestFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_test, null);
            initView(rootView);// 控件初始化
        }
        return rootView;
    }

    public void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_test_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TestAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
//        mWebView = (WebView) view.findViewById(R.id.wv_test);
//        mWebView.loadUrl("http://xw.qq.com/index.htm");
//
//        mWebView.setWebChromeClient(new WebChromeClient(){
//
//        });
//
//        mWebView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return super.shouldOverrideUrlLoading(view, url);
//            }
//        });
    }

}
