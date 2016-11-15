package com.demo.discount;

import com.demo.common.model.Discount;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * BlogController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@Before(BlogInterceptor.class)
public class DiscountController extends Controller {
	public void index() {
		setAttr("blogPage", Discount.me.paginate(getParaToInt(0, 1), 10));
		render("discount.html");
	}
	
	public void add() {
	}
	
	@Before(BlogValidator.class)
	public void save() {
		getModel(Discount.class).save();
		redirect("/discount");
	}
	
	public void edit() {
		setAttr("discount", Discount.me.findById(getParaToInt()));
	}
	
	@Before(BlogValidator.class)
	public void update() {
		getModel(Discount.class).update();
		redirect("/discount");
	}
	
	public void delete() {
		Discount.me.deleteById(getParaToInt());
		redirect("/discount");
	}
}


