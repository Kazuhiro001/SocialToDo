package jp.sdnaKensyu.socialtodo;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.sdnaKensyu.socialtodo.R.id;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.net.http.AndroidHttpClient;
import android.util.Log;
import android.util.Xml;
import android.widget.EditText;
import android.widget.Spinner;

public class MyHttpConnection implements Runnable {
	private AndroidHttpClient mHttpClient;
	private HttpContext mHttpContext;
	private CookieStore mCookieStore;
	private String mHttpData;
	private String mHttpStatus;
	private String mUrl;
	private String mRequestUrl;
	private HttpUriRequest mRequest;

	public MyHttpConnection() {
		mUrl = "http://yoshio916.s349.xrea.com/api/v1/";
		mCookieStore = new BasicCookieStore();
		mHttpContext = new BasicHttpContext();
		mHttpContext.setAttribute(ClientContext.COOKIE_STORE, mCookieStore);
		mHttpClient = AndroidHttpClient.newInstance("Android UserAgent");
	}

	public String login(String name, String password) {
		Log.d("TAG", "loginの中");
		Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("name", name);
        requestParams.put("password", password);
        return httpGet("login", requestParams);
	}

	public ArrayList<Task> getProjectTasks(String projectID) throws IOException {
		ArrayList<Task> tasks = new ArrayList<Task>();
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("projectId", projectID);
		String xml = httpGet("GetTaskListInformation", requestParams);
		Log.d("InGetProjectTasks", "xml=" + xml);
		System.out.println(xml);
		InputStream fin = null;
		Reader in = null;
	    StringBuilder buffer = new StringBuilder();

		try {
			fin = new ByteArrayInputStream(xml.getBytes());
			in = new InputStreamReader(fin, "UTF-8");
		    int c;
		    c = in.read();
		    if (c != -1) {
		    	Log.d("C", String.valueOf((char) c));
		        if (c != 0xFEFF && c != 0xEFBBBF && c != 0xFFFE && c != 0xEFBFBE) { buffer.append((char) c); }
		        while ((c = in.read()) != -1) { buffer.append((char) c); }
		    }
		} finally {
		    if (in != null) {
		        in.close();
		    } else if (fin != null) {
		        fin.close();
		    }
		}
		xml = buffer.toString();
		Log.d("InGetProjectTasks", "xml=" + xml);

		//xmlの解析
		//taskの入力用要素
		String name = null;
		String deadLine = null;
		int priority = 0;
		String infomation = null;
		int group = Integer.valueOf(projectID);
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(xml));
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					if ("task_name".equals(parser.getName())) {
						eventType = parser.next();
						if (eventType == XmlPullParser.TEXT) {
							Log.d("TAG", "task_name" + "=" + parser.getText());
							name = parser.getText();
						}
					} else if ("task_contents".equals(parser.getName())) {
						eventType = parser.next();
						if (eventType == XmlPullParser.TEXT) {
							Log.d("TAG", "task_contents" + "=" + parser.getText());
							infomation = parser.getText();
						}
					} else if ("inportant_degree".equals(parser.getName())) {
						eventType = parser.next();
						if (eventType == XmlPullParser.TEXT) {
							priority = Integer.valueOf(parser.getText());
						}
					} else if ("dead_line_time".equals(parser.getName())) {
						eventType = parser.next();
						if (eventType == XmlPullParser.TEXT) {
							deadLine = parser.getText();
						}
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					if ("/item".equals(parser.getName())) {
						Log.d("TAG", parser.getName());
						Task task = new Task(null, null, 0, null);
						task.setName(name);
						task.setDeadLine(deadLine);
						task.setPriority(priority);
						task.setGroup(group);
						task.setInfomation(infomation);
						tasks.add(task);
					}
				}
				eventType = parser.next();
			}
		} catch (IllegalStateException e) {
			Log.e("TAG", e.toString());
		} catch (IOException e) {
			Log.e("TAG", e.toString());
		} catch (XmlPullParserException e) {
			Log.e("TAG", e.toString());
		}
		return tasks;
	}

	public String httpGet(String request,Map<String,String> requestParams)
	{
		Log.d("TAG", "httpGet");
		String url = mUrl + "/" + request + "/";
		StringBuilder builder = new StringBuilder(url);
		if(requestParams != null)
		{
			for(Map.Entry<String, String> entry: requestParams.entrySet())
			{
				builder.append((String) entry.getKey());
				builder.append("/");
				builder.append((String) entry.getValue());
				builder.append("/");
			}
			String temp = builder.toString();
			temp = temp.substring(0, temp.length() - 1);
			mRequestUrl = temp;
		}
		else
		{
			mRequestUrl = url;
		}
		URI uri = null;
		try {
			uri = new URI(mRequestUrl);
		} catch (URISyntaxException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		mRequest = new HttpGet(uri);

		Log.d("TAG", "getProjectTasks:mRequstUrl = "+ mRequestUrl);
		Thread t = new Thread(this);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return mHttpData;
	}

	public String httpPost(String url,Map<String,String> requestParams)
	{
		try
		{
			List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                params.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
            }
            mRequest = new HttpPost(url);
            ((HttpResponse) mRequest).setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            Thread t = new Thread(this);
            t.start();
            return mHttpData;
		}
		catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
		return null;
	}

	@Override
	public void run() {
		Log.d("TAG", "run");
		try
		{
			HttpResponse response = mHttpClient.execute(mRequest, mHttpContext);
			List<Cookie> cookies = mCookieStore.getCookies();
			if( !cookies.isEmpty() ){
			    for (Cookie cookie : cookies){
			        String cookieString = cookie.getName() + " : " + cookie.getValue();
			        Log.d("InRun", cookieString);
			    }
			}
			if(200 != response.getStatusLine().getStatusCode())
			{
				mHttpData = String.valueOf(response.getStatusLine().getStatusCode());
				mHttpStatus = String.valueOf(response.getStatusLine().getStatusCode());
			} else {
				HttpEntity httpEntity = response.getEntity();
				mHttpData = EntityUtils.toString(httpEntity);
				mHttpStatus = String.valueOf(response.getStatusLine().getStatusCode());
			}
		}
		catch (ClientProtocolException e) {
            e.printStackTrace();
        }
		catch (IOException e) {
            e.printStackTrace();
        }
		finally {
            if (mRequest != null) {
                mRequest.abort();
            }
        }
	}

	public String getStatus() {
		return mHttpStatus;
	}

	public String getUserInformation() {
		return httpGet("getUserInformation", null);
	}

	///入力したxmlから指定したタグのデータをリストで取得する
	///xml 入力したxml
	///key 指定したタグ
	///return 指定したタグのデータ
	public ArrayList<String> parseXml(String xml, String key) {
		ArrayList<String> list = new ArrayList<String>();
		if (mHttpData == null) {
			return null;
		}
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(xml));
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					if (key.equals(parser.getName())) {
						eventType = parser.next();
								if (eventType == XmlPullParser.TEXT) {
									Log.d("TAG", key + "=" + parser.getText());
									list.add(parser.getText());
								}
					}
				}
				eventType = parser.next();
			}
		} catch (IllegalStateException e) {
			Log.e("TAG", e.toString());
		} catch (IOException e) {
			Log.e("TAG", e.toString());
		} catch (XmlPullParserException e) {
			Log.e("TAG", e.toString());
		}

		return list;
	}

	public String httpGetJson(String request,Map<String,String> requestParams)
	{
		Log.d("TAG", "httpGet");
		String url = mUrl + "/" + request + "/";
		StringBuilder builder = new StringBuilder(url);
		if(requestParams != null)
		{
			for(Map.Entry<String, String> entry: requestParams.entrySet())
			{
				builder.append((String) entry.getKey());
				builder.append("/");
				builder.append((String) entry.getValue());
				builder.append("/");
			}
			String temp = builder.toString();
			temp = temp.substring(0, temp.length() - 1);
			mRequestUrl = temp;
		}
		else
		{
			mRequestUrl = url;
		}
		mRequestUrl = mRequestUrl + "/format/json/";
		URI uri = null;
		try {
			uri = new URI(mRequestUrl);
		} catch (URISyntaxException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		mRequest = new HttpGet(uri);

		Log.d("TAG", "getProjectTasks:mRequstUrl = "+ mRequestUrl);
		Thread t = new Thread(this);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return mHttpData;
	}

	public ArrayList<Task> getProjectTasksJson(String projectID) throws IOException, JSONException {
		ArrayList<Task> tasks = new ArrayList<Task>();
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("projectId", projectID);
		String json = httpGetJson("GetTaskListInformation", requestParams);
		Log.d("InGetProjectTasks", "json=" + json);
		System.out.println(json);

		//jsonの解析
		//taskの入力用要素
		String name = null;
		String deadLine = null;
		int priority = 0;
		String infomation = null;
		int group = Integer.valueOf(projectID);
		try {
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jo = jsonArray.getJSONObject(i);
				name = jo.getString("task_name");
				deadLine = jo.getString("task_dead_line");
				priority = jo.getInt("task_important_degree");
				infomation = jo.getString("task_contents");
				Task task = new Task(null, null, 0, null);
				task.setName(name);
				task.setDeadLine(deadLine);
				task.setPriority(priority);
				task.setGroup(group);
				task.setInfomation(infomation);
				tasks.add(task);
			}
		} catch (IllegalStateException e) {
			Log.e("TAG", e.toString());
		}
		return tasks;
	}
}
