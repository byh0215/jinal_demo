package com.demo.goods;

import com.demo.common.model.Goods;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * BlogController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@Before(BlogInterceptor.class)
public class GoodsController extends Controller {
	public void index() {
		setAttr("blogPage", Goods.me.paginate(getParaToInt(0, 1), 10));
		render("goods.html");
	}
	
	public void add() {
		
	}
	
	@Before(BlogValidator.class)
	public void save() {
		getModel(Goods.class).save();
		redirect("/goods");
	}
	
	public void edit() {
		setAttr("goods", Goods.me.findById(getParaToInt()));
	}
	
	@Before(BlogValidator.class)
	public void update() {
		getModel(Goods.class).update();
		redirect("/goods");
	}
	
	public void delete() {
		Goods.me.deleteById(getParaToInt());
		redirect("/goods");
	}
}


