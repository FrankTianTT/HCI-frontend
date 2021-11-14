package com.huawei.courselearningdemo.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.ExaminationWare;
import com.huawei.courselearningdemo.model.Question;
import com.huawei.courselearningdemo.ui.fragment.HomeFragment;
import com.huawei.courselearningdemo.ui.fragment.QueryFragment;
import com.huawei.courselearningdemo.ui.fragment.QuestionFragment;
import com.huawei.courselearningdemo.utils.ToastUtil;
import com.huawei.courselearningdemo.viewmodel.CourseViewModel;
import com.huawei.courselearningdemo.viewmodel.SharedViewModel;

public class TestActivity extends AppCompatActivity {
    private SharedViewModel sharedViewModel;
    private Integer examId;
    private QuestionFragment questionFragment;
    private CourseViewModel courseViewModel;
    private ExaminationWare exam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
//        if(getIntent().getExtras() != null) {
//            exam = (ExaminationWare) getIntent().getSerializableExtra("exam");
//            if (exam == null)
//                examId = getIntent().getIntExtra("id", 0);
//        }else{
//            ToastUtil.showLongToast("当前页面加载错误，请稍后重试！");
//        }
        initView();
        initListener();
        initViewModel();
        initObserver();
    }

    private void initView() {
        questionFragment = new QuestionFragment();

    }

    private void initListener(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.question_container, questionFragment);
        fragmentTransaction.commit();
    }

    private void initViewModel() {
//        courseViewModel = new ViewModelProvider(TestActivity.this).get(CourseViewModel.class);
//        if(exam != null)
//            courseViewModel.setExaminationWareData(exam);
    }

    private void initObserver() {

    }



}