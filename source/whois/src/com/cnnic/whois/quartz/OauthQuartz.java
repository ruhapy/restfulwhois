package com.cnnic.whois.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.cnnic.whois.dao.oauth.OAuthAccessorDao;

public class OauthQuartz extends QuartzJobBean {

	private static OAuthAccessorDao oauthAccessorDao = new OAuthAccessorDao();
	
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		oauthAccessorDao.deleteInvalidDate();
		System.out.println("---------------------------");
	}
	
}
