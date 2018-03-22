package com.lpdata.action;

//import org.apache.struts2.convention.annotation.Action;
//import org.apache.struts2.convention.annotation.Namespace;

import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zenngwei
 * @date 2018/03/21 15:44
 */

public class IndexAction {
    public void  index() throws IOException {
        System.out.println("进来了IndexAction的index方法");
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String requestURI = request.getRequestURI();
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        System.out.println(requestURI);
        response.sendRedirect("/login");
    }
}
