package cn.jesse.magicbox.util;

import android.text.TextUtils;

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

    /**
     * 获取文件后缀
     *
     * @param fileName 文件名
     * @return suffix
     */
    public static String getFileSuffix(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }


        if (fileName.length() > 0) {
            int dot = fileName.lastIndexOf('.');
            if ((dot > -1) && (dot < (fileName.length() - 1))) {
                return fileName.substring(dot + 1);
            }
        }
        return null;
    }

    /**
     * 传入文件是否为文本文件
     *
     * @param fileName file name
     * @return bool
     */
    public static boolean isTextFile(String fileName) {
        String suffix = getFileSuffix(fileName);
        if (TextUtils.isEmpty(suffix)) {
            return false;
        }
        return "txt".equals(suffix.toLowerCase());
    }

    /**
     * 传入文件是否为图片文件
     *
     * @param fileName file name
     * @return bool
     */
    public static boolean isImageFile(String fileName) {
        String suffix = getFileSuffix(fileName);
        if (TextUtils.isEmpty(suffix)) {
            return false;
        }

        suffix = suffix.toLowerCase();
        return "jpg".equals(suffix) || "gif".equals(suffix) || "png".equals(suffix) || "jpeg".equals(suffix) || "bmp".equals(suffix);
    }
}
