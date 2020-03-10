package com.example.vtextview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by kxyu on 2020-02-20
 * 文字纵向排版
 */
public class git  extends View {

 /** view 宽度 **/
 private float mViewWidth;
 /** view 高度 **/
 private float mViewHeight;
 /** 字体 大小 **/
 private float fontSize;

 /** 纵向文本真实高度 **/
 float realw = 0;

 /** 英文个数 **/
 private int englishCount = 0;
 /** 中文个数 **/
 private int hCount = 0;

 /**
  * 最大字体大小
  */
 float maxFontSize;

 int CHAR_TYPE_H = 0;  // 需要一字一行的横向文字类型，中文或其它语种，看具体业务
 int CHAR_TYPE_V = 1;  // 需要竖向的文字类型，英文，可能有的需求也会包含数字等

 Map<String, Integer> strMap = new LinkedHashMap();  // 存储字符组，要选择有序容器，保留字符串中原本的字符顺序
 private Paint mPaint;
 private String text = "";

 public VerticalTextView(Context context) {
  this(context,null);
 }

 public VerticalTextView(Context context, AttributeSet attrs) {
  this(context, attrs,0);
 }

 public VerticalTextView(Context context, AttributeSet attrs, int defStyleAttr) {
  super(context, attrs, defStyleAttr);
  mPaint = new Paint();
 }

 public void setText(String text) {
  this.text = text;
  mPaint.setColor(Color.WHITE);
  mPaint.setStyle(Paint.Style.STROKE);//设置画笔的样式
  mPaint.setStrokeCap(Paint.Cap.BUTT);//线帽
  mPaint.setStrokeJoin(Paint.Join.BEVEL);
  mPaint.setTextAlign(Paint.Align.CENTER);

  calculatText();
  requestLayout();
 }
 private float getFontSize(float size, String txt){
  if(TextUtils.isEmpty(txt)){
   return size;
  }
   float fontH;

   mPaint.setTextSize(size);
   Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
   fontH = fontMetrics.bottom - fontMetrics.top;
   realw = (mPaint.measureText(txt)/txt.length()) * englishCount + fontH*hCount;
   if(realw > mViewHeight){
      return getFontSize(size - 1, txt);
   } else {
//    Log.i("kxyu_s"," realw  :  "+realw+"  mViewHeight : "+mViewHeight+"  font size : "+size+"  max ： "+maxFontSize);
    return size > maxFontSize? maxFontSize : size;
   }
 }

 @Override
 protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
  super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  calculatText();
  mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
  mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
  maxFontSize = mViewWidth * 3 / 5;
  fontSize = mViewHeight / text.length();

  fontSize = getFontSize(fontSize , text);
//  Log.i("kxyu_s",text+ "    =================== onMeasure =========  "+fontSize);
 }


 @Override
 protected void onDraw(Canvas canvas) {
  super.onDraw(canvas);
  mPaint.setTextSize(fontSize);
//  Log.i("kxyu_s",text+"    -------------   onDraw  -----------  "+fontSize);
  Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();

  /** 字体高度 **/
  float fontHeight = fontMetrics.descent - fontMetrics.ascent;

  /** 文字基点坐标**/
  float x ,y;

//  realw = (mPaint.measureText(text)/text.length()) * englishCount + (fontMetrics.bottom - fontMetrics.top)*hCount;
  y = (mViewHeight - realw)/2 + 2*fontMetrics.bottom - fontMetrics.descent - fontMetrics.top ;
  x = mViewWidth/2;

  Paint paint = new Paint();
  paint.setStrokeWidth(5);
  paint.setStyle(Paint.Style.STROKE);
  paint.setColor(Color.RED);


  for (Map.Entry<String, Integer> entry : strMap.entrySet()) {
    String s1 = entry.getKey();
    char[] chars = text.toCharArray();
    Integer type = entry.getValue();
    if (type == CHAR_TYPE_H) {
      chars = s1.toCharArray();
      for (int i = 0 ; i < chars.length; i ++){
       canvas.drawPoint( x, y, paint);
        canvas.drawText(chars[i]+"", x, y, mPaint);
        if( i != chars.length -1){
         /** 下一个文字 基点y坐标 **/
         y += fontHeight;
        }
      }
     }else {
        float w = mPaint.measureText(entry.getKey()) + fontHeight/2;
        y += w/2;
        canvas.drawPoint( x, y, paint);
        drawAngleText(canvas, entry.getKey(), mViewWidth/2 - fontHeight/4, y , mPaint, 90);
        y += w/2 + fontHeight/2;
     }
  }
 }

 void drawAngleText(Canvas canvas ,String text , float x ,float y,Paint paint ,float angle){
  if(angle != 0){
   canvas.rotate(angle, x, y);
  }
  canvas.drawText(text, x, y, paint);
  if(angle != 0){
   canvas.rotate(-angle, x, y);
  }
 }

 public static boolean isLetter(String str){
  java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[a-zA-Z0-9.]+");
  java.util.regex.Matcher m = pattern.matcher(str);
  return m.matches();
 }

 public void calculatText() {
  englishCount = 0;
  hCount = 0;
  strMap.clear();

  char[] chars = text.toCharArray();
  StringBuilder s = new StringBuilder();
  int lastCharType = CHAR_TYPE_H;  // 上一个字符的类型，这里设置默认值
  int currentCharType = CHAR_TYPE_H;  // 记录遍历时字符的类型，这里设置默认值
  for(int i = 0; i < chars.length; i++) {
   /** 判断当前字符的类型 **/
   if (isLetter(chars[i]+"")) {
    currentCharType = CHAR_TYPE_V;
    englishCount += 1;
   }else{
    currentCharType = CHAR_TYPE_H;
    hCount += 1;
   }

   /** 是否与上一个字符类型相同，如果不同则截断，形成字符组，如果相同则不做处理 **/
   if (i != 0 && currentCharType != lastCharType) {
    strMap.put(s.toString(), lastCharType);
    s = new StringBuilder();
    s.append(chars[i]);
   } else {
    s.append(chars[i]);
   }
   /** 更新lastCharType **/
   lastCharType = currentCharType;
   if(i == chars.length -1 && !strMap.keySet().contains(s)){
    strMap.put(s.toString(), currentCharType);
   }
  }
 }

}