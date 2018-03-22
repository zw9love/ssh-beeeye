package com.lpdata.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zenngwei
 * @date 2018/03/22 11:38
 */
public class RequestInterceptor extends MethodFilterInterceptor {
    @Override
    protected String doIntercept(ActionInvocation actionInvocation) throws Exception {
        // System.out.println(invocation.getInvocationContext().getParameters());
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String requestURI = request.getRequestURI();
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        System.out.println(requestURI);
        if (requestURI.equals("/")){
            response.sendRedirect("/login");
            return null;
        }
        else{
            return actionInvocation.invoke();
        }
        // // 前处理
        // System.out.println("RequestMethodInterceptor 的前处理!");
        // // 放行
        // String result = invocation.invoke();
        // // 后处理
        // System.out.println("RequestMethodInterceptor 的后处理!");
        // return result;
    }
}
