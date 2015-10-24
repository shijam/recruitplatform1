/**
 * 文件名：HttpServer
 * 创建时间：2011-11-14 09:32:31
 * @author Jonze
 * @version 1.0.0
 */
package flytv.qaonline.http;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Proxy;
import flytv.qaonline.image.BitmapUtils;

/**
 *	@author Jonze
 *	@version 1.0.0
 *	@category 基于http协议的普通网络服务类
 */
public class HttpServer {
	private static final int CONNECTTIMEOUT_TIME = 10*1000;//http timeout time
	public static final String HTTPSTATE_SUCCESS = "http_resp_ok";// resp code of ok
	public static final String HTTPSTATE_NONET = "http_resp_nonet";// resp code of no network
	public static final String HTTPSTATE_ERROR = "http_resp_error";// resp code of network error
	public static final String URLNULL_ERROR = "http_url_null";// resp code of network error
	public static final String HTTPSTATE_TIMEOUT = "http_resp_timeout";// resp code of connect timeout
	public static final String HTTPREQUEST_POST = "POST";
	public static final String HTTPREQUEST_GET = "GET";
	
	private Context mContext;
	private static HttpServer mHttpServer;
	private HttpURLConnection mUrlConnection;
	private HttpResponse mHttpResponse;
	
	private HttpServer(Context context) {
		this.mContext = context;
	}

	public static HttpServer getHttpServer(Context context){
		if(mHttpServer == null){
			mHttpServer = new HttpServer(context);
		}
		return mHttpServer;
	}
	
	public HttpURLConnection getHttpURLConnection(){
		return mUrlConnection;
	}
	
	public HttpResponse getHttpResponse(){
		return mHttpResponse;
	}
	
	/**
	 * GET方式请求网络服务器数据
	 *@param aServerUrl 连接服务器地址,aSessionId sessiondId不使用设置为"";
	 *@return 返回数据或请求异常返回标识
	 */
	public String requestByGet(String aServerUrl,String aSessionId){
		String httpResult = null;
		try {
			if(aServerUrl == null || "".equals(aServerUrl))
				return URLNULL_ERROR; 
			mUrlConnection = getConnection(aServerUrl,HttpServer.HTTPREQUEST_GET,aSessionId);
			mUrlConnection.connect();
			if (mUrlConnection.getResponseCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream bytestream = new ByteArrayOutputStream();  
				int count;  
				while ((count = mUrlConnection.getInputStream().read()) != -1){
					bytestream.write(count);  
				}  
				byte[] data = bytestream.toByteArray();  
				bytestream.close();  
				httpResult = new String(data);
			} else {
				httpResult = HTTPSTATE_NONET;
			}
			mUrlConnection.disconnect();
		}catch (MalformedURLException e){//url无效
			httpResult = HTTPSTATE_ERROR;
		}catch (ProtocolException e){//设置请求方式异常
			httpResult = HTTPSTATE_ERROR;
		}catch (ConnectTimeoutException e){//超时
			httpResult = HTTPSTATE_TIMEOUT;
		}catch (Exception e) {
			httpResult = HTTPSTATE_NONET;
		}
		return httpResult;
	}
	
	/**
	 * POST方式请求网络服务器数据
	 *@param aServerUrl 连接服务器地址，aRequestData post数据,aSessionId sessiondId不使用设置为"";
	 *@return 返回数据或请求异常返回标识
	 */
	public String requestByPost(String aServerUrl,String aRequestData,String aSessionId){
		String httpResult = null;
		try {
			if(aServerUrl == null || "".equals(aServerUrl))
				return URLNULL_ERROR; 
			mUrlConnection = getConnection(aServerUrl,HttpServer.HTTPREQUEST_POST,aSessionId);
			mUrlConnection.connect();
			if(aRequestData!=null && !"".equals(aRequestData)){
				byte[] postData = aRequestData.getBytes();
				DataOutputStream dataOutputStr = new DataOutputStream(mUrlConnection.getOutputStream());
				dataOutputStr.write(postData);
				dataOutputStr.flush();
				dataOutputStr.close();
			}
			if (mUrlConnection.getResponseCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream bytestream = new ByteArrayOutputStream();  
				int count;  
				while ((count = mUrlConnection.getInputStream().read()) != -1){
					bytestream.write(count);  
				}  
				byte[] data = bytestream.toByteArray();  
				bytestream.close();  
				httpResult = new String(data);
			} else {
				httpResult = HTTPSTATE_NONET;
			}
			mUrlConnection.disconnect();
		}catch (MalformedURLException e){//url无效
			httpResult = HTTPSTATE_ERROR;
		}catch (ProtocolException e){//设置请求方式异常
			httpResult = HTTPSTATE_ERROR;
		}catch (ConnectTimeoutException e){//超时
			httpResult = HTTPSTATE_TIMEOUT;
		}catch (Exception e) {
			httpResult = HTTPSTATE_NONET;
		}
		return httpResult;
	}
	
