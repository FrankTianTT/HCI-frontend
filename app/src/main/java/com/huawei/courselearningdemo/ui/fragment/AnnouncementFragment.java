package com.huawei.courselearningdemo.ui.fragment;

import android.graphics.Rect;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.CourseWare;
import com.huawei.courselearningdemo.ui.adapter.CourseWareAdapter;
import com.huawei.courselearningdemo.utils.SizeUtil;
import com.huawei.courselearningdemo.viewmodel.CourseViewModel;
import com.huawei.courselearningdemo.viewmodel.SharedViewModel;

import java.util.List;

import butterknife.BindView;

public class AnnouncementFragment extends BaseFragment {
    private CourseViewModel courseViewModel;
    @BindView(R.id.course_content_intro_tv)
    TextView courseIntroTv;
    @BindView(R.id.course_ratingbar)
    RatingBar courseScoreBar;
    @BindView(R.id.course_score_intro_tv)
    TextView courseScoreIntroTv;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_announcement;
    }

    @Override
    protected void initView(){
    }

    @Override
    protected void initViewModel() {
        courseViewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);
    }

    protected void initObserver() {
        courseViewModel.getCourseData().observe(this, new Observer<Course>() {
            @Override
            public void onChanged(Course course) {
                if(course!=null){
                    courseIntroTv.setText(course.getIntro());
                }
            }
        });
        courseViewModel.getCourseScore().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                courseScoreBar.setIsIndicator(true);
                if (aDouble < 0){
                    courseScoreIntroTv.setText("抱歉，目前暂时无人评分。");
                    courseScoreBar.setRating(0);
                }
                else{
                    courseScoreBar.setRating(aDouble.floatValue());
                }
            }
        });

    }
}
