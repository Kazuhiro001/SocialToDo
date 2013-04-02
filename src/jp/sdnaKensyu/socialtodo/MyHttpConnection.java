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

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
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

	public MyHttpConnection() {
		mUrl = "http://yoshio916.s349.xrea.com/api/v1/";
//		mHttpClient = AndroidHttpClient.newInstance("Android UserAgent");
		mCookieStore = new BasicCookieStore();
		mHttpContext = new BasicHttpContext();
		mHttpContext.setAttribute(ClientContext.COOKIE_STORE, mCookieStore);
		mHttpClient = AndroidHttpClient.newInstance("Android UserAgent", (Context) mHttpContext);
	}

	public String login(String name, String password) {
		Log.d("TAG", "loginの中");
		Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("name", name);
        requestParams.put("password", password);
        List<Cookie> cookies = mCookieStore.getCookies();
        if( !cookies.isEmpty() ){
            for (Cookie cookie : cookies){
                String cookieString = cookie.getName() + " : " + cookie.getValue();
                Log.d("TAG", cookieString);
            }
        }
        return httpGet("login", requestParams);
	}

	public String getProjectTasks(String projectID) {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("projectId", projectID);
		List<Cookie> cookies = mCookieStore.getCookies();
		if( !cookies.isEmpty() ){
		    for (Cookie cookie : cookies){
		        String cookieString = cookie.getName() + " : " + cookie.getValue();
		        Log.d("InGetProjectTasks", cookieString);
		    }
		}
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
		HttpPost httpPost = null;
		try
		{
			List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                params.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
            }
            httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse response = mHttpClient.execute(httpPost, mHttpContext);
            Log.d("context", mHttpContext.toString());
            if(200 != response.getStatusLine().getStatusCode())
			{
				return String.valueOf(response.getStatusLine().getStatusCode());
			}
            HttpEntity httpEntity = response.getEntity();
            return EntityUtils.toString(httpEntity);
		}
		catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpPost != null) {
                httpPost.abort();
            }
        }
		return null;
	}

	@Override
	public void run() {
		Log.d("TAG", "run");
		HttpGet httpGet = null;
		try
		{
			URI uri = new URI(mRequestUrl);
			httpGet = new HttpGet(uri);;
			HttpResponse response = mHttpClient.execute(httpGet, mHttpContext);
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
        } catch (URISyntaxException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		finally {
            if (httpGet != null) {
                httpGet.abort();
            }
        }
	}

	public String getStatus() {
		return mHttpStatus;
	}

	public String getUserInformation() {
		mRequestUrl = mUrl + "GetUserInformation/";
		Thread t = new Thread(this);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		Log.d("TAG", "loginの中");
		return mHttpData;
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