	/**
	 * GET方式请求网络服务器数据
	 *@param aServerUrl 连接服务器地址,aSessionId sessiondId不使用设置为"";
	 *@return 返回数据或请求异常返回标识
	 */
	public String requestByHttpGet(String aServerUrl,String aSessionId) {
		String httpResult = null;
		if(aServerUrl == null || "".equals(aServerUrl))
			return URLNULL_ERROR; 
		HttpGet httpGet = new HttpGet(aServerUrl);
		try {
			httpGet.setHeader("Content-Type", "text/plain");
			httpGet.setHeader("charset", HTTP.UTF_8);
			if(aSessionId != null && !"".equals(aSessionId))
				httpGet.setHeader("sessionId", aSessionId);
			HttpClient httpClient = HttpServer.getHttpClient(mContext);
			mHttpResponse = httpClient.execute(httpGet);
			if (mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				httpResult = EntityUtils.toString(mHttpResponse.getEntity(), "UTF-8");
			} else {
				httpResult = HTTPSTATE_NONET;
			}
			httpGet.abort();
		} catch (ConnectTimeoutException e) {
			httpResult = HTTPSTATE_TIMEOUT;
		} catch (ClientProtocolException e) {
			httpResult = HTTPSTATE_ERROR;
		} catch (ParseException e) {
			httpResult = HTTPSTATE_ERROR;
		} catch (Exception e) {
			httpResult = HTTPSTATE_NONET;
		}finally {
			if (httpGet != null) {
				httpGet.abort();// 释放资源
			}
		}
		return httpResult;
	}
	
