package com.huawei.courselearningdemo.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.CourseWare;
import com.huawei.courselearningdemo.model.DiscussWare;
import com.huawei.courselearningdemo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscussWareAdapter extends RecyclerView.Adapter<DiscussWareAdapter.InnerHolder>{
    private List<DiscussWare> mData = new ArrayList<>();

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discuss_ware_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        // 绑定数据
        DiscussWare discussWare = mData.get(position);
        holder.setData(discussWare);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<DiscussWare> dataList) {
        this.mData.clear();
        this.mData.addAll(dataList);
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.discuss_ware_item_uname)
        protected TextView unameTv;
        @BindView(R.id.discuss_ware_item_type)
        protected TextView typeTv;
        @BindView(R.id.discuss_ware_item_content)
        protected TextView contentTv;
        @BindView(R.id.discuss_ware_item_ratingbar)
        protected RatingBar ratingRb;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(DiscussWare data) {
            unameTv.setText(data.getUname());
            typeTv.setText(data.getQuery()?"的疑问：":"的评价：");
            contentTv.setText(data.getContent());
            ratingRb.setRating(data.getScore());
        }
    }
}
