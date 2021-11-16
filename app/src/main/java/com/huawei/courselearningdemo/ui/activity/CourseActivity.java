package com.huawei.courselearningdemo.ui.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.ui.fragment.AnnouncementFragment;
import com.huawei.courselearningdemo.ui.fragment.BaseFragment;
import com.huawei.courselearningdemo.ui.fragment.CommentFragment;
import com.huawei.courselearningdemo.ui.fragment.CourseWareFragment;
import com.huawei.courselearningdemo.ui.fragment.ExaminationFragment;
import com.huawei.courselearningdemo.ui.fragment.QueryFragment;
import com.huawei.courselearningdemo.utils.ToastUtil;
import com.huawei.courselearningdemo.viewmodel.CourseViewModel;

public class CourseActivity extends AppCompatActivity {
    private Integer courseId;

    public Course getCourse() {
        return course;
    }

    private Course course;

    private String defaultFragmentName;

    TextView courseNameTv;
    TextView courseProviderTv;
    // 导航栏
    private BottomNavigationView mainNavigationView;
    // 上一次显示的fragment
    private BaseFragment lastOneFragment = null;
    // fragment管理器
    private FragmentManager fragmentManager = null;
    private AnnouncementFragment announcementFragment;
    private CourseWareFragment courseWareFragment;
    private ExaminationFragment examinationFragment;
    private CommentFragment commentFragment;
    private QueryFragment queryFragment;

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

        defaultFragmentName = getIntent().getStringExtra("fragment");

        courseNameTv = findViewById(R.id.course_content_name_tv);
        courseProviderTv = findViewById(R.id.course_content_provider_tv);
        //refreshFromFragment("examination");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshFriend");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);


        initView();
        initListener();
        initViewModel();
        initObserver();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRefreshBroadcastReceiver);
    }

    private void initView() {
        mainNavigationView = this.findViewById(R.id.course_navigation_bar);
        announcementFragment = new AnnouncementFragment();
        courseWareFragment = new CourseWareFragment();
        examinationFragment = new ExaminationFragment();
        commentFragment = new CommentFragment();
        queryFragment = new QueryFragment();

        fragmentManager = getSupportFragmentManager();

        switch (defaultFragmentName) {
            case "announcement":
                switchFragment(announcementFragment);
                mainNavigationView.setSelectedItemId(R.id.announcement);
                break;
            case "course_ware":
                switchFragment(courseWareFragment);
                mainNavigationView.setSelectedItemId(R.id.course_ware);
                break;
            case "examination":
                switchFragment(examinationFragment);
                mainNavigationView.setSelectedItemId(R.id.examination);
                break;
            case "query":
                switchFragment(queryFragment);
                mainNavigationView.setSelectedItemId(R.id.query);
                break;
            case "comment":
                switchFragment(commentFragment);
                mainNavigationView.setSelectedItemId(R.id.comment);
                break;
        }


    }

    private void initListener(){
        mainNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.announcement:
                        switchFragment(announcementFragment);
                        break;
                    case R.id.course_ware:
                        switchFragment(courseWareFragment);
                        break;
                    case R.id.examination:
                        switchFragment(examinationFragment);
                        break;
                    case R.id.query:
                        switchFragment(queryFragment);
                        break;
                    case R.id.comment:
                        switchFragment(commentFragment);
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

    public void refreshFromFragment(String fragmentName){
        ToastUtil.showShortToast("fresh");
        System.out.println("fresh");
        finish();
        overridePendingTransition(0, 0);
        Intent intent = new Intent(this, CourseActivity.class);
        intent.putExtra("course", course);
        intent.putExtra("fragment", fragmentName);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.refreshFriend"))
            {
                refreshFromFragment("examination");
            }
        }
    };

}
