package com.huawei.courselearningdemo.ui.fragment;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.Comment;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.ui.activity.CourseActivity;
import com.huawei.courselearningdemo.ui.adapter.CommentAdapter;
import com.huawei.courselearningdemo.utils.KeyboardUtil;
import com.huawei.courselearningdemo.utils.SizeUtil;
import com.huawei.courselearningdemo.utils.ToastUtil;
import com.huawei.courselearningdemo.viewmodel.CourseViewModel;

import java.util.List;

import butterknife.BindView;

public class CommentFragment extends BaseFragment {
    private CommentAdapter mAdapter;
    private CourseViewModel courseViewModel;
    @BindView(R.id.comment_content_list)
    public RecyclerView recyclerView;
    @BindView(R.id.comment_add_input)
    public TextView commentInput;
    @BindView(R.id.comment_button)
    public Button commentBtn;
    @BindView(R.id.comment_ratingbar)
    public RatingBar commentRating;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_comment;
    }

    @Override
    protected void initView(){
        mAdapter = new CommentAdapter();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        // 设置四周的分割线
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
    }

    protected void initObserver() {
        courseViewModel.getCommentWareData().observe(this, new Observer<List<Comment>>() {
            @Override
            public void onChanged(List<Comment> commentList) {
                mAdapter.setData(commentList);
            }
        });
    }

    @Override
    protected void initListener() {
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentContent = commentInput.getText().toString();
                Integer commentRatingValue = (int)commentRating.getRating();
                if (commentContent.trim().equals("")){
                    ToastUtil.showShortToast("你倒是说句话啊");
                    return;
                }
                if (commentRatingValue == 0){
                    ToastUtil.showShortToast("给点面子");
                    return;
                }
                KeyboardUtil.hide(getContext(), commentInput);
                commentInput.setText("");

                Course c = ((CourseActivity)getActivity()).getCourse();

                courseViewModel.setCommentContent(commentContent, commentRatingValue);
                courseViewModel.addComment(c);

                ((CourseActivity)getActivity()).refreshFromFragment("comment");
            }
        });
    }
}
