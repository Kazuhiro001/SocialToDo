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
import android.view.WindowManager;
import android.widget.LinearLayout;

@SuppressLint("SimpleDateFormat")
public class PlotView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
	boolean isLoop = true;

	public PlotView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
	}

	@Override
	public void run() {
		///画面初期化
		Canvas canvas = getHolder().lockCanvas();
		if (canvas != null) {
			canvas.drawColor(Color.WHITE);
		}

		Paint paint = new Paint();

		///背景描写
		paint.setColor(Color.BLACK);
		canvas.drawLine(10, 10, findViewById(R.id.plotView1).getWidth()-10, 10, paint);
		canvas.drawLine(10, 10, 10, findViewById(R.id.plotView1).getHeight()-10, paint);

		getHolder().unlockCanvasAndPost(canvas);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		isLoop = false;
	}

	private void plotTask(Task task, Canvas canvas, Paint paint) {
		//plotする位置を決める
		int x = task.getPriority()*50;
		int y = x;
		if (canvas != null) {
			canvas.drawCircle(x, y, 10, paint);
		}
	}
}