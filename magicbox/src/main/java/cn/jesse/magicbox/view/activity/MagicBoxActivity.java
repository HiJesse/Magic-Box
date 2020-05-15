package cn.jesse.magicbox.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import cn.jesse.magicbox.MagicBox;
import cn.jesse.magicbox.R;
import cn.jesse.magicbox.data.MagicBoxToolData;
import cn.jesse.magicbox.manager.NetworkInfoManager;
import cn.jesse.magicbox.view.adapter.MagicBoxToolsAdapter;

/**
 * magic box主页面
 *
 * @author jesse
 */
public class MagicBoxActivity extends Activity implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "MagicBoxActivity";
    private static final String PARAMS_TITLE = "PARAMS_TITLE";
    private static final String PARAMS_EXTERNAL_TOOLS = "PARAMS_EXTERNAL_TOOLS";
    // result code 基数
    public static final int BASE_RESULT_CODE = 100;

    private TextView titleText;
    private GridView toolsGridView;
    private MagicBoxToolsAdapter toolsAdapter;

    private CheckBox cpuCheckBox;
    private CheckBox memCheckBox;
    private CheckBox fpsCheckBox;

    private RadioButton blockNetRadioButton;
    private RadioButton timeoutNetRadioButton;
    private RadioButton weekNetRadioButton;
    private RadioButton closeNetSimulationRadioButton;
    private CheckBox netLogCheckBox;

    public static void start(@NonNull Context context) {
        start(context, "");
    }

    /**
     * 启动默认魔盒主页
     *
     * @param context context
     * @param title   标题
     */
    public static void start(@NonNull Context context, String title) {
        Intent intent = new Intent(context, MagicBoxActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (!TextUtils.isEmpty(title)) {
            intent.putExtra(PARAMS_TITLE, title);
        }
        context.startActivity(intent);
    }

    public static void startActivityWithResult(@NonNull Activity context, int requestCode, @NonNull String[] tools) {
        startActivityWithResult(context, "", requestCode, tools);
    }

    /**
     * 启动魔盒主页, 支持小工具扩展, 并监听回调
     * <p>
     * 回调以{@link #BASE_RESULT_CODE} 为基数
     * 加上tools数组下标为result code, 进行回调
     *
     * @param context     context
     * @param title       标题
     * @param requestCode 请求code
     * @param tools       扩展小工具
     */
    public static void startActivityWithResult(@NonNull Activity context, String title, int requestCode, @NonNull String[] tools) {
        Intent intent = new Intent(context, MagicBoxActivity.class);
        intent.putExtra(PARAMS_EXTERNAL_TOOLS, tools);
        if (!TextUtils.isEmpty(title)) {
            intent.putExtra(PARAMS_TITLE, title);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivityForResult(intent, requestCode);
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

        closeNetSimulationRadioButton = findViewById(R.id.rb_net_simulation_close);
        blockNetRadioButton = findViewById(R.id.rb_net_simulation_block);
        timeoutNetRadioButton = findViewById(R.id.rb_net_simulation_timeout);
        weekNetRadioButton = findViewById(R.id.rb_net_simulation_week);
        netLogCheckBox = findViewById(R.id.cb_net_log);

        closeNetSimulationRadioButton.setOnCheckedChangeListener(this);
        blockNetRadioButton.setOnCheckedChangeListener(this);
        timeoutNetRadioButton.setOnCheckedChangeListener(this);
        weekNetRadioButton.setOnCheckedChangeListener(this);
        netLogCheckBox.setOnCheckedChangeListener(this);
    }

    private void initData() {
        String title = getIntent().getStringExtra(PARAMS_TITLE);
        if (!TextUtils.isEmpty(title)) {
            titleText.setText(title);
        }

        initToolsData();
        cpuCheckBox.setChecked(MagicBox.getPerformanceManager().isMonitoringCPU());
        memCheckBox.setChecked(MagicBox.getPerformanceManager().isMonitoringMem());
        fpsCheckBox.setChecked(MagicBox.getPerformanceManager().isMonitoringFPS());

        netLogCheckBox.setChecked(MagicBox.getNetworkInfoManager().isRequestLoggerEnable());

        if (!MagicBox.getNetworkInfoManager().isSimulationEnable()) {
            closeNetSimulationRadioButton.setChecked(true);
            return;
        }

        switch (MagicBox.getNetworkInfoManager().getSimulationType()) {
            case NetworkInfoManager.SIMULATION_TYPE_BLOCK:
                blockNetRadioButton.setChecked(true);
                break;
            case NetworkInfoManager.SIMULATION_TYPE_TIMEOUT:
                timeoutNetRadioButton.setChecked(true);
                break;
            case NetworkInfoManager.SIMULATION_TYPE_SPEED_LIMIT:
                weekNetRadioButton.setChecked(true);
                break;
            default:
                // ignore
        }
    }

    private void initToolsData() {
        toolsAdapter = new MagicBoxToolsAdapter(this);
        toolsGridView.setAdapter(toolsAdapter);
        toolsAdapter.addData(new MagicBoxToolData("App信息", new MagicBoxToolsAdapter.OnToolClickListener() {
            @Override
            public void onToolClick(int index, String toolName) {
                MagicBoxAppInfoActivity.start(MagicBoxActivity.this);
            }
        }));

        toolsAdapter.addData(new MagicBoxToolData("设备信息", new MagicBoxToolsAdapter.OnToolClickListener() {
            @Override
            public void onToolClick(int index, String toolName) {
                MagicBoxDeviceInfoActivity.start(MagicBoxActivity.this);
            }
        }));

        toolsAdapter.addData(new MagicBoxToolData("开关仪表盘", new MagicBoxToolsAdapter.OnToolClickListener() {
            @Override
            public void onToolClick(int index, String toolName) {
                if (MagicBox.getDashboard().isShowing()) {
                    MagicBox.getDashboard().dismissDashboard();
                } else {
                    MagicBox.getDashboard().showDashboard();
                }
            }
        }));

        toolsAdapter.addData(new MagicBoxToolData("Crash查看", new MagicBoxToolsAdapter.OnToolClickListener() {
            @Override
            public void onToolClick(int index, String toolName) {
                MagicBoxFileExplorerActivity.start(MagicBoxActivity.this);
            }
        }));

        // 添加外部扩展小工具
        String[] externalTools = getIntent().getStringArrayExtra(PARAMS_EXTERNAL_TOOLS);
        if (externalTools == null || externalTools.length == 0) {
            return;
        }

        for (int i = 0; i < externalTools.length; i++) {
            final int finalI = i;
            toolsAdapter.addData(new MagicBoxToolData(externalTools[i], new MagicBoxToolsAdapter.OnToolClickListener() {
                @Override
                public void onToolClick(int index, String toolName) {
                    setResult(BASE_RESULT_CODE + finalI);
                    finish();
                }
            }));
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
        } else if (i == R.id.rb_net_simulation_block) {
            checkNetworkBlock(checked);
        } else if (i == R.id.rb_net_simulation_timeout) {
            checkNetworkTimeout(checked);
        } else if (i == R.id.rb_net_simulation_week) {
            checkNetworkWeek(checked);
        } else if (i == R.id.rb_net_simulation_close) {
            MagicBox.getNetworkInfoManager().setSimulationEnable(false);
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
