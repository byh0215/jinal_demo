package com.demo.blog;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.demo.common.model.Blog;
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
public class BlogController extends Controller {
	public String Path="C:/Users/Administrator/workspace/Xiaocaidao/WebRoot/upload/";
	public void index() {
		//
		setAttr("blogPage", Blog.me.paginate(getParaToInt(0, 1), 10));
		render("blog.html");
	}
	
	public void add() {
	}
	
	@Before(BlogValidator.class)
	public void save() {
		HttpServletRequest r = getRequest();
		StringBuffer label=new StringBuffer();
		String name = r.getParameter("blog.name");
		String content = r.getParameter("blog.content");
		for(int i=0;i<7;i++){
			String tag="label"+i;
			String aLabel = r.getParameter(tag);
			if(aLabel==null){
				aLabel="0";
			}
			label.append(aLabel);
		}
		new Blog().set("name",name).set("label", label).set("content", content).save();
		redirect("/blog");	
	}
	
	public void edit() {
		setAttr("blog", Blog.me.findById(getParaToInt()));
	}
	
	@Before(BlogValidator.class)
	public void update() {
		getModel(Blog.class).update();
		redirect("/blog");
	
	}
	
	public void delete() {
		Blog.me.deleteById(getParaToInt());
		redirect("/blog");
	}
	public void fetchHotBlogList(){//finish
		String sql=
				"select b.*,u.name as userName from blog b inner join user u on b.edit_user=u.account ORDER BY b.thumb DESC";
	    List<Blog> blog = Blog.me.find(sql);
	    List<User> u = User.me.find("select u.*,b.edit_user,b.thumb from user u inner join blog b on b.edit_user=u.account ORDER BY b.thumb DESC,u.id");
	    changePath(blog,u);
		renderJson(blog);
	}
	public void findBlog(){//finish(find a blog)//收藏页
		String id = getPara("id");
		String sql=
				"select b.*,u.name as userName from blog b inner join user u on b.edit_user=u.account where b.id= "+id+" ORDER BY b.id";
	    List<Blog> blog = Blog.me.find(sql);
	    List<User> u = User.me.find("select u.*,b.edit_user,b.id from user u inner join blog b on b.edit_user=u.account where b.id= "+id+" ORDER BY b.id");
	    changePath(blog,u);
		renderJson(blog);
	}
	public static List<Blog> findBlog_server(String id){//finish(find a blog)//收藏页
		String sql=
				"select b.*,u.name as userName from blog b inner join user u on b.edit_user=u.account where b.id= "+id+" ORDER BY b.id";
	    List<Blog> blog = Blog.me.find(sql);
	    List<User> u = User.me.find("select u.*,b.edit_user,b.id from user u inner join blog b on b.edit_user=u.account where b.id= "+id+" ORDER BY b.id");
	    changePath(blog,u);
		return(blog);
	}
	public void fetchMyBlogList(){//finish
		String ac = getPara("account");
		String sql=
				"select * from blog where edit_user= "+"'"+ac+"'"+" ORDER BY id";
	    List<Blog> blog = Blog.me.find(sql);
		renderJson(blog);
	}
	public void findEditUser(){//finish
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		List<Blog> blog = Blog.me.find("select * from blog where id="+"'"+id+"'");
		String ac=blog.get(0).getEditUser();
//		List<User> user = User.me.find("select * from user where account="+"'"+ac+"'");
//		String na=user.get(0).getName();
		renderText(ac);
	}
	public void uploadBlog(){//finish
		UploadFile files =getFile("uploadfile");//存文件
		String name = getPara("name");
		String label = getPara("label");
		String edituser= getPara("account");
		String content= getPara("content");
		String fileName=files.getFileName();
		String img=Path+fileName;
		new Blog().set("name",name).set("label", label).set("edit_user",edituser).set("img_src",img).set("content", content).save();
		renderText("1");
	}
	public void fetchNewlyBlogList(){//finish
		String sql=
				"select b.*,u.name as userName from blog b inner join user u on b.edit_user=u.account ORDER BY b.id DESC";
	    List<Blog> blog = Blog.me.find(sql);
	    List<User> u = User.me.find("select u.*,b.edit_user,b.id from user u inner join blog b on b.edit_user=u.account ORDER BY b.id DESC");
	    changePath(blog,u);
		renderJson(blog);
	}
	public void setThumb(){//finish
		HttpServletRequest r = getRequest();
		String id = r.getParameter("blog_id");
		List<Blog> th = Blog.me.find("select thumb from blog where id="+"'"+id+"'");
		Blog.me.findById(Integer.parseInt(id)).set("thumb",(th.get(0).getThumb())+1).update();
		System.out.println((th.get(0).getThumb())+"");
		renderText((th.get(0).getThumb())+1+"");
	}
	public static void changePath(List<Blog> blog,List<User> u){
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
}



