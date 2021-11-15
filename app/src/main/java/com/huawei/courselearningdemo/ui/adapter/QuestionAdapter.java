package com.huawei.courselearningdemo.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.ExaminationWare;
import com.huawei.courselearningdemo.model.Question;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.InnerHolder>{
    private List<Question> mData = new ArrayList<>();
//    private ExaminationWareAdapter.TestClickListener testClickListener;

    @NonNull
    @Override
    public QuestionAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_content, parent, false);
        return new QuestionAdapter.InnerHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.InnerHolder holder, int position) {
        // 绑定数据
        Question question = mData.get(position);
        holder.setData(question);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//        holder.testBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(testClickListener != null){
//                    testClickListener.testClicked(examinationWare);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<Question> dataList) {
        this.mData.clear();
        this.mData.addAll(dataList);
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.question_item_name_tv)
        protected TextView questionTv;
        @BindView(R.id.answer_a_rb)
        protected RadioButton Arb;
        @BindView(R.id.answer_b_rb)
        protected RadioButton Brb;
        @BindView(R.id.answer_c_rb)
        protected RadioButton Crb;
        @BindView(R.id.answer_d_rb)
        protected RadioButton Drb;
        @BindView(R.id.answer_group)
        protected RadioGroup group;


        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(Question data) {
            questionTv.setText(data.getOptionA());
            Arb.setText(data.getOptionA());
            Brb.setText(data.getOptionB());
            Crb.setText(data.getOptionC());
            Drb.setText(data.getOptionD());
        }
    }
}
