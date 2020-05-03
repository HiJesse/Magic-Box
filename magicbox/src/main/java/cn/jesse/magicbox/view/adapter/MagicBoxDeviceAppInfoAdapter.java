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
import cn.jesse.magicbox.data.MagicBoxDeviceAppInfoData;

/**
 * 魔盒设备app信息Adapter
 *
 * @author jesse
 */
public class MagicBoxDeviceAppInfoAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<MagicBoxDeviceAppInfoData> data = new ArrayList<>();

    public MagicBoxDeviceAppInfoAdapter(@NonNull Activity activity) {
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.activity_magic_box_device_info_item, null);

        TextView nameText = view.findViewById(R.id.tv_name);
        TextView valueText = view.findViewById(R.id.tv_value);
        MagicBoxDeviceAppInfoData itemData = data.get(position);

        nameText.setText(itemData.getName());
        valueText.setText(itemData.getValue());
        return view;
    }

    public void addData(List<MagicBoxDeviceAppInfoData> info) {
        if (info == null || info.isEmpty()) {
            return;
        }

        data.addAll(info);
        notifyDataSetChanged();
    }
}
