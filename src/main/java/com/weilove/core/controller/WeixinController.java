package com.weilove.core.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.weilove.core.HomeController;


@Controller
@RequestMapping("/weixin")
public class WeixinController{  
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private static Map<String ,String> m=new HashMap<String ,String>();
	static{
		m.put("20170525","三刘志权");
	}
	
	
	@RequestMapping(value="/sign.do",method =RequestMethod.GET)
	public void valid(HttpServletRequest final_request,HttpServletResponse final_response){  
        String echostr=final_request.getParameter("echostr");  
        if(null==echostr||echostr.isEmpty()){  
            responseMsg(final_request,final_response);  
        }else{  
            if(this.checkSignature(final_request)){  
                this.print(echostr,final_response);  
            }else{  
                this.print("error",final_response);                                                                                                                                                                                                                                                                                                                                           
            }  
        }  
    }  
    
	//自动回复内容  
	@RequestMapping(value="/sign.do",method =RequestMethod.POST)
    public void responseMsg(HttpServletRequest final_request,HttpServletResponse final_response){  
       
		logger.info("Welcome home!");
		String textValue="欢迎使用好邻居系统";
		 
		String postStr=null;  
        try{  
            postStr=this.readStreamParameter(final_request.getInputStream());  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
        //System.out.println(postStr);  
        if (null!=postStr&&!postStr.isEmpty()){  
            Document document=null;  
            try{  
                document = DocumentHelper.parseText(postStr);  
            }catch(Exception e){  
                e.printStackTrace();  
            }  
            if(null==document){  
                this.print("",final_response);  
                return;  
            }  
            Element root=document.getRootElement();  
            String fromUsername = root.elementText("FromUserName");  
            String toUsername = root.elementText("ToUserName");  
            String keyword = root.elementTextTrim("Content");  
            String MsgType = root.elementTextTrim("MsgType");
            String Event = root.elementTextTrim("Event");  
            
            String time = new Date().getTime()+"";  
          
            String textTpl = "<xml>"+  
                        "<ToUserName><![CDATA[%1$s]]></ToUserName>"+  
                        "<FromUserName><![CDATA[%2$s]]></FromUserName>"+  
                        "<CreateTime>%3$s</CreateTime>"+  
                        "<MsgType><![CDATA[%4$s]]></MsgType>"+  
                        "<Content><![CDATA[%5$s]]></Content>"+  
                        "<FuncFlag>0</FuncFlag>"+  
                        "</xml>";               
              
            
            if(null!=keyword&&!keyword.equals("")){  
                String msgType = "text";  
                String contentStr = textValue;  
                String resultStr = textTpl.format(textTpl, fromUsername, toUsername, time, msgType, contentStr);  
                this.print(resultStr,final_response);  
            }else if("event".equals(MsgType)){
            	if("subscribe".equals(Event)){
            		 String msgType = "text";  
                     String contentStr = "欢迎您关注好邻居公众号！！！";  
                     String resultStr = textTpl.format(textTpl, fromUsername, toUsername, time, msgType, contentStr);  
                     this.print(resultStr,final_response);  
            	}
            	
                if("unsubscribe".equals(Event)){
                	 String msgType = "text";  
                     String contentStr = "您已经取消好邻居公众号！！！";  
                     String resultStr = textTpl.format(textTpl, fromUsername, toUsername, time, msgType, contentStr);  
                     this.print(resultStr,final_response);  
            	}
            	
            }else{  
                this.print("Input something...",final_response);  
            }  
  
        }else {  
            this.print("",final_response);  
        }  
    }  
	
	
    //微信接口验证  
    public boolean checkSignature(HttpServletRequest final_request){  
        String signature = final_request.getParameter("signature");  
        String timestamp = final_request.getParameter("timestamp");  
        String nonce = final_request.getParameter("nonce");  
        String token="weixin";  
        String[] tmpArr={token,timestamp,nonce};  
        Arrays.sort(tmpArr);  
        String tmpStr=this.ArrayToString(tmpArr);  
        tmpStr=this.SHA1Encode(tmpStr);  
        if(tmpStr.equalsIgnoreCase(signature)){  
            return true;  
        }else{  
            return false;  
        }  
    }  
    //向请求端发送返回数据  
    public void print(String content,HttpServletResponse final_response){  
        try{ 
        	final_response.setCharacterEncoding("UTF-8");  
            final_response.getWriter().print(content);  
            final_response.getWriter().flush();  
            final_response.getWriter().close();  
        }catch(Exception e){  
              
        }  
    }  
    //数组转字符串  
    public String ArrayToString(String [] arr){  
        StringBuffer bf = new StringBuffer();  
        for(int i = 0; i < arr.length; i++){  
         bf.append(arr[i]);  
        }  
        return bf.toString();  
    }  
    //sha1加密  
    public String SHA1Encode(String sourceString) {  
        String resultString = null;  
        try {  
           resultString = new String(sourceString);  
           MessageDigest md = MessageDigest.getInstance("SHA-1");  
           resultString = byte2hexString(md.digest(resultString.getBytes()));  
        } catch (Exception ex) {  
        }  
        return resultString;  
    }  
    public final String byte2hexString(byte[] bytes) {  
        StringBuffer buf = new StringBuffer(bytes.length * 2);  
        for (int i = 0; i < bytes.length; i++) {  
            if (((int) bytes[i] & 0xff) < 0x10) {  
                buf.append("0");  
            }  
            buf.append(Long.toString((int) bytes[i] & 0xff, 16));  
        }  
        return buf.toString().toUpperCase();  
    }  
    //从输入流读取post参数  
    public String readStreamParameter(ServletInputStream in){  
        StringBuilder buffer = new StringBuilder();  
        BufferedReader reader=null;  
        try{  
            reader = new BufferedReader(new InputStreamReader(in));  
            String line=null;  
            while((line = reader.readLine())!=null){  
                buffer.append(line);  
            }  
        }catch(Exception e){  
            e.printStackTrace();  
        }finally{  
            if(null!=reader){  
                try {  
                    reader.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return buffer.toString();  
    }  
    
}  
