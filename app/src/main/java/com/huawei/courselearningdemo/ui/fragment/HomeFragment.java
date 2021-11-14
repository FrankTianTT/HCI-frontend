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

public class HomeFragment extends BaseFragment implements CourseAdapter.BuyCourseItemClickListener, CourseAdapter.ShowCourseItemClickListener ,CourseAdapter.StarCourseItemClickListener{
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
        // 设置四周的分割线
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtil.dip2px(getContext(),5.0f);
                outRect.bottom = SizeUtil.dip2px(getContext(),5.0f);
                outRect.left = SizeUtil.dip2px(getContext(),8.0f);
                outRect.right = SizeUtil.dip2px(getContext(),8.0f);
            }
        });
        // 允许加载更多
        refreshLayout.setEnableLoadmore(true);
        // 允许下拉刷新
        refreshLayout.setEnableRefresh(true);
        // 开启越界回弹模式
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
                        ToastUtil.showLongToast("没有更多内容了");
                        refreshLayout.finishLoadmore();
                        refreshLayout.finishRefreshing();
                        break;
                    case ERROR:
                        ToastUtil.showLongToast("网络异常，请稍后重试！");
                        refreshLayout.finishLoadmore();
                        refreshLayout.finishRefreshing();
                        break;
                    case LOADING:
                        ToastUtil.showLongToast("请耐心等待，内容正在加载");
                        break;
                }
            }
        });
        homeViewModel.getCourseOrderFlag().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    ToastUtil.showShortToast("课程购买成功，请前往课程学习页面查看");
//                    homeViewModel.query();
                }else{
                    ToastUtil.showShortToast("购买失败，请检查钻石余额是否充足");
                }
                // 不管成功与否，都刷新用户的余额
                sharedViewModel.reloadUserDataFromServer();
            }
        });
    }

    @Override
    protected void initListener() {
        // 设置当点击搜索框后，按下软键盘的搜索按钮后，开始搜索并收起软键盘
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
                // 去加载更多的内容
                homeViewModel.getNextPageData();
            }

            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                // 刷新当前页面
                homeViewModel.query();
            }
        });
        // 为课程列表中的每一项内的两个按钮加上监听器
        mAdapter.setBuyCourseItemClickListener(this);
        mAdapter.setShowCourseItemClickListener(this);
        mAdapter.setStarCourseItemClickListener(this);
    }

    // 点击了购买课程按钮后执行
    @Override
    public void buyCourseItemClicked(Course course) {
        showPurchaseDialog(course);
    }

    // 点击了查看课程按钮后执行
    @Override
    public void showCourseItemClicked(Course course) {
        Intent intent = new Intent(getActivity(), CourseActivity.class);
        // 将course对象绑定到intent上，传递给课程详情页面
        intent.putExtra("course", course);
        intent.putExtra("fragment", "course_ware");
        startActivity(intent);
    }

    private void showPurchaseDialog(Course course){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireActivity());
        dialogBuilder.setTitle("购买课程 "+course.getName());
        dialogBuilder.setMessage("确认支付 "+ course.getCost()+" 个钻石？");
        dialogBuilder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(homeViewModel.getUid() == null){
                            ToastUtil.showShortToast("请先登录账号！");
                        }else {
                            homeViewModel.createCourseOrder(course);
                        }
                    }
                });
        dialogBuilder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtil.showShortToast("取消购买");
                    }
                });
        // 显示对话框
        dialogBuilder.show();
    }

    @Override
    public void starCourseItemClicked(Course course) {
        homeViewModel.addStar(course);
        ((MainActivity)getActivity()).refreshFromFragment("home");
    }
}
