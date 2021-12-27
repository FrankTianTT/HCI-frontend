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

public class ContentViewActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener {
    private PDFView pdfView;
    private CourseWare courseWare;
    private Integer courseWareId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pdfView = (PDFView) findViewById(R.id.pdfView);

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
            pdfView.fromAsset("test.pdf");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        Toast.makeText(ContentViewActivity.this, "page= " + page +
                " pageCount= " + pageCount, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void loadComplete(int nbPages) {
        Toast.makeText(ContentViewActivity.this, "加载完成" + nbPages, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageError(int page, Throwable t) {
        Log.e("PageError", "Cannot load page " + page);
    }
}

