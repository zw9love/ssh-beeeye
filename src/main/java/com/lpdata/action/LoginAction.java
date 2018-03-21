package com.lpdata.action;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author zenngwei
 * @date 2018/03/21 15:09
 */
public class LoginAction extends ActionSupport {
    public String execute() throws Exception {
        System.out.println("进来了LoginAction的execute默认方法");
        return SUCCESS;
    }

    public String root(){
        System.out.println("进来了LoginActiond的root方法");
        return SUCCESS;
    }
}
