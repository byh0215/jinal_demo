package com.demo.recommend;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.demo.common.model.Fans;
import com.demo.common.model.Recommend;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * BlogController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@Before(BlogInterceptor.class)
public class RecommendController extends Controller {
	public void index() {
		setAttr("recommendPage", Recommend.me.paginate(getParaToInt(0, 1), 10));
		render("recommend.html");
	}
	
	public void add() {
		
	}
	
	@Before(RecommendValidator.class)
	public void save() {
		getModel(Recommend.class).save();
		redirect("/recommend");
	}
	
	public void edit() {
		setAttr("recommend", Recommend.me.findById(getParaToInt()));
	}
	
	@Before(RecommendValidator.class)
	public void update() {
		getModel(Recommend.class).update();
		redirect("/recommend");
	}
	
	public void delete() {
		Recommend.me.deleteById(getParaToInt());
		redirect("/recommend");
	}
	public void findRecommend(){//finish
		HttpServletRequest r = getRequest();
		String day = r.getParameter("day");
		List<Recommend> re = null;
		if(day.equals("1")){
			re = Recommend.me.find("select * from recommend where day=1");
		}
		if(day.equals("2")){
			re = Recommend.me.find("select * from recommend where day=2");
		}
		if(day.equals("3")){
			re = Recommend.me.find("select * from recommend where day=3");
		}
		if(day.equals("4")){
			re = Recommend.me.find("select * from recommend where day=4");
		}
		if(day.equals("5")){
			re = Recommend.me.find("select * from recommend where day=5");
		}
		if(day.equals("6")){
			re = Recommend.me.find("select * from recommend where day=6");
		}
		if(day.equals("7")){
			re = Recommend.me.find("select * from recommend where day=7");
		}
		renderJson(re);
	}
}


