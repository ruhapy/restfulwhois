package com.cnnic.whois.proxy43;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.cnnic.whois.util.WhoisProperties;

public class ManagementProxyServer  {

	/**
	 * Start or shutDown Whois43ProxyServer
	 */
	
	public void startProxyServer() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		BufferedReader br = null;

		try {
			serverSocket = new ServerSocket(WhoisProperties.getManagementPort());
			boolean stopFlag = true;
			
			Thread t = new Thread(new WhoisProxyServer());
			t.setDaemon(true);
			t.start();
			while (stopFlag) {
				socket = serverSocket.accept();
				br = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				if (br.readLine().equals(WhoisProperties.getShutDown()))
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
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
	
	public static void main(String[] args) {
		 WhoisProperties.setClassesurl(args[0]);
		new ManagementProxyServer().startProxyServer();
	}
}
