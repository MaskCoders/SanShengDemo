package hstt.update;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;

public class HttpGetUtils {
	
	
	
	/**
	 * Get方法获取数据
	 * @param url
	 * @return null 网络故障，或者其他
	 */
//	public static String executeHttpGet(String url) {
		
	public static String executeHttpGet(Context context,String url) {
	
		
		
//		
//		if (!GlobalParameters.getInstance(context).getNetWorkStatus()) {
//			return null;
//		}
		System.out.println("http get data... =>" + url);
		
		 HttpGet httpRequest = new HttpGet(url);

         try {
        	 
        	

            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpRequest);      
            if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                String strResult = EntityUtils.toString(httpResponse.getEntity());

                return strResult;

            } else {

                return null;

            }

         } catch(Exception e) {
        	 e.printStackTrace();
         }
         return null;	
	
	}
	
	
	
	/**
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String executeHttpPost(Context context,String url,Map<String,String> params) {
		//网络判断
		if (!GlobalParameters.getInstance(context).getNetWorkStatus()) {	
			return null;
		}
		
		DefaultHttpClient httpclient = new DefaultHttpClient();  
        String body = null;  
          
          
        HttpPost post = postForm(url, params);  
          
        body = invoke(httpclient, post);  
          
        httpclient.getConnectionManager().shutdown();  
          
        return body;  
	}
	
	
	private static HttpPost postForm(String url, Map<String, String> params){
		
		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList <NameValuePair>();
		
		Set<String> keySet = params.keySet();
		for(String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
		}
		
		try {

//			httpost.setEntity(new UrlEncodedFormEntity(nvps, "GB2312"));
			httpost.setEntity(new UrlEncodedFormEntity(nvps, "GB18030"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return httpost;
	}

	
	
	private static String invoke(DefaultHttpClient httpclient,
			HttpUriRequest httpost) {
		
		HttpResponse response = sendRequest(httpclient, httpost);
		String body = paseResponse(response);
		
		return body;
	}

	private static String paseResponse(HttpResponse response) {
		
//		HttpEntity entity = response.getEntity();
		
		
//		String charset = EntityUtils.getContentCharSet(entity);
	
		
		String body = null;
		try {
			HttpEntity entity = response.getEntity();
			String charset = EntityUtils.getContentCharSet(entity);
			
			body = EntityUtils.toString(entity);
		
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return body;
	}

	private static HttpResponse sendRequest(DefaultHttpClient httpclient,
			HttpUriRequest httpost) {
		
		HttpResponse response = null;
		
		try {
			response = httpclient.execute(httpost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	
	
	
	
	
	 

	 
}
