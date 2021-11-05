package com.huawei.courselearningdemo.ui.fragment;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.DiscussWare;
import com.huawei.courselearningdemo.model.ExaminationWare;
import com.huawei.courselearningdemo.ui.adapter.DiscussWareAdapter;
import com.huawei.courselearningdemo.ui.adapter.ExaminationWareAdapter;
import com.huawei.courselearningdemo.utils.SizeUtil;
import com.huawei.courselearningdemo.viewmodel.CourseViewModel;

import java.util.List;

import butterknife.BindView;

public class ExaminationFragment extends BaseFragment {
    private ExaminationWareAdapter mAdapter;
    private CourseViewModel courseViewModel;
    @BindView(R.id.examination_ware_content_list)
    public RecyclerView recyclerView;

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
//        courseViewModel.getExaminationWareData().observe(this, new Observer<List<ExaminationWare>>() {
//            @Override
//            public void onChanged(List<ExaminationWare> examinationWareList) {
//                mAdapter.setData(examinationWareList);
//            }
//        });
    }
}
