package cn.jesse.magicbox.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.File;

import cn.jesse.magicbox.R;
import cn.jesse.magicbox.util.MBPlatformUtil;
import cn.jesse.magicbox.util.WorkspaceUtil;

/**
 * 文件浏览页面
 * <p>
 * 暂支持txt, img
 *
 * @author jesse
 */
public class MagicBoxFileContentActivity extends Activity {
    private static final String PARAMS_PATH = "PARAMS_PATH";
    private static final String PARAMS_FILE_TYPE = "PARAMS_FILE_TYPE";
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_IMAGE = 2;

    private TextView textContentView;
    private ImageView imageContentView;

    /**
     * 打开指定文件 进行预览
     *
     * @param context context
     * @param file    要打开的文件路径
     */
    public static void start(@NonNull Context context, String file) {
        start(context, -1, file);
    }

    /**
     * 打开指定文件 进行预览
     *
     * @param context  context
     * @param fileType 文件类型
     * @param file     要打开的文件路径
     */
    public static void start(@NonNull Context context, int fileType, String file) {
        Intent intent = new Intent(context, MagicBoxFileContentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (!TextUtils.isEmpty(file)) {
            intent.putExtra(PARAMS_PATH, file);
        }
        intent.putExtra(PARAMS_FILE_TYPE, fileType);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_file_content);
        initView();
        initData();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("文件预览");
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textContentView = findViewById(R.id.tv_content);
        textContentView.setMovementMethod(new ScrollingMovementMethod());

        imageContentView = findViewById(R.id.iv_image);
    }

    private void initData() {
        String filePath = getIntent().getStringExtra(PARAMS_PATH);

        if (TextUtils.isEmpty(filePath)) {
            MBPlatformUtil.toast("入参文件为空");
            finish();
            return;
        }

        if (!WorkspaceUtil.isFileValid(new File(filePath))) {
            MBPlatformUtil.toast("文件无效");
            finish();
            return;
        }

        int fileType = getIntent().getIntExtra(PARAMS_FILE_TYPE, 0);
        if (fileType < TYPE_TEXT || fileType > TYPE_IMAGE) {

        }

        textContentView.setText("");
    }

    private void showTextContent(String content) {
        textContentView.setVisibility(View.VISIBLE);
        textContentView.setText(content);
    }

    private void showImageContent() {
        imageContentView.setVisibility(View.VISIBLE);
    }
}
