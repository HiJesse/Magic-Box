package cn.jesse.magicbox.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.File;

import cn.jesse.magicbox.R;
import cn.jesse.magicbox.util.FileUtil;
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
    private static final String TAG = "MagicBoxFileContentActivity";
    private static final String PARAMS_PATH = "PARAMS_PATH";
    private static final String PARAMS_FILE_TYPE = "PARAMS_FILE_TYPE";
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_IMAGE = 2;

    private TextView textContentView;
    private ImageView imageContentView;
    private View loadingView;

    private Handler handler;
    private Handler mainHandler;

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

        loadingView = findViewById(R.id.iv_loading);
        textContentView = findViewById(R.id.tv_content);
        textContentView.setMovementMethod(new ScrollingMovementMethod());

        imageContentView = findViewById(R.id.iv_image);
    }

    private void initData() {
        String filePath = getIntent().getStringExtra(PARAMS_PATH);

        if (TextUtils.isEmpty(filePath)) {
            finishAfterToast("入参文件为空");
            return;
        }

        if (!WorkspaceUtil.isFileValid(new File(filePath))) {
            finishAfterToast("文件无效");
            return;
        }

        int fileType = getIntent().getIntExtra(PARAMS_FILE_TYPE, 0);
        if ((fileType < TYPE_TEXT || fileType > TYPE_IMAGE) && !FileUtil.isImageFile(filePath) && !FileUtil.isTextFile(filePath)) {
            finishAfterToast("暂不支持该格式文件展示");
            return;
        }

        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        mainHandler = new Handler(Looper.getMainLooper());

        if (FileUtil.isTextFile(filePath) || fileType == TYPE_TEXT) {
            showTextContent(filePath);
            return;
        }

        if (FileUtil.isImageFile(filePath) || fileType == TYPE_IMAGE) {
            showImageContent(filePath);
        }
    }

    /**
     * 解析并显示文本数据
     *
     * @param filePath 文件路径
     **/
    private void showTextContent(@NonNull final String filePath) {
        loadingView.setVisibility(View.VISIBLE);
        textContentView.setVisibility(View.GONE);
        handler.post(new Runnable() {
            @Override
            public void run() {
                parseTextFile(filePath);
            }
        });
    }

    /**
     * 子线程读写文件, 并在主线程中展示结果
     *
     * @param filePath 文件路径
     */
    private void parseTextFile(@NonNull String filePath) {
        final String content;

        try {
            content = FileUtil.getTextFileContent(filePath);
        } catch (Exception e) {
            finishAfterToast("读取文件失败");
            return;
        }

        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                loadingView.setVisibility(View.GONE);
                textContentView.setVisibility(View.VISIBLE);
                textContentView.setText(content);
            }
        });
    }

    /**
     * 解析并显示图片数据
     *
     * @param filePath 文件路径
     */
    private void showImageContent(@NonNull String filePath) {
        loadingView.setVisibility(View.GONE);
        imageContentView.setVisibility(View.VISIBLE);
        try {
            Bitmap bit = BitmapFactory.decodeFile(filePath);
            if (bit == null) {
                finishAfterToast("解析图片失败");
                return;
            }
            imageContentView.setImageBitmap(bit);
        } catch (Exception e) {
            finishAfterToast("解析图片失败");
        }
    }

    /**
     * toast 后关闭页面
     *
     * @param msg toast
     */
    private void finishAfterToast(@NonNull String msg) {
        MBPlatformUtil.toast(msg);
        finish();
    }
}
