package com.px.dlauncher.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.logging.Logger;


public class ViewPagerIndicator extends LinearLayout {

    public static final int SHAPE_TRIANGLE = 0X111;
    public static final int SHAPE_RECTANGLE = 0X112;

    private  int mTextSize = 18;
    private  int mTextColor = 0xffa3a2a2;
    private  int mTextSelectColor = 0xffffffff;

    private int mBackgroundId = 0 ;
    private int mSelectBackgroundId = 0  ;

    private int mShapeType;
    private int mShapeWidth;
    private int maxShapeWidth;
    private int mShapeHeight;
    private float mShapeHeightRate = 1/6f;
    private float mShapeWidthRate = 1/6f;
    private String mShapeColor = "#ffffff";
    private int mPathCornerRadios = 0;
    private int mShapeStartX ;
    private int mTranslationX;
    private int mTableCount;
    private int mVisibleTableCount = 4;

    private Paint mPaint;
    private Path mPath;
    private ViewPager mViewPager;
    private PagerChangeListener pagerChangeListener;

    public ViewPagerIndicator(Context context) {
        this(context ,null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs ,0);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public void setItem (int visibleTableCount ,float shapeWidthRate ,float shapeHeightRate){
        mVisibleTableCount = visibleTableCount;
        mShapeWidthRate = shapeWidthRate;
        mShapeHeightRate =shapeHeightRate;
    }

    public void setTextTitle(String [] texts,int backgroundResId ,int selectBackgroundId ,int textSize ,int textColor, int textSelectColor) {
        mTableCount = texts.length;
        if(textSize>0){
            mTextSize = textSize;
        }
        if(textColor>0){
            mTextColor = textColor;
        }
        if(textSelectColor>0){
            mTextSelectColor = textSelectColor;
        }
        if(backgroundResId >0){
            mBackgroundId = backgroundResId;
        }
        if(selectBackgroundId >0){
            mSelectBackgroundId = selectBackgroundId;
        }
        if(texts.length>0){
            this.removeAllViews();
            for(int i = 0 ; i< texts.length ;i++){
                addView(createTextView(texts[i]));
            }
        }
        setOnItemClickListener();
        setOnItemSelectedListener();
    }

    private TextView createTextView (String s){
        TextView textView = new TextView(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT , LayoutParams.MATCH_PARENT);
        lp.width = getScreenWidth()/mVisibleTableCount;
        textView.setText(s);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(mBackgroundId);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP ,mTextSize);
        textView.setTextColor(mTextColor);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        textView.setLayoutParams(lp);
        return textView;
    }

    public void setButtonTitle(String [] buttonTexts , int backgroundResId ,int selectBackgroundId,int textSize ,int textColor , int textSelectColor){
        mTableCount = buttonTexts.length;
        mBackgroundId = backgroundResId;
        if(textSize>0){
            mTextSize = textSize;
        }
        if(textColor>0){
            mTextColor = textColor;
        }
        if(textSelectColor>0){
            mTextSelectColor = textSelectColor;
        }
        if(backgroundResId >0){
            mBackgroundId = backgroundResId;
        }
        if(selectBackgroundId >0){
            mSelectBackgroundId = selectBackgroundId;
        }
        if(buttonTexts.length >0){
            this.removeAllViews();
            for(int i =0 ; i < buttonTexts.length ; i++){
                addView(createButton(buttonTexts[i]));
            }
        }
        setOnItemClickListener();
        setOnItemSelectedListener();
    }

