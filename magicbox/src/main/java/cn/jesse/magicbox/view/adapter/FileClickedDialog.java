package cn.jesse.magicbox.view.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import java.io.File;

import cn.jesse.magicbox.R;

/**
 * 文件浏览器 点击文件弹窗
 *
 * @author jesse
 */
public class FileClickedDialog extends Dialog implements View.OnClickListener {
    private View openTextView;
    private File clickedFile;

    public FileClickedDialog(@NonNull Context context) {
        super(context, R.style.commonDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_file_clicked, null);
        Window window = getWindow();
        if (window != null) {
            window.setContentView(dialogView);

            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
        setContentView(R.layout.dialog_file_clicked);
        initView();
    }

    private void initView() {
        openTextView = findViewById(R.id.tv_text);

        openTextView.setOnClickListener(this);

        findViewById(R.id.tv_export).setOnClickListener(this);
        findViewById(R.id.tv_dismiss).setOnClickListener(this);
    }

    /**
     * 点击文件, 展示弹窗
     *
     * @param file 文件
     */
    public void show(@NonNull File file) {
        clickedFile = file;
        super.show();
    }

    @Override
    public void dismiss() {
        clickedFile = null;
        super.dismiss();
    }

    @Override
    public void onClick(View v) {
        if (v == null) {
            return;
        }

        if (v.getId() == R.id.tv_dismiss) {
            dismiss();
        } else if (v.getId() == R.id.tv_export) {

        } else if (v.getId() == R.id.tv_text) {

        }
    }
}
