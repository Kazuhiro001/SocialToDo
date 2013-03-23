package jp.sdnaKensyu.socialtodo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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

		///背景描写
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		canvas.drawLine(10, 10, 180, 10, paint);
		canvas.drawLine(10, 10, 10, 180, paint);

		///task描写
		List<Task> tasks = null;
		paint.setColor(Color.RED);
		try {
			tasks = getTasks();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		for(Task task: tasks) {
			plotTask(task, canvas, paint);
		}
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
			canvas.drawCircle(x, y, 3, paint);
		}
	}

	private List<Task> getTasks() throws ParseException {
		List<Task> tasks = new ArrayList<Task>();
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		tasks.add(new Task("name1", df.parse("2013/03/21"), 3));
		tasks.add(new Task("name2", df.parse("2013/03/22"), 2));
		tasks.add(new Task("name3", df.parse("2013/03/23"), 1));
		return tasks;
	}
}