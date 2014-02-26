 package com.server.proxy;
 
 import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
 public class ProxyServlet extends HttpServlet
   implements Servlet
 {
   static final long serialVersionUID = 1L;
   private Boolean debug;
   private Logger logger;
   private GetMethodProxy proxy_get;
   private PostMethodProxy proxy_post;
 
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
     throws ServletException, IOException
   {
     try
     {
       getProxy_get().doGet(request, response);
     } catch (ProxyException e) {
       response.sendError(e.getErrorCode());
       getLogger().warning("代理过程中错�?" + e.getMessage());
     } catch (Throwable e) {
       getLogger().log(Level.SEVERE, "代理过程出现严重异常", e);
     }
   }
 
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
     throws ServletException, IOException
   {
     try
     {
       getProxy_post().doPost(request, response);
     } catch (ProxyException e) {
       response.sendError(e.getErrorCode());
       getLogger().warning("代理过程中错�?" + e.getMessage());
     } catch (Throwable e) {
       getLogger().log(Level.SEVERE, "代理过程出现严重异常", e);
     }
   }
 
   private Logger getLogger()
   {
     if (this.logger == null) {
       String strLoggerName = getClass().getName() + 
         getServletContext().hashCode();
       this.logger = Logger.getLogger(strLoggerName);
 
       if (isDebugMode())
         this.logger.setLevel(Level.ALL);
       else {
         this.logger.setLevel(Level.WARNING);
       }
     }
     return this.logger;
   }
 
   private String getLogEncoding()
   {
     String enc = getInitParameter("LogEncoding");
     if (enc == null) return "GB2312";
     return enc;
   }
 
   public GetMethodProxy getProxy_get()
   {
     if (this.proxy_get == null) {
       this.proxy_get = new GetMethodProxy(Boolean.valueOf(isDebugMode()), getLogger(), getLogEncoding());
     }
     return this.proxy_get;
   }
 
   public PostMethodProxy getProxy_post()
   {
     if (this.proxy_post == null) {
       this.proxy_post = new PostMethodProxy(Boolean.valueOf(isDebugMode()), getLogger(), getLogEncoding());
     }
     return this.proxy_post;
   }
 
   private boolean isDebugMode()
   {
     if (this.debug == null) {
       String strDebugFlag = getInitParameter("Debug");
       if (strDebugFlag == null) {
         this.debug = Boolean.valueOf(false);
       }
       else if (strDebugFlag.trim().equalsIgnoreCase("true"))
         this.debug = Boolean.valueOf(true);
       else {
         this.debug = Boolean.valueOf(false);
       }
     }
 
     return this.debug.booleanValue();
   }
 }