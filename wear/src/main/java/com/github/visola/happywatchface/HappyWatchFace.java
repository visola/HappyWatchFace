package com.github.visola.happywatchface;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.view.SurfaceHolder;
import android.view.WindowInsets;

import java.util.Calendar;

public class HappyWatchFace extends CanvasWatchFaceService {

    private static final Typeface NORMAL_TYPEFACE = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);

    @Override
    public HappyWatchFaceEngine onCreateEngine() {
        return new HappyWatchFaceEngine();
    }

    public class HappyWatchFaceEngine extends CanvasWatchFaceService.Engine {

        UpdateTimeHandler mUpdateTimeHandler = new UpdateTimeHandler(this);
        Paint mBackgroundPaint;
        Paint mHourTextPaint;
        Paint mDayTextPaint;
        boolean mAmbient;
        Calendar mCalendar;
        float mHourYOffset;
        float mLineSpace;
        boolean mLowBitAmbient;

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            setWatchFaceStyle(new WatchFaceStyle.Builder(HappyWatchFace.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_VARIABLE)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .setAcceptsTapEvents(true)
                    .build());

            Resources resources = HappyWatchFace.this.getResources();

            mHourYOffset = resources.getDimension(R.dimen.digital_hour_y_offset);
            mLineSpace = resources.getDimension(R.dimen.digital_text_line_space);

            mBackgroundPaint = new Paint();
            mBackgroundPaint.setColor(resources.getColor(R.color.background));

            mHourTextPaint = createTextPaint(resources.getColor(R.color.digital_text));
            mDayTextPaint = createTextPaint(resources.getColor(R.color.digital_text));

            mCalendar = Calendar.getInstance();
        }

        @Override
        public void onApplyWindowInsets(WindowInsets insets) {
            super.onApplyWindowInsets(insets);

            // Load resources that have alternate values for round watches.
            Resources resources = HappyWatchFace.this.getResources();
            mHourTextPaint.setTextSize(resources.getDimension(R.dimen.digital_hour_text_size));
            mDayTextPaint.setTextSize(resources.getDimension(R.dimen.digital_day_text_size));
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
            mLowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            if (mAmbient != inAmbientMode) {
                mAmbient = inAmbientMode;
                if (mLowBitAmbient) {
                    mHourTextPaint.setAntiAlias(!inAmbientMode);
                    mDayTextPaint.setAntiAlias(!inAmbientMode);
                }
                invalidate();
            }

            updateTimer();
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            if (isInAmbientMode()) {
                canvas.drawColor(Color.BLACK);
            } else {
                canvas.drawRect(0, 0, bounds.width(), bounds.height(), mBackgroundPaint);
            }

            mCalendar.setTimeInMillis(System.currentTimeMillis());

            final String hourText;
            if (mAmbient) {
                hourText = String.format("%d:%02d", mCalendar.get(Calendar.HOUR), mCalendar.get(Calendar.MINUTE));
            } else {
                hourText = String.format("%d:%02d:%02d", mCalendar.get(Calendar.HOUR), mCalendar.get(Calendar.MINUTE), mCalendar.get(Calendar.SECOND));
            }

            Rect hourTextBounds = new Rect();
            mHourTextPaint.getTextBounds(hourText, 0, hourText.length(), hourTextBounds);

            canvas.drawText(hourText, canvas.getWidth() / 2 - hourTextBounds.width() / 2, mHourYOffset, mHourTextPaint);

            final String dayText = String.format("%04d-%02d-%02d", mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH) + 1, mCalendar.get(Calendar.DAY_OF_MONTH));

            Rect dayTextBounds = new Rect();
            mDayTextPaint.getTextBounds(dayText, 0, dayText.length(), dayTextBounds);

            canvas.drawText(dayText, canvas.getWidth() / 2 - dayTextBounds.width() / 2, mHourYOffset + hourTextBounds.height() + mLineSpace, mDayTextPaint);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            updateTimer();
        }

        boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        private Paint createTextPaint(int textColor) {
            Paint paint = new Paint();
            paint.setColor(textColor);
            paint.setTypeface(NORMAL_TYPEFACE);
            paint.setAntiAlias(true);
            return paint;
        }

        private void updateTimer() {
            mUpdateTimeHandler.removeMessages(UpdateTimeHandler.MSG_UPDATE_TIME);
            if (shouldTimerBeRunning()) {
                mUpdateTimeHandler.sendEmptyMessage(UpdateTimeHandler.MSG_UPDATE_TIME);
            }
        }

    }
}
