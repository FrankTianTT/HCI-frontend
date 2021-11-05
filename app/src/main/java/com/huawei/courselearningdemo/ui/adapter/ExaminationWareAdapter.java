package com.huawei.courselearningdemo.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.DiscussWare;
import com.huawei.courselearningdemo.model.ExaminationWare;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExaminationWareAdapter extends RecyclerView.Adapter<ExaminationWareAdapter.InnerHolder>{
    private List<ExaminationWare> mData = new ArrayList<>();

    @NonNull
    @Override
    public ExaminationWareAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discuss_ware_content, parent, false);
        return new ExaminationWareAdapter.InnerHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ExaminationWareAdapter.InnerHolder holder, int position) {
        // 绑定数据
        ExaminationWare examinationWare = mData.get(position);
        holder.setData(examinationWare);
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

    public void setData(List<ExaminationWare> dataList) {
        this.mData.clear();
        this.mData.addAll(dataList);
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.test_item_name_tv)
        protected TextView examinationTv;
        @BindView(R.id.answer_a_btn)
        protected Button answerABtn;
        @BindView(R.id.answer_b_btn)
        protected Button answerBBtn;
        @BindView(R.id.answer_c_btn)
        protected Button answerCBtn;
        @BindView(R.id.answer_d_btn)
        protected Button answerDBtn;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(ExaminationWare data) {
            examinationTv.setText("1+2=?");
            answerABtn.setText('2');
            answerBBtn.setText('3');
            answerCBtn.setText('4');
            answerDBtn.setText('5');
        }
    }
}
