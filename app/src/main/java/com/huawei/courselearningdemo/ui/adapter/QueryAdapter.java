package com.huawei.courselearningdemo.ui.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.Query;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QueryAdapter extends RecyclerView.Adapter<QueryAdapter.InnerHolder>{
    private List<Query> mData = new ArrayList<>();

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_query_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        // 绑定数据
        Query query = mData.get(position);
        holder.setData(query);

        if (query.getReply() == null){
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

    public void setData(List<Query> dataList) {
        this.mData.clear();
        this.mData.addAll(dataList);
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.query_item_title)
        protected TextView titleTv;
        @BindView(R.id.query_item_content)
        protected TextView contentTv;
        @BindView(R.id.query_item_reply_title)
        protected TextView replyTitleTv;
        @BindView(R.id.query_item_reply)
        protected TextView replyTv;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(Query data) {
            String title = "<font color='#0080FF'>"+data.getUname()+"</font>";

            titleTv.setText(Html.fromHtml(title));
            contentTv.setText(data.getContent());
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
