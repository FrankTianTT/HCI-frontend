package com.huawei.courselearningdemo.ui.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.CourseWare;
import com.huawei.courselearningdemo.ui.adapter.CourseWareAdapter;
import com.huawei.courselearningdemo.utils.SizeUtil;
import com.huawei.courselearningdemo.utils.ToastUtil;
import com.huawei.courselearningdemo.viewmodel.CourseViewModel;

public class CourseActivity extends AppCompatActivity {
    private Integer courseId;
    private Course course;
    private CourseWareAdapter mAdapter;
    private CourseViewModel courseViewModel;
    @BindView(R.id.course_ware_content_list)
    public RecyclerView recyclerView;
    @BindView(R.id.course_content_name_tv)
    TextView courseNameTv;
    @BindView(R.id.course_content_provider_tv)
    TextView courseProviderTv;
    @BindView(R.id.course_content_intro_tv)
    TextView courseIntroTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        ButterKnife.bind(this);
        if(getIntent().getExtras() != null) {
            course = (Course) getIntent().getSerializableExtra("course");
            if (course == null)
                courseId = getIntent().getIntExtra("courseId", 0);
        }else{
            ToastUtil.showLongToast("当前页面加载错误，请稍后重试！");
        }
        initView();
        initViewModel();
        initObserver();
    }

    private void initView(){
        mAdapter = new CourseWareAdapter();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(CourseActivity.this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        // 设置四周的分割线
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtil.dip2px(CourseActivity.this,5.0f);
                outRect.bottom = SizeUtil.dip2px(CourseActivity.this,5.0f);
                outRect.left = SizeUtil.dip2px(CourseActivity.this,2.5f);
                outRect.right = SizeUtil.dip2px(CourseActivity.this,2.5f);
            }
        });
    }

    private void initViewModel() {
        courseViewModel = new ViewModelProvider(CourseActivity.this).get(CourseViewModel.class);
        if(course != null)
            courseViewModel.setCourse(course);
        else if(courseId != null && courseId != 0)
            courseViewModel.loadCourse(course);
    }

    private void initObserver() {
        courseViewModel.getCourseData().observe(this, new Observer<Course>() {
            @Override
            public void onChanged(Course course) {
                if(course!=null){
                    courseNameTv.setText(course.getName());
                    courseProviderTv.setText(course.getProvider());
                    courseIntroTv.setText(course.getIntro());
                }
            }
        });
        courseViewModel.getCourseWareData().observe(this, new Observer<List<CourseWare>>() {
            @Override
            public void onChanged(List<CourseWare> courseWareList) {
                mAdapter.setData(courseWareList);
            }
        });
    }
}
