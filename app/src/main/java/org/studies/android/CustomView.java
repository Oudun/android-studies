package org.studies.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Denis on 1/27/2018.
 */

public class CustomView extends View {

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        canvas.drawRect(20, 20, 20, 20, paint);
    }

}
