package com.huawei.courselearningdemo.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.courselearningdemo.ui.activity.MainActivity;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.List;

import butterknife.BindView;
import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.ui.activity.CourseActivity;
import com.huawei.courselearningdemo.ui.adapter.CourseAdapter;
import com.huawei.courselearningdemo.utils.KeyboardUtil;
import com.huawei.courselearningdemo.utils.LoadState;
import com.huawei.courselearningdemo.utils.LogUtil;
import com.huawei.courselearningdemo.utils.SizeUtil;
import com.huawei.courselearningdemo.utils.ToastUtil;
import com.huawei.courselearningdemo.viewmodel.HomeViewModel;
import com.huawei.courselearningdemo.viewmodel.SharedViewModel;

public class HomeFragment extends BaseFragment implements CourseAdapter.ShowCourseItemClickListener{
    private SharedViewModel sharedViewModel;
    private HomeViewModel homeViewModel;
    private CourseAdapter mAdapter;
    @BindView(R.id.course_list_refresh_layout)
    protected TwinklingRefreshLayout refreshLayout;
    @BindView(R.id.course_list_rv)
    public RecyclerView recyclerView;
    @BindView(R.id.search_course)
    protected EditText searchEditText;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        mAdapter = new CourseAdapter();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        // ????????????????????????
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtil.dip2px(getContext(),5.0f);
                outRect.bottom = SizeUtil.dip2px(getContext(),5.0f);
                outRect.left = SizeUtil.dip2px(getContext(),8.0f);
                outRect.right = SizeUtil.dip2px(getContext(),8.0f);
            }
        });
        // ??????????????????
        refreshLayout.setEnableLoadmore(true);
        // ??????????????????
        refreshLayout.setEnableRefresh(true);
        // ????????????????????????
        refreshLayout.setEnableOverScroll(true);
    }

    @Override
    protected void initViewModel() {
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    protected void initObserver() {
        sharedViewModel.getUid().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String uid) {
                homeViewModel.setUid(uid);
            }
        });
        homeViewModel.getData().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                LogUtil.d(homeViewModel, "courses changed size = "+ courses.size());
                mAdapter.setData(courses);
            }
        });
        homeViewModel.getState().observe(this, new Observer<LoadState>() {
            @Override
            public void onChanged(LoadState loadState) {
                LogUtil.d(homeViewModel, "course list load state: "+loadState.toString());
                switch (loadState){
                    case SUCCESS:
                        refreshLayout.finishLoadmore();
                        refreshLayout.finishRefreshing();
                        break;
                    case EMPTY:
                        ToastUtil.showLongToast("?????????????????????");
                        refreshLayout.finishLoadmore();
                        refreshLayout.finishRefreshing();
                        break;
                    case ERROR:
                        ToastUtil.showLongToast("?????????????????????????????????");
                        refreshLayout.finishLoadmore();
                        refreshLayout.finishRefreshing();
                        break;
                    case LOADING:
                        ToastUtil.showLongToast("????????????????????????????????????");
                        break;
                }
            }
        });
        homeViewModel.getCourseOrderFlag().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    ToastUtil.showShortToast("??????????????????????????????????????????????????????");
//                    homeViewModel.query();
                }else{
                    ToastUtil.showShortToast("????????????????????????????????????????????????");
                }
                // ?????????????????????????????????????????????
                sharedViewModel.reloadUserDataFromServer();
            }
        });
    }

    @Override
    protected void initListener() {
        // ????????????????????????????????????????????????????????????????????????????????????????????????
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                homeViewModel.setQueryKey(v.getText().toString());
                homeViewModel.query();
                KeyboardUtil.hide(getContext(), v);
                return true;
            }
        });
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                // ????????????????????????
                homeViewModel.getNextPageData();
            }

            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                // ??????????????????
                homeViewModel.query();
            }
        });
        // ???????????????????????????????????????????????????????????????
        mAdapter.setShowCourseItemClickListener(this);
    }


    // ????????????????????????????????????
    @Override
    public void showCourseItemClicked(Course course) {
        Intent intent = new Intent(getActivity(), CourseActivity.class);
        // ???course???????????????intent?????????????????????????????????
        intent.putExtra("course", course);
        intent.putExtra("fragment", "course_ware");
        startActivity(intent);
    }

}
