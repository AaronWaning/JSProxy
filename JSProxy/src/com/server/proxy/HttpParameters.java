package com.server.proxy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpParameters
{
  private Map<String, String> params = new HashMap<String,String>();

  public HttpParameters(String paramsString) throws UnsupportedEncodingException
  {
    System.out.println(paramsString);

    String currentEncoding = "UTF-8";
    boolean encodingChanged = false;

    String[] pvs = paramsString.split("\\&");
    for (int i = 0; i < pvs.length; i++)
    {
      String[] param_value = pvs[i].split("\\=", 2);
      if (param_value.length == 2) {
        String key = URLDecoder.decode(param_value[0], currentEncoding);
        String value = URLDecoder.decode(param_value[1], currentEncoding);

        if (!encodingChanged)
          if ((isEncodingValid(key)) && (isEncodingValid(value))) {
            this.params.put(key, value);
          }
          else {
            encodingChanged = true;
            currentEncoding = "GB2312";

            key = URLDecoder.decode(param_value[0], currentEncoding);
            value = URLDecoder.decode(param_value[1], currentEncoding);
            this.params.put(key, value);
          }
      }
    }
  }

  public static HttpParameters createInstance(InputStream is)
    throws IOException
  {
    ByteArrayOutputStream buf = new ByteArrayOutputStream(4096);
    while (true)
    {
      int b = is.read();
      if (b == -1) break; buf.write(b);
    }

    byte[] idata = buf.toByteArray();
    String paramsString = new String(idata, "UTF-8");

    return new HttpParameters(paramsString);
  }

  public String getParameter(String key)
  {
    return (String)this.params.get(key);
  }

  private boolean isEncodingValid(String str)
  {
    for (int f = 0; f < str.length(); f++) {
      if (str.charAt(f) == 65533) return false;
    }
    return true;
  }
}