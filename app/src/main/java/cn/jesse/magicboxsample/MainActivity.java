package cn.jesse.magicboxsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import cn.jesse.magicbox.MagicBox;
import cn.jesse.magicbox.manager.PerformanceInfoManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MagicBox.init(getApplication());
    }

    @Override
    protected void onResume() {
        super.onResume();
        PerformanceInfoManager.getInstance().startMonitorFPS();
        PerformanceInfoManager.getInstance().startMonitorCPU();
        PerformanceInfoManager.getInstance().startMonitorMem();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PerformanceInfoManager.getInstance().stopMonitorFPS();
        PerformanceInfoManager.getInstance().stopMonitorCPU();
        PerformanceInfoManager.getInstance().stopMonitorMem();
    }
}
