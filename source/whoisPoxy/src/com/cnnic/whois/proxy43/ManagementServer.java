package com.cnnic.whois.proxy43;

import java.io.*;
import java.net.*;
import com.cnnic.whois.util.WhoisProperties;

public class ManagementServer {
	/**
	 * ShutDown Whois43Proxy
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		WhoisProperties.setClassesurl(args[0]);
		Socket socket = null;
		BufferedWriter bos = null;
		try {

			socket = new Socket(WhoisProperties.getPorxyIp(),
					WhoisProperties.getManagementPort());
			bos = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			bos.write(WhoisProperties.getShutDown());
		} catch (ConnectException connExc) {
			System.out.println("Could not connect.");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
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
}