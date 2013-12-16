package com.cnnic.whois.controller.oauth;

import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cnnic.whois.util.OAuthProvider;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.server.OAuthServlet;

/**
 * Request token request handler
 */
@Controller
public class RequestTokenController {

	@PostConstruct
	public void init() throws ServletException {
      try{
          OAuthProvider.loadConsumers();
      }catch(IOException e){
          throw new ServletException(e.getMessage());
      }
	}
    
    @RequestMapping(value = "/request_token")
	public void list(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception{
    	try {
            OAuthMessage requestMessage = OAuthServlet.getMessage(request, null);
            
            OAuthConsumer consumer = OAuthProvider.getConsumer(requestMessage);
            
            OAuthAccessor accessor = new OAuthAccessor(consumer);
            OAuthProvider.VALIDATOR.validateMessage(requestMessage, accessor);
            {
                // Support the 'Variable Accessor Secret' extension
                // described in http://oauth.pbwiki.com/AccessorSecret
                String secret = requestMessage.getParameter("oauth_accessor_secret");
                if (secret != null) {
                    accessor.setProperty(OAuthConsumer.ACCESSOR_SECRET, secret);
                }
            }
            // generate request_token and secret
            OAuthProvider.generateRequestToken(accessor);
            
            response.setContentType("text/plain");
            OutputStream out = response.getOutputStream();
            OAuth.formEncode(OAuth.newList("oauth_token", accessor.requestToken,
                                           "oauth_token_secret", accessor.tokenSecret),
                             out);
            out.close();
            
        } catch (Exception e){
            OAuthProvider.handleException(e, request, response, true);
        }
	}
    

}
