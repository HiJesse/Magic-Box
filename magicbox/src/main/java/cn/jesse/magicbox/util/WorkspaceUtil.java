package cn.jesse.magicbox.util;

import android.app.Application;

import androidx.annotation.NonNull;

import java.io.File;

/**
 * 魔盒工作空间工具
 *
 * @author jesse
 */
public class WorkspaceUtil {
    private static final String PATH_INTERNAL = "magic_box/";
    private static final String PATH_TOMBSTONE = PATH_INTERNAL + "tombstone/";

    /**
     * 初始化工作空间
     *
     * @param application app
     */
    private static void initWorkspace(@NonNull Application application) {
        File internalWorkspaceDir = new File(application.getCacheDir(), PATH_INTERNAL);
        if (!internalWorkspaceDir.isDirectory() || !internalWorkspaceDir.exists()) {
            internalWorkspaceDir.delete();
            internalWorkspaceDir.mkdirs();
        }

        File internalTombstoneDir = new File(application.getCacheDir(), PATH_TOMBSTONE);
        if (!internalTombstoneDir.isDirectory() || !internalTombstoneDir.exists()) {
            internalTombstoneDir.delete();
            internalTombstoneDir.mkdirs();
        }
    }

    /**
     * 获取沙盒文件夹
     *
     * @param application app
     * @return sandbox
     */
    public static File getSandboxDir(@NonNull Application application) {
        return application.getFilesDir().getParentFile();
    }

    /**
     * 获取内部 crash工作路径
     *
     * @param application app
     * @return file
     */
    public static File getInternalCrashDir(@NonNull Application application) {
        initWorkspace(application);
        return new File(application.getCacheDir(), PATH_TOMBSTONE);
    }

    /**
     * 确定入参是是有效文件夹
     *
     * @param file 文件夹
     * @return bool
     */
    public static boolean isDirValid(@NonNull File file) {
        try {
            return file.exists() && file.isDirectory();
        } catch (Exception e) {
            return false;
        }
    }
}
