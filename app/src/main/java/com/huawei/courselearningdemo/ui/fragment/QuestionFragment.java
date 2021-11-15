package com.huawei.courselearningdemo.ui.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.ExaminationWare;
import com.huawei.courselearningdemo.model.Query;
import com.huawei.courselearningdemo.model.Question;
import com.huawei.courselearningdemo.model.QuestionList;
import com.huawei.courselearningdemo.repository.UserLocalRepository;
import com.huawei.courselearningdemo.ui.activity.CourseActivity;
import com.huawei.courselearningdemo.ui.activity.MainActivity;
import com.huawei.courselearningdemo.ui.activity.TestActivity;
import com.huawei.courselearningdemo.ui.adapter.ExaminationWareAdapter;
import com.huawei.courselearningdemo.ui.adapter.QueryAdapter;
import com.huawei.courselearningdemo.ui.adapter.QuestionAdapter;
import com.huawei.courselearningdemo.utils.KeyboardUtil;
import com.huawei.courselearningdemo.utils.ScreenUtils;
import com.huawei.courselearningdemo.utils.SizeUtil;
import com.huawei.courselearningdemo.utils.ToastUtil;
import com.huawei.courselearningdemo.viewmodel.CourseViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class QuestionFragment extends BaseFragment{
    private QuestionAdapter mAdapter;
    private boolean isTeacher;
    private QuestionFragment thisFragment;
    private CourseActivity courseActivity;
    private CourseViewModel courseViewModel;
    @BindView(R.id.question_ware_content_list)
    public RecyclerView recyclerView;
    @BindView(R.id.submit_btn)
    public Button submitBtn;
    @BindView(R.id.ask_btn)
    public Button askBtn;


    protected int getRootViewResId() {
        return R.layout.fragment_question;
    }

    protected void initView(){
        mAdapter = new QuestionAdapter();
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

    protected void initViewModel() {
        courseViewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);
        String teacherId = courseViewModel.getCourseData().getValue().getTeacherId();
        String uid = UserLocalRepository.getUser().getUid();
        isTeacher = uid.equals(teacherId);
        if(!isTeacher){
            askBtn.setVisibility(View.INVISIBLE);
        }
    }

    protected void initObserver() {
        courseViewModel.getQuestionData().observe(this, new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> QuestionList) {
                mAdapter.setData(QuestionList);
            }
        });

    }


        protected void initListener() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> answers = new ArrayList<>();
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    RelativeLayout layout = (RelativeLayout) recyclerView.getChildAt(i);
                    RadioGroup group = layout.findViewById(R.id.answer_group);
                    RadioButton answerRb= layout.findViewById(group.getCheckedRadioButtonId());
                    if(answerRb!=null){
                        String answer = answerRb.getText().toString();
                        if(answer==null) answers.add(" ");
                        else answers.add(answer.split(" ")[0]);
                        courseViewModel.judge(answers);
                        ToastUtil.showShortToast("提交成功！");
                    }
                    else{
                        ToastUtil.showShortToast("未作答！");
                    }

                }

//                Intent intent = new Intent(getActivity(), CourseActivity.class);
//                startActivity(intent);
            }
        });
        askBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TestActivity)getActivity()).showDialog();
            }
        });


    }

}

