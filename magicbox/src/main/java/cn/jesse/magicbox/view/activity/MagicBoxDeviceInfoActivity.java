package cn.jesse.magicbox.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.jesse.magicbox.R;
import cn.jesse.magicbox.data.MagicBoxDeviceAppInfoData;
import cn.jesse.magicbox.util.MBPlatformUtil;
import cn.jesse.magicbox.view.adapter.MagicBoxDeviceAppInfoAdapter;

/**
 * 魔盒设备信息页面
 *
 * @author jesse
 */
public class MagicBoxDeviceInfoActivity extends Activity {
    private ListView listView;

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
        MagicBoxDeviceAppInfoAdapter adapter = new MagicBoxDeviceAppInfoAdapter(this);
        listView.setAdapter(adapter);

        List<MagicBoxDeviceAppInfoData> list = new ArrayList<>();
        list.add(new MagicBoxDeviceAppInfoData("品牌", MBPlatformUtil.getBrand()));
        list.add(new MagicBoxDeviceAppInfoData("型号", MBPlatformUtil.getModel()));
        list.add(new MagicBoxDeviceAppInfoData("系统版本", MBPlatformUtil.getOSVersion()));
        list.add(new MagicBoxDeviceAppInfoData("指令集", MBPlatformUtil.getABI()));
        list.add(new MagicBoxDeviceAppInfoData("root", MBPlatformUtil.isRooted() ? "已root" : "未root"));
        int[] displayMetrics = MBPlatformUtil.getDisplayMetrics(this);
        list.add(new MagicBoxDeviceAppInfoData("分辨率", displayMetrics[0] + "*" + displayMetrics[1]));

        list.add(new MagicBoxDeviceAppInfoData("", ""));
        list.add(new MagicBoxDeviceAppInfoData("网络状态", MBPlatformUtil.isNetworkAvailable(this) ? "正常" : "异常"));
        if (MBPlatformUtil.isWifiConnected(this)) {
            list.add(new MagicBoxDeviceAppInfoData("wifi状态", "正常"));
            list.add(new MagicBoxDeviceAppInfoData("ssid", "" + MBPlatformUtil.getSSID(this)));
            int[] linkSpeedAndRssi = MBPlatformUtil.getWIFIRssiAndLinkSpeed(this);
            list.add(new MagicBoxDeviceAppInfoData("wifi信号", "[0, 100]  " + linkSpeedAndRssi[0]));
            list.add(new MagicBoxDeviceAppInfoData("wifi速度", linkSpeedAndRssi[1] + WifiInfo.LINK_SPEED_UNITS));
        } else {
            list.add(new MagicBoxDeviceAppInfoData("wifi状态", "异常"));
        }
        list.add(new MagicBoxDeviceAppInfoData("运营商", "" + MBPlatformUtil.getCellularOperatorTypeString(this)));

        adapter.addData(list);
    }

}
