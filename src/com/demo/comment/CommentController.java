package com.demo.comment;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.demo.common.model.Blog;
import com.demo.common.model.Comment;
import com.demo.common.model.Fans;
import com.demo.common.model.Recommend;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

/**
 * BlogController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@Before(BlogInterceptor.class)
public class CommentController extends Controller {
	String webPath="http://127.0.0.1/upload/";
	public void uploadComment(){
		HttpServletRequest r = getRequest();
		String content = r.getParameter("content");
		String account = r.getParameter("account");
		String blog_id= r.getParameter("blog_id");
		new Comment().set("content",content).set("account", account).set("blog_id",blog_id).save();
	}
	public void findCommentList(){
		HttpServletRequest r = getRequest();
		String blog_id = r.getParameter("blog_id");
		List<Comment> co = Comment.me.find("select blog_id from comment where blog_id="+blog_id);
		renderJson(co);
	}
	
//	public void index() {
//		setAttr("recommendPage", Recommend.me.paginate(getParaToInt(0, 1), 10));
//		render("recommend.html");
//	}
//	
//	public void add() {
//		
//	}
//	
//	@Before(RecommendValidator.class)
//	public void save() {
//		getModel(Recommend.class).save();
//		redirect("/recommend");
//	}
//	
//	public void edit() {
//		setAttr("recommend", Recommend.me.findById(getParaToInt()));
//	}
//	
//	@Before(RecommendValidator.class)
//	public void update() {
//		getModel(Recommend.class).update();
//		redirect("/recommend");
//	}
//	
//	public void delete() {
//		Recommend.me.deleteById(getParaToInt());
//		redirect("/recommend");
//	}
}


