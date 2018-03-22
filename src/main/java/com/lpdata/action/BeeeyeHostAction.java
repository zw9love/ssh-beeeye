package com.lpdata.action;

import com.alibaba.fastjson.JSONObject;
import com.lpdata.bean.Host;
import com.lpdata.util.MD5Util;
import com.lpdata.util.MyUtil;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * @author zenngwei
 * @date 2018/03/21 16:27
 */
public class BeeeyeHostAction extends ActionSupport {
    private String tableName = "beeeye_host";
    private static SessionFactory sf;
    static {
        //创建SessionFactory对象
        sf = new Configuration().configure().buildSessionFactory();
    }

    public void get() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        String select = " SELECT * FROM  " + tableName;
        String count = " SELECT count(*) FROM  " + tableName;
        Map<String, Object> json = MyUtil.getJsonData(request);
        Map<String, Object> page = (Map<String, Object>) json.get("page");
        String host_ids = MyUtil.getString(json, "host_ids");
        int pageNumber = MyUtil.getInt(page, "pageNumber");
        int pageSize = MyUtil.getInt(page, "pageSize");
        int pageStart = (pageNumber - 1) * pageSize;
        String pageSql = " limit :pageStart, :pageSize ";
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        JSONObject data;
        if (host_ids != null) {
            String where = " where host_ids = ? ";
            String sql = select + where;
            NativeQuery nativeQuery = session.createNativeQuery(sql);
            //把每一行记录封装为指定的对象类型
            List list = nativeQuery.addEntity(Host.class).list();
            data = MyUtil.getJson("成功", 200, list.get(0));
        } else {
            NativeQuery nativeQuery = session.createNativeQuery(select + pageSql);
            nativeQuery.setParameter("pageStart", pageStart);
            nativeQuery.setParameter("pageSize", pageSize);
            //把每一行记录封装为指定的对象类型
            List list = nativeQuery.addEntity(Host.class).list();
            NativeQuery countQuery = session.createNativeQuery(count);
            int totalRow = ((Number) countQuery.uniqueResult()).intValue();
//        int totalRow = ((BigInteger)countQuery.list().get(0)).intValue();
            JSONObject pageJson = MyUtil.getPageJson(list, pageNumber, pageSize, totalRow);
            data = MyUtil.getJson("成功", 200, pageJson);
        }

        //关闭
        tx.commit();
        session.close();
        response.getWriter().write(data.toString());
    }

    public void post() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        Map<String, Object> json = MyUtil.getJsonData(request);
        String name = MyUtil.getString(json, "name");
        String ip = MyUtil.getString(json, "ip");
        int port = MyUtil.getInt(json, "port");
        int status = MyUtil.getInt(json, "status");
        String os_type = MyUtil.getString(json, "os_type");
        String os_version = MyUtil.getString(json, "os_version");
        String os_arch = MyUtil.getString(json, "os_arch");
        String login_name = MyUtil.getString(json, "login_name");
        String login_pwd = MD5Util.encrypt(MyUtil.getString(json, "login_pwd"));
        String hostIds = MyUtil.getRandomString();
        Host host = new Host();
        host.setHost_ids(hostIds);
        host.setName(name);
        host.setIp(ip);
        host.setPort(port);
        host.setStatus(status);
        host.setOs_arch(os_arch);
        host.setOs_version(os_version);
        host.setOs_type(os_type);
        host.setLogin_name(login_name);
        host.setLogin_pwd(login_pwd);
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        session.save(host);
        //关闭
        tx.commit();
        session.close();
        JSONObject data = MyUtil.getJson("成功", 200, null);
        response.getWriter().write(data.toString());
    }

    public void put() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        Map<String, Object> json = MyUtil.getJsonData(request);
        String name = MyUtil.getString(json, "name");
        String ip = MyUtil.getString(json, "ip");
        int port = MyUtil.getInt(json, "port");
        int status = MyUtil.getInt(json, "status");
        String hostIds = MyUtil.getRandomString();
        Host host = new Host();
        host.setHost_ids(hostIds);
        host.setName(name);
        host.setIp(ip);
        host.setPort(port);
        host.setStatus(status);
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        session.update(host);
        //关闭
        tx.commit();
        session.close();
        JSONObject data = MyUtil.getJson("成功", 200, null);
        response.getWriter().write(data.toString());
    }

    public void delete() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        Map<String, Object> json = MyUtil.getJsonData(request);
        String host_ids = MyUtil.getString(json, "host_ids");
        Host host = new Host();
        host.setHost_ids(host_ids);
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        session.delete(host);
        //关闭
        tx.commit();
        session.close();
        JSONObject data = MyUtil.getJson("成功", 200, null);
        response.getWriter().write(data.toString());
    }

    public void testSpring() throws IOException {
        System.out.println("进来了");
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        System.out.print("111");
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        System.out.print(context);
        Host host = context.getBean("host", Host.class);
        System.out.println("进来了222");
        host.setName("麦迪");
        response.getWriter().write(MyUtil.getJson("成功", 200, host.getName()).toString());
    }
}
