package cn.jesse.magicbox.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import cn.jesse.magicbox.R;
import cn.jesse.magicbox.util.WorkspaceUtil;

/**
 * 内置文件浏览器页面
 *
 * @author jesse
 */
public class MagicBoxFileExplorerActivity extends Activity implements View.OnClickListener {
    private static final String PARAMS_PATH = "params_path";
    private TextView currentPathText;

    private String rootPath;

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
        findViewById(R.id.tv_back_to_parent).setOnClickListener(this);
        findViewById(R.id.ib_back).setOnClickListener(this);

        currentPathText = findViewById(R.id.tv_current_path);
    }

    private void initData() {
        rootPath = getIntent().getStringExtra(PARAMS_PATH);
        if (TextUtils.isEmpty(rootPath)) {
            rootPath = WorkspaceUtil.getSandboxDir(getApplication()).getPath();
        }

        currentPathText.setText(rootPath);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_back_to_parent) {

        } else {
            finish();
        }
    }
}
