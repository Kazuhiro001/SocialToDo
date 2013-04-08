package jp.sdnaKensyu.socialtodo;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import jp.sdnaKensyu.socialtodo.R.id;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class PlotActivity extends Activity {
	PlotView mPlotView;
	FrameLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plot);
		layout = (FrameLayout) findViewById(R.id.Frame);

		///グループ選択用
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.add("test");
		Spinner spinner = (Spinner) findViewById(id.spinner_group);
		spinner.setAdapter(adapter);

		///背景
		mPlotView = (PlotView) findViewById(R.id.plotView1);

		///タスク表示
		String projectID = "2";
		List<String> userNames = null;
		List<Task> tasks = null;
		userNames = getProjectUsers(projectID);
		for(String userName: userNames){
			try {
				tasks = getProjectTasks(projectID);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			List<Task> userTasks = filterTasksWithName(tasks, userName);
			///一ユーザーのtask描写
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			for(Task task: userTasks) {
				plotTask(task,paint);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.plot, menu);
		return true;
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
	public void plotTask(Task task, Paint paint) {
		TaskView t = new TaskView(this, task, paint);
		layout.addView(t);
	}

	private List<Task> getProjectTasks(String projectID) throws JSONException, IOException, ParseException {
		List<Task> tasks = MainActivity.myHttpConnection.getProjectTasksJson(projectID);
		return tasks;
	}

}
