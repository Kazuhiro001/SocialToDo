package jp.sdnaKensyu.socialtodo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import jp.sdnaKensyu.socialtodo.R.id;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.app.TimePickerDialog;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

public class TaskEntryActivity extends Activity {
	String trimOutputForGetGroup(String output){
		String str = null;
		int startIndex = output.indexOf("<project_name>")+14;
		int endIndex = output.indexOf("</project_name>");
		str = output.substring(startIndex, endIndex);
		return str;
	}

	boolean checkOutputForGetGroup(String output){
		int startIndex = output.indexOf("<project_name>")+14;
		int endIndex = output.indexOf("</project_name>");
		try{
			output.substring(startIndex, endIndex);
			return true;
		}catch(IndexOutOfBoundsException e){
			return false;
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_taskentry);

		//////////////////////////////////////////////////////
		//////////重要度のスピナー////////////////////////////
		//////////////////////////////////////////////////////
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// アイテムを追加します
		adapter.add("高");
		adapter.add("中");
		adapter.add("低");
		Spinner spinner = (Spinner) findViewById(id.spinnerForPriority);
		spinner.setAdapter(adapter);

		///////////////////////////////////////////////////////////
		/////////////////締切期日設定//////////////////////////////
		///////////////////////////////////////////////////////////
		final Calendar calendar = Calendar.getInstance();
		final int year = calendar.get(Calendar.YEAR);
		final int month = calendar.get(Calendar.MONTH);
		final int day = calendar.get(Calendar.DAY_OF_MONTH);
		final DatePickerDialog datePickerDialog = new DatePickerDialog(
				this,
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						EditText editText = (EditText) findViewById(id.editTextForDeadLineDate);
						editText.setText(
									String.valueOf(year) + "/" +
									String.valueOf(monthOfYear + 1) + "/" +
									String.valueOf(dayOfMonth));
						}
					},
					year, month, day);

		Button buttonForDeadLine = (Button) findViewById(id.buttonForDeadLineDate);
		buttonForDeadLine.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
					datePickerDialog.show();
			}
		});

		///////////////////////////////////////////////////////////
		/////////////////締切時間設定//////////////////////////////
		///////////////////////////////////////////////////////////
		final int hour = calendar.get(Calendar.HOUR_OF_DAY);
		final int minute = calendar.get(Calendar.MINUTE);
	    final TimePickerDialog timePickerDialog = new TimePickerDialog(
	    		this,
	    		new TimePickerDialog.OnTimeSetListener() {
	    			@Override
	    			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	    				EditText editText = (EditText) findViewById(id.editTextForDeadLineTime);
						editText.setText(
	    						String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
	    			}
	    		}, hour, minute, true);

		buttonForDeadLine = (Button) findViewById(id.buttonForDeadLineTime);
		buttonForDeadLine.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
				 timePickerDialog.show();
			}
		});

		///////////////////////////////////////////////////////////////
		/////////////所属グループ//////////////////////////////////////
		///////////////////////////////////////////////////////////////
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		URI uri = null;
	    //アラートダイアログをメッセージ以外作成しておく
	    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("警告");
		alertDialogBuilder.setPositiveButton("確認",
				new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(TaskEntryActivity.this,
							 MainActivity.class);
					 startActivity(intent);
				}
			});
		alertDialogBuilder.setCancelable(true);

		try{
			uri = new URI("http://yoshio916.s349.xrea.com/api/v1/GetAllProjectInformation/");
		}catch(URISyntaxException e ){
			alertDialogBuilder.setMessage("URLが正しく設定できていません。\nメイン画面に戻ります。");
	        AlertDialog alertDialog = alertDialogBuilder.create();
	        alertDialog.show();
		}
		try{
		    HttpGet objGet   = new HttpGet(uri);
	        HttpResponse response = MainActivity.http.execute(objGet);

	        if (response.getStatusLine().getStatusCode() >= 400){
	        //レスポンスが400以上であればエラー
				alertDialogBuilder.setMessage("エラーコード" + response.getStatusLine().getStatusCode() + "\nメイン画面に戻ります。");
		        AlertDialog alertDialog = alertDialogBuilder.create();
		        alertDialog.show();
	        }
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// アイテムを追加します
			InputStream objStream = response.getEntity().getContent();
            InputStreamReader objReader = new InputStreamReader(objStream);
            BufferedReader objBuf = new BufferedReader(objReader);
            StringBuilder objJson = new StringBuilder();
            String sLine;
            while((sLine = objBuf.readLine()) != null){
                objJson.append(sLine);
                if(checkOutputForGetGroup(sLine)){
                	adapter.add(trimOutputForGetGroup(sLine));
                }
            }
            objStream.close();
			spinner = (Spinner) findViewById(id.spinnerForGroup);
			spinner.setAdapter(adapter);
		}catch(IOException e){
			alertDialogBuilder.setMessage("httpへのコネクトが正しくできていません。\nメイン画面に戻ります。");
	        AlertDialog alertDialog = alertDialogBuilder.create();
	        alertDialog.show();
		}

		////////////////////////////////////////////////////////////////
		///////////登録ボタン///////////////////////////////////////////
		////////////////////////////////////////////////////////////////
		Button button = (Button) findViewById(id.buttonToEntry);
		button.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
				 //タスク作成
				 EditText editText1 = (EditText) findViewById(id.editTextForName);
				 EditText editText2 = (EditText) findViewById(id.editTextForDeadLineDate);
				 EditText editText3 = (EditText) findViewById(id.editTextForDeadLineTime);
				 Spinner spinner = (Spinner) findViewById(id.spinnerForPriority);;
				 Task task = new Task(null, null, 0,null);
				 task.setName(editText1.getText().toString());
				 task.setDeadLine(editText2.getText().toString());
				 task.setDeadLineTime(editText3.getText().toString());
				 task.setPriority(spinner.getSelectedItemPosition());
				 spinner = (Spinner) findViewById(id.spinnerForGroup);;
				 task.setGroup(spinner.getSelectedItemPosition());
				 editText1 = (EditText) findViewById(id.editTextForInfo);
				 task.setInfomation(editText1.getText().toString());
				 //タスクを放り込む
				 
			}
		});

		//////////////////////////////////////////////////////
		//////////メインに戻るボタン//////////////////////////
		//////////////////////////////////////////////////////
		button = (Button) findViewById(id.buttonToMain);
		button.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
				 // ボタンがクリックされた時に呼び出されます
				 Intent intent = new Intent(TaskEntryActivity.this,
						 MainActivity.class);
				 startActivity(intent);
			}
		});
	}
}



