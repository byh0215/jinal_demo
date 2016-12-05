package com.demo.collection;

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
public class CollectionController extends Controller {
	public void postCollectionIdList(){//blocked
		HttpServletRequest r = getRequest();
		String account = r.getParameter("account");
		List<Collection> idl = Collection.me.find("select blog_id from user where account="+account);
		renderJson(idl);
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


