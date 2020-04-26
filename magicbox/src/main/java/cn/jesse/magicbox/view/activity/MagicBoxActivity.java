package cn.jesse.magicbox.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;

import cn.jesse.magicbox.MagicBox;
import cn.jesse.magicbox.R;
import cn.jesse.magicbox.manager.NetworkInfoManager;

/**
 * magic box主页面
 *
 * @author jesse
 */
public class MagicBoxActivity extends Activity implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "MagicBoxActivity";

    private TextView titleText;
    private GridView toolsGridView;

    private CheckBox cpuCheckBox;
    private CheckBox memCheckBox;
    private CheckBox fpsCheckBox;

    private RadioButton blockRadioButton;
    private RadioButton timeoutRadioButton;
    private RadioButton weekRadioButton;
    private CheckBox netLogCheckBox;

    public static void start(Context context) {
        Intent intent = new Intent(context, MagicBoxActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_magic_box);
        initView();
        initData();
    }

    private void initView() {
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleText = findViewById(R.id.tv_title);

        toolsGridView = findViewById(R.id.gv_tools);

        cpuCheckBox = findViewById(R.id.cb_cpu);
        memCheckBox = findViewById(R.id.cb_mem);
        fpsCheckBox = findViewById(R.id.cb_fps);

        cpuCheckBox.setOnCheckedChangeListener(this);
        memCheckBox.setOnCheckedChangeListener(this);
        fpsCheckBox.setOnCheckedChangeListener(this);

        blockRadioButton = findViewById(R.id.rb_block);
        timeoutRadioButton = findViewById(R.id.rb_timeout);
        weekRadioButton = findViewById(R.id.rb_week);
        netLogCheckBox = findViewById(R.id.cb_net_log);

        blockRadioButton.setOnCheckedChangeListener(this);
        timeoutRadioButton.setOnCheckedChangeListener(this);
        weekRadioButton.setOnCheckedChangeListener(this);
        netLogCheckBox.setOnCheckedChangeListener(this);
    }

    private void initData() {
        cpuCheckBox.setChecked(MagicBox.getPerformanceManager().isMonitoringCPU());
        memCheckBox.setChecked(MagicBox.getPerformanceManager().isMonitoringMem());
        fpsCheckBox.setChecked(MagicBox.getPerformanceManager().isMonitoringFPS());

        netLogCheckBox.setChecked(MagicBox.getNetworkInfoManager().isRequestLoggerEnable());

        if (!MagicBox.getNetworkInfoManager().isSimulationEnable()) {
            return;
        }

        switch (MagicBox.getNetworkInfoManager().getSimulationType()) {
            case NetworkInfoManager.SIMULATION_TYPE_BLOCK:
                blockRadioButton.setChecked(true);
                break;
            case NetworkInfoManager.SIMULATION_TYPE_TIMEOUT:
                timeoutRadioButton.setChecked(true);
                break;
            case NetworkInfoManager.SIMULATION_TYPE_SPEED_LIMIT:
                weekRadioButton.setChecked(true);
                break;
            default:
                // ignore
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton checkBox, boolean checked) {
        if (!checkBox.isPressed()) {
            return;
        }

        int i = checkBox.getId();
        if (i == R.id.cb_cpu) {
            checkPerformanceCPU(checked);
        } else if (i == R.id.cb_mem) {
            checkPerformanceMem(checked);
        } else if (i == R.id.cb_fps) {
            checkPerformanceFps(checked);
        } else if (i == R.id.rb_block) {
            checkNetworkBlock(checked);
        } else if (i == R.id.rb_timeout) {
            checkNetworkTimeout(checked);
        } else if (i == R.id.rb_week) {
            checkNetworkWeek(checked);
        } else if (i == R.id.cb_net_log) {
            MagicBox.getNetworkInfoManager().setRequestLoggerEnable(checked);
            MagicBox.getNetworkInfoManager().setRequestLoggerHostWhiteList(new String[]{"www.github.com"});
        }
    }

    private void checkPerformanceCPU(boolean checked) {
        if (checked) {
            MagicBox.getPerformanceManager().startMonitorCPU();
        } else {
            MagicBox.getPerformanceManager().stopMonitorCPU();
        }
    }

    private void checkPerformanceMem(boolean checked) {
        if (checked) {
            MagicBox.getPerformanceManager().startMonitorMem();
        } else {
            MagicBox.getPerformanceManager().stopMonitorMem();
        }
    }

    private void checkPerformanceFps(boolean checked) {
        if (checked) {
            MagicBox.getPerformanceManager().startMonitorFPS();
        } else {
            MagicBox.getPerformanceManager().stopMonitorFPS();
        }
    }

    private void checkNetworkBlock(boolean checked) {
        MagicBox.getNetworkInfoManager().setSimulationEnable(checked);
        if (checked) {
            MagicBox.getNetworkInfoManager().setSimulationType(NetworkInfoManager.SIMULATION_TYPE_BLOCK);
        }
    }

    private void checkNetworkTimeout(boolean checked) {
        MagicBox.getNetworkInfoManager().setSimulationEnable(checked);
        if (checked) {
            MagicBox.getNetworkInfoManager().setSimulationType(NetworkInfoManager.SIMULATION_TYPE_TIMEOUT);
        }
    }

    private void checkNetworkWeek(boolean checked) {
        MagicBox.getNetworkInfoManager().setSimulationEnable(checked);
        if (checked) {
            MagicBox.getNetworkInfoManager().setSimulationType(NetworkInfoManager.SIMULATION_TYPE_SPEED_LIMIT);
            MagicBox.getNetworkInfoManager().setSimulationRequestSpeed(5);
        }
    }
}