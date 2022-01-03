package com.huawei.courselearningdemo.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.huawei.courselearningdemo.viewmodel.HomeViewModel;
import com.huawei.courselearningdemo.viewmodel.SharedViewModel;

public class CourseActivity extends AppCompatActivity {
    private Integer courseId;

    public Course getCourse() {
        return course;
    }

    private Course course;

    private String defaultFragmentName;

    TextView courseNameTv;
    TextView courseProviderTv;
    ImageButton buyBtn;
    ImageButton starBtn;
    ImageButton starBtn1;
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
    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        if (getIntent().getExtras() != null) {
            course = (Course) getIntent().getSerializableExtra("course");
            if (course == null)
                courseId = getIntent().getIntExtra("courseId", 0);
        } else {
            ToastUtil.showLongToast("当前页面加载错误，请稍后重试！");
        }

        defaultFragmentName = getIntent().getStringExtra("fragment");

        courseNameTv = findViewById(R.id.course_content_name_tv);
        courseProviderTv = findViewById(R.id.course_content_provider_tv);
        buyBtn = findViewById(R.id.course_buy_btn);
        starBtn = findViewById(R.id.course_star_btn);
        starBtn1 = findViewById(R.id.course_star_btn1);
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

    private void initListener() {
        mainNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
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

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPurchaseDialog(course);
            }
        });

        starBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (course.isStarred()) {
                    courseViewModel.cancelStar(course);
                    Toast.makeText(v.getContext(), "收藏已取消,请刷新", Toast.LENGTH_LONG).show();

                } else {
                    courseViewModel.addStar(course);
                    Toast.makeText(v.getContext(), "你收藏了该课程，请刷新", Toast.LENGTH_LONG).show();
                }
                starBtn.setVisibility(View.GONE);
                starBtn1.setVisibility(View.VISIBLE);

            }
        });
        starBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (course.isStarred()) {
                    courseViewModel.cancelStar(course);
                    Toast.makeText(v.getContext(), "收藏已取消,请刷新", Toast.LENGTH_LONG).show();

                } else {
                    courseViewModel.addStar(course);
                    Toast.makeText(v.getContext(), "你收藏了该课程，请刷新", Toast.LENGTH_LONG).show();
                }
                starBtn1.setVisibility(View.GONE);
                starBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initViewModel() {
        courseViewModel = new ViewModelProvider(CourseActivity.this).get(CourseViewModel.class);
        sharedViewModel = new ViewModelProvider(CourseActivity.this).get(SharedViewModel.class);
        if (course != null)
            courseViewModel.setCourse(course);
        else if (courseId != null && courseId != 0)
            courseViewModel.loadCourse(course);
    }

    private void initObserver() {
        courseViewModel.getCourseData().observe(this, new Observer<Course>() {
            @Override
            public void onChanged(Course course) {
                if (course != null) {
                    courseNameTv.setText(course.getName());
                    courseProviderTv.setText(course.getProvider());
                    if (course.isBought()) {
                        buyBtn.setEnabled(false);
                    } else {
                    }
                    if (course.isStarred()) {
                        starBtn.setVisibility(View.GONE);
                        starBtn1.setVisibility(View.VISIBLE);
                    } else {
                        starBtn.setVisibility(View.VISIBLE);
                        starBtn1.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void switchFragment(BaseFragment targetFragment) {
        // 如果上一个fragment跟当前要切换的fragment是同一个，那么不需要切换
        if (lastOneFragment == targetFragment) {
            return;
        }
        // 使用add和hide的方式来控制Fragment的切换
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (lastOneFragment != null) {
            fragmentTransaction.hide(lastOneFragment);
        }
        if (!targetFragment.isAdded()) {
            fragmentTransaction.add(R.id.course_page_container, targetFragment);
        } else {
            fragmentTransaction.show(targetFragment);
        }
        lastOneFragment = targetFragment;
        //fragmentTransaction.replace(R.id.main_page_container, targetFragment);
        fragmentTransaction.commit();
    }

    public void refreshFromFragment(String fragmentName) {
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
            if (action.equals("action.refreshFriend")) {
                refreshFromFragment("examination");
            }
        }
    };

    private void showPurchaseDialog(Course course) {
        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("购买课程 " + course.getName());
        dialogBuilder.setMessage("确认支付 " + course.getCost() + " 个钻石？");
        dialogBuilder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (sharedViewModel.getUid() == null) {
                            ToastUtil.showShortToast("请先登录账号！");
                        } else {
                            courseViewModel.createCourseOrder(course, sharedViewModel.getUid().getValue());
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


}
