package cn.jesse.magicbox.view.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cn.jesse.magicbox.R;
import cn.jesse.magicbox.data.MagicBoxToolData;

/**
 * 魔盒 小工具Adapter
 *
 * @author jesse
 */
public class MagicBoxToolsAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<MagicBoxToolData> toolsData = new ArrayList<>();

    public MagicBoxToolsAdapter(@NonNull Activity activity) {
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return toolsData == null || toolsData.isEmpty() ? 0 : toolsData.size();
    }

    @Override
    public Object getItem(int position) {
        return toolsData == null || toolsData.isEmpty() ? null : toolsData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //将布局文件转化为View对象
        View view = mInflater.inflate(R.layout.activity_magic_box_tools_item, null);

        View rootView = view.findViewById(R.id.ll_root);
        TextView toolNameText = view.findViewById(R.id.tv_tool_name);

        final MagicBoxToolData toolData = toolsData.get(position);
        toolNameText.setText(toolData.getToolName());
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toolData.getToolClickListener() != null) {
                    toolData.getToolClickListener().onToolClick(position, toolData.getToolName());
                }
            }
        });
        return view;
    }

    /**
     * 添加一个工具
     *
     * @param data 工具数据
     */
    public void addData(@NonNull MagicBoxToolData data) {
        if (toolsData == null) {
            toolsData = new ArrayList<>();
        }

        toolsData.add(data);
        notifyDataSetChanged();
    }

    /**
     * 小工具点击监听
     */
    public interface OnToolClickListener {

        /**
         * 小工具点击
         *
         * @param index    位置 0开始
         * @param toolName 工具名称
         */
        void onToolClick(int index, String toolName);
    }
}

