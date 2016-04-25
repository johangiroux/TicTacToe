package com.bionicapps.tictactoe.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import com.bionicapps.tictactoe.R;

/**
 * Created by johan on 4/24/16.
 */
public class GridCell extends TextView {

    private int position;
    private int rowsCount;
    private int line;
    private int column;

    private Paint paint;

    public GridCell(Context context) {
        super(context);
        commonInit();
    }

    public GridCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit();
    }

    public GridCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GridCell(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        commonInit();
    }

    private void commonInit() {
        this.setGravity(Gravity.CENTER);
    }

    public int getPosition() {
        return position;
    }


    public void setPosition(int position, int rowsCount) {
        this.position = position;
        this.rowsCount = rowsCount;
        this.line = (position / rowsCount) + 1;
        this.column = (position % rowsCount) + 1;

        if (paint == null) {
            paint = new Paint();
            paint.setColor(ContextCompat.getColor(getContext(), R.color.black));
            paint.setStrokeWidth(2.f);
        }
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 84/rowsCount);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        float stokeWidth = paint.getStrokeWidth();
        if (line != rowsCount) {
            // draw bottom line
            canvas.drawLine(0, getHeight()-stokeWidth, getWidth(), getHeight()-stokeWidth, paint);
        }
        if (column != rowsCount) {
            // draw right line
            canvas.drawLine(getWidth()-stokeWidth, 0, getWidth()-stokeWidth, getHeight(), paint);
        }

        super.onDraw(canvas);
    }
}
