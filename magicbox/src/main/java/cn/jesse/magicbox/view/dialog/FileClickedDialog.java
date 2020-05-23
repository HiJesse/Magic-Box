package cn.jesse.magicbox.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import java.io.File;

import cn.jesse.magicbox.R;
import cn.jesse.magicbox.util.FileUtil;
import cn.jesse.magicbox.util.MBPlatformUtil;
import cn.jesse.magicbox.util.PermissionUtil;
import cn.jesse.magicbox.util.WorkspaceUtil;

/**
 * 文件浏览器 点击文件弹窗
 *
 * @author jesse
 */
public class FileClickedDialog extends Dialog implements View.OnClickListener {
    private Activity activity;
    private View openTextView;
    private File clickedFile;

    public FileClickedDialog(@NonNull Activity context) {
        super(context, R.style.commonDialog);
        activity = context;
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
            exportFile();
        } else if (v.getId() == R.id.tv_text) {

        }
    }

    /**
     * 导出文件
     */
    private void exportFile() {
        if (activity == null || clickedFile == null) {
            return;
        }

        if (!PermissionUtil.checkStoragePermission(activity)) {
            MBPlatformUtil.toast("请授予存储权限");
            PermissionUtil.requestStoragePermisson(activity, 0);
            return;
        }

        File external = WorkspaceUtil.getExternalCacheDir(activity.getApplication());
        if (external == null) {
            return;
        }

        if (FileUtil.copyFile(clickedFile, new File(external, clickedFile.getName()))) {
            MBPlatformUtil.toast(String.format("文件已导出至SD卡%s", external.getPath()));
        } else {
            MBPlatformUtil.toast("文件已导出失败");
        }

        dismiss();
    }
}
