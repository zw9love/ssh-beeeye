package com.lpdata.action;

//import org.apache.struts2.convention.annotation.Action;
//import org.apache.struts2.convention.annotation.Namespace;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zenngwei
 * @date 2018/03/21 15:44
 */

public class IndexAction {
    private static final Logger logger = LoggerFactory.getLogger(IndexAction.class);
    public void  index() throws IOException {
        logger.debug("进来了IndexAction的index方法");
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String requestURI = request.getRequestURI();
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        response.sendRedirect("/login");
    }
}
