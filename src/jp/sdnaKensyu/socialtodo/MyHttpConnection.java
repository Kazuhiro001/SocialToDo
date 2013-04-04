package jp.sdnaKensyu.socialtodo;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.net.http.AndroidHttpClient;
import android.util.Log;
import android.util.Xml;

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

	public String getProjectTasks(String projectID) {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("projectId", projectID);
		return httpGet("GetProjectInformation", requestParams);
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
}
