package jp.sdnaKensyu.socialtodo;

import jp.sdnaKensyu.socialtodo.R.id;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class PlotActivity extends Activity {
	PlotView mPlotView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plot);

		///グループ選択用
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.add("test");
		Spinner spinner = (Spinner) findViewById(id.spinner_group);
		spinner.setAdapter(adapter);


		mPlotView = (PlotView) findViewById(R.id.plotView1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.plot, menu);
		return true;
	}

}
