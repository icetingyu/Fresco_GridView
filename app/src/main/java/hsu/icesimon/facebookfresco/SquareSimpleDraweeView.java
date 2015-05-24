package hsu.icesimon.facebookfresco;

/**
 * Created by Simon Hsu on 15/5/24.
 */

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.view.SimpleDraweeView;

public class SquareSimpleDraweeView extends SimpleDraweeView
{
    public SquareSimpleDraweeView(Context context)
    {
        super(context);
    }

    public SquareSimpleDraweeView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public SquareSimpleDraweeView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
    }
}