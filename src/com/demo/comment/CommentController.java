package com.demo.comment;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.demo.common.model.Blog;
import com.demo.common.model.Comment;
import com.demo.common.model.Fans;
import com.demo.common.model.Recommend;
import com.demo.common.model.User;
import com.demo.user.UserController;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

/**
 * BlogController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@Before(BlogInterceptor.class)
public class CommentController extends Controller {
	public void index() {
		//
		setAttr("commentPage", Comment.me.paginate(getParaToInt(0, 1), 10));
		render("comment.html");
	}
	public void delete() {
		Blog.me.deleteById(getParaToInt());
		redirect("/blog");
	}
	public void uploadComment(){
		HttpServletRequest r = getRequest();
		String account = r.getParameter("account");
		String blog_id= r.getParameter("blog_id");
		String content = r.getParameter("content");
		new Comment().set("content",content).set("account", account).set("blog_id",blog_id).save();
		renderText("1");
	}
	public void findCommentList(){
		String blog_id = getPara("blog_id");
		String sql=
				"select c.*,u.name as userName,u.img_src as userImg from comment c inner join user u on c.account=u.account where blog_id= "+"'"+blog_id+"'"+" ORDER BY c.id";
		List<User> u = User.me.find("select u.*,c.account from user u inner join comment c on c.account=u.account where blog_id= "+"'"+blog_id+"'"+" ORDER BY c.id");
	    List<Comment> co = Comment.me.find(sql);
	    changePath_C(co,u);
	    System.out.println(co);
		renderJson(co);
	}
	public void changePath_C(List<Comment> co,List<User> u){
	    for (int i = 0; i < u.size(); i++) {
	    	User temp=u.get(i);
	    	User old=u.get(i);
	    	String text=old.getImgSrc();
	    	temp.set("img_src",UserController.getWebPath(text));
	    	Collections.replaceAll(u,old,temp);
	    	String up=u.get(i).getImgSrc();
	    	co.get(i).put("userImg",up);
		}
	}
}


