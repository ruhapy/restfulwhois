package com.cnnic.whois.controller.oauth;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cnnic.whois.bean.oauth.UserApp;
import com.cnnic.whois.dao.oauth.UserAppDao;
/**
 * oauth app controller
 * @author nic
 *
 */
@Controller
public class AppController {
	
	@Autowired
	private UserAppDao userAppDao;
	/**
	 * list all app
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/app")
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception{
		String idValue = String.valueOf(request.getParameter("user_id"));
		List<UserApp> userAppList = userAppDao.getUserApps(Integer.valueOf(idValue));
		model.addAttribute("list", userAppList);
		model.addAttribute("user_id", idValue);
		return "/oauth/user_app";
	}
	/**
	 * update app info
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/app/add-update")
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception{
		String id = request.getParameter("id");
		UserApp userApp = null;
		if(id != null && !"".equals(id)){
			userApp = userAppDao.getUserAppById(Integer.valueOf(request.getParameter("id")));
		}
		model.addAttribute("userApp", userApp);
		model.addAttribute("user_id", request.getParameter("user_id"));
		return "/oauth/user_app_update";
	}
	/**
	 * save app info
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/app/save")
	public String save(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception{
		String id = request.getParameter("id");
		String app_description = request.getParameter("app_description");
		String user_id = request.getParameter("user_id");
		UserApp userApp = new UserApp();
		userApp.setUser_id(Integer.valueOf(user_id));
		userApp.setApp_description(app_description);
		if(id != null && !"".equals(id)){
			userAppDao.update(Integer.valueOf(id), userApp);
		}else {
			userAppDao.save(new UserApp(app_description, Integer.valueOf(user_id)));
		}
		return "redirect:/app?user_id="+user_id;
	}
	/**
	 * delete app info
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/app/delete")
	public String delete(HttpServletRequest request, HttpServletResponse response ) throws Exception{
		userAppDao.delete(Integer.valueOf(request.getParameter("id")));
		String user_id = request.getParameter("user_id");
		return "redirect:/app?user_id="+user_id;
	}

}
