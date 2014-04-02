package com.cnnic.whois.controller;

import java.io.IOException;
import java.net.IDN;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cnnic.whois.bean.DomainQueryParam;
import com.cnnic.whois.bean.EntityQueryParam;
import com.cnnic.whois.bean.IpQueryParam;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.dao.query.QueryEngine;
import com.cnnic.whois.dao.query.search.EntityQueryDao;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.execption.RedirectExecption;
import com.cnnic.whois.service.QueryService;
import com.cnnic.whois.util.WhoisUtil;
import com.cnnic.whois.util.validate.ValidateUtils;
/**
 * query controller,mapping url begin with ".well-known/rdap/"
 * 
 * @author nic
 *
 */
@Controller
@RequestMapping("/{dot}well-known/rdap")
public class QueryController extends BaseController {
	@Autowired
	private QueryService queryService;
	@Autowired
	private QueryEngine queryEngine;
	
	/**
	 * api doc
	 * @return
	 */
	@RequestMapping(value = { "/", "" }, method = RequestMethod.GET)
	public String index() {
		return "/doc/index";
	}
	/**
	 * fuzzy query domain by domain name
	 * @param name : domain name
	 * @param request:http request
	 * @param response:http response
	 * @throws QueryException
	 * @throws RedirectExecption
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/domains", method = RequestMethod.GET)
	@ResponseBody
	public void fuzzyQueryDomain(@RequestParam(required = false) String name,
			HttpServletRequest request, HttpServletResponse response)
			throws QueryException, RedirectExecption, IOException,
			ServletException {
		request.setAttribute("queryType", "domain");
		DomainQueryParam domainQueryParam = super.praseDomainQueryParams(request);
		if (StringUtils.isBlank(name) || ValidateUtils.isInvalidPunyPartSearch(name)) {
			super.renderResponseError400(request, response,domainQueryParam);
			return;
		}
		name = WhoisUtil.urlDecode(name);
		name = StringUtils.trim(name);
		name = ValidateUtils.deleteLastPoint(name);
		Map<String, Object> resultMap = null;
		name = super.getNormalization(name);
		if ("*".equals(name)) {
			super.renderResponseError422(request, response,domainQueryParam);
			return;
		}
		name = WhoisUtil.getLowerCaseByLabel(name);
		String punyDomainName = name;
		try {
			punyDomainName = IDN.toASCII(name);// long lable exception/not utf8 exception
		} catch (Exception e) {
			super.renderResponseError400(request, response,domainQueryParam);
			return;
		}
		request.setAttribute("queryPara", IDN.toUnicode(punyDomainName));
		if (!ValidateUtils.isCommonInvalidStr(punyDomainName)) {
			super.renderResponseError400(request, response,domainQueryParam);
			return;
		}
		domainQueryParam.setQueryType(QueryType.SEARCHDOMAIN);
		domainQueryParam.setQ(name);
		domainQueryParam.setDomainPuny(punyDomainName);
		setMaxRecordsForFuzzyQ(domainQueryParam);
		resultMap = queryService.query(domainQueryParam);
		request.setAttribute("pageBean", domainQueryParam.getPage());
		request.setAttribute("queryPath", "domains");
		renderResponse(request, response, resultMap, domainQueryParam);
	}
	
	public static void main(String[] args) {
		String a = "xn--hxaajaoebldbselhkqsqmapxidccaaahjrgk3chhdip9bclcgddbb4ooioa";
		String p = IDN.toUnicode(a);
		System.err.println(p.length());
	}
	/**
	 * pricise query domain by domain name
	 * @param domainName:domain name
	 * @param request:http request
	 * @param response:http response:http response
	 * @throws QueryException
	 * @throws RedirectExecption
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/domain/{domainName}", method = RequestMethod.GET)
	@ResponseBody
	public void queryDomain(@PathVariable String domainName,
			HttpServletRequest request, HttpServletResponse response)
			throws QueryException, RedirectExecption, IOException,
			ServletException {
		domainName = StringUtils.trim(domainName);
		domainName = StringUtils.lowerCase(domainName);
		domainName = ValidateUtils.deleteLastPoint(domainName);
		String punyDomainName = domainName;
		Map<String, Object> resultMap = null;
		DomainQueryParam domainQueryParam = super
				.praseDomainQueryParams(request);
		request.setAttribute("queryType", "domain");
		try {
			domainName = WhoisUtil.toChineseUrl(domainName);
			punyDomainName = IDN.toASCII(domainName);// long lable exception
		} catch (Exception e) {
			super.renderResponseError400(request, response,domainQueryParam);
			return;
		}
		request.setAttribute("queryPara", IDN.toUnicode(punyDomainName));
		if (!ValidateUtils.validateDomainName(punyDomainName)) {
			resultMap = WhoisUtil.processError(WhoisUtil.COMMENDRRORCODE,domainQueryParam);
		} else {
			domainQueryParam.setQueryType(QueryType.DOMAIN);
			domainQueryParam.setQ(domainName);
			domainQueryParam.setDomainPuny(punyDomainName);
			resultMap = queryService.queryDomain(domainQueryParam);
		}
		renderResponse(request, response, resultMap, domainQueryParam);
	}

	/**
	 * fuzzy query entity by name or handle
	 * @param fn:entity name param
	 * @param handle:entity handle param
	 * @param request:http request
	 * @param response:http response
	 * @throws QueryException
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/entities", method = RequestMethod.GET)
	@ResponseBody
	public void fuzzyQueryEntity(@RequestParam(required = false) String fn,
			@RequestParam(required = false) String handle,
			HttpServletRequest request, HttpServletResponse response)
			throws QueryException, SQLException, IOException, ServletException {
		Map<String, Object> resultMap = null;
		EntityQueryParam queryParam = super.praseEntityQueryParams(request);
		request.setAttribute("queryType", "entity");
		if (StringUtils.isBlank(fn) && StringUtils.isBlank(handle)) {
			super.renderResponseError400(request, response,queryParam);
			return;
		}
		String q = handle;
		if (StringUtils.isNotBlank(fn)) {
			q = fn;
		}
		q = WhoisUtil.urlDecode(q);
		try {
			IDN.toASCII(q);// long lable exception/not utf8 exception
		} catch (Exception e) {
			super.renderResponseError400(request, response,queryParam);
			return;
		}
		q = super.getNormalization(q);
		if ("*".equals(q)) {
			super.renderResponseError422(request, response,queryParam);
			return;
		}
		q = WhoisUtil.getLowerCaseIfAllAscii(q);
		String decodeQ = WhoisUtil.toChineseUrl(q);
		String fuzzyQuerySolrPropName = "handle";
		String paramName = "handle";
		if (StringUtils.isNotBlank(fn)) {
			fuzzyQuerySolrPropName = "entityNames";
			paramName = "fn";
		}
		queryParam.setQueryType(QueryType.SEARCHENTITY);
		queryParam.setFuzzyQueryParamName(fuzzyQuerySolrPropName);
		queryParam.setQ(decodeQ);
		setMaxRecordsForFuzzyQ(queryParam);
		resultMap = queryService.fuzzyQueryEntity(queryParam);
		request.setAttribute("pageBean", queryParam.getPage());
		request.setAttribute("queryPath", "entities");
		request.setAttribute("queryPara", paramName + ":" + decodeQ);
		renderResponse(request, response, resultMap, queryParam);
	}

	/**
	 * pricise query entity by name
	 * @param entityName:entity name
	 * @param request:http request
	 * @param response:http response
	 * @throws QueryException
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/entity/{entityName}", method = RequestMethod.GET)
	@ResponseBody
	public void queryEntity(@PathVariable String entityName,
			HttpServletRequest request, HttpServletResponse response)
			throws QueryException, SQLException, IOException, ServletException {
		EntityQueryParam queryParam = super.praseEntityQueryParams(request);
		queryParam.setQueryType(QueryType.ENTITY);
		queryParam.setQ(entityName);
		Map<String, Object> resultMap = queryService.queryEntity(queryParam);
		request.setAttribute("queryType", "entity");
		request.setAttribute("queryPara", entityName);
		renderResponse(request, response, resultMap, queryParam);
	}

	/**
	 * fuzzy query ns by ns name, or ip 
	 * @param name:ns name
	 * @param ip:ns's ip.if 'name' param is not blank, 'ip' param will be ignored
	 * @param request:http request
	 * @param response:http response
	 * @throws QueryException
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 * @throws RedirectExecption
	 */
	@RequestMapping(value = "/nameservers", method = RequestMethod.GET)
	@ResponseBody
	public void fuzzyQueryNs(@RequestParam(required = false) String name, 
			@RequestParam(required = false) String ip,
			HttpServletRequest request, HttpServletResponse response)
			throws QueryException, SQLException, IOException, ServletException,
			RedirectExecption {
		Map<String, Object> resultMap = null;
		QueryParam queryParam = super.praseQueryParams(request);
		request.setAttribute("queryType", "nameserver");
		if (StringUtils.isBlank(name) && StringUtils.isBlank(ip)) {
			super.renderResponseError400(request, response,queryParam);
			return;
		}
		if(StringUtils.isNotBlank(name)){
			name = StringUtils.trim(name);
			name = WhoisUtil.urlDecode(name);
			name = ValidateUtils.deleteLastPoint(name);
			name = super.getNormalization(name);
			if ("*".equals(name)) {
				super.renderResponseError422(request, response,queryParam);
				return;
			}
			name = WhoisUtil.getLowerCaseByLabel(name);
			String punyQ = name;
			try {
				// long lable exception/not utf8 exception
				punyQ = IDN.toASCII(name);
			} catch (Exception e) {
				super.renderResponseError400(request, response,queryParam);
				return;
			}
			request.setAttribute("queryPara", name);
			if (!ValidateUtils.verifyFuzzyDomain(name) || ValidateUtils.isInvalidPunyPartSearch(name) ) {
				resultMap = WhoisUtil.processError(WhoisUtil.COMMENDRRORCODE,queryParam);
			} else {
				geneNsQByName(queryParam, punyQ, request);
				resultMap = queryService.fuzzyQueryNameServer(queryParam);
				renderResponse(request, response, resultMap, queryParam);
				return;
			}
		}
		
		if(StringUtils.isNotBlank(ip) && StringUtils.isBlank(name)){
			String net = "0";
			if (!ValidateUtils.verifyIP(ip, net)) {
				super.renderResponseError400(request, response,queryParam);
				return;
			}
			geneNsQByIp(queryParam, ip, request);
			resultMap = queryService.fuzzyQueryNameServer(queryParam);
			renderResponse(request, response, resultMap, queryParam);
		}
	}
	
