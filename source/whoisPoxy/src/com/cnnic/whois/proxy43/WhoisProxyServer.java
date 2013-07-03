package com.cnnic.whois.proxy43;

import java.io.*;
import java.net.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.cnnic.whois.util.WhoisProperties;

public class WhoisProxyServer implements Runnable{
	
	/**
	 * Start ProxyServer
	 */
	@Override
	public void run() {
		System.out.println("Whois port 43 proxy server started.");
		Thread cleanThread = new Thread(new CleanQverTimeMap());
		cleanThread.setDaemon(true);
		cleanThread.start();
		boolean runningFlag = true;
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(WhoisProperties.getPort());
		
			ThreadPoolExecutor executor = new ThreadPoolExecutor(WhoisProperties.getCorePoolSize(), 
										WhoisProperties.getMaximumPoolSize(), 
										WhoisProperties.getKeepAliveTime(),
										TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(3),
										new ThreadPoolExecutor.CallerRunsPolicy());
			
			while (runningFlag) {
				Socket socket = null;
				socket = serverSocket.accept();
				executor.execute(new ProxyService(socket));
			}
			
			executor.shutdown();
			System.out.println("Whois port 43 proxy server shutdown.");
		} catch (IOException e) { 
			e.printStackTrace();
		}
		
	}
	
	
}
