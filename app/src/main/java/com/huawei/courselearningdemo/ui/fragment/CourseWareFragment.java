package com.huawei.courselearningdemo.ui.fragment;

import android.graphics.Rect;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.CourseWare;
import com.huawei.courselearningdemo.ui.activity.CourseActivity;
import com.huawei.courselearningdemo.ui.adapter.CourseWareAdapter;
import com.huawei.courselearningdemo.utils.SizeUtil;
import com.huawei.courselearningdemo.utils.ToastUtil;
import com.huawei.courselearningdemo.viewmodel.CourseViewModel;
import com.huawei.courselearningdemo.viewmodel.HomeViewModel;
import com.huawei.courselearningdemo.viewmodel.LectureViewModel;
import com.huawei.courselearningdemo.viewmodel.SharedViewModel;

import java.util.List;

import butterknife.BindView;

public class CourseWareFragment extends BaseFragment {
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

    protected void initObserver() {
        courseViewModel.getCourseWareData().observe(this, new Observer<List<CourseWare>>() {
            @Override
            public void onChanged(List<CourseWare> courseWareList) {
                mAdapter.setData(courseWareList);
            }
        });
    }
}
