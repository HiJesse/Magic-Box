package cn.jesse.magicbox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 自定义grid view 兼容scroll view嵌套
 *
 * @author jesse
 */
public class ScrollInsideGridView extends GridView {

    public ScrollInsideGridView(Context context) {
        super(context);
    }

    public ScrollInsideGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollInsideGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