	private void geneNsQByName(QueryParam queryParam, String punyQ, HttpServletRequest request){
		queryParam.setQueryType(QueryType.SEARCHNS);
		queryParam.setQ(WhoisUtil.escapeQueryChars(punyQ));
		request.setAttribute("pageBean", queryParam.getPage());
		request.setAttribute("queryPath", "nameservers");
		setMaxRecordsForFuzzyQ(queryParam);
	}
	
	/**
	 * generate ns query params by ip
	 * @param queryParam:origin queryParam
	 * @param ip:ip param
	 * @param request:http request
	 */
	private void geneNsQByIp(QueryParam queryParam, String ip, HttpServletRequest request){
		String punyQ = ip;
		request.setAttribute("queryPara", ip);
		queryParam.setQueryType(QueryType.SEARCHNS);
		punyQ = punyQ.replace("\\:", ":");
		if(ValidateUtils.isIpv4(punyQ)){
			punyQ = EntityQueryDao.geneNsQByPreciseIpv4(punyQ);			
		}else if (ValidateUtils.isIPv6(punyQ)){
			punyQ = EntityQueryDao.geneNsQByPreciseIpv6(punyQ);
		}
		queryParam.setQ(punyQ);
		request.setAttribute("pageBean", queryParam.getPage());
		request.setAttribute("queryPath", "nameservers");
		setMaxRecordsForFuzzyQ(queryParam);
	}

