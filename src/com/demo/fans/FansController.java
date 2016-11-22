package com.demo.fans;

import com.demo.common.model.Fans;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * BlogController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@Before(BlogInterceptor.class)
public class FansController extends Controller {
	public void index() {
		setAttr("blogPage", Fans.me.paginate(getParaToInt(0, 1), 10));
		render("discount.html");
	}
	
	public void add() {
	}
	
	@Before(BlogValidator.class)
	public void save() {
		getModel(Fans.class).save();
		redirect("/discount");
	}
	
	public void edit() {
		setAttr("discount", Fans.me.findById(getParaToInt()));
	}
	
	@Before(BlogValidator.class)
	public void update() {
		getModel(Fans.class).update();
		redirect("/discount");
	}
	
	public void delete() {
		Fans.me.deleteById(getParaToInt());
		redirect("/discount");
	}
}


