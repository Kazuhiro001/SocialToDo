package jp.sdnaKensyu.socialtodo;

import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
//import android.view.Menu;
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

public class TaskEntryActivity extends Activity {
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
		// アイテムを追加します
		adapter.add("グループA");
		adapter.add("グループB");
		adapter.add("グループC");
		spinner = (Spinner) findViewById(id.spinnerForGroup);
		spinner.setAdapter(adapter);
		
		////////////////////////////////////////////////////////////////
		///////////登録ボタン///////////////////////////////////////////
		////////////////////////////////////////////////////////////////
		Button button = (Button) findViewById(id.buttonToEntry);
		button.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
				 // ボタンがクリックされた時に呼び出されます
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
