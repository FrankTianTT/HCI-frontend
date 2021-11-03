package com.huawei.courselearningdemo.ui.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.CourseWare;
import com.huawei.courselearningdemo.ui.adapter.CourseWareAdapter;
import com.huawei.courselearningdemo.ui.fragment.AccountFragment;
import com.huawei.courselearningdemo.ui.fragment.AnnouncementFragment;
import com.huawei.courselearningdemo.ui.fragment.BaseFragment;
import com.huawei.courselearningdemo.ui.fragment.DiscussFragment;
import com.huawei.courselearningdemo.ui.fragment.ExaminationFragment;
import com.huawei.courselearningdemo.ui.fragment.HomeFragment;
import com.huawei.courselearningdemo.ui.fragment.LectureFragment;
import com.huawei.courselearningdemo.ui.fragment.StudyFragment;
import com.huawei.courselearningdemo.utils.SizeUtil;
import com.huawei.courselearningdemo.utils.ToastUtil;
import com.huawei.courselearningdemo.viewmodel.CourseViewModel;
import com.huawei.courselearningdemo.viewmodel.SharedViewModel;

public class CourseActivity extends AppCompatActivity {
    private Integer courseId;
    private Course course;

    TextView courseNameTv;
    TextView courseProviderTv;
    // 导航栏
    private BottomNavigationView mainNavigationView;
    // 上一次显示的fragment
    private BaseFragment lastOneFragment = null;
    // fragment管理器
    private FragmentManager fragmentManager = null;
    private AnnouncementFragment announcementFragment;
    private LectureFragment lectureFragment;
    private ExaminationFragment examinationFragment;
    private DiscussFragment discussFragment;
    private CourseViewModel courseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        if(getIntent().getExtras() != null) {
            course = (Course) getIntent().getSerializableExtra("course");
            if (course == null)
                courseId = getIntent().getIntExtra("courseId", 0);
        }else{
            ToastUtil.showLongToast("当前页面加载错误，请稍后重试！");
        }

        courseNameTv = findViewById(R.id.course_content_name_tv);
        courseProviderTv = findViewById(R.id.course_content_provider_tv);

        initView();
        initListener();
        initViewModel();
        initObserver();
    }

    private void initView() {
        mainNavigationView = this.findViewById(R.id.course_navigation_bar);
        announcementFragment = new AnnouncementFragment();
        lectureFragment = new LectureFragment();
        examinationFragment = new ExaminationFragment();
        discussFragment = new DiscussFragment();

        fragmentManager = getSupportFragmentManager();
        switchFragment(announcementFragment);
    }

    private void initListener(){
        mainNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.announcement:
                        switchFragment(announcementFragment);
                        break;
                    case R.id.lecture:
                        switchFragment(lectureFragment);
                        break;
                    case R.id.examination:
                        switchFragment(examinationFragment);
                        break;
                    case R.id.discuss:
                        switchFragment(discussFragment);
                        break;
                }
                return true;
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
                Log.d("CourseActivity_initObserver", course.getName());
                Log.d("CourseActivity_initObserver", String.valueOf(courseNameTv));
                if(course!=null){
                    courseNameTv.setText(course.getName());
                    courseProviderTv.setText(course.getProvider());
                }
            }
        });
    }

    private void switchFragment(BaseFragment targetFragment) {
        // 如果上一个fragment跟当前要切换的fragment是同一个，那么不需要切换
        if(lastOneFragment == targetFragment) {
            return;
        }
        // 使用add和hide的方式来控制Fragment的切换
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(lastOneFragment != null) {
            fragmentTransaction.hide(lastOneFragment);
        }
        if(!targetFragment.isAdded()) {
            fragmentTransaction.add(R.id.course_page_container, targetFragment);
        } else {
            fragmentTransaction.show(targetFragment);
        }
        lastOneFragment = targetFragment;
        //fragmentTransaction.replace(R.id.main_page_container, targetFragment);
        fragmentTransaction.commit();
    }
}
