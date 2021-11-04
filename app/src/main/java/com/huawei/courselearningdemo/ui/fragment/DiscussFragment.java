package com.huawei.courselearningdemo.ui.fragment;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.CourseWare;
import com.huawei.courselearningdemo.model.DiscussWare;
import com.huawei.courselearningdemo.ui.adapter.CourseWareAdapter;
import com.huawei.courselearningdemo.ui.adapter.DiscussWareAdapter;
import com.huawei.courselearningdemo.utils.SizeUtil;
import com.huawei.courselearningdemo.viewmodel.CourseViewModel;

import java.util.List;

import butterknife.BindView;

public class DiscussFragment extends BaseFragment {
    private DiscussWareAdapter mAdapter;
    private CourseViewModel courseViewModel;
    @BindView(R.id.discuss_ware_content_list)
    public RecyclerView recyclerView;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_discuss;
    }

    @Override
    protected void initView(){
        mAdapter = new DiscussWareAdapter();
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
        courseViewModel.getDiscussWareData().observe(this, new Observer<List<DiscussWare>>() {
            @Override
            public void onChanged(List<DiscussWare> discussWareList) {
                mAdapter.setData(discussWareList);
            }
        });
    }
}
