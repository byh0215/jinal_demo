package com.demo.orderf;

import com.demo.common.model.Orderf;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * BlogController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@Before(BlogInterceptor.class)
public class OrderfController extends Controller {
	public void index() {
		//
		setAttr("blogPage", Orderf.me.paginate(getParaToInt(0, 1), 10));
		render("orderf.html");
	}
	
	public void add() {
	}
	
	@Before(BlogValidator.class)
	public void save() {
		getModel(Orderf.class).save();
		redirect("/orderf");
	}
	
	public void edit() {
		setAttr("orderf", Orderf.me.findById(getParaToInt()));
	}
	
	@Before(BlogValidator.class)
	public void update() {
		getModel(Orderf.class).update();
		redirect("/orderf");
	}
	
	public void delete() {
		Orderf.me.deleteById(getParaToInt());
		redirect("/orderf");
	}
}


