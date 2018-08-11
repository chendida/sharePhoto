package com.zq.dynamicphoto.ui.widge;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/8/10.
 */

public class OutlineTextView extends AppCompatTextView {
    public OutlineTextView(Context context) {
        super(context);
    }

    public OutlineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OutlineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setLayoutParams (ViewGroup.LayoutParams params){
        super.setLayoutParams(params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout (boolean changed, int left, int top, int right, int bottom){
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private float spacing = StrokeTextView.Spacing.NORMAL;
    private CharSequence originalText = "";

    /**
     * 设置间距
     *
     * @param spacing
     */

    public void setSpacing(float spacing) {
        this.spacing = spacing;
        applySpacing();
    }


    @Override
    public void setText(CharSequence text, BufferType type) {
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


    public class Spacing {
        public final static float NORMAL = 0;
    }
}
