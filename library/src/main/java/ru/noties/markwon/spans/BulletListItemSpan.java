package ru.noties.markwon.spans;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.style.LeadingMarginSpan;

public class BulletListItemSpan implements LeadingMarginSpan {

    private SpannableTheme theme;

    private final Paint paint = ObjectsPool.paint();
    private final RectF circle = ObjectsPool.rectF();
    private final Rect rectangle = ObjectsPool.rect();

    private final int blockIndent;
    private final int level;
    private final int start;

    public BulletListItemSpan(
            @NonNull SpannableTheme theme,
            @IntRange(from = 0) int blockIndent,
            @IntRange(from = 0) int level,
            @IntRange(from = 0) int start) {
        this.theme = theme;
        this.blockIndent = blockIndent;
        this.level = level;
        this.start = start;
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return theme.getBlockMargin();
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {

        // if there was a line break, we don't need to draw anything
        if (this.start != start) {
            return;
        }

        paint.set(p);

        theme.applyListItemStyle(paint);

        final int save = c.save();
        try {

            final int width = theme.getBlockMargin();
            final int height = bottom - top;

            final int side = theme.getBulletWidth(bottom - top);

            final int marginLeft = (width - side) / 2;
            final int marginTop = (height - side) / 2;

            final int l = (width * (blockIndent - 1)) + marginLeft;
            final int t = top + marginTop;
            final int r = l + side;
            final int b = t + side;

            if (level == 0
                    || level == 1) {

                circle.set(l, t, r, b);

                final Paint.Style style = level == 0
                        ? Paint.Style.FILL
                        : Paint.Style.STROKE;
                paint.setStyle(style);

                c.drawOval(circle, paint);
            } else {

                rectangle.set(l, t, r, b);

                paint.setStyle(Paint.Style.FILL);

                c.drawRect(rectangle, paint);
            }

        } finally {
            c.restoreToCount(save);
        }
    }
}
