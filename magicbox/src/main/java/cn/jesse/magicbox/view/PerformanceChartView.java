package cn.jesse.magicbox.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorRes;

import java.util.LinkedList;

import cn.jesse.magicbox.util.MBPlatformUtil;

/**
 * 性能线图
 *
 * @author jesse
 */
public class PerformanceChartView extends View {
    private static int SIZE_DEFAULT_MAX_POINT = 60;
    private int width;
    private int height;
    private Paint xyLinePaint;
    private Paint linePaint;

    private MagicBoxLimitQueue<Point> points = new MagicBoxLimitQueue(SIZE_DEFAULT_MAX_POINT);
    private int maxPointSize = SIZE_DEFAULT_MAX_POINT;

    public PerformanceChartView(Context context) {
        super(context);
        init();
    }

    public PerformanceChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PerformanceChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 设置图标最大展示点数
     *
     * @param size 最大点数
     */
    public void setPointSize(int size) {
        if (size <= 0) {
            return;
        }
        this.maxPointSize = size;
        points.setLimit(maxPointSize);
    }

    /**
     * 设置线图的颜色
     *
     * @param color color
     */
    public void setLineColor(@ColorRes int color) {
        linePaint.setColor(getResources().getColor(color));
    }

    /**
     * 更新并刷新cpu使用率
     *
     * @param cpuUsage cpu
     */
    public void updateCPUUsage(float cpuUsage) {
        int pointY = (int) (height * (100 - cpuUsage) / 100);
        // 只计算Y轴, x轴在绘制的时候 根据position实时计算
        points.offer(new Point(0, pointY));
        invalidate();
    }

    /**
     * 更新并刷新内存使用率
     *
     * @param memUsage mem
     */
    public void updateMemUsage(float memUsage) {
        float totalMem = MBPlatformUtil.getAppTotalMem();
        if (totalMem == 0) {
            return;
        }

        int pointY = (int) (height * (1 - memUsage / totalMem));
        points.offer(new Point(0, pointY));
        invalidate();
    }

    /**
     * 更新并刷新fps
     *
     * @param fps fps
     */
    public void updateFPS(int fps) {
        int pointY = height * (65 - fps) / 30;
        points.offer(new Point(0, pointY));
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制xy轴
        canvas.drawLine(0, height, width, height, xyLinePaint);
        canvas.drawLine(0, 0, 0, height, xyLinePaint);

        // 绘制线图
        LinkedList<Point> chatData = this.points.getRealData();
        Path linePath = new Path();
        for (int i = 0; i < chatData.size(); i++) {
            Point point = chatData.get(i);
            // x轴数据 根据position 和每隔大小进行计算
            float pointX = i * width / maxPointSize;
            if (i == 0) {
                linePath.moveTo(pointX, point.y);
            } else {
                linePath.lineTo(pointX, point.y);
            }
        }
        canvas.drawPath(linePath, linePaint);
        linePath.close();

    }

    private void init() {
        xyLinePaint = new Paint();
        xyLinePaint.setStrokeWidth(2);

        linePaint = new Paint();
        xyLinePaint.setStrokeWidth(4);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.BLACK);
    }

}
