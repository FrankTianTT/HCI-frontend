package com.huawei.courselearningdemo.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.CourseWare;
import com.huawei.courselearningdemo.ui.activity.ContentViewActivity;
import com.huawei.courselearningdemo.ui.activity.CourseActivity;
import com.huawei.courselearningdemo.ui.adapter.CourseWareAdapter;
import com.huawei.courselearningdemo.utils.KeyboardUtil;
import com.huawei.courselearningdemo.utils.SizeUtil;
import com.huawei.courselearningdemo.viewmodel.CourseViewModel;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.List;

import butterknife.BindView;

public class CourseWareFragment extends BaseFragment implements CourseWareAdapter.ShowCourseWareItemClickListener {
    private CourseWareAdapter mAdapter;
    private CourseViewModel courseViewModel;
    @BindView(R.id.course_ware_content_list)
    public RecyclerView recyclerView;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_lecture;
    }

    @Override
    protected void initView(){
        mAdapter = new CourseWareAdapter();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        // 设置四周的分割线
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtil.dip2px(getContext(),5.0f);
                outRect.bottom = SizeUtil.dip2px(getContext(),5.0f);
                outRect.left = SizeUtil.dip2px(getContext(),2.5f);
                outRect.right = SizeUtil.dip2px(getContext(),2.5f);
            }
        });
    }

    @Override
    protected void initViewModel() {
        courseViewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);
    }

    @Override
    protected void initListener() {
        // 为课程列表中的每一项内的两个按钮加上监听器
        mAdapter.setShowCourseWareItemClickListener(this);
    }

    protected void initObserver() {
        courseViewModel.getCourseWareData().observe(this, new Observer<List<CourseWare>>() {
            @Override
            public void onChanged(List<CourseWare> courseWareList) {
                mAdapter.setData(courseWareList);
            }
        });
    }

    // 点击了查看课程按钮后执行
    @Override
    public void showCourseWareItemClicked(CourseWare courseWare) {
        Intent intent = new Intent(getActivity(), ContentViewActivity.class);
        // 将course对象绑定到intent上，传递给课程详情页面
        intent.putExtra("courseWare", courseWare);
        startActivity(intent);
    }
}