	/**
	 * query ns by ns name
	 * @param nsName:ns name
	 * @param request:http request
	 * @param response:http response
	 * @throws QueryException
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/nameserver/{nsName}", method = RequestMethod.GET)
	@ResponseBody
	public void queryNs(@PathVariable String nsName,
			HttpServletRequest request, HttpServletResponse response)
			throws QueryException, SQLException, IOException, ServletException {
		request.setAttribute("queryType", "nameserver");
		nsName = StringUtils.trim(nsName);
		nsName = StringUtils.lowerCase(nsName);
		nsName = ValidateUtils.deleteLastPoint(nsName);
		String punyNsName = WhoisUtil.toChineseUrl(nsName);
		DomainQueryParam queryParam = super.praseDomainQueryParams(request);
		try{
			punyNsName = IDN.toASCII(WhoisUtil.toChineseUrl(nsName));
			// long lable exception/not utf8 exception
		} catch (Exception e) {
			super.renderResponseError400(request, response,queryParam);
			return;
		}
		Map<String, Object> resultMap = null;
		if (!ValidateUtils.verifyNameServer(nsName)) {
			resultMap = WhoisUtil.processError(WhoisUtil.COMMENDRRORCODE,queryParam);
		} else {
			queryParam.setQ(nsName);
			queryParam.setDomainPuny(punyNsName);
			queryParam.setQueryType(QueryType.NAMESERVER);
			resultMap = queryService.query(queryParam);
			request.setAttribute("queryPara", IDN.toUnicode(punyNsName));
		}
		renderResponse(request, response, resultMap, queryParam);
	}

	/**
	 * query Autonomous number
	 * @param autnum : as number
	 * @param request:http request
	 * @param response:http response
	 * @throws QueryException
	 * @throws RedirectExecption
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/autnum/{autnum}", method = RequestMethod.GET)
	@ResponseBody
	public void queryAs(@PathVariable String autnum,
			HttpServletRequest request, HttpServletResponse response)
			throws QueryException, RedirectExecption, IOException,
			ServletException {
		QueryParam queryParam = super.praseQueryParams(request);
		queryParam.setQ(autnum);
		Map<String, Object> resultMap = queryService.queryAS(queryParam);
		request.setAttribute("queryType", "autnum");
		request.setAttribute("queryPara", autnum);
		renderResponse(request, response, resultMap, queryParam);
	}

	@RequestMapping(value = "/dsData/{q}", method = RequestMethod.GET)
	@ResponseBody
	public void queryDsData(@PathVariable String q, HttpServletRequest request,
			HttpServletResponse response) throws QueryException,
			RedirectExecption, IOException, ServletException {
		query(QueryType.DSDATA, q, request, response);
	}

	/**
	 * query event by handle
	 * @param q:envent handle
	 * @param request:http request
	 * @param response:http response
	 * @throws QueryException
	 * @throws RedirectExecption
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/events/{q}", method = RequestMethod.GET)
	@ResponseBody
	public void queryEvents(@PathVariable String q, HttpServletRequest request,
			HttpServletResponse response) throws QueryException,
			RedirectExecption, IOException, ServletException {
		query(QueryType.EVENTS, q, request, response);
	}

	/**
	 * query help
	 * @param request:http request
	 * @param response:http response
	 * @throws QueryException
	 * @throws RedirectExecption
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/help", method = RequestMethod.GET)
	@ResponseBody
	public void queryHelp(HttpServletRequest request,
			HttpServletResponse response) throws QueryException,
			RedirectExecption, IOException, ServletException {
		Map<String, Object> resultMap = null;
		QueryParam queryParam = super.praseQueryParams(request);
		queryParam.setQ("helpID");
		resultMap = queryService.queryHelp(queryParam);
		renderResponse(request, response, resultMap, queryParam);
	}

	/**
	 * query ip error with tail '/',return 400 error
	 * @param request:http request
	 * @param response:http response
	 * @throws QueryException
	 * @throws RedirectExecption
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = { "/ip/{ip}/" }, method = RequestMethod.GET)
	@ResponseBody
	public void queryIpErrTail(HttpServletRequest request,
			HttpServletResponse response) throws QueryException,
			RedirectExecption, IOException, ServletException {
		QueryParam queryParam = super.praseQueryParams(request);
		request.setAttribute("queryType", "ip");
		super.renderResponseError400(request, response,queryParam);
		return;
	}
	
	/**
	 * query ip by ip address,return subnet info of the ip
	 * @param ip:ip v4/v6 address
	 * @param request:http request
	 * @param response:http response
	 * @throws QueryException
	 * @throws RedirectExecption
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = { "/ip/{ip}" }, method = RequestMethod.GET)
	@ResponseBody
	public void queryIp(@PathVariable String ip, HttpServletRequest request,
			HttpServletResponse response) throws QueryException,
			RedirectExecption, IOException, ServletException {
		String net = "0";
		doQueryIp(ip, request, response, net);
	}

	/**
	 * query ip with net mask
	 * @param ip:ip v4/v6 address
	 * @param net:net mask.should between [0-32] when ipv4, and between [0-128] when ipv6,Contains the boundary
	 * @param request:http request
	 * @param response:http response
	 * @throws QueryException
	 * @throws RedirectExecption
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = { "/ip/{ip}/{net}", "/ip/{ip}/{net}/" }, method = RequestMethod.GET)
	@ResponseBody
	public void queryIpWithNet(@PathVariable String ip,
			@PathVariable String net, HttpServletRequest request,
			HttpServletResponse response) throws QueryException,
			RedirectExecption, IOException, ServletException {
		net = StringUtils.trim(net);
		doQueryIp(ip, request, response, net);
	}

	private void doQueryIp(String ip, HttpServletRequest request,
			HttpServletResponse response, String ipLength)
			throws QueryException, IOException, ServletException,
			RedirectExecption {
		ip = StringUtils.trim(ip);
		Map<String, Object> resultMap = null;
		IpQueryParam queryParam = super.praseIpQueryParams(request);
		String strInfo = ip;
		request.setAttribute("queryPara", ip);
		request.setAttribute("queryType", "ip");
		String uri = request.getRequestURI();
		if(uri.indexOf("//") != -1){
			super.renderResponseError400(request, response,queryParam);
			return;
		}
		if (!ValidateUtils.verifyIP(strInfo, ipLength)) {
			super.renderResponseError400(request, response,queryParam);
			return;
		}
		queryParam.setQ(ip);
		queryParam.setIpInfo(strInfo);
		queryParam.setIpLength(Integer.parseInt(ipLength));
		resultMap = queryService.queryIP(queryParam);
		viewResolver.writeResponse(queryParam.getFormat(),
				queryParam.getQueryType(), request, response, resultMap);
	}

	@RequestMapping(value = "/keyData/{q}", method = RequestMethod.GET)
	@ResponseBody
	public void queryKeyData(@PathVariable String q,
			HttpServletRequest request, HttpServletResponse response)
			throws QueryException, RedirectExecption, IOException,
			ServletException {
		query(QueryType.KEYDATA, q, request, response);
	}

	/**
	 * query link by link id
	 * @param q:link id
	 * @param request:http request
	 * @param response:http response
	 * @throws QueryException
	 * @throws RedirectExecption
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/links/{q}", method = RequestMethod.GET)
	@ResponseBody
	public void queryLinks(@PathVariable String q, HttpServletRequest request,
			HttpServletResponse response) throws QueryException,
			RedirectExecption, IOException, ServletException {
		query(QueryType.LINKS, q, request, response);
	}

	/**
	 * query notice by notice id
	 * @param q:notice id
	 * @param request:http request
	 * @param response:http response
	 * @throws QueryException
	 * @throws RedirectExecption
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/notices/{q}", method = RequestMethod.GET)
	@ResponseBody
	public void queryNotices(@PathVariable String q,
			HttpServletRequest request, HttpServletResponse response)
			throws QueryException, RedirectExecption, IOException,
			ServletException {
		query(QueryType.NOTICES, q, request, response);
	}

	/**
	 * query phone by phone id
	 * @param q:phone id
	 * @param request:http request
	 * @param response:http response
	 * @throws QueryException
	 * @throws RedirectExecption
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/phones/{q}", method = RequestMethod.GET)
	@ResponseBody
	public void queryPhones(@PathVariable String q, HttpServletRequest request,
			HttpServletResponse response) throws QueryException,
			RedirectExecption, IOException, ServletException {
		query(QueryType.PHONES, q, request, response);
	}

	/**
	 * query address by address id
	 * @param q:address id
	 * @param request:http request
	 * @param response:http response
	 * @throws QueryException
	 * @throws RedirectExecption
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/postalAddress/{q}", method = RequestMethod.GET)
	@ResponseBody
	public void queryPostalAddress(@PathVariable String q,
			HttpServletRequest request, HttpServletResponse response)
			throws QueryException, RedirectExecption, IOException,
			ServletException {
		query(QueryType.POSTALADDRESS, q, request, response);
	}

	/**
	 * query securedns by id
	 * @param q:securedns id
	 * @param request:http request
	 * @param response:http response
	 * @throws QueryException
	 * @throws RedirectExecption
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/secureDNS/{q}", method = RequestMethod.GET)
	@ResponseBody
	public void querySecureDNS(@PathVariable String q,
			HttpServletRequest request, HttpServletResponse response)
			throws QueryException, RedirectExecption, IOException,
			ServletException {
		query(QueryType.SECUREDNS, q, request, response);
	}

	/**
	 * query remark by id
	 * @param q: remark id
	 * @param request:http request
	 * @param response:http response
	 * @throws QueryException
	 * @throws RedirectExecption
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/remarks/{q}", method = RequestMethod.GET)
	@ResponseBody
	public void queryRemarks(@PathVariable String q,
			HttpServletRequest request, HttpServletResponse response)
			throws QueryException, RedirectExecption, IOException,
			ServletException {
		query(QueryType.REMARKS, q, request, response);
	}

	/**
	 * query variant by id
	 * @param q:variant id
	 * @param request:http request
	 * @param response:http response
	 * @throws QueryException
	 * @throws RedirectExecption
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/variants/{q}", method = RequestMethod.GET)
	@ResponseBody
	public void queryVariants(@PathVariable String q,
			HttpServletRequest request, HttpServletResponse response)
			throws QueryException, RedirectExecption, IOException,
			ServletException {
		query(QueryType.VARIANTS, q, request, response);
	}

	/**
	 * common query
	 * @param queryType:query type @see QueryType
	 * @param q:query param
	 * @param request :http request
	 * @param response:http response
	 * @throws QueryException
	 * @throws IOException
	 * @throws ServletException
	 */
	private void query(QueryType queryType, String q,
			HttpServletRequest request, HttpServletResponse response)
			throws QueryException, IOException, ServletException {
		Map<String, Object> resultMap = null;
		QueryParam queryParam = praseQueryParams(request);
		queryParam.setQueryType(queryType);
		queryParam.setQ(q);
		request.setAttribute("queryType", queryType.getName());
		if (!ValidateUtils.isCommonInvalidStr(queryParam.getQ())) {
			super.renderResponseError400(request, response,queryParam);
		} else {
			resultMap = queryService.query(queryParam);
			request.setAttribute("queryPara", q);
			renderResponse(request, response, resultMap, queryParam);
		}
	}

	/**
	 * other invalid query uri will response 400 error
	 */
	@RequestMapping(value = "/**")
	@ResponseBody
	public void error400(HttpServletRequest request,
			HttpServletResponse response) throws QueryException,
			RedirectExecption, IOException, ServletException {
		QueryParam queryParam = praseQueryParams(request);
		Map<String, Object> resultMap = WhoisUtil
				.processError(WhoisUtil.COMMENDRRORCODE,queryParam);
		renderResponse(request, response, resultMap, queryParam);
		return;
	}

	/**
	 * redirect exception handle,response 301
	 * @param ex:exception
	 * @param request:http request
	 * @param response:http response
	 * @return null
	 */
	@ExceptionHandler(value = { RedirectExecption.class })
	@ResponseStatus(value = HttpStatus.MOVED_PERMANENTLY)
	public String exp(Exception ex, HttpServletRequest request,
			HttpServletResponse response) {
		RedirectExecption rEx = (RedirectExecption) ex;
		response.setHeader("Accept", getFormatCookie(request));
		response.setHeader("Location", rEx.getRedirectURL());
		response.setHeader("Connection", "close");
		return null;
	}
}
