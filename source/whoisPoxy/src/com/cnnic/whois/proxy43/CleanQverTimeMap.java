package com.cnnic.whois.proxy43;

import java.util.Set;

import com.cnnic.whois.util.WhoisProperties;

public class CleanQverTimeMap implements Runnable {
	private static final long THREADSLEEP = 5000;
	/**
	 * Data out of the collection will be stored query time
	 */
	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(THREADSLEEP);
				Set<String> key = ProxyService.overTimeMap.keySet();
				if (key != null) {
					for (String keyName : key) {
						if ((ProxyService.overTimeMap.get(keyName) - System
								.currentTimeMillis()) >= WhoisProperties
								.getMaxWaitTime()) {
							ProxyService.overTimeMap.remove(keyName);
						}

					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
