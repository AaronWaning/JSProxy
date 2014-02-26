 package com.server.proxy;
 
 import java.io.ByteArrayOutputStream;
 import java.io.IOException;
 import java.io.OutputStream;
 import java.net.HttpURLConnection;
 import java.net.MalformedURLException;
 import java.net.URL;
 import java.net.URLDecoder;
 import java.util.logging.Logger;
 import javax.servlet.ServletException;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 /**
  * 
  * date: 2014-2-26 上午11:19:34 <br/>
  *
  * @author wan_song
  * @version 
  * @since JDK 1.6
  */
 public class GetMethodProxy extends AbstractProxy
 {
   public GetMethodProxy(Boolean debug, Logger logger, String logEncoding)
   {
     super(debug, logger, logEncoding);
   }
 
   void doGet(HttpServletRequest request, HttpServletResponse response)
     throws ProxyException
   {
     String eviRequestInfo = getRequestInfo(request);
 
     String vParam_about = request.getParameter("about");
     String vParam_request = request.getParameter("request");
 
     if (vParam_about != null)
       try {
         process_about(response);
       } catch (IOException e) {
         throw new ProxyException(500, String.format("%s;关于对话处理异常", new Object[] { eviRequestInfo }));
       }
     else if (vParam_request != null) {
       if (vParam_request.equals("gotourl"))
         try {
           process_gotourl(request, response);
         } catch (IOException e) {
           throw new ProxyException(500, String.format("%s;转跳处理异常", new Object[] { eviRequestInfo }));
         } catch (ServletException e) {
           throw new ProxyException(500, String.format("%s;转跳处理异常", new Object[] { eviRequestInfo }));
         }
     }
     else
       throw new ProxyException(400, String.format("%s;代理请求参数不正确[%s]", new Object[] { eviRequestInfo, request.getQueryString() }));
   }
 
   void process_gotourl(HttpServletRequest request, HttpServletResponse response)
     throws ServletException, IOException, ProxyException
   {
     String eviRequestInfo = getRequestInfo(request);
 
     String pServURLString = request.getParameter("url");
     if (pServURLString == null) throw new ProxyException(400, eviRequestInfo + ";" + "[url]参数缺失");
     pServURLString = URLDecoder.decode(pServURLString, "UTF-8");
 
     getLogger().info(getRequestInfo(request) + ";" + "TargetURL[" + pServURLString + "]");
     URL pServiceURL=null;
     try
     {
       pServiceURL = new URL(pServURLString);
     }
     catch (MalformedURLException e)
     {
       throw new ProxyException(400, eviRequestInfo + ";" + "URL格式非法");
     }
     HttpURLConnection pServiceConn = createServiceConnection(pServiceURL, "GET", 3);
     try
     {
       ByteArrayOutputStream pProxyReceivedBuffer = new ByteArrayOutputStream(1024);
 
       pipe_Service2Proxy(pServiceConn, pProxyReceivedBuffer);
 
       pipe_Proxy2Client(response, pServiceConn, pProxyReceivedBuffer.toByteArray());
     } catch (IOException e) {
       throw new ProxyException(500, eviRequestInfo + ";" + "处理异常[" + e.getLocalizedMessage() + "]");
     } finally {
       pServiceConn.disconnect();
     }
   }
 
   private void process_about(HttpServletResponse response)
     throws IOException
   {
     String pMessage = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\"><title>EzServerProxy6 Help</title></head><body><p>EzServerProxy6小服务：</p><p>1)使用POST方法向此URL发�?信息</p><p>2)将要目标服务位置，放入HTTP POST 参数里面，目标服务URL放入[url]�?要发送的XML数据,放入[xml]参数中�?</p></body></html>";
 
     byte[] pbinMessage = pMessage.getBytes("gb2312");
     response.setContentType("text/html;charset=gb2312");
     response.setContentLength(pbinMessage.length);
     OutputStream pOS = response.getOutputStream();
     pOS.write(pbinMessage);
     pOS.flush();
   }
 }