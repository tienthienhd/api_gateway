package com.syl.ai.zuul_server.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import javax.servlet.http.HttpServletRequest;

public class AddHeaderFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        final RequestContext ctx = RequestContext.getCurrentContext();
        final HttpServletRequest request = ctx.getRequest();
        //Here is the authorization header being read.
        final String xAuth = request.getHeader("Authorization");
        //Use the below method to add anything to the request header to read downstream. if needed.
        ctx.addZuulRequestHeader("abc", "abc");
        return null;
    }
}
