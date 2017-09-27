<%@page import="com.weilove.core.util.Sign"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<%
 String path=request.getContextPath();
 request.setAttribute("path", path);
%>
<html>
<head>
	<title>Home</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>WeUI</title>
    <script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    
    <script type="text/javascript">
    
	    wx.config({
	        debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
	        appId: '${appId}', // 必填，公众号的唯一标识
	        timestamp:'${timestamp}', // 必填，生成签名的时间戳
	        nonceStr: '${nonceStr}', // 必填，生成签名的随机串
	        signature:'${signature}',// 必填，签名，见附录1
	        jsApiList: [chooseImage] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
	    });
	    
	    wx.ready(function(){
	        // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
	    	alert("ok");
	    });
	    
	    
	    wx.checkJsApi({
	        jsApiList: ['chooseImage'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
	        success: function(res) {
	            // 以键值对的形式返回，可用的api值true，不可用为false
	            // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
	        	alert("ok1");
	        }
	    });
    </script>
    
</head>
<body>
<h1>
	Hello world!  
</h1>

<P>  The time on the server is ${serverTime}. ${openid}</P>
</body>
</html>
