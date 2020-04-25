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

import cn.jesse.magicbox.R;

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

        blockRadioButton.setOnCheckedChangeListener(this);
        timeoutRadioButton.setOnCheckedChangeListener(this);
        weekRadioButton.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton checkBox, boolean checked) {
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
        }
    }

    private void checkPerformanceCPU(boolean checked) {

    }

    private void checkPerformanceMem(boolean checked) {

    }

    private void checkPerformanceFps(boolean checked) {

    }

    private void checkNetworkBlock(boolean checked) {

    }

    private void checkNetworkTimeout(boolean checked) {

    }

    private void checkNetworkWeek(boolean checked) {

    }
}
