package cn.jesse.magicbox.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import cn.jesse.magicbox.R;
import cn.jesse.magicbox.view.adapter.MagicBoxDeviceAppInfoAdapter;

/**
 * 魔盒设备信息页面
 *
 * @author jesse
 */
public class MagicBoxDeviceInfoActivity extends Activity {
    private ListView listView;
    private MagicBoxDeviceAppInfoAdapter adapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, MagicBoxDeviceInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_magic_box_device_info);
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

        listView = findViewById(R.id.lv_content);
        ((TextView) findViewById(R.id.tv_title)).setText("设备信息");

    }

    void initData() {
        adapter = new MagicBoxDeviceAppInfoAdapter();
        listView.setAdapter(adapter);
    }

}
