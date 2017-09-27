package com.weilove.core;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.weilove.core.util.HttpsGetUtil;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	private String appID="wx764d5a3a5ed0fb1c";
	private String appsecret="26c573bb6dd7991ee14ddaae3a561a26";
	private String URL="https://weilove.applinzi.com/weixin/sign.do";
	private String Token="weixin";
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws Exception 
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		//静默授权
        String get_access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?"
                + "appid="
                + appID
                + "&secret="
                + appsecret
                + "&code=CODE&grant_type=authorization_code";

        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String code = request.getParameter("code");
        System.out.println("******************code=" + code);
        if(code!=null&&!"".equals(code)){
	       get_access_token_url = get_access_token_url.replace("CODE", code);
	       String json = HttpsGetUtil.doHttpsGetJson(get_access_token_url);
	       JSONObject jsonObject = JSONObject.fromObject(json);
	       String openid = jsonObject.getString("openid");
	       HttpSession session=request.getSession();
	       session.setAttribute("openid", openid);
	   	  // model.addAttribute("openid",openid);
	   }
       
    	
		return "home";
	}
	
}