	/**
	 * POST方式请求网络服务器数据
	 *@param aServerUrl 连接服务器地址，aRequestData post数据,aSessionId sessiondId不使用设置为"",aContext 上下文环境
	 *@return 返回数据或请求异常返回标识
	 */
	public String requestByHttpPost(String aServerUrl,String aRequestData,String aSessionId,Context aContext) {
		String httpResult = null;
		if(aServerUrl == null || "".equals(aServerUrl))
			return URLNULL_ERROR; 
		if(aRequestData == null || "".equals(aRequestData))
			return HTTPSTATE_ERROR; 
		HttpPost httpPost = new HttpPost(aServerUrl);
		try {
			httpPost.setEntity(new ByteArrayEntity(aRequestData.getBytes("UTF8")));
			httpPost.setHeader("Content-Type", "text/plain");
			if(aSessionId!=null && !"".equals(aSessionId))
				httpPost.setHeader("sessionId", aSessionId);
			HttpClient httpClient = HttpServer.getHttpClient(aContext);
			mHttpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = mHttpResponse.getEntity();
			if(httpEntity!=null){
				httpResult = EntityUtils.toString(httpEntity, "UTF-8"); 
			}else {
				httpResult = HTTPSTATE_NONET;
			}
			httpPost.abort();
		} catch (ConnectTimeoutException e) {
			httpResult = HTTPSTATE_TIMEOUT;
		} catch (ClientProtocolException e) {
			httpResult = HTTPSTATE_ERROR;
		} catch (ParseException e) {
			httpResult = HTTPSTATE_ERROR;
		} catch (Exception e) {
			httpResult = HTTPSTATE_NONET;
		} finally {
			if (httpPost != null) {
				httpPost.abort();// 释放资源
			}
		}
		return httpResult;
	}
	
	
	/**
	 * POST方式上传文件
	 */
	public String requestByHttpPost(String aServerUrl,File file,Context aContext) {
		String httpResult = null;
		if(aServerUrl == null || "".equals(aServerUrl))
			return URLNULL_ERROR; 
		if(file == null || !file.exists() || file.length() == 0)
			return HTTPSTATE_ERROR; 
		HttpPost httpPost = new HttpPost(aServerUrl);
		try {
			byte[] buffer = null;
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1024*1024);
			byte[] bt = new byte[1024*1024];
			int n;
			while ((n = fis.read(bt)) != -1) {
				bos.write(bt, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
			httpPost.setEntity(new ByteArrayEntity(buffer));
			HttpClient httpClient = HttpServer.getHttpClient(aContext);
			mHttpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = mHttpResponse.getEntity();
			if(httpEntity!=null){
				httpResult = EntityUtils.toString(httpEntity, "UTF-8"); 
			}else {
				httpResult = HTTPSTATE_NONET;
			}
			httpPost.abort();
		} catch (ConnectTimeoutException e) {
			httpResult = HTTPSTATE_TIMEOUT;
		} catch (ClientProtocolException e) {
			httpResult = HTTPSTATE_ERROR;
		} catch (ParseException e) {
			httpResult = HTTPSTATE_ERROR;
		} catch (Exception e) {
			httpResult = HTTPSTATE_NONET;
		} finally {
			if (httpPost != null) {
				httpPost.abort();// 释放资源
			}
		}
		return httpResult;
	}
	
	/**
	 * POST方式请求网络服务器数据
	 *@param aServerUrl 连接服务器地址，aRequestData post数据,aSessionId sessiondId不使用设置为"",aContext 上下文环境
	 *@return 返回数据或请求异常返回标识
	 */
	public String requestByHttpPost(String aServerUrl,Map<String,String> headers,Map<String,String> requestData) {
		String httpResult = null;
		if(aServerUrl == null || "".equals(aServerUrl))
			return URLNULL_ERROR; 
		HttpPost httpPost = new HttpPost(aServerUrl);
		try {
			for(String key : headers.keySet()){
				httpPost.setHeader(key,headers.get(key));
			}
			if(requestData != null && !requestData.isEmpty()){
				ArrayList<BasicNameValuePair> nvp = new ArrayList<BasicNameValuePair>();
				for(String key : requestData.keySet()){
					BasicNameValuePair appidPair = new BasicNameValuePair(
							key,requestData.get(key));
					nvp.add(appidPair);
				}
				UrlEncodedFormEntity entityin = new UrlEncodedFormEntity(nvp,HTTP.UTF_8);
				httpPost.setEntity(entityin);
			}
			HttpClient httpClient = HttpServer.getHttpClient(mContext);
			mHttpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = mHttpResponse.getEntity();
			if(httpEntity!=null){
				httpResult = EntityUtils.toString(httpEntity, "UTF-8"); 
			}else {
				httpResult = HTTPSTATE_NONET;
			}
			httpPost.abort();
		} catch (ConnectTimeoutException e) {
			httpResult = HTTPSTATE_TIMEOUT;
		} catch (ClientProtocolException e) {
			httpResult = HTTPSTATE_ERROR;
		} catch (ParseException e) {
			httpResult = HTTPSTATE_ERROR;
		} catch (Exception e) {
			httpResult = HTTPSTATE_ERROR;
		} finally {
			if (httpPost != null) {
				httpPost.abort();// 释放资源
			}
		}
		return httpResult;
	}
	
	/**
	 * POST方式请求网络服务器数据
	 *@param aServerUrl 连接服务器地址，aRequestData post数据,aSessionId sessiondId不使用设置为"",aContext 上下文环境
	 *@return 返回数据或请求异常返回标识
	 */
	public String requestByHttpPost(String aServerUrl,Map<String,String> requestData) {
		String httpResult = null;
		if(aServerUrl == null || "".equals(aServerUrl))
			return URLNULL_ERROR; 
		HttpPost httpPost = new HttpPost(aServerUrl);
		try {
			if(requestData != null && !requestData.isEmpty()){
				ArrayList<BasicNameValuePair> nvp = new ArrayList<BasicNameValuePair>();
				for(String key : requestData.keySet()){
					BasicNameValuePair appidPair = new BasicNameValuePair(
							key,requestData.get(key));
					nvp.add(appidPair);
				}
				UrlEncodedFormEntity entityin = new UrlEncodedFormEntity(nvp,HTTP.UTF_8);
				httpPost.setEntity(entityin);
			}
			httpPost.setHeader("Content-Type", "text/plain");
			HttpClient httpClient = HttpServer.getHttpClient(mContext);
			mHttpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = mHttpResponse.getEntity();
			if(httpEntity!=null){
				httpResult = EntityUtils.toString(httpEntity, "UTF-8"); 
			}else {
				httpResult = HTTPSTATE_NONET;
			}
			httpPost.abort();
		} catch (ConnectTimeoutException e) {
			httpResult = HTTPSTATE_TIMEOUT;
		} catch (ClientProtocolException e) {
			httpResult = HTTPSTATE_ERROR;
		} catch (ParseException e) {
			httpResult = HTTPSTATE_ERROR;
		} catch (Exception e) {
			httpResult = HTTPSTATE_ERROR;
		} finally {
			if (httpPost != null) {
				httpPost.abort();// 释放资源
			}
		}
		return httpResult;
	}
	
	public HttpURLConnection getConnection(String aServerUrl, String aMethodType,String aSessionId) throws IOException{
		URL connUrl = new URL(aServerUrl);
		HttpURLConnection connection = (HttpURLConnection) connUrl.openConnection();
		connection.setConnectTimeout(CONNECTTIMEOUT_TIME);
		if (aSessionId != null && !"".equals(aSessionId)) {
			connection.setRequestProperty("sessionId", aSessionId);
		}
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod(aMethodType);
		if(aMethodType.equals(HttpServer.HTTPREQUEST_POST))
			connection.setUseCaches(false);
		else
			connection.setUseCaches(true);
		connection.setInstanceFollowRedirects(false);
		connection.setRequestProperty("Content-Type", "text/plain");
		return connection;
	}
	
	@SuppressWarnings("deprecation")
	public static HttpClient getHttpClient(Context aContext){
		String host = Proxy.getHost(aContext.getApplicationContext());
		int port = Proxy.getPort(aContext.getApplicationContext());
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams,CONNECTTIMEOUT_TIME);// 请求超时
		if (host != null) {
			HttpHost httpHost = new HttpHost(host, port);
			httpParams.setParameter(ConnRouteParams.DEFAULT_PROXY, httpHost);
		}
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		return httpClient;
	}
	
	public boolean checkURLisOk(String url){
		boolean value=false;
		try {
			HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			int code = conn.getResponseCode();
			if(code != HttpURLConnection.HTTP_OK){
				value = false;
			}else{
				value = true;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		 } catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 下载网络图片 
	 */                                                           
    public static Bitmap downloadBitmap(String url,int desWidth,int desHeight) {
        final HttpClient client = new DefaultHttpClient();
        final HttpGet getRequest = new HttpGet(url);
        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
                    byte[] buffer = new byte[1024];  
                    int len = 0;  
                    while( (len = inputStream.read(buffer)) != -1){  
                        outStream.write(buffer, 0, len);  
                    }  
                    outStream.close();    
                    return BitmapUtils.getBitmap(outStream.toByteArray(),desWidth, desHeight);
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                        inputStream = null;
                    }
                    entity.consumeContent();
                }
            }
        } catch (IOException e) {
            getRequest.abort();
        } catch (IllegalStateException e) {
            getRequest.abort();
        } catch (Exception e) {
            getRequest.abort();
        } finally {
            client.getConnectionManager().shutdown();
        }
        return null;
    }
	
}

//end of the file

