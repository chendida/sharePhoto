package com.zq.dynamicphoto.ui.widge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2018/8/1.
 */

public class StrokeTextView extends AppCompatTextView {
    private static final String TAG = "StrokeTextView";
    private OutlineTextView borderText = null;///用于描边的TextView

    public StrokeTextView(Context context) {
        super(context);
        borderText = new OutlineTextView(context);
        setTextSize(TypedValue.COMPLEX_UNIT_PX,getTextSize());
    }

    public StrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        borderText = new OutlineTextView(context,attrs);
    }

    public StrokeTextView(Context context, AttributeSet attrs,
                          int defStyle) {
        super(context, attrs, defStyle);
        borderText = new OutlineTextView(context,attrs,defStyle);
        //borderText.setTextSize(TypedValue.COMPLEX_UNIT_PX,getTextSize());
    }


    public void setOutLine(int color, float degree, int alpha) {
        TextPaint tp1 = borderText.getPaint();
        tp1.setStrokeWidth(degree);                                  //设置描边宽度
        tp1.setStyle(Paint.Style.STROKE);                             //对文字只描边
        borderText.setTextColor(Color.argb(alpha,Color.red(color),Color.green(color),Color.blue(color)));  //设置描边颜色
        borderText.setGravity(getGravity());
        postInvalidate();
    }

    @Override
    public void setLayoutParams (ViewGroup.LayoutParams params){
        super.setLayoutParams(params);
        borderText.setLayoutParams(params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        CharSequence tt = borderText.getText();

        //两个TextView上的文字必须一致
        if(tt== null || !tt.equals(this.getText())){
            borderText.setText(getText());
            this.postInvalidate();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        borderText.measure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout (boolean changed, int left, int top, int right, int bottom){
        super.onLayout(changed, left, top, right, bottom);
        borderText.layout(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        borderText.draw(canvas);
        super.onDraw(canvas);
    }

    private float spacing = Spacing.NORMAL;
    private CharSequence originalText = "";

    /**
     * 设置间距
     *
     * @param spacing
     */

    public void setSpacing(float spacing) {
        this.spacing = spacing;
        borderText.setSpacing(spacing);
        applySpacing();
    }

    public float getSpacing() {
        return spacing;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        Log.i(TAG,"setText(CharSequence text, BufferType type)");
        originalText = text;
        applySpacing();
    }

    @Override
    public CharSequence getText() {
        return originalText;
    }

    @Override
    public void addOnLayoutChangeListener(OnLayoutChangeListener listener) {
        super.addOnLayoutChangeListener(listener);

    }

    private void applySpacing() {
        if (TextUtils.isEmpty(originalText)) return;
        Log.i(TAG,"applySpacing()"+"originalText = " + originalText);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < originalText.length(); i++) {
            builder.append(originalText.charAt(i));
            if (i + 1 < originalText.length()) {
                // \u00A0 不间断空格
                // 追加空格
                builder.append("\u00A0");
            }
        }
        // 我们也是通过这个，去设置空格
        SpannableString finalText = new SpannableString(builder.toString());
        if (builder.toString().length() > 1) { // 如果当前TextView内容长度大于1，则进行空格添加
            for (int i = 1; i < builder.toString().length(); i += 2) { // 小demo：100  1 0 0
                // 按照x轴等比例进行缩放 通过我们设置的字间距+1除以10进行等比缩放
                finalText.setSpan(new ScaleXSpan((spacing + 1) / length()), i, i + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        //this.text = finalText.toString();
        super.setText(finalText, TextView.BufferType.SPANNABLE);
    }

    public void setLayout(int left, int top, int right, int bottom) {
        borderText.layout(left,top,right,bottom);
        borderText.postInvalidate();
    }

    public void setPlace(int place) {
        setGravity(place);
        borderText.setGravity(place);
    }

    public void setTypeFont(Typeface typeFont) {
        setTypeface(typeFont);
        borderText.setTypeface(typeFont);
    }

    public void changeTextSize(int complexUnitPx, int process) {
        setTextSize(complexUnitPx, process);
        borderText.setTextSize(complexUnitPx,process);
    }

    public void changeTextContent(String content) {
        setText(content);
        borderText.setText(content);
        postInvalidate();
    }


    public class Spacing {
        public final static float NORMAL = 0;
    }
}
