package com.huawei.courselearningdemo.ui.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.CourseWare;
import com.huawei.courselearningdemo.ui.activity.ContentViewActivity;
import com.huawei.courselearningdemo.ui.activity.MainActivity;
import com.huawei.courselearningdemo.utils.ToastUtil;

public class CourseWareAdapter extends RecyclerView.Adapter<CourseWareAdapter.InnerHolder>{
    private List<CourseWare> mData = new ArrayList<>();
    private ShowCourseWareItemClickListener showCourseWareItemClickListener;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_ware_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        // 绑定数据
        CourseWare courseWare = mData.get(position);
        holder.setData(courseWare);
        // 若用户有权限获取课件，则隐藏加锁图标
        if(courseWare.getAvailableFlag())
            holder.imageView.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(courseWare.getAvailableFlag())
                    // 模拟课件预览效果
//                    ToastUtil.showShortToast("正在预览课件");
                    if(showCourseWareItemClickListener != null){
                        showCourseWareItemClickListener.showCourseWareItemClicked(courseWare);
                    }
                else
                    ToastUtil.showShortToast("当前课件尚未解锁");

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<CourseWare> dataList) {
        this.mData.clear();
        this.mData.addAll(dataList);
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.course_ware_item_name_tv)
        protected TextView nameTv;
        @BindView(R.id.course_ware_locked)
        protected ImageView imageView;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(CourseWare data) {
            nameTv.setText(data.getTitle());
        }
    }

    public void setShowCourseWareItemClickListener(CourseWareAdapter.ShowCourseWareItemClickListener listener) {
        this.showCourseWareItemClickListener = listener;
    }

    public interface ShowCourseWareItemClickListener {
        void showCourseWareItemClicked(CourseWare courseWare);
    }
}
