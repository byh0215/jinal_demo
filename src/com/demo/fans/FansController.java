package com.demo.fans;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.demo.common.model.Collection;
import com.demo.common.model.Fans;
import com.demo.common.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * fansController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@Before(BlogInterceptor.class)
public class FansController extends Controller {
	public void index() {
		setAttr("fansPage", Fans.me.paginate(getParaToInt(0, 1), 10));
		render("fans.html");
	}
	
	public void add() {
	}
	
	@Before(FansValidator.class)
	public void save() {
		getModel(Fans.class).save();
		redirect("/fans");
	}
	
	public void edit() {
		setAttr("fans", Fans.me.findById(getParaToInt()));
	}
	
	@Before(FansValidator.class)
	public void update() {
		getModel(Fans.class).update();
		redirect("/fans");
	}
	
	public void delete() {
		Fans.me.deleteById(getParaToInt());
		redirect("/fans");
	}
	public void fetchFollowsList(){//finish
		HttpServletRequest r = getRequest();
		String account = r.getParameter("account");
		List<Fans> fo = Fans.me.find("select follows from collection where account="+account);
		renderJson(fo);
	}
	public void fetchFansList(){//finish
		HttpServletRequest r = getRequest();
		String follows = r.getParameter("account");
		List<Fans> fa = Fans.me.find("select account from collection where follows="+follows);
		renderJson(fa);
	}
	public void setFollows(){//finish
		HttpServletRequest r = getRequest();
		String account = r.getParameter("account");
		String follows = r.getParameter("follows");
		new Fans().set("account", account).set("follows", follows).save();
		User.me.setFollowNum(account);
		User.me.setFansNum(account);
		renderText("1");
	}
}


