package jp.sdnaKensyu.socialtodo;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

@SuppressLint("SimpleDateFormat")
public class PlotView extends ViewGroup {
	boolean isLoop = true;

	public PlotView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false);
	}
	   @Override
	   public void addView(View view) {
	        super.addView(view);
	        int currentScreen = -1;
	     final int index = indexOfChild(view);
	       if (index > currentScreen) {
	         if (currentScreen > 0) {
	             view.setVisibility(View.GONE);//★非表示
	            }
	           currentScreen = index;
	          view.setVisibility(View.VISIBLE);//★表示
	      }

	    }
	    @Override
   protected void onLayout(boolean changed, int l, int t, int r, int b) {
      // TODO Auto-generated method stub
	  final int count = getChildCount();
      final int left = getLeft();
      final int top = getTop();
      final int right = getRight();
      final int bottom = getBottom();
      for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
          if (view.getVisibility() != View.GONE) {
                view.layout(100, 100, right, bottom);
          }
       }
        invalidate();
   }

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO 自動生成されたメソッド・スタブ
		super.onDraw(canvas);
		Paint paint = new Paint();
		///背景描写
		paint.setColor(Color.BLACK);
		canvas.drawLine(10, 10, findViewById(R.id.plotView1).getWidth()-10, 10, paint);
		canvas.drawLine(10, 10, 10, findViewById(R.id.plotView1).getHeight()-10, paint);
	}
}