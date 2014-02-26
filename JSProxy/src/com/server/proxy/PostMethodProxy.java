package com.server.proxy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * date: 2014-2-26 上午11:19:48 <br/>
 * 
 * @author wan_song
 * @version
 * @since JDK 1.6
 */
public class PostMethodProxy extends AbstractProxy {
	public PostMethodProxy(Boolean debug, Logger logger, String logEncoding) {
		super(debug, logger, logEncoding);
	}

	void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ProxyException {
		HttpParameters requestParams = null;
		String eviRequestInfo = getRequestInfo(request);
		try {
			requestParams = HttpParameters.createInstance(request
					.getInputStream());
		} catch (IOException e) {
			throw new ProxyException(500, eviRequestInfo + ";" + "输入流异");
		}
		String pServURLString = requestParams.getParameter("url");
		if (pServURLString == null)
			throw new ProxyException(400, eviRequestInfo + ";" + "[url]参数缺失");
		String pProxySendXML = requestParams.getParameter("xml");
		if (pProxySendXML == null)
			throw new ProxyException(400, eviRequestInfo + ";" + "[xml]参数缺失");

		String recvLog_client = String.format("%s; TargetURL[%s]; SendXML[%s]",
				new Object[] { eviRequestInfo, pServURLString,
						getAbbreviatoryInfo(pProxySendXML) });
		getLogger().info(recvLog_client);
		URL pServiceURL = null;
		try {
			pServiceURL = new URL(pServURLString);
		} catch (MalformedURLException e) {
			throw new ProxyException(400, eviRequestInfo + ";" + "URL格式非法["
					+ pServURLString + "]");
		}
		HttpURLConnection pServiceConn = createServiceConnection(pServiceURL,
				"POST", 3);

		ByteArrayOutputStream pProxyReceivedBuffer = new ByteArrayOutputStream(
				1024);
		try {
			pipe_Proxy2Service(request, pProxySendXML, pServiceConn);

			pipe_Service2Proxy(pServiceConn, pProxyReceivedBuffer);

			pipe_Proxy2Client(response, pServiceConn, pProxyReceivedBuffer
					.toByteArray());
		} catch (IOException e) {
			throw new ProxyException(500, eviRequestInfo + ";" + "数据代理传输过程中异"
					+ e.getLocalizedMessage());
		} finally {
			pServiceConn.disconnect();
		}

		try {
			String recvData_service = pProxyReceivedBuffer
					.toString(getLogEncoding());
			String recvLog_service = String.format("%s;RecvXML[%s]",
					new Object[] { eviRequestInfo,
							getAbbreviatoryInfo(recvData_service) });
			getLogger().info(recvLog_service);
		} catch (UnsupportedEncodingException e) {
			String recvData_service = pProxyReceivedBuffer.toString();
			String recvLog_service = String.format("%s;RecvXML[%s]",
					new Object[] { eviRequestInfo,
							getAbbreviatoryInfo(recvData_service) });
			getLogger().info(recvLog_service);
		}
	}

	protected void pipe_Proxy2Service(HttpServletRequest request,
			String pProxySendXML, HttpURLConnection pServiceConn)
			throws IOException {
		String strSendData = "xml=" + URLEncoder.encode(pProxySendXML, "UTF-8")
				+ "&jsonstr=" + URLEncoder.encode(pProxySendXML, "UTF-8");
		byte[] bPart = strSendData.getBytes("UTF-8");
		System.out.println("请求数据" + strSendData);
		pServiceConn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		pServiceConn.setRequestProperty("Content-Length", Integer
				.toString(bPart.length));

		OutputStream pServiceOS = pServiceConn.getOutputStream();
		pServiceOS.write(bPart);
		pServiceOS.flush();
	}
}