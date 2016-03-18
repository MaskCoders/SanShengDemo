package hstt.update;

import java.util.HashMap;
import java.util.Map;


import android.content.Context;

public class GlobalParameters {
	//实际存储的是lname
	private static final String USERNAME_KEY = "username";
	//存储原用户名
	private static final String USERNAMEORG_KEY = "usernameorg";
	//网络状态，true 可以链接，false 不能链接 （链接指Wifi或者 移动网络）
	private static final String NETWORKSTATUS = "networkstatus";
	//网络状态类型  ConnectivityManager.TYPE_MOBILE  ConnectivityManager.TYPE_WIFI
	private static final String NETWORKTYPE = "networktype";
	private static GlobalParameters instance = new GlobalParameters();
	private static Map<String,Object> map = new HashMap<String,Object>();
	private static Context context;
	
	private GlobalParameters() {
		
	}
	
	public static GlobalParameters getInstance (Context context) {
		if (GlobalParameters.context == null) {
			GlobalParameters.context = context;
		}
		if (map == null) {
			
			map = new HashMap<String,Object>();
			map.put(NETWORKSTATUS, true);
			String lname = "rXwUiTrQyQrP";
			map.put(USERNAME_KEY, lname);
		}
		return instance;
	}
	
	public void put(String key,Object value) {
		if (key.equals(USERNAME_KEY)) {
			return;
		}
		map.put(key, value);
	}
	
	public Object get(String key) {
		return map.get(key);
	}
	
	
	/**
	 * 加密后的用户名 lname
	 * @param userName
	 */
	public void setUserName (String userName) {
		map.put(USERNAME_KEY, userName);
	}
	
	public String getUserName () {
		String lname = (String)map.get(USERNAME_KEY);
		if (lname == null) {
			lname = "rXwUiTrQyQrP";
			map.put(USERNAME_KEY, lname);
		}
		return lname;
	}
	
	/**
	 * 没加密的原用户名
	 * @param userNameOrg
	 */
	public void setUserNameOrg (String userNameOrg) {
		map.put(USERNAMEORG_KEY, userNameOrg);
	}
	
	public String getUserNameOrg () {
		return (String)map.get(USERNAMEORG_KEY);
	}
	
	
	public void setNetWorkStatus(boolean status) {
//		map.put(NETWORKSTATUS, new Boolean(status));
		map.put(NETWORKSTATUS, status);
		
	}
	
	public boolean getNetWorkStatus() {
		return (Boolean)map.get(NETWORKSTATUS);
				
	}
	
	public void setNetWorkType (int type) {
		map.put(NETWORKTYPE, type);
	}
	
	/**
	 * ConnectivityManager.TYPE_WIFI
	 * ConnectivityManager.TYPE_MOBILE
	 * @param type
	 * @return
	 */
	public int getNetWorkType (int type) {
		return (Integer)map.get(NETWORKTYPE);
	}
}
