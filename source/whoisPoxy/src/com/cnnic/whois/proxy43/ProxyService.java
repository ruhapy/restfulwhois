package com.cnnic.whois.proxy43;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cnnic.whois.util.WhoisProperties;

public class ProxyService implements Runnable {
	private static final int BUF_LENGTH = 10 * 1024;
	public static Map<String, String> dataMap = new ConcurrentHashMap<String, String>();
	private static Map<String, String> queryMap = new ConcurrentHashMap<String, String>();
	public static Map<String, Long> overTimeMap = new ConcurrentHashMap<String, Long>();

	private Socket socket;
	private String clienURL;
	/**
	 * The queryMap stored data
	 */
	static {
		queryMap.put("as", "autnum");
		queryMap.put("ns", "nameserver");
		queryMap.put("entity", "entity");
	}

	public ProxyService(Socket socket) {
		this.socket = socket;
		clienURL = socket.getInetAddress().getHostAddress();
	}

	/**
	 * Socket resolve queries
	 */
	public void run() {
		BufferedReader br = null;

		try {
			InputStreamReader isr = new InputStreamReader(
					socket.getInputStream());
			br = new BufferedReader(isr);
			String value = br.readLine();
			
			if (dataMap.get(clienURL) == null) {
				// ÅÐ¶ÏÊÇ·ñÊÇipºÍdomain
				if (queryMap.get(value) != null) {
					dataMap.put(clienURL, value);
					//new Thread(new ClearMapService(clienURL)).start();
				} else {
				
					String[] values = value.split(" ");
					String queryStr = "";
					if (values.length > 1) {
						if(queryMap.get(values[0]) == null) {
							responseError(socket, "query error");
							return;
						}
						queryStr = queryMap.get(values[0]) + "/"  + values[1];
						
					} else {
						queryStr = generateIPDomainQueryStr(value);
					}
					
					if (queryCountControl(clienURL)) {
						transferData(socket, queryStr);
					} else {
						responseError(socket, "query error");
					}
				}
			} else {
				if (queryMap.get(dataMap.get(clienURL)) != null) {
					String queryStr = queryMap.get(dataMap.get(clienURL)) + "/"
							+ value;
					dataMap.remove(clienURL);
					if (queryCountControl(clienURL)) {
						transferData(socket, queryStr);
					} else {
						responseError(socket, "query error");
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			try {
				responseError(socket, e.getMessage());
			} catch (IOException e1) {
			}
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}

	}

	/**
	 * Determine whether the ip or domain query
	 * 
	 * @param value
	 * @return queryStr
	 */
	private String generateIPDomainQueryStr(String value) {
		String queryType = "domain/";
		String ipLength = "0";
		String strInfo = value;

		if (value.indexOf("/") >= 0) {
			String[] infoArray = value.split("/");
			strInfo = infoArray[0];
			ipLength = infoArray[1];
		}

		if (verifyIP(strInfo, ipLength)) {
			queryType = "ip/";
		}
		return queryType + value;
	}

	/**
	 * After the query data is written to the client
	 * 
	 * @param socket
	 * @param queryStr
	 * @throws IOException
	 */
	private void transferData(Socket socket, String queryStr)
			throws IOException {
		URL urlAdress = new URL(WhoisProperties.getRequesUrl() + queryStr);
		InputStream is = urlAdress.openStream();

		byte[] buffer = new byte[BUF_LENGTH];
		OutputStream os = socket.getOutputStream();

		while (true) {
			int charsRead = -1;
			if ((charsRead = is.read(buffer)) == -1)
				break;
			
			os.write(buffer, 0, charsRead);
		}
	}

	/**
	 * Control call this method when the number of queries in a certain period
	 * of time
	 * 
	 * @param userIP
	 * @return If you meet certain requirements, return true, otherwise returns
	 *         false
	 */
	private boolean queryCountControl(String userIP) {
		long currentTime = System.currentTimeMillis();

		if (overTimeMap.get(userIP) == null) {
			overTimeMap.put(userIP, currentTime);
			return true;
		} else {
			if ((currentTime - overTimeMap.get(userIP)) <= WhoisProperties
					.getExpireTime()) {
				overTimeMap.put(userIP, currentTime);
				return false;
			}
		}
		return true;
	}

	/**
	 * Verifying the IPv6 parameters
	 * 
	 * @param address
	 * @return The correct parity returns true, failure to return false
	 */
	private boolean isIPv6(String address) {
		boolean result = false;
		String regHex = "(\\p{XDigit}{1,4})";

		String regIPv6Full = "^(" + regHex + ":){7}" + regHex + "$";

		String regIPv6AbWithColon = "^(" + regHex + "(:|::)){0,6}" + regHex
				+ "$";

		String regIPv6AbStartWithDoubleColon = "^(" + "::(" + regHex
				+ ":){0,5}" + regHex + ")$";

		String regIPv6 = "^(" + regIPv6Full + ")|("
				+ regIPv6AbStartWithDoubleColon + ")|(" + regIPv6AbWithColon
				+ ")$";

		if (address.indexOf(":") != -1) {
			if (address.length() <= 39) {
				String addressTemp = address;
				int doubleColon = 0;
				if (address.equals("::"))
					return true;
				while (addressTemp.indexOf("::") != -1) {
					addressTemp = addressTemp
							.substring(addressTemp.indexOf("::") + 2,
									addressTemp.length());
					doubleColon++;
				}
				if (doubleColon <= 1) {
					result = address.matches(regIPv6);
				}
			}
		}
		return result;
	}

	/**
	 * Verifying the IP parameters
	 * 
	 * @param ipStr
	 * @param ipLengthStr
	 * @return The correct parity returns true, failure to return false
	 */
	private boolean verifyIP(String ipStr, String ipLengthStr) {
		String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
		if (ipLengthStr.equals("0")) {
			if (!ipStr.matches(regex) && !isIPv6(ipStr))
				return false;
		} else {
			if (!ipStr.matches(regex))
				return false;
		}
		if (!ipLengthStr.matches("^[0-9]*$"))
			return false;

		return Integer.parseInt(ipLengthStr) >= 0
				&& Integer.parseInt(ipLengthStr) <= 32;
	}

	/**
	 * The processing Error
	 * 
	 * @param socket
	 * @param errorStr
	 * @throws IOException
	 */
	private void responseError(Socket socket, String errorStr)
			throws IOException {
		BufferedWriter bos = new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream()));
		bos.write(errorStr);
	}

}
