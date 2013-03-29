package jp.sdnaKensyu.socialtodo;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
		alertDialogBuilder.setTitle("警告");
		alertDialogBuilder.setPositiveButton("確認",
				new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(LoginActivity.this,
							 MainActivity.class);
					 startActivity(intent);
				}
			});
		alertDialogBuilder.setCancelable(true);
		
		 button.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
				 URI uri = null;
				HttpClient http;
				http = new DefaultHttpClient();
				HttpParams params = http.getParams();
				HttpConnectionParams.setConnectionTimeout(params, 1000); //接続のタイムアウト
				HttpConnectionParams.setSoTimeout(params, 1000); //データ取得のタイムアウト
				EditText edittext1 = (EditText) findViewById(id.editTextForName);
				EditText edittext2 = (EditText) findViewById(id.editTextForPassword);
				try{
					uri = new URI("http://yoshio916.s349.xrea.com/api/v1/login/name/"+ edittext1.getText().toString() +"/password/"+ edittext2.getText().toString() +"/");
				}catch(URISyntaxException e ){
				}
				try{
				    HttpGet objGet   = new HttpGet(uri);
				    HttpResponse response = http.execute(objGet);
					alertDialogBuilder.setMessage("コード確認：" + response.getStatusLine().getStatusCode() + "\nメイン画面に戻ります。");
				    AlertDialog alertDialog = alertDialogBuilder.create();
				    alertDialog.show();
				}catch(IOException e){
				}
			}
		});
	}
}
