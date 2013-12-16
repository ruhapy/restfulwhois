package com.cnnic.whois.controller.oauth;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cnnic.whois.util.OAuthProvider;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthMessage;
import net.oauth.server.OAuthServlet;

/**
 * Access Token request handler
 */
@Controller
public class AccessTokenController {
    
	@RequestMapping(value = "/access_token")
	public void list(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception{
		try{
            OAuthMessage requestMessage = OAuthServlet.getMessage(request, null);
            
            OAuthAccessor accessor = OAuthProvider.getAccessor(requestMessage);
            OAuthProvider.VALIDATOR.validateMessage(requestMessage, accessor);
            
//          TODO :  Not sure whether to delete this part
            // make sure token is authorized
//            if (!Boolean.TRUE.equals(accessor.getProperty("authorized"))) {
//                 OAuthProblemException problem = new OAuthProblemException("permission_denied");
//                throw problem;
//            }
            // generate access token and secret
            OAuthProvider.generateAccessToken(accessor);
            
            response.setContentType("text/plain");
            OutputStream out = response.getOutputStream();
            OAuth.formEncode(OAuth.newList("oauth_token", accessor.accessToken,
                                           "oauth_token_secret", accessor.tokenSecret),
                             out);
            out.close();
            
        } catch (Exception e){
            OAuthProvider.handleException(e, request, response, true);
        }
	}

}
