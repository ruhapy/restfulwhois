package com.cnnic.whois.controller;

import java.net.IDN;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.execption.RedirectExecption;
import com.cnnic.whois.service.QueryService;
import com.cnnic.whois.util.validate.ValidateUtils;

@Controller
@RequestMapping("/")
public class QueryController extends BaseController {
	@Autowired
	private QueryService queryService;

	@RequestMapping(value = "/domain/{domainName}", method = RequestMethod.GET)
	@ResponseBody
	public String queryDomain(@PathVariable String domainName,
			HttpServletRequest request) throws QueryException,
			RedirectExecption {
		domainName = StringUtils.trim(domainName);
		String queryParaPuny = IDN.toASCII(domainName);
		if (!ValidateUtils.validateDomainName(queryParaPuny)) {
			// return WhoisUtil.processError(WhoisUtil.COMMENDRRORCODE, role,
			// format);
		}
		QueryParam queryParam = super.praseQueryParams(request);
		queryParam.setQ(domainName);
		Map<String, Object> result = queryService.queryDomain(queryParam);
		System.err.println(result);
		return null;
	}

	@RequestMapping(value = "/entity/{entityName}", method = RequestMethod.GET)
	@ResponseBody
	public String queryEntity(@PathVariable String domainName,
			HttpServletRequest request) {
		return null;
	}

	@RequestMapping(value = "/nameserver/{nsName}", method = RequestMethod.GET)
	@ResponseBody
	public String queryNs(@PathVariable String domainName,
			HttpServletRequest request) {
		return null;
	}

	@RequestMapping(value = "/autnum/{autnum}", method = RequestMethod.GET)
	@ResponseBody
	public String queryAs(@PathVariable String domainName,
			HttpServletRequest request) {
		return null;
	}

	@RequestMapping(value = "/dsData/{dsData}", method = RequestMethod.GET)
	@ResponseBody
	public String queryDsData(@PathVariable String domainName,
			HttpServletRequest request) {
		return null;
	}

	@RequestMapping(value = "/events/{events}", method = RequestMethod.GET)
	@ResponseBody
	public String queryEvents(@PathVariable String domainName,
			HttpServletRequest request) {
		return null;
	}
}
