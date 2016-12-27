package com.demo.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.demo.blog.BlogController;
import com.demo.common.model.Blog;
import com.demo.common.model.Collection;
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
public class CollectionController extends Controller {
	public void setCollection(){//finished
		HttpServletRequest r = getRequest();
		String account = r.getParameter("account");
		String bid = r.getParameter("blog_id");
		new Collection().set("account", account).set("blog_id", bid).save();
		renderText("1");
	}
	@SuppressWarnings("null")
	public void fetchCollectionList(){//finished
		List<User> u= new ArrayList<User>();
		String account = getPara("account");
		String sql=
				"select b.* from blog b inner join collection c on b.id=c.blog_id where c.account="+"'"+account+"'"+"ORDER BY b.id DESC";
	    List<Blog> co = Blog.me.find(sql);
	    for (int i = 0;i <co.size();i++) {
	    	List<Blog> temp= new ArrayList<Blog>();
	    	String bid=co.get(i).getId()+"";
	    	temp=BlogController.findBlog_server(bid);
	    	co.get(i).put("userImg",temp.get(0).get("userImg"));
	    	co.get(i).put("userName",temp.get(0).get("userName"));
	    	}
		renderJson(co);
	}
	public void checkCollection(){//finished
		HttpServletRequest r = getRequest();
		String bid = r.getParameter("blog_id");
		List<Collection> idl = Collection.me.find("select id from collection where blog_id="+bid);
		if(idl.size()>0){
			renderText("1");
		}else{
			renderText("0");
		}
	}
	public void changePath(List<Blog> blog,List<User> u){
		for (int i = 0; i < blog.size(); i++) {
	    	Blog temp=blog.get(i);
	    	Blog old=blog.get(i);
	    	String text=old.getImgSrc();
	    	temp.set("img_src",UserController.getWebPath(text));
	    	Collections.replaceAll(blog,old,temp);
		}
	    for (int i = 0; i < u.size(); i++) {
	    	User temp=u.get(i);
	    	User old=u.get(i);
	    	String text=old.getImgSrc();
	    	temp.set("img_src",UserController.getWebPath(text));
	    	Collections.replaceAll(u,old,temp);
	    	String up=u.get(i).getImgSrc();
	    	blog.get(i).put("userImg",up);
		}
	}
//	public void index() {
//		setAttr("fansPage", Fans.me.paginate(getParaToInt(0, 1), 10));
//		render("fans.html");
//	}
//	
//	public void add() {
//	}
//	
//	@Before(CollectionValidator.class)
//	public void save() {
//		getModel(Fans.class).save();
//		redirect("/fans");
//	}
//	
//	public void edit() {
//		setAttr("fans", Fans.me.findById(getParaToInt()));
//	}
//	
//	@Before(CollectionValidator.class)
//	public void update() {
//		getModel(Fans.class).update();
//		redirect("/fans");
//	}
//	
//	public void delete() {
//		Fans.me.deleteById(getParaToInt());
//		redirect("/fans");
//	}
}


