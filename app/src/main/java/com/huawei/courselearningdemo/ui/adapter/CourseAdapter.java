package com.huawei.courselearningdemo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.utils.UrlUtil;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.InnerHolder>{
    private List<Course> mData = new ArrayList<>();
    private BuyCourseItemClickListener buyCourseItemClickListener;
    private ShowCourseItemClickListener showCourseItemClickListener;
    private StarCourseItemClickListener starCourseItemClickListener;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_content, parent,false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        // 绑定数据
        Course course = mData.get(position);
        holder.setData(course);
        // 若用户已经购买过该课程，则隐藏购买按钮
        if(course.isBought()) {
            holder.buyBtn.setVisibility(View.GONE);
        }else{
            holder.buyBtn.setVisibility(View.VISIBLE);
        }
        if(course.isStarred()){
            holder.starBtn.setText("已收藏");
        }
        else{
            holder.starBtn.setText("收藏课程");
        }
        holder.buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buyCourseItemClickListener != null){
                    buyCourseItemClickListener.buyCourseItemClicked(course);
                }
            }
        });
        holder.showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showCourseItemClickListener != null){
                    showCourseItemClickListener.showCourseItemClicked(course);
                }
            }
        });
        holder.starBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(course.isStarred()){
                    if(starCourseItemClickListener != null){
                        starCourseItemClickListener.starCourseItemClicked(course);
                        Toast.makeText(v.getContext(), "收藏已取消",Toast.LENGTH_LONG ).show();
                    }
                }
                else{
                    if(starCourseItemClickListener != null){
                        starCourseItemClickListener.starCourseItemClicked(course);
                        Toast.makeText(v.getContext(), "你收藏了该课程",Toast.LENGTH_LONG ).show();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<Course> dataList) {
        this.mData.clear();
        this.mData.addAll(dataList);
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.course_item_picture)
        protected ImageView cover;

        @BindView(R.id.course_item_name_tv)
        protected TextView nameTv;

        @BindView(R.id.course_item_intro_tv)
        protected TextView introTv;

        @BindView(R.id.course_item_buy_btn)
        protected Button buyBtn;

        @BindView(R.id.course_item_show_btn)
        protected Button showBtn;

        @BindView(R.id.course_item_star_btn)
        protected Button starBtn;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(Course data) {
            String coverPath = UrlUtil.getCourseImageUrl(data.getPicture());
            Glide.with(cover.getContext()).load(coverPath).into(cover);
            nameTv.setText(data.getName());
            introTv.setText(data.getIntro());
            String buyButtonHint = "购买课程  "+data.getCost();
            buyBtn.setText(buyButtonHint);
        }
    }

    public void setBuyCourseItemClickListener(BuyCourseItemClickListener listener) {
        this.buyCourseItemClickListener = listener;
    }

    public interface BuyCourseItemClickListener {
        void buyCourseItemClicked(Course course);
    }

    public void setShowCourseItemClickListener(ShowCourseItemClickListener listener) {
        this.showCourseItemClickListener = listener;
    }

    public interface ShowCourseItemClickListener {
        void showCourseItemClicked(Course course);
    }
    public void setStarCourseItemClickListener(StarCourseItemClickListener listener) {
        this.starCourseItemClickListener = listener;
    }

    public interface StarCourseItemClickListener {
        void starCourseItemClicked(Course course);
    }
}
