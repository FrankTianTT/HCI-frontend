package com.huawei.courselearningdemo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = loadRootView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        initView();
        initViewModel();
        initObserver();
        initListener();
        return rootView;
    }

    /**
     * 如果子类需要初始化各种UI组件，覆盖此方法
     */
    protected void initView() {

    }

    /**
     * 如果子类需要去设置监听事件，覆盖此方法
     */
    protected void initListener() {

    }

    /**
     * 如果子类需要初始化ViewModel，覆盖此方法
     */
    protected void initViewModel() {

    }
    /**
     * 如果子类需要观察数据变化，覆盖此方法
     */
    protected void initObserver() {

    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        int restId = getRootViewResId();
        return inflater.inflate(restId, container, false);
    }

    protected abstract int getRootViewResId();
}
