package com.demo.fans;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.demo.common.model.Collection;
import com.demo.common.model.Comment;
import com.demo.common.model.Fans;
import com.demo.common.model.User;
import com.demo.user.UserController;
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
	public void fetchFansList(){//finish
		HttpServletRequest r = getRequest();
		String follows = r.getParameter("account");
		String sql=
				"select fa.*,u.name as userName,u.img_src as userImg from fans fa inner join user u on fa.account=u.account where fa.follows="+"'"+follows+"'"+"ORDER BY fa.id";
	    List<Fans> fa = Fans.me.find(sql);
	    List<User> u = User.me.find("select u.*,u.account from user u inner join fans fa on fa.account=u.account where fa.follows= "+"'"+follows+"'"+" ORDER BY fa.id");
	    changePath_F(fa,u);
		renderJson(fa);
	}
	public void fetchFollowsList(){//finish
		HttpServletRequest r = getRequest();
		String account = r.getParameter("account");
		String sql=
				"select fo.*,u.name as userName from fans fo inner join user u on fo.follows=u.account where fo.account= "+"'"+account+"'"+" ORDER BY fo.id";
		List<User> u = User.me.find("select u.*,u.account from user u inner join fans fo on fo.follows=u.account where fo.account= "+"'"+account+"'"+" ORDER BY fo.id");
	    List<Fans> fo= Fans.me.find(sql);
	    changePath_F(fo,u);
		renderJson(fo);
	}
	public void setFollows(){//finish
		HttpServletRequest r = getRequest();
		String account = r.getParameter("account");
		String follows = r.getParameter("follows");
		String type = r.getParameter("type");
		List<Fans> fo = Fans.me.find("select id from fans where account="+"'"+account+"'"+" and follows="+"'"+follows+"'");
		System.out.println(fo.size());
		if(fo.size()!=0){
			renderText("0");			
		}else{
			new Fans().set("account", account).set("follows", follows).save();
			User.me.setFollowNum(account);
			User.me.setFansNum(follows);
			renderText("1");	
		}	
	}
	//doing here
	public void changePath_F(List<Fans> f,List<User> u){
	    for (int i = 0; i < u.size(); i++) {
	    	User temp=u.get(i);
	    	User old=u.get(i);
	    	String text=old.getImgSrc();
	    	temp.set("img_src",UserController.getWebPath(text));
	    	Collections.replaceAll(u,old,temp);
	    	String up=u.get(i).getImgSrc();
	    	f.get(i).put("userImg",up);
		}
	}
}


