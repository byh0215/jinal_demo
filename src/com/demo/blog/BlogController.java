package com.demo.blog;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.demo.common.model.Blog;
import com.demo.common.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

/**
 * BlogController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@Before(BlogInterceptor.class)
public class BlogController extends Controller {
	String webPath="http://127.0.0.1/upload/";
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
		HttpServletRequest r = getRequest();
		String re = r.getParameter("request");
		if(re.equals("do")){
			List<Blog> blog = Blog.me.find("SELECT TOP 3 * FROM blog ORDER BY thumb DESC");
			renderJson(blog);
			//其中的content需要在客户端缩写成summary
		}
		renderText("Error!");
	}
	public void findBlog(){//finish(find a blog)
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		List<Blog> blog = Blog.me.find("select * from blog where id="+"'"+id+"'");
		renderJson(blog);
	}
	public void findEditUserName(){//finish
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		List<Blog> blog = Blog.me.find("select * from blog where id="+"'"+id+"'");
		String ac=blog.get(0).getEditUser();
		List<User> user = User.me.find("select * from user where account="+"'"+ac+"'");
		String na=user.get(0).getName();
		renderText(na);
	}
	public void uploadBlog(){//finish
		HttpServletRequest r = getRequest();
		String name = r.getParameter("name");
		String label = r.getParameter("label");
		String edituser= r.getParameter("edit_user");
		String content= r.getParameter("content");
		UploadFile files = getFile(getPara("uploadfile"),"upload");//存文件
		String fileName=files.getFileName();
		String img=webPath+fileName;
		new Blog().set("name",name).set("label", label).set("edit_user",edituser).set("img_src",img).set("content", content).save();
	}
	public void fetchNewlyBlogList(){//finish
		HttpServletRequest r = getRequest();
		String re = r.getParameter("request");
		if(re.equals("do")){
			List<Blog> blog = Blog.me.find("SELECT * FROM blog ORDER BY id");
			renderJson(blog);
			//其中的content需要在客户端list界面缩写成summary
		}
		//可写错误信息
		renderText("Error!");
		
	}
}



