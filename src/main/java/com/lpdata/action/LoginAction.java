package com.lpdata.action;

import com.alibaba.fastjson.JSONObject;
import com.lpdata.bean.User;
import com.lpdata.util.MD5Util;
import com.lpdata.util.MyUtil;
import com.lpdata.validator.LoginValidator;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.springframework.jdbc.core.RowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author zenngwei
 * @date 2018/03/21 15:09
 */
public class LoginAction extends ActionSupport {
    private static final Logger logger = LoggerFactory.getLogger(LoginAction.class);
    private static SessionFactory sf;
    static {
        //创建SessionFactory对象
        sf = new Configuration().configure().buildSessionFactory();
    }

    public String execute() throws Exception {
        System.out.println("进来了LoginAction的execute默认方法");
        return SUCCESS;
    }

    public void doLogin() throws Exception {
        System.out.println("进来了doLogin方法");
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        Map<String, Object> json = MyUtil.getJsonData(request);
        JSONObject jsonObj;
        JSONObject checkObj = new LoginValidator().dologinValidate(json);
        if (checkObj.getInteger("status").equals(200)) {
            String loginName = json.get("login_name").toString();
            String loginPwd = MD5Util.encrypt(json.get("login_pwd").toString());
            logger.info("login_name = " + loginName);
            logger.info("login_pwd = " + loginPwd);
            logger.info("科比科比科比");
            String sql = " SELECT * FROM common_user where login_name = :login_name and login_pwd = :login_pwd ";
            Session session = sf.openSession();
            Transaction tx = session.beginTransaction();
            NativeQuery nativeQuery = session.createNativeQuery(sql);
            nativeQuery.setParameter("login_name", loginName);
            nativeQuery.setParameter("login_pwd", loginPwd);
            List<User> list = nativeQuery.addEntity(User.class).list();
            //关闭
            tx.commit();
            session.close();
            if (list.size() > 0) {
                JSONObject obj = new JSONObject();
                String userLoginName = list.get(0).getLoginName();
                String userName = list.get(0).getUsername();
                String ids = list.get(0).getIds();
                int expireTime = MyUtil.getRefreshTime();
                obj.put("login_name", userLoginName);
                obj.put("username", userName);
                obj.put("ids", ids);
                obj.put("expireTime", expireTime);
                HttpSession httpSession = request.getSession();
                String token = MyUtil.getRandomString();
                httpSession.setAttribute(token, obj);
                httpSession.setAttribute(userLoginName, token);
                response.setHeader("token", token);
                jsonObj = MyUtil.getJson("成功", 200, null);
            } else
                jsonObj = MyUtil.getJson("用户名或密码错误", 606, null);
        } else
            jsonObj = MyUtil.getJson(checkObj.getString("msg"), 606, null);

        response.getWriter().write(jsonObj.toString());
    }
}
