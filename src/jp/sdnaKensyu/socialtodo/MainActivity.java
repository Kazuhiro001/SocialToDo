package jp.sdnaKensyu.socialtodo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import jp.sdnaKensyu.socialtodo.R.id;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button button = (Button) findViewById(id.buttonToTaskEntry);
		 button.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
				 // ボタンがクリックされた時に呼び出されます
				 Intent intent = new Intent(MainActivity.this,
						 TaskEntryActivity.class);
				 startActivity(intent);
			}
		});

		button = (Button) findViewById(id.buttonForLogin);
		button.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
				 // ボタンがクリックされた時に呼び出されます
				 Intent intent = new Intent(MainActivity.this,
						 LoginActivity.class);
				 startActivity(intent);
			}
		});

		button = (Button) findViewById(id.buttonToPlotView);
		button.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
				 // ボタンがクリックされた時に呼び出されます
				 Intent intent = new Intent(MainActivity.this,
						 PlotActivity.class);
				 startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}