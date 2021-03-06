package com.huawei.courselearningdemo.ui.fragment;

import android.graphics.Rect;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.Query;
import com.huawei.courselearningdemo.repository.UserLocalRepository;
import com.huawei.courselearningdemo.ui.activity.CourseActivity;
import com.huawei.courselearningdemo.ui.adapter.QueryAdapter;
import com.huawei.courselearningdemo.utils.KeyboardUtil;
import com.huawei.courselearningdemo.utils.SizeUtil;
import com.huawei.courselearningdemo.utils.ToastUtil;
import com.huawei.courselearningdemo.viewmodel.CourseViewModel;

import java.util.List;

import butterknife.BindView;

public class QueryFragment extends BaseFragment {
    private QueryAdapter mAdapter;
    private QueryFragment thisFragment;
    private CourseViewModel courseViewModel;

    public void setAnsweredQueryId(Integer answeredQueryId) {
        this.answeredQueryId = answeredQueryId;
    }

    private Integer answeredQueryId=-1;
    public Boolean isTeacher;
    @BindView(R.id.query_content_list)
    public RecyclerView recyclerView;
    @BindView(R.id.query_add_input)
    public EditText queryInput;
    @BindView(R.id.query_button)
    public Button queryBtn;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_query;
    }

    @Override
    protected void initView(){
        mAdapter = new QueryAdapter();
        thisFragment = this;
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        // ????????????????????????
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtil.dip2px(getContext(),.0f);
                outRect.bottom = SizeUtil.dip2px(getContext(),.5f);
                outRect.left = SizeUtil.dip2px(getContext(),.0f);
                outRect.right = SizeUtil.dip2px(getContext(),.0f);
            }
        });
    }

    @Override
    protected void initViewModel() {
        courseViewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);
        String teacherId = courseViewModel.getCourseData().getValue().getTeacherId();
        String uid = UserLocalRepository.getUser().getUid();
        isTeacher = uid.equals(teacherId);

        if (isTeacher){
            queryBtn.setText("??????");
        }
        else{
            queryBtn.setText("??????");
        }

        mAdapter.setTeacher(isTeacher);
        mAdapter.setQueryInput(queryInput);
        mAdapter.setFather(this);
    }

    protected void initObserver() {
        courseViewModel.getQueryWareData().observe(this, new Observer<List<Query>>() {
            @Override
            public void onChanged(List<Query> queryList) {
                mAdapter.setData(queryList);
            }
        });
    }

    @Override
    protected void initListener() {
        queryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String queryContent = queryInput.getText().toString();
                if (queryContent.trim().equals("")){
                    ToastUtil.showShortToast("?????????????????????");
                    return;
                }

                if (isTeacher && answeredQueryId==-1){
                    ToastUtil.showShortToast("???????????????????????????");
                    return;
                }

                KeyboardUtil.hide(getContext(), queryInput);
                queryInput.setText("");

                Course c = ((CourseActivity)getActivity()).getCourse();

                if (isTeacher){
                    courseViewModel.reply(answeredQueryId, queryContent);
                }
                else{
                    courseViewModel.setQueryContent(queryContent);
                    courseViewModel.addQuery(c);
                }

                ((CourseActivity)getActivity()).refreshFromFragment("query");
            }
        });
    }
}
