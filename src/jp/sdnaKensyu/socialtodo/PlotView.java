package jp.sdnaKensyu.socialtodo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

		String projectID = "2";
		List<String> userNames = null;
		List<Task> tasks = null;
		userNames = getProjectUsers(projectID);

		for(String userName: userNames){
			try {
				tasks = getProjectTasks(projectID);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			List<Task> userTasks = filterTasksWithName(tasks, userName);
			///一ユーザーのtask描写
			paint.setColor(Color.RED);
			for(Task task: userTasks) {
				plotTask(task, canvas, paint);
			}
		}
		getHolder().unlockCanvasAndPost(canvas);
	}

	private List<Task> filterTasksWithName(List<Task> tasks, String userName) {
		// TODO 自動生成されたメソッド・スタブ
		return tasks;
	}

	private List<String> getProjectUsers(String projectID) {
		List<String> users = new ArrayList<String>();
		users.add("testUser");
		return users;
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
		tasks.add(new Task("name1", df.parse("2013/03/21"), 3, "testUser"));
		tasks.add(new Task("name2", df.parse("2013/03/22"), 2, "testUser"));
		tasks.add(new Task("name3", df.parse("2013/03/23"), 1, "testUser"));
		return tasks;
	}

	private List<Task> getProjectTasks(String projectID) throws ParseException {
		List<Task> tasks = new ArrayList<Task>();
		Log.d("GetProjectTasks", MainActivity.myHttpConnection.getProjectTasks(projectID));
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		tasks.add(new Task("name1", df.parse("2013/03/21"), 3, "testUser"));
		tasks.add(new Task("name2", df.parse("2013/03/22"), 2, "testUser"));
		tasks.add(new Task("name3", df.parse("2013/03/23"), 1, "testUser"));
		return tasks;
	}
}