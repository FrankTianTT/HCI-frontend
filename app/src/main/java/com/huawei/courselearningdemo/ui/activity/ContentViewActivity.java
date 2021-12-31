package com.huawei.courselearningdemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.CourseWare;
import com.huawei.courselearningdemo.utils.ToastUtil;

import java.io.File;

import butterknife.BindView;

public class ContentViewActivity extends AppCompatActivity {
    protected PDFView pdfView;
    private CourseWare courseWare;
    private Integer courseWareId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_view);
        pdfView = findViewById(R.id.pdfView);

        if (getIntent().getExtras() != null) {
            courseWare = (CourseWare) getIntent().getSerializableExtra("courseWare");
            if (courseWare == null)
                courseWareId = getIntent().getIntExtra("courseId", 0);
        } else {
            ToastUtil.showLongToast("当前页面加载错误，请稍后重试！");
        }

        displayFromFile(new File(getExternalFilesDir(null) + "/courseWare/test.pdf"));
    }

    private void displayFromFile(File file) {
        try {
            pdfView.fromAsset("test.pdf").load();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}

