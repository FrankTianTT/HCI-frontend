package com.huawei.courselearningdemo.ui.adapter;

import android.text.Html;
import android.util.Log;
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
        if (discussWare.getQuery()){
            holder.ratingRb.setVisibility(View.GONE);
        }
        else{
            holder.replyTitleTv.setVisibility(View.GONE);
            holder.replyTv.setVisibility(View.GONE);
        }
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
        @BindView(R.id.discuss_ware_item_title)
        protected TextView titleTv;
        @BindView(R.id.discuss_ware_item_content)
        protected TextView contentTv;
        @BindView(R.id.discuss_ware_item_ratingbar)
        protected RatingBar ratingRb;
        @BindView(R.id.discuss_ware_item_reply_title)
        protected TextView replyTitleTv;
        @BindView(R.id.discuss_ware_item_reply)
        protected TextView replyTv;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(DiscussWare data) {
            String type = data.getQuery()?"<font color='#00FF00'>疑问</font>":"<font color='#FF0000'>评价</font>";
            String title = "<font color='#0000FF'>"+data.getUname()+"</font>" + " 的 " + type + ":";

            titleTv.setText(Html.fromHtml(title));
            contentTv.setText(data.getContent());
            ratingRb.setRating(data.getScore());
            ratingRb.setIsIndicator(true);
            if (data.getReply() == null){
                replyTitleTv.setText("老师还没有回答你哦，再轰炸ta的邮箱试试吧");
                replyTv.setText("");
            }
            else{
                String replyTitle = "老师的回答：";
                replyTitleTv.setText(Html.fromHtml(replyTitle));
                replyTv.setText(data.getReply());
            }
        }
    }
}
