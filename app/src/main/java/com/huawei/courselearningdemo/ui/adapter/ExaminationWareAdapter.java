package com.huawei.courselearningdemo.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.ExaminationWare;
import com.huawei.courselearningdemo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExaminationWareAdapter extends RecyclerView.Adapter<ExaminationWareAdapter.InnerHolder>{
    private List<ExaminationWare> mData = new ArrayList<>();
    private TestClickListener testClickListener;

    @NonNull
    @Override
    public ExaminationWareAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_content, parent, false);
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
        holder.testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(testClickListener != null){
                    testClickListener.testClicked(examinationWare);
                }
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
        @BindView(R.id.test_item_content)
        protected TextView testTv;
        @BindView(R.id.test_btn)
        protected Button testBtn;
        @BindView(R.id.score_item_content)
        protected TextView scoreTv;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(ExaminationWare data) {
            testTv.setText(data.getTitle());
            scoreTv.setText(data.getScore().toString());
        }
    }

    public void setTestClickListener(TestClickListener listener) {
        this.testClickListener = listener;
    }

    public interface TestClickListener {
        void testClicked(ExaminationWare exam);
    }
}
