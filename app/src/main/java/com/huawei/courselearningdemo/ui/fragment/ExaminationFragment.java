package com.huawei.courselearningdemo.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.ExaminationWare;
import com.huawei.courselearningdemo.ui.activity.CourseActivity;
import com.huawei.courselearningdemo.ui.activity.MainActivity;
import com.huawei.courselearningdemo.ui.activity.TestActivity;
import com.huawei.courselearningdemo.ui.adapter.CourseAdapter;
import com.huawei.courselearningdemo.ui.adapter.ExaminationWareAdapter;
import com.huawei.courselearningdemo.utils.KeyboardUtil;
import com.huawei.courselearningdemo.utils.SizeUtil;
import com.huawei.courselearningdemo.utils.ToastUtil;
import com.huawei.courselearningdemo.viewmodel.CourseViewModel;

import java.util.List;

import butterknife.BindView;

public class ExaminationFragment extends BaseFragment implements ExaminationWareAdapter.TestClickListener {
    private ExaminationWareAdapter mAdapter;
    private CourseViewModel courseViewModel;
    @BindView(R.id.test_ware_content_list)
    public RecyclerView recyclerView;
    @BindView(R.id.add_test_input)
    public TextView testInput;
    @BindView(R.id.add_test_button)
    public Button addTestBtn;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_examination;
    }

    @Override
    protected void initView(){
        mAdapter = new ExaminationWareAdapter();
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
        courseViewModel.getExaminationWareData().observe(this, new Observer<List<ExaminationWare>>() {
            @Override
            public void onChanged(List<ExaminationWare> examinationWareList) {
                mAdapter.setData(examinationWareList);
            }
        });
    }
    @Override
    protected void initListener() {
        mAdapter.setTestClickListener(this);
        addTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String testContent = testInput.getText().toString();
                if (testContent.trim().equals("")){
                    ToastUtil.showShortToast("请输入题目");
                    return;
                }
                KeyboardUtil.hide(getContext(), testInput);
                testInput.setText("");

                Course c = ((CourseActivity)getActivity()).getCourse();

                courseViewModel.addTest(c,testContent);

                ((CourseActivity)getActivity()).refreshFromFragment("examination");
            }
        });
    }
    @Override
    public void testClicked(ExaminationWare exam) {
        Intent intent = new Intent(getActivity(), TestActivity.class);
        courseViewModel.setExaminationWareData(exam);
        startActivity(intent);
    }
}
