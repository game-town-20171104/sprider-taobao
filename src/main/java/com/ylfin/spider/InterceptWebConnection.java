package com.ylfin.spider;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.FalsifyingWebConnection;

import java.io.IOException;

class InterceptWebConnection extends FalsifyingWebConnection {
    public InterceptWebConnection(WebClient webClient) throws IllegalArgumentException{
        super(webClient);
    }
    @Override
    public WebResponse getResponse(WebRequest request) throws IOException {
        WebResponse response=super.getResponse(request);
        if(response.getWebRequest().getUrl().toString().startsWith("https://localhost.wwbizsrv.alibaba.com:4013")){
            System.out.println("https://localhost.wwbizsrv.alibaba.com:4013");
            return createWebResponse(response.getWebRequest(), "", "'application/x-javascript", 200, "Ok");
        }
        if(response.getWebRequest().getUrl().toString().startsWith("https://tce.alicdn.com/api/data.htm")){
            System.out.println("https://tce.alicdn.com/api/data.htm");
            return createWebResponse(response.getWebRequest(), "", "text/html", 200, "Ok");
        }

        return super.getResponse(request);
    }
}