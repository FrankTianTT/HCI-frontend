package com.huawei.courselearningdemo.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;

import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.ui.activity.CourseActivity;
import com.huawei.courselearningdemo.ui.activity.MainActivity;
import com.huawei.courselearningdemo.ui.adapter.CourseAdapter;
import com.huawei.courselearningdemo.utils.SizeUtil;
import com.huawei.courselearningdemo.utils.StringUtil;
import com.huawei.courselearningdemo.utils.ToastUtil;
import com.huawei.courselearningdemo.viewmodel.SharedViewModel;
import com.huawei.courselearningdemo.viewmodel.StudyViewModel;

public class StudyFragment extends BaseFragment implements CourseAdapter.ShowCourseItemClickListener {
    private SharedViewModel sharedViewModel;
    private StudyViewModel studyViewModel;
    private CourseAdapter mAdapter;
    @BindView(R.id.login_warn_tv)
    protected TextView loginWarnTv;
    @BindView(R.id.none_bought_tv)
    protected TextView noneBoughtTv;
    @BindView(R.id.star_course_list_rv)
    protected RecyclerView recyclerView;
    @BindView(R.id.back_to_home)
    protected Button backToHomeBtn;
    @BindView(R.id.none_bought_layout)
    protected LinearLayout noneBoughtLayout;

    public static StudyViewModel studyViewModelCopy;
    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_study;
    }

    protected void initView() {
        mAdapter = new CourseAdapter();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        // 设置四周的分割线
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtil.dip2px(getContext(), 5.0f);
                outRect.bottom = SizeUtil.dip2px(getContext(), 5.0f);
                outRect.left = SizeUtil.dip2px(getContext(), 8.0f);
                outRect.right = SizeUtil.dip2px(getContext(), 8.0f);
            }
        });
    }

    @Override
    protected void initViewModel() {
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        studyViewModel = new ViewModelProvider(this).get(StudyViewModel.class);
        studyViewModelCopy = studyViewModel;
    }

    @Override
    protected void initObserver() {
        sharedViewModel.getUid().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String uid) {
                if (StringUtil.hasText(uid)) {
                    studyViewModel.setUid(uid);
                    initLoggedInView();
                } else {
                    studyViewModel.setUid(null);
                    initUnLogInView();
                }
            }
        });
        studyViewModel.getStudyCourseListLiveData().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                if (courses.size() == 0)
                    noneBoughtLayout.setVisibility(View.VISIBLE);
                mAdapter.setData(courses);
            }
        });
    }

    @Override
    protected void initListener() {
        mAdapter.setShowCourseItemClickListener(this);
        backToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
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

    private void initUnLogInView() {
        loginWarnTv.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        noneBoughtLayout.setVisibility(View.GONE);
    }

    private void initLoggedInView() {
        loginWarnTv.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