    private Button createButton (String s){
        Button button = new Button(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT , LayoutParams.MATCH_PARENT);
        lp.width = getScreenWidth()/mVisibleTableCount;
        button.setText(s);
        button.setGravity(Gravity.CENTER);
        button.setBackgroundResource(mBackgroundId);
        button.setTextColor(mTextColor);
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP ,mTextSize);
        button.setLayoutParams(lp);
        button.setPadding(5,5,5,5);
        button.setAllCaps(false);
        return button;
    }



    public void setImageTitle(int [] imageIds ,int backgroundResId ,int selectBackGround ) {
        mTableCount = imageIds.length;
        mBackgroundId = backgroundResId;
        mSelectBackgroundId = selectBackGround;
        if(imageIds.length>0){
            this.removeAllViews();
            for(int i:imageIds){
                addView(createImageView(i ,mBackgroundId ));
            }
        }
        setOnItemClickListener();
        setOnItemSelectedListener();
    }

    private ImageView createImageView (int resId , int backgroundResId){
        ImageView imageView = new ImageView(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT , LayoutParams.MATCH_PARENT);
        lp.width = getScreenWidth()/mVisibleTableCount;
        imageView.setImageResource(resId);
        imageView.setBackgroundResource(backgroundResId);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setPadding(5,5,5,5);
        imageView.setLayoutParams(lp);
        return imageView;
    }


    public void setPaint (String shapeColor ,int pathCornerRadios){
        if(shapeColor != null){
            mShapeColor = shapeColor;
        }
        mPathCornerRadios = pathCornerRadios;
    }

    public void setShape (int shapeType) {
        mShapeType = shapeType;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mPath = new Path();
        mPath.moveTo(0,0);
        if(mShapeType == SHAPE_RECTANGLE){
            mShapeWidth = (int) (w/mVisibleTableCount * mShapeWidthRate);
            mShapeHeight = (int) (h*mShapeHeightRate);
            mShapeStartX = w/mVisibleTableCount/2 - mShapeWidth/2;
            mPath.lineTo(mShapeWidth ,0);
            mPath.lineTo(mShapeWidth,-mShapeHeight);
            mPath.lineTo(0 ,-mShapeHeight);
            mPath.close();
        }else if(mShapeType == SHAPE_TRIANGLE){
            maxShapeWidth = (int) (getScreenWidth()/mVisibleTableCount*mShapeWidthRate);
            mShapeWidth = (int)(w/mVisibleTableCount * mShapeWidthRate);
            mShapeWidth = Math.min(maxShapeWidth , maxShapeWidth);
            mShapeHeight = mShapeWidth/2;
            mShapeStartX = w/mVisibleTableCount/2 - mShapeWidth/2;
            mPath.lineTo(mShapeWidth ,0);
            mPath.lineTo(mShapeWidth/2 , -mShapeHeight);
            mPath.close();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        mPaint.setColor(Color.parseColor(mShapeColor));
        mPaint.setPathEffect(new CornerPathEffect(mPathCornerRadios));
        canvas.save();
        canvas.translate(mShapeStartX+mTranslationX , getHeight()+mPathCornerRadios);
        canvas.drawPath(mPath ,mPaint);
        canvas.restore();;
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = getChildCount();
        if(count==0)return;
        for (int i = 0; i <count ; i++) {
            View view = getChildAt(i);
            LayoutParams lp= (LayoutParams) view.getLayoutParams();
            lp.weight = 0;
            lp.width = getScreenWidth()/mVisibleTableCount;
            view.setLayoutParams(lp);
        }
        setOnItemClickListener();
        setOnItemSelectedListener();
    }

    public interface PagerChangeListener {
        public void onPagerSelect(int position);
        public void onPagerScroll(int position, float positionOffset, int positionOffsetPixels);
        public void onPagerStateChange(int state);
    }


    public void setPagerChangeListener(PagerChangeListener pagerChangeListener){
        this.pagerChangeListener = pagerChangeListener;
    }


    public void setIndicatorScroll(int position, float positionOffset) {
        int tabWidth = getScreenWidth()/mVisibleTableCount;
        mTranslationX = (int) ((position+positionOffset)*tabWidth);
        if(position>=(mVisibleTableCount-2)&& positionOffset>0&&getChildCount()>mVisibleTableCount){
            if(mVisibleTableCount!=1){
                this.scrollTo((int) ((position-(mVisibleTableCount-2))*tabWidth + tabWidth*positionOffset),0);
            }else{
                this.scrollTo((int) (position*tabWidth+tabWidth*positionOffset),0);
            }
        }
        invalidate();
    }

    public void attachViewPager (ViewPager viewPager , int currentPosition){
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setIndicatorScroll(position,positionOffset);
                if(pagerChangeListener !=null){
                    pagerChangeListener.onPagerScroll(position,positionOffset ,positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if(pagerChangeListener!=null){
                    pagerChangeListener.onPagerSelect(position);
                }
                setSelectTextColor(position);
                setSelectImageBackground(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(pagerChangeListener != null){
                    pagerChangeListener.onPagerStateChange(state);
                }
            }
        });
        mViewPager.setCurrentItem(currentPosition);
        setSelectTextColor(currentPosition);
        setSelectImageBackground(currentPosition);
    }

    public void resetTextColor(){
        for(int i=0 ;i<getChildCount();i++){
            View view = getChildAt(i);
            if(view instanceof TextView){
                ((TextView) view).setTextColor(mTextColor);
            }
        }
    }

    private void setSelectTextColor(int position){
        resetTextColor();
        View view = getChildAt(position);
        if(view instanceof TextView){
            ((TextView) view).setTextColor(mTextSelectColor);
        }
    }

    public void resetImageBackground (){
        for(int i=0 ;i<getChildCount();i++){
            View view = getChildAt(i);
            if(view instanceof ImageView){
                ((ImageView) view).setBackgroundResource(mBackgroundId);
            }
            if(view instanceof Button){
                ((Button) view).setBackgroundResource(mBackgroundId);
            }
            if(view instanceof TextView){
                ((TextView) view).setBackgroundResource(mBackgroundId);
            }
        }
    }

    public void setSelectImageBackground (int position){
        resetImageBackground();
        View view = getChildAt(position);
        if(view instanceof ImageView){
            ((ImageView) view).setBackgroundResource(mSelectBackgroundId);
        }
        if(view instanceof Button){
            ((Button) view).setBackgroundResource(mSelectBackgroundId);
        }
        if(view instanceof TextView){
            ((TextView) view).setBackgroundResource(mSelectBackgroundId);
        }
    }

    public void setOnItemClickListener(){
        int count = getChildCount();
        for (int i = 0; i < count  ; i++) {
            final int j = i;
            View view = getChildAt(j);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }

    public void setOnItemSelectedListener(){
        int count = getChildCount();
        for (int i = 0; i < count  ; i++) {
            final int j = i;
            View view = getChildAt(j);
            view.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        try {
                            mViewPager.setCurrentItem(j);
                        }catch (Exception e){

                        }
                    }
                }
            });
        }
    }

    public int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

}
