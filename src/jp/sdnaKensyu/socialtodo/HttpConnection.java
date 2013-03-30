package jp.sdnaKensyu.socialtodo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


public class HttpConnection {
	private static HttpClient httpClient = new DefaultHttpClient();

	public static String httpGet(String url,Map<String,String> requestParams)
	{
		HttpGet httpGet = null;
		try
		{
			String tmpUrl = "";
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
				tmpUrl = temp;
			}
			else
			{
				tmpUrl = url;
			}
			httpGet = new HttpGet(tmpUrl);
			HttpResponse response = httpClient.execute(httpGet);
			if(200 != response.getStatusLine().getStatusCode())
			{
				return String.valueOf(response.getStatusLine().getStatusCode());
			}
			HttpEntity httpEntity = response.getEntity();
//			return EntityUtils.toString(httpEntity);
			return String.valueOf(response.getStatusLine().getStatusCode());
		}
		catch (ClientProtocolException e) {
            e.printStackTrace();
        }
		catch (IOException e) {
            e.printStackTrace();
        }
		finally {
            if (httpGet != null) {
                httpGet.abort();
            }
        }
		return null;
	}
	@SuppressWarnings("deprecation")
	public static String httpPost(String url,Map<String,String> requestParams)
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
            HttpResponse response = httpClient.execute(httpPost);
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
}
