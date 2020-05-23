package cn.jesse.magicbox.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件工具
 *
 * @author jesse
 */
public class FileUtil {
    private static final String TAG = "FileUtil";

    private FileUtil() {
        // unused
    }

    /**
     * 拷贝文件
     *
     * @param srcFile    源文件
     * @param targetFile 目的文件
     * @return status
     */
    public static boolean copyFile(File srcFile, File targetFile) {
        if (srcFile == null || targetFile == null) {
            return false;
        }

        boolean status = false;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = new FileInputStream(srcFile);
            outputStream = new FileOutputStream(targetFile);
            byte[] buffer = new byte[1024 * 4];
            int c;
            while ((c = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, c);
            }
            status = true;

        } catch (Exception ex) {
            MBLog.e(TAG, "copyFile " + ex.getMessage());
        } finally {
            close(outputStream);
            close(inputStream);
        }
        return status;
    }

    /**
     * 关闭IO
     *
     * @param closeable io
     */
    public static void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (IOException e) {
            MBLog.e(TAG, "close " + e.getMessage());
        }
    }
}
