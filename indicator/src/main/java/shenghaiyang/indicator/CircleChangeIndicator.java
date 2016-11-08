/*
 * Copyright (C) 2016 shenghaiyang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package shenghaiyang.indicator;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author shenghaiyang
 */
public class CircleChangeIndicator extends View implements PagerIndicator {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private static final int DEFAULT_ORIENTATION = HORIZONTAL;

    private ViewPager mViewPager;
    private int mCount;
    private int mCurrentPage;

    private int mMaxRadius; //solidRadius+strokeWidth

    private int mOrientation;
    private int mSpacing;

    private int mNormalSolidRadius;
    private int mNormalStrokeWidth;
    private int mSelectedSolidRadius;
    private int mSelectedStrokeWidth;

    private int mNormalSolidColor;
    private int mNormalStrokeColor;
    private int mSelectedSolidColor;
    private int mSelectedStrokeColor;

    private static final Paint mNormalSolidPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final Paint mNormalStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final Paint mSelectedSolidPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final Paint mSelectedStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CircleChangeIndicator(Context context) {
        this(context, null);
    }

    public CircleChangeIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleChangeIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final Resources resources = context.getResources();
        final int defaultSpacing = resources.getDimensionPixelSize(R.dimen.default_spacing);

        final int defaultNormalSolidRadius = resources.getDimensionPixelSize(R.dimen.default_normal_solid_radius);
        final int defaultNormalStrokeWidth = resources.getDimensionPixelSize(R.dimen.default_normal_stroke_width);
        final int defaultSelectedSolidRadius = resources.getDimensionPixelSize(R.dimen.default_selected_solid_radius);
        final int defaultSelectedStrokeWidth = resources.getDimensionPixelSize(R.dimen.default_selected_stroke_width);

        final int defaultNormalSolidColor = ContextCompat.getColor(context, R.color.default_normal_solid_color);
        final int defaultNormalStrokeColor = ContextCompat.getColor(context, R.color.default_normal_stroke_color);
        final int defaultSelectedSolidColor = ContextCompat.getColor(context, R.color.default_selected_solid_color);
        final int defaultSelectedStrokeColor = ContextCompat.getColor(context, R.color.default_selected_stroke_color);

        TypedArray array = context.getResources().obtainAttributes(attrs, R.styleable.CircleChangeIndicator);
        mOrientation = array.getInt(R.styleable.CircleChangeIndicator_orientation, DEFAULT_ORIENTATION);
        mSpacing = array.getDimensionPixelSize(R.styleable.CircleChangeIndicator_spacing, defaultSpacing);

        mNormalSolidRadius = array.getDimensionPixelSize(R.styleable.CircleChangeIndicator_normalSolidRadius, defaultNormalSolidRadius);
        mNormalStrokeWidth = array.getDimensionPixelSize(R.styleable.CircleChangeIndicator_normalStrokeWidth, defaultNormalStrokeWidth);
        mSelectedSolidRadius = array.getDimensionPixelSize(R.styleable.CircleChangeIndicator_selectedSolidRadius, defaultSelectedSolidRadius);
        mSelectedStrokeWidth = array.getDimensionPixelSize(R.styleable.CircleChangeIndicator_selectedStrokeWidth, defaultSelectedStrokeWidth);

