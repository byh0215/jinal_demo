package com.demo.user;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.demo.common.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * BlogController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@Before(BlogInterceptor.class)
public class UserController extends Controller {
	public void index() {
		setAttr("userPage", User.me.paginate(getParaToInt(0, 1), 10));
		render("user.html");
	}
	
	public void add() {
	}
	
	@Before(UserValidator.class)
	public void save() {
		getModel(User.class).save();
		redirect("/user");
	}
	
	public void edit() {
		setAttr("user", User.me.findById(getParaToInt()));
	}
	
	@Before(UserValidator.class)
	public void update() {
		getModel(User.class).update();
		redirect("/user");
	}
	
	public void delete() {
		User.me.deleteById(getParaToInt());
		redirect("/user");
	}
	public void login(){
		HttpServletRequest r = getRequest();
		String uac = r.getParameter("uac");
		String upw = r.getParameter("upw");
		List<User> users = User.me.find("select * from user where uac="+uac+"and upw="+upw);
		if(users!=null){
			renderText("1");
		}else{
			renderText("0");
		}	
		//1、url访问：
		//本机访问：http://localhost/blog/test  http://127.0.0.1/blog/test
		//内网访问：http://内网ip/blog/test
		//2、服务器端获取请求后，返回结果
		//2.1返回字符串：renderText方法。
		//renderText("<person sex=\"female\"><firstname>Anna</firstname><lastname>Smith</lastname></person>");
		//renderText("{\"firstName\":\"Bill\" , \"lastName\":\"Gates\"}");
		//2.2返回html：render(view);
//		setAttr("blogPage", Blog.me.paginate(getParaToInt(0, 1), 10));
//		render("blog.html");
		//2.3返回xml：
		//无参数：renderXml("blog.xml");
		//有参数：
		//setAttr("blogPage", Blog.me.paginate(getParaToInt(0, 1), 10));
		//renderXml("blog.xml");
		//2.4返回json：
		//List<Blog> lb = Blog.me.find("select * from blog");
		//renderJson(lb);
		//3、获取客户端请求参数
		//3.1 jfinal框架支持的url映射的方式：http://10.7.88.190/blog/edit/21
		//获取方法：getParaToInt()
		//3.2 传统web请求方式：
		//HttpServletRequest r = getRequest();
		//String uname = r.getParameter("username");
	}
}


