package cn.jesse.magicbox.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.File;

import cn.jesse.magicbox.R;
import cn.jesse.magicbox.util.MBPlatformUtil;
import cn.jesse.magicbox.util.WorkspaceUtil;
import cn.jesse.magicbox.view.adapter.MagicBoxFileExplorerAdapter;

/**
 * 内置文件浏览器页面
 *
 * @author jesse
 */
public class MagicBoxFileExplorerActivity extends Activity implements View.OnClickListener, MagicBoxFileExplorerAdapter.OnFileExplorerClickListener {
    private static final String PARAMS_PATH = "params_path";
    private TextView currentPathText;
    private TextView backToParentText;
    private ListView fileExplorerList;

    private File rootDir;
    private File currentDir;
    private MagicBoxFileExplorerAdapter fileExplorerAdapter;

    /**
     * 打开默认沙盒空间的文件浏览器
     *
     * @param context context
     */
    public static void start(@NonNull Context context) {
        start(context, "");
    }

    /**
     * 打开指定文件夹的文件浏览器
     *
     * @param context context
     * @param path    要打开的文件路径
     */
    public static void start(@NonNull Context context, String path) {
        Intent intent = new Intent(context, MagicBoxFileExplorerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (!TextUtils.isEmpty(path)) {
            intent.putExtra(PARAMS_PATH, path);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_file_explorer);
        initView();
        initData();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("文件浏览器");
        backToParentText = findViewById(R.id.tv_back_to_parent);
        backToParentText.setOnClickListener(this);
        findViewById(R.id.ib_back).setOnClickListener(this);

        currentPathText = findViewById(R.id.tv_current_path);
        fileExplorerList = findViewById(R.id.lv_files);
    }

    private void initData() {
        String rootPath = getIntent().getStringExtra(PARAMS_PATH);

        if (TextUtils.isEmpty(rootPath)) {
            rootDir = WorkspaceUtil.getSandboxDir(getApplication());
        } else {
            rootDir = new File(rootPath);
            if (!WorkspaceUtil.isDirValid(rootDir)) {
                MBPlatformUtil.toast("无效文件夹");
                finish();
                return;
            }
        }
        currentPathText.setText(rootDir.getPath());

        fileExplorerAdapter = new MagicBoxFileExplorerAdapter(this, this);
        fileExplorerList.setAdapter(fileExplorerAdapter);
        fileExplorerAdapter.setDir(rootDir);

        currentDir = rootDir;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_back_to_parent) {
            backToParent();
        } else {
            finish();
        }
    }

    @Override
    public void onFileExplorerItemClick(int position, File current) {
        currentDir = current;
        if (rootDir == null || currentDir == null) {
            backToParentText.setVisibility(View.GONE);
            return;
        }

        backToParentText.setVisibility(View.VISIBLE);
        currentPathText.setText(current.getPath());
    }

    /**
     * 用户点击返回上一级
     */
    private void backToParent() {
        if (rootDir == null || currentDir == null) {
            return;
        }

        if (rootDir.getPath().equals(currentDir.getPath())) {
            backToParentText.setVisibility(View.GONE);
            return;
        }

        // 获取上级目录 并展示内容
        fileExplorerAdapter.setDir(currentDir.getParentFile());
        currentDir = currentDir.getParentFile();

        if (currentDir == null) {
            return;
        }

        // 更新当前路径, 并判断是否需要隐藏返回上层入口
        currentPathText.setText(currentDir.getPath());

        if (rootDir.getPath().equals(currentDir.getPath())) {
            backToParentText.setVisibility(View.GONE);
        }
    }
}
