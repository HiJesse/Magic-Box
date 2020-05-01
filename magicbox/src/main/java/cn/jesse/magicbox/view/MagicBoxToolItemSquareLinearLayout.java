package cn.jesse.magicbox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 小工具 grid item 自定义正方形item linear layout
 *
 * @author jesse
 */
public class MagicBoxToolItemSquareLinearLayout extends LinearLayout {

    public MagicBoxToolItemSquareLinearLayout(Context context) {
        super(context);
    }

    public MagicBoxToolItemSquareLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MagicBoxToolItemSquareLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
