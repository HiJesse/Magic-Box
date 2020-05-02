package cn.jesse.magicbox.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.jesse.magicbox.data.MagicBoxDeviceAppInfoData;

/**
 * 魔盒设备app信息Adapter
 *
 * @author jesse
 */
public class MagicBoxDeviceAppInfoAdapter extends BaseAdapter {
    private List<MagicBoxDeviceAppInfoData> data = new ArrayList<>();

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public void setData(List<MagicBoxDeviceAppInfoData> info) {
        if (info == null || info.isEmpty()) {
            return;
        }

        data.addAll(info);
    }
}
