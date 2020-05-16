package cn.jesse.magicbox.view.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import cn.jesse.magicbox.R;
import cn.jesse.magicbox.util.MBLog;

/**
 * 文件浏览器adapter
 *
 * @author jesse
 */
public class MagicBoxFileExplorerAdapter extends BaseAdapter {
    private static final String TAG = "FileExplorerAdapter";
    private LayoutInflater mInflater;
    private List<File> fileList;
    private OnFileExplorerClickListener onItemClickListener;

    public MagicBoxFileExplorerAdapter(@NonNull Activity activity, OnFileExplorerClickListener listener) {
        mInflater = LayoutInflater.from(activity);
        this.onItemClickListener = listener;
    }

    @Override
    public int getCount() {
        return isDirEmpty() ? 0 : fileList.size();
    }

    @Override
    public Object getItem(int position) {
        return isDirEmpty() ? 0 : fileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.activity_file_explorer_item, null);

        View rootView = view.findViewById(R.id.ll_root);
        ImageView itemIcon = view.findViewById(R.id.iv_item_icon);
        TextView itemName = view.findViewById(R.id.tv_item_name);

        final File file = fileList.get(position);
        itemIcon.setBackgroundResource(file.isDirectory() ?
                R.mipmap.activity_file_explorer_folder :
                R.mipmap.activity_file_explorer_file);

        itemName.setText(file.getName());


        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file.isDirectory()) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onFileExplorerItemClick(position, file);
                    }
                    setDir(file);
                    return;
                }


            }
        });
        return view;
    }

    private boolean isDirEmpty() {
        return fileList == null || fileList.isEmpty();
    }

    /**
     * 设置要打开的文件夹
     *
     * @param dir path
     */
    public void setDir(File dir) {
        if (dir == null) {
            return;
        }

        try {
            if (!dir.isDirectory() || dir.listFiles() == null) {
                return;
            }

            fileList = Arrays.asList(dir.listFiles());
        } catch (Exception e) {
            MBLog.e(TAG, "setDir " + e.getMessage());
        }
        notifyDataSetChanged();
    }

    /**
     * 点击文件浏览
     */
    public interface OnFileExplorerClickListener {

        /**
         * 点击文件浏览item
         *
         * @param position 位置
         * @param current  点击的文件夹
         */
        void onFileExplorerItemClick(int position, File current);
    }
}
