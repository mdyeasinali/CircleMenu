package yeasin.hitechwebdesign.circlemenu.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;


public class UpCircleMenuLayout extends ViewGroup {

    private int mRadius;

    public static final float DEFAULT_BANNER_WIDTH = 750.0f;
    public static final float DEFAULT_BANNER_HEIGTH = 420.0f;


    private static final float RADIO_DEFAULT_CHILD_DIMENSION = 40.0f;

    private static final float RADIO_TOP_CHILD_DIMENSION = 60.0f;


    private float RADIO_DEFAULT_CENTERITEM_DIMENSION = 1 / 3f;

    private static final float RADIO_PADDING_LAYOUT = 20;


    private static final int RADIO_MARGIN_LAYOUT = 20;

    private static final int FLINGABLE_VALUE = 300;


    private static final int NOCLICK_VALUE = 3;

    private int mFlingableValue = FLINGABLE_VALUE;

    private float mPadding;


    private double mStartAngle = 18;

    private int[] mItemImgs;

    private String[] mItemTexts;


    private int mMenuItemCount;


    private float mTmpAngle;

    private long mDownTime;

    private boolean isTouchUp = true;



    private int mCurrentPosition = 0;


    public UpCircleMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        setPadding(0, 0, 0, 0);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int resWidth = 0;
        int resHeight = 0;
        double startAngle = mStartAngle;

