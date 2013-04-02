package jp.sdnaKensyu.socialtodo;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpConnection;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import jp.sdnaKensyu.socialtodo.R.id;
import android.app.Activity;

public class LoginActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Button button = (Button) findViewById(id.buttonForLogin);
	    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("応答コード確認");
		alertDialogBuilder.setPositiveButton("確認",
				new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
		alertDialogBuilder.setCancelable(true);

		 button.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
				Log.d("TAG", "loginを押した");
				EditText edittext1 = (EditText) findViewById(id.editTextForName);
				EditText edittext2 = (EditText) findViewById(id.editTextForPassword);
				alertDialogBuilder.setMessage("コード確認：" + MainActivity.myHttpConnection.login("kazuhiro", "test"));
			    AlertDialog alertDialog = alertDialogBuilder.create();
			    alertDialog.show();
			}
		});

		button = (Button) findViewById(id.buttonForCheck);
			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					alertDialogBuilder.setMessage("コード確認：" + MainActivity.myHttpConnection.getStatus());
				    AlertDialog alertDialog = alertDialogBuilder.create();
				    alertDialog.show();
			}
		});

		button = (Button) findViewById(id.buttonToMain);
			button.setOnClickListener(new View.OnClickListener() {
				 @Override
				 public void onClick(View v) {
					 // ボタンがクリックされた時に呼び出されます
					 Intent intent = new Intent(LoginActivity.this,
							 MainActivity.class);
					 startActivity(intent);
				}
			});


	}
}