        mNormalSolidColor = array.getColor(R.styleable.CircleChangeIndicator_normalSolidColor, defaultNormalSolidColor);
        mNormalStrokeColor = array.getColor(R.styleable.CircleChangeIndicator_normalStrokeColor, defaultNormalStrokeColor);
        mSelectedSolidColor = array.getColor(R.styleable.CircleChangeIndicator_selectedSolidColor, defaultSelectedSolidColor);
        mSelectedStrokeColor = array.getColor(R.styleable.CircleChangeIndicator_selectedStrokeColor, defaultSelectedStrokeColor);

        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMaxRadius = Math.max(mNormalSolidRadius + mNormalStrokeWidth, mSelectedSolidRadius + mSelectedStrokeWidth);
        mCount = mViewPager.getAdapter().getCount();
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        final int diameter = 2 * mMaxRadius;
        int width, height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            int padding = getPaddingLeft() + getPaddingRight();
            if (mOrientation == HORIZONTAL) {
                width = padding + mCount * (diameter + mSpacing) - mSpacing;
            } else {
                width = padding + diameter;
            }
            if (widthMode == MeasureSpec.UNSPECIFIED) {
                width = Math.min(widthSize, width);
            }
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int padding = getPaddingTop() + getPaddingBottom();
            if (mOrientation == HORIZONTAL) {
                height = padding + diameter;
            } else {
                height = padding + mCount * (diameter + mSpacing) - mSpacing;
            }
            if (widthMode == MeasureSpec.UNSPECIFIED) {
                height = Math.min(heightSize, height);
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mViewPager == null) {
            return;
        }
        mCount = mViewPager.getAdapter().getCount();
        if (mCount < 1) {
            return;
        }
        mNormalSolidPaint.setColor(mNormalSolidColor);

        mNormalStrokePaint.setStyle(Paint.Style.STROKE);
        mNormalStrokePaint.setColor(mNormalStrokeColor);
        mNormalStrokePaint.setStrokeWidth(mNormalStrokeWidth);

        mSelectedSolidPaint.setColor(mSelectedSolidColor);

        mSelectedStrokePaint.setStyle(Paint.Style.STROKE);
        mSelectedStrokePaint.setColor(mSelectedStrokeColor);
        mSelectedStrokePaint.setStrokeWidth(mSelectedStrokeWidth);

        final int startX = getPaddingLeft() + mMaxRadius;
        final int startY = getPaddingTop() + mMaxRadius;
        final int pointSpacing = mMaxRadius * 2 + mSpacing;
        int pointX;
        int pointY;
        for (int i = 0; i < mCount; i++) {
            if (mOrientation == HORIZONTAL) {
                pointX = startX + i * pointSpacing;
                pointY = startY;
            } else {
                pointX = mMaxRadius;
                pointY = mMaxRadius + i * pointSpacing;
            }
            if (i == mCurrentPage) {
                canvas.drawCircle(pointX, pointY, mSelectedSolidRadius, mSelectedSolidPaint);
                canvas.drawCircle(pointX, pointY, mSelectedSolidRadius, mSelectedStrokePaint);
            } else {
                canvas.drawCircle(pointX, pointY, mNormalSolidRadius, mNormalSolidPaint);
                canvas.drawCircle(pointX, pointY, mNormalSolidRadius, mNormalStrokePaint);
            }
        }
    }

    @Override
    public void setupWithViewPager(@NonNull ViewPager viewPager) {
        if (viewPager == null) {
            throw new NullPointerException("ViewPager can not be a null object.");
        }
        if (viewPager.getAdapter() == null) {
            throw new NullPointerException("ViewPager must have an adapter instance.");
        }
        this.mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(this);
        invalidate();
    }

    @Override
    public void notifyDataSetChanged() {
        requestLayout();
        invalidate();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPage = position;
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SaveState saveState = new SaveState(superState);
        saveState.currentPage = mCurrentPage;
        return saveState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SaveState saveState = (SaveState) state;
        super.onRestoreInstanceState(saveState.getSuperState());
        mCurrentPage = saveState.currentPage;
        requestLayout();
    }

    static class SaveState extends BaseSavedState {

        int currentPage;

        public SaveState(Parcel source) {
            super(source);
            currentPage = source.readInt();
        }

        public SaveState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
        }

        public static final Creator<SaveState> CREATOR = new Creator<SaveState>() {
            @Override
            public SaveState createFromParcel(Parcel parcel) {
                return new SaveState(parcel);
            }

            @Override
            public SaveState[] newArray(int size) {
                return new SaveState[size];
            }
        };
    }

    public void setOrientation(@Orientation int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("Orientation must be one of HORIZONTAL and VERTICAL.");
        }
        mOrientation = orientation;
        requestLayout();
    }

    @IntDef({HORIZONTAL, VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Orientation {
    }
}
