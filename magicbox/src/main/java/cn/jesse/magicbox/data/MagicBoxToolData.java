package cn.jesse.magicbox.data;

import cn.jesse.magicbox.view.adapter.MagicBoxToolsAdapter;

public class MagicBoxToolData {

    private String toolName;
    private MagicBoxToolsAdapter.OnToolClickListener toolClickListener;

    public MagicBoxToolData(String toolName, MagicBoxToolsAdapter.OnToolClickListener toolClickListener) {
        this.toolName = toolName;
        this.toolClickListener = toolClickListener;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public MagicBoxToolsAdapter.OnToolClickListener getToolClickListener() {
        return toolClickListener;
    }

    public void setToolClickListener(MagicBoxToolsAdapter.OnToolClickListener toolClickListener) {
        this.toolClickListener = toolClickListener;
    }
}
