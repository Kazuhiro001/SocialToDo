package jp.sdnaKensyu.socialtodo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class PlotActivity extends Activity {
	PlotView mPlotView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plot);
		mPlotView = (PlotView) findViewById(R.id.plotView1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.plot, menu);
		return true;
	}

}
