package com.huawei.courselearningdemo.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

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
        courseViewModel = new ViewModelProvider(TestActivity.this).get(CourseViewModel.class);
    }
    private void initObserver() {

    }
    public void showDialog(){
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(TestActivity.this);
        final View dialogView = LayoutInflater.from(TestActivity.this)
                .inflate(R.layout.dialog_question,null);
        customizeDialog.setTitle("上传题目");
        customizeDialog.setView(dialogView);
        customizeDialog.setPositiveButton("提交",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText title = (EditText) dialogView.findViewById(R.id.question_title);
                        EditText optionA = (EditText) dialogView.findViewById(R.id.option_A);
                        EditText optionB = (EditText) dialogView.findViewById(R.id.option_B);
                        EditText optionC = (EditText) dialogView.findViewById(R.id.option_C);
                        EditText optionD = (EditText) dialogView.findViewById(R.id.option_D);
                        EditText answer = (EditText) dialogView.findViewById(R.id.question_answer);
                        System.out.println(answer.getText().toString()==null);
                        if(title.getText().toString()==null||optionA.getText().toString()==null||optionB.getText().toString()==null||optionC.getText().toString()==null||optionD.getText().toString()==null||answer.getText().toString()==null){
                            ToastUtil.showShortToast("请输入完整信息！");
                        }
                        else{
                            Question question = new Question();
                            question.setContent(title.getText().toString());
                            question.setOptionA(optionA.getText().toString());
                            question.setOptionB(optionB.getText().toString());
                            question.setOptionC(optionC.getText().toString());
                            question.setOptionD(optionD.getText().toString());
                            question.setAnswer(answer.getText().toString());
                            courseViewModel.addQuestion(question);
                        }
                    }
                });
        customizeDialog.show();
    }



}