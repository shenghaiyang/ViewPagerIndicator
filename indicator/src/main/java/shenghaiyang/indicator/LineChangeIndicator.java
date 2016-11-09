package shenghaiyang.indicator;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author shenghaiyang
 */
public class LineChangeIndicator extends View implements PagerIndicator {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private static final int DEFAULT_ORIENTATION = HORIZONTAL;

    private ViewPager mViewPager;
    private int mCount;
    private int mCurrentPage;

    private int mOrientation;
    private int mSpacing;

    private int mLineWidth;
    private int mLineHeight;
    private int mNormalColor;
    private int mSelectedColor;

    private static final Paint mNormalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final Paint mSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public LineChangeIndicator(Context context) {
        this(context, null);
    }

    public LineChangeIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChangeIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final Resources resources = context.getResources();
        final int defaultSpacing = resources.getDimensionPixelSize(R.dimen.default_spacing);
        final int defaultLineWidth = resources.getDimensionPixelSize(R.dimen.default_line_width);
        final int defaultLineHeight = resources.getDimensionPixelSize(R.dimen.default_line_height);
        final int defaultNormalColor = ContextCompat.getColor(context, R.color.default_normal_color);
        final int defaultSelectedColor = ContextCompat.getColor(context, R.color.default_selected_color);

        TypedArray array = context.getResources().obtainAttributes(attrs, R.styleable.LineChangeIndicator);
        mOrientation = array.getInt(R.styleable.LineChangeIndicator_orientation, DEFAULT_ORIENTATION);
        mSpacing = array.getDimensionPixelSize(R.styleable.LineChangeIndicator_spacing, defaultSpacing);
        mLineWidth = array.getDimensionPixelSize(R.styleable.LineChangeIndicator_lineWidth, defaultLineWidth);
        mLineHeight = array.getDimensionPixelSize(R.styleable.LineChangeIndicator_lineHeight, defaultLineHeight);
        mNormalColor = array.getColor(R.styleable.LineChangeIndicator_normalColor, defaultNormalColor);
        mSelectedColor = array.getColor(R.styleable.LineChangeIndicator_selectedColor, defaultSelectedColor);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mCount = mViewPager.getAdapter().getCount();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width, height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            int padding = getPaddingLeft() + getPaddingRight();
            if (mOrientation == HORIZONTAL) {
                width = padding + mCount * (mLineWidth + mSpacing) - mSpacing;
            } else {
                width = padding + mLineWidth;
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
                height = padding + mLineHeight;
            } else {
                height = padding + mCount * (mLineHeight + mSpacing) - mSpacing;
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
        mNormalPaint.setColor(mNormalColor);
        mSelectedPaint.setColor(mSelectedColor);

        final int startLeft = getPaddingLeft();
        final int startTop = getPaddingTop();
        int left, top, right, bottom;
        for (int i = 0; i < mCount; i++) {
            if (mOrientation == HORIZONTAL) {
                left = startLeft + i * (mLineWidth + mSpacing);
                right = left + mLineWidth;
                top = startTop;
                bottom = top + mLineHeight;
            } else {
                left = startLeft;
                right = left + mLineWidth;
                top = startTop + i * (mLineHeight + mSpacing);
                bottom = top + mLineHeight;
            }
            if (i == mCurrentPage) {
                canvas.drawRect(left, top, right, bottom, mSelectedPaint);
            } else {
                canvas.drawRect(left, top, right, bottom, mNormalPaint);
            }
        }
    }

    @Override
    public void setupWithViewPager(ViewPager viewPager) {
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
        CircleChangeIndicator.SaveState saveState = new CircleChangeIndicator.SaveState(superState);
        saveState.currentPage = mCurrentPage;
        return saveState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        CircleChangeIndicator.SaveState saveState = (CircleChangeIndicator.SaveState) state;
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

        public static final Creator<CircleChangeIndicator.SaveState> CREATOR = new Creator<CircleChangeIndicator.SaveState>() {
            @Override
            public CircleChangeIndicator.SaveState createFromParcel(Parcel parcel) {
                return new CircleChangeIndicator.SaveState(parcel);
            }

            @Override
            public CircleChangeIndicator.SaveState[] newArray(int size) {
                return new CircleChangeIndicator.SaveState[size];
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
