package com.huawei.courselearningdemo.ui.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.CommentWare;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentWareAdapter extends RecyclerView.Adapter<CommentWareAdapter.InnerHolder>{
    private List<CommentWare> mData = new ArrayList<>();

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_ware_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        // 绑定数据
        CommentWare commentWare = mData.get(position);
        holder.setData(commentWare);
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

    public void setData(List<CommentWare> dataList) {
        this.mData.clear();
        this.mData.addAll(dataList);
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.comment_ware_item_title)
        protected TextView titleTv;
        @BindView(R.id.comment_ware_item_content)
        protected TextView contentTv;
        @BindView(R.id.comment_ware_item_ratingbar)
        protected RatingBar ratingRb;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(CommentWare data) {
            String title = "<font color='#0080FF'>"+data.getUname()+"</font>";

            titleTv.setText(Html.fromHtml(title));
            contentTv.setText(data.getContent());
            ratingRb.setRating(data.getScore());
            ratingRb.setIsIndicator(true);
        }
    }
}
