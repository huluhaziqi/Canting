package cn.caimatou.canting.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import cn.caimatou.canting.R;
import cn.caimatou.canting.utils.GLResourcesUtil;


/**
 * Description：画虚线
 * <br/><br/>Created by Fausgoal on 15/8/30.
 * <br/><br/>
 */
public class GLDashLineView extends View {
    private Context context;

    public GLDashLineView(Context context) {
        super(context);
        this.context = context;
    }

    public GLDashLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public GLDashLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setColor(GLResourcesUtil.getColor(R.color.gray3));
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(getWidth(), 0);
        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);
        super.onDraw(canvas);
    }
}