        double angle = 360 / 10;

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY
                || heightMode != MeasureSpec.EXACTLY) {

            resWidth = getDefaultWidth();

            resHeight = (int) (resWidth * DEFAULT_BANNER_HEIGTH /
                    DEFAULT_BANNER_WIDTH);

        } else {

            resWidth = resHeight = Math.min(width, height);
        }

        setMeasuredDimension(resWidth, resHeight);


        mRadius = Math.max(getMeasuredWidth(), getMeasuredHeight());


        final int count = getChildCount();
        // menu item
        int childSize;

        // menu item
        int childMode = MeasureSpec.EXACTLY;


        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            startAngle = startAngle % 360;
            if (startAngle > 269 && startAngle < 271 && isTouchUp) {
                mOnMenuItemClickListener.itemClick(i);
                mCurrentPosition = i;
                childSize = DensityUtil.dip2px(getContext(), RADIO_TOP_CHILD_DIMENSION);
            } else {
                childSize = DensityUtil.dip2px(getContext(), RADIO_DEFAULT_CHILD_DIMENSION);
            }
            if (child.getVisibility() == GONE) {
                continue;
            }

            int makeMeasureSpec = -1;

            makeMeasureSpec = MeasureSpec.makeMeasureSpec(childSize,
                    childMode);
            child.measure(makeMeasureSpec, makeMeasureSpec);
            startAngle += angle;
        }
        //item
        mPadding = DensityUtil.dip2px(getContext(), RADIO_MARGIN_LAYOUT);

    }

    /**
     * MenuItem
     *
     * @author zhy
     */
    public interface OnMenuItemClickListener {
        void itemClick(int pos);

        void itemCenterClick(View view);
    }

    /**
     * MenuItem
     */
    private OnMenuItemClickListener mOnMenuItemClickListener;

    /**
     *MenuItem
     * @param mOnMenuItemClickListener
     */
    public void setOnMenuItemClickListener(
            OnMenuItemClickListener mOnMenuItemClickListener) {
        this.mOnMenuItemClickListener = mOnMenuItemClickListener;
    }




    /**
     * 设置menu item
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int layoutRadius = mRadius;
        // Laying out the child views
        final int childCount = getChildCount();

        int left, top;
        // menu item
        int cWidth;

        // menu item
        float angleDelay = 360 / 10;
        // menuitem
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (mStartAngle > 269 && mStartAngle < 271 && isTouchUp) {
                cWidth = DensityUtil.dip2px(getContext(), RADIO_TOP_CHILD_DIMENSION);
                child.setSelected(true);
            } else {
                cWidth = DensityUtil.dip2px(getContext(), RADIO_DEFAULT_CHILD_DIMENSION);
                child.setSelected(false);
            }
            if (child.getVisibility() == GONE) {
                continue;
            }
            //360 360
            mStartAngle = mStartAngle % 360;

            float tmp = 0;
            tmp = layoutRadius / 2f - cWidth / 2 - mPadding;
            left = layoutRadius
                    / 2
                    + (int) Math.round(tmp
                    * Math.cos(Math.toRadians(mStartAngle)) - 1 / 2f
                    * cWidth) + DensityUtil
                    .dip2px(getContext(), 1);
            top = layoutRadius
                    / 2
                    + (int) Math.round(tmp
                    * Math.sin(Math.toRadians(mStartAngle)) - 1 / 2f * cWidth) + DensityUtil
                    .dip2px(getContext(), 8);
            child.layout(left, top, left + cWidth, top + cWidth);

            mStartAngle += angleDelay;
        }
    }

    private void backOrPre() {
        isTouchUp = true;
        float angleDelay = 360 / 10;
        if ((mStartAngle-18)%angleDelay==0){
            return;
        }
        float angle = (float)((mStartAngle-18)%36);
        if (angleDelay/2 > angle){
            mStartAngle -= angle;
        }else if (angleDelay/2<angle){
            mStartAngle = mStartAngle - angle + angleDelay;         //mStartAngle
        }
        requestLayout();
    }

    private float mLastX;
    private float mLastY;

    //dispatchTouchEvent Activity dispatchTouchEvent super.dispatchTouchEvent(ev)，

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mDownTime = System.currentTimeMillis();
                mTmpAngle = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                isTouchUp = false;
                float start = getAngle(mLastX, mLastY);

                float end = getAngle(x, y);
                if (getQuadrant(x, y) == 1 || getQuadrant(x, y) == 4) {
                    mStartAngle += end - start;
                    mTmpAngle += end - start;
                } else
                {
                    mStartAngle += start - end;
                    mTmpAngle += start - end;
                }
                if (mTmpAngle != 0) {
                    requestLayout();
                }

                mLastX = x;
                mLastY = y;

                break;
            case MotionEvent.ACTION_UP:
                backOrPre();
                break;
        }
        return super.dispatchTouchEvent(event);
    }


    /**
     *
     *
     * @param xTouch
     * @param yTouch
     * @return
     */
    private float getAngle(float xTouch, float yTouch) {
        double x = xTouch - (mRadius / 2d);
        double y = yTouch - (mRadius / 2d);
        return (float) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
    }

    /**
     *
     *
     * @param x
     * @param y
     * @return
     */
    private int getQuadrant(float x, float y) {
        int tmpX = (int) (x - mRadius / 2);
        int tmpY = (int) (y - mRadius / 2);
        if (tmpX >= 0) {
            return tmpY >= 0 ? 4 : 1;
        } else {
            return tmpY >= 0 ? 3 : 2;
        }

    }


    public void setAngle(int position) {
        float angleDelay = 360 / 10;
        if (position > mCurrentPosition) {
            mStartAngle += (mCurrentPosition - position) * angleDelay;
        } else {
            mStartAngle -= (position - mCurrentPosition) * angleDelay;
        }
        requestLayout();
    }

    /**
     *
     *
     * @param resIds
     */
    public void setMenuItemIconsAndTexts(int[] resIds) {
        mItemImgs = resIds;

        //
        if (resIds == null) {
            throw new IllegalArgumentException("IllegalArgumentException");
        }

        // mMenuCount
        mMenuItemCount = resIds == null ? 0 : resIds.length;


        addMenuItems();

    }
    /**
     *
     */
    private void addMenuItems() {

        /**
         *
         */
        for (int i = 0; i < mMenuItemCount; i++) {
            final int j = i;
            ImageView iv = new ImageView(getContext());

            if (iv != null) {
                iv.setVisibility(View.VISIBLE);
                iv.setImageResource(mItemImgs[i]);
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        isTouchUp = true;
                    }
                });
            }

            //
            addView(iv);
        }
    }

    /**
     *
     *
     * @param mFlingableValue
     */
    public void setFlingableValue(int mFlingableValue) {
        this.mFlingableValue = mFlingableValue;
    }

    /**
     *
     *
     * @param mPadding
     */
    public void setPadding(float mPadding) {
        this.mPadding = mPadding;
    }

    /**
     *
     *
     * @return
     */
    private int getDefaultWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return Math.min(outMetrics.widthPixels, outMetrics.heightPixels);
    }

}
