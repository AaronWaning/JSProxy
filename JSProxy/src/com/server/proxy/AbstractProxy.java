 package com.server.proxy;
 
 import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *  
 * ClassName: AbstractProxy <br/>
 * date: 2014-2-26 上午11:19:17 <br/>
 *
 * @author wan_song
 * @version 
 * @since JDK 1.6
 */
public class AbstractProxy
 {
   private final String logEncoding;
   private final Boolean debug;
   private final Logger logger;
 
   public AbstractProxy(Boolean debug, Logger logger, String logEncoding)
   {
     this.debug = debug;
     this.logger = logger;
     this.logEncoding = logEncoding;
   }
 
   public String getLogEncoding()
   {
     return this.logEncoding;
   }
 
   protected String getRequestInfo(HttpServletRequest request)
   {
     String pRemote = "Remote[" + request.getRemoteAddr() + "];";
     String pSession = "Session[" + request.getSession().hashCode() + "];";
     String pServ = "Request[" + request.hashCode() + "];";
     return pRemote + pSession + pServ;
   }
 
   protected String getAbbreviatoryInfo(String vString)
   {
     String vShort = vString;
     if (vString.length() > 240) {
       vShort = vString.substring(0, 240);
       vShort = vShort + "...";
     }
 
     return vShort.replaceAll("[\r\n]", "");
   }
 
   protected Logger getLogger()
   {
     return this.logger;
   }
 
   protected boolean isDebugMode()
   {
     return this.debug.booleanValue();
   }
 
   protected HttpURLConnection createServiceConnection(URL serviceURL, String method, int tryTime)
     throws ProxyException
   {
     HttpURLConnection pServiceConn = null;
     try {
       pServiceConn = createServiceConnection(serviceURL, method);
     }
     catch (IOException localIOException)
     {
     }
     if (pServiceConn == null) throw new ProxyException(504, "远程连接错误");
     return pServiceConn;
   }
 
   private HttpURLConnection createServiceConnection(URL serviceURL, String method)
     throws IOException
   {
     HttpURLConnection pServiceConn = null;
     try
     {
       pServiceConn = (HttpURLConnection)serviceURL.openConnection(Proxy.NO_PROXY);
     } catch (UnsupportedOperationException e) {
       pServiceConn = (HttpURLConnection)serviceURL.openConnection();
     }
 
     if (method.equalsIgnoreCase("GET")) pServiceConn.setDoOutput(false); else {
       pServiceConn.setDoOutput(true);
     }
     pServiceConn.setInstanceFollowRedirects(true);
     pServiceConn.setAllowUserInteraction(false);
     pServiceConn.setDoInput(true);
     pServiceConn.setUseCaches(false);
     pServiceConn.setRequestMethod(method);
     return pServiceConn;
   }
 
   protected void pipe_Service2Proxy(HttpURLConnection pServiceConn, OutputStream pProxyReceivedBuffer)
     throws IOException, ProxyException
   {
     if (pServiceConn.getResponseCode() == 200) {
       InputStream pServiceIS = pServiceConn.getInputStream();
 
       int pByte = pServiceIS.read();
       while (pByte != -1) {
         pProxyReceivedBuffer.write(pByte);
         pByte = pServiceIS.read();
       }
     } else {
       throw new ProxyException(pServiceConn.getResponseCode(), "远端服务器返回码异常");
     }
   }
 
   protected void pipe_Proxy2Client(HttpServletResponse response, HttpURLConnection pServiceConn, byte[] pData2Send)
     throws IOException
   {
     response.setContentType(pServiceConn.getContentType());
     response.setContentLength(pServiceConn.getContentLength());
     OutputStream pClientOS = response.getOutputStream();
     pClientOS.write(pData2Send);
     pClientOS.flush();
   }
 }




