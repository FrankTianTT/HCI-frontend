package com.huawei.courselearningdemo.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.ExaminationWare;
import com.huawei.courselearningdemo.ui.fragment.QuestionFragment;
import com.huawei.courselearningdemo.ui.fragment.StarFragment;
import com.huawei.courselearningdemo.viewmodel.CourseViewModel;
import com.huawei.courselearningdemo.viewmodel.SharedViewModel;

public class StarActivity extends  AppCompatActivity {
    private SharedViewModel sharedViewModel;
    private StarFragment starFragment;
    private CourseViewModel courseViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
        initListener();
        initViewModel();
        initObserver();
    }

    private void initView() {
        starFragment = new StarFragment();

    }

    private void initListener(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.question_container, starFragment);
        fragmentTransaction.commit();
    }

    private void initViewModel() {

    }
    private void initObserver() {

    }
}
