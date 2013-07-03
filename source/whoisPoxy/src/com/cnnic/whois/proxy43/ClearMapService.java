package com.cnnic.whois.proxy43;

public class ClearMapService implements Runnable {
	private String clienURL;
	private static final long THREADSLEEP = 2000;
	public ClearMapService(String clienURL) {
		this.clienURL = clienURL;
	}

	/**
	 * whois query data stored in the collection if there is a corresponding IP
	 * information is removed from the collection.
	 */
	@Override
	public void run() {
		try {
			Thread.sleep(THREADSLEEP);
			if (ProxyService.dataMap.get(clienURL) != null)
				ProxyService.dataMap.remove(clienURL);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
