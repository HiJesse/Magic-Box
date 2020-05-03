package cn.jesse.magicbox.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.jesse.magicbox.R;
import cn.jesse.magicbox.data.MagicBoxDeviceAppInfoData;
import cn.jesse.magicbox.util.MBAppUtil;
import cn.jesse.magicbox.util.MBPlatformUtil;
import cn.jesse.magicbox.view.adapter.MagicBoxDeviceAppInfoAdapter;

/**
 * 魔盒APP信息页面
 *
 * @author jesse
 */
public class MagicBoxAppInfoActivity extends Activity {
    private ListView listView;
    private ImageView appIconImage;

    public static void start(Context context) {
        Intent intent = new Intent(context, MagicBoxAppInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_magic_box_app_info);
        initView();
        initData();
    }

    void initView() {
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        appIconImage = findViewById(R.id.iv_app_icon);
        listView = findViewById(R.id.lv_content);
        ((TextView) findViewById(R.id.tv_title)).setText("APP信息");

    }

    void initData() {
        Bitmap bitmap = MBAppUtil.getAppIcon(this);
        if (bitmap != null) {
            appIconImage.setImageBitmap(bitmap);
        }

        MagicBoxDeviceAppInfoAdapter adapter = new MagicBoxDeviceAppInfoAdapter(this);
        listView.setAdapter(adapter);

        List<MagicBoxDeviceAppInfoData> list = new ArrayList<>();
        list.add(new MagicBoxDeviceAppInfoData("名称", MBAppUtil.getAppName(this)));
        list.add(new MagicBoxDeviceAppInfoData("版本号", MBAppUtil.getVersionName(this)));
        list.add(new MagicBoxDeviceAppInfoData("版本码", String.valueOf(MBAppUtil.getVersionCode(this))));
        list.add(new MagicBoxDeviceAppInfoData("包名", MBAppUtil.getPackageName(this)));

        adapter.addData(list);
    }

}
