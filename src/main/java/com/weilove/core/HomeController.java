package com.weilove.core;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

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
import com.weilove.core.util.Sign;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	/**
	 * git 测试
	 * */
	private String appID="wx764d5a3a5ed0fb1c";
	private String appsecret="26c573bb6dd7991ee14ddaae3a561a26";
	private String URL="https://weilove.applinzi.com/weixin/sign.do";
	private String Token="weixin";
	/**
	 * access_token  两小时更新一次
	 * */
	private String access_token="LD5l7FYU9BJLrNdNJg_RiAMN3ozlt3r4eUxtcc6ctYBCbs-lu6n6hk-DuMfrkmtFczK9TkWBzP0Z3lZpmPC3vmzChKR7VmhzInrcVaZEDP8vGSRYYRyDUIb-Hdlpz8X3ITLfAFACNF";
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws Exception 
	 */
	@RequestMapping(value = "/home.do", method = RequestMethod.GET)
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
	   	   model.addAttribute("openid",openid);
	   }
        
        model.addAttribute("appID", appID);
       
        String jsapi_ticket = getticket();
        
        

        //注意 URL 一定要动态获取，不能 hardcode
        String url = request.getRequestURL().toString();
        
        Map<String, String> ret = Sign.sign(jsapi_ticket, url);
        
        model.addAllAttributes(ret);
    	
		return "home";
	}
	
	public String getticket(){
		//String url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx764d5a3a5ed0fb1c&secret=26c573bb6dd7991ee14ddaae3a561a26";
		String getticketUrl= "http://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token="+access_token;
		//http://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=LD5l7FYU9BJLrNdNJg_RiAMN3ozlt3r4eUxtcc6ctYBCbs-lu6n6hk-DuMfrkmtFczK9TkWBzP0Z3lZpmPC3vmzChKR7VmhzInrcVaZEDP8vGSRYYRyDUIb-Hdlpz8X3ITLfAFACNF
		 String json = HttpsGetUtil.doHttpsGetJson(getticketUrl);
	       JSONObject jsonObject = JSONObject.fromObject(json);
	       String ticket = jsonObject.getString("ticket");
	       return ticket;
	}
	
}
