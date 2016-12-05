package com.demo.user;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Response;

import com.demo.common.model.Blog;
import com.demo.common.model.Collection;
import com.demo.common.model.Comment;
import com.demo.common.model.Fans;
import com.demo.common.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

/**
 * BlogController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
public class UserController extends Controller {
	String webPath="http://127.0.0.1/upload/";
	public void index() {
		setAttr("userPage", User.me.paginate(getParaToInt(0, 1), 10));
		render("user.html");
	}
	
	public void add() {
	}
	public void save() {
		final int permitedSize = 314572800;
		Map<String, String> textMap = new HashMap<String, String>(); 
		Map<String, String> fileMap = new HashMap<String, String>();  
		try {                 
		    String type = "";  
		    String name = "";  
		    String originalFilename = "";  
		    String extension1 = "";  
		    String extension2 = "";  
		    String filename = "";  
		      
		    //上传目录  
		    String strDirectory = "upload";  
		    String uploadPath = getRequest().getRealPath(strDirectory+"//");
		      
		    // 获取句柄  
		    MultipartRequest multipartRequest = new MultipartRequest(
		    								getRequest(),
		    								uploadPath,   
		    								permitedSize, 
		    								"ISO-8859-1", 
		    								new DefaultFileRenamePolicy());   
		          
		    // 取得文件  
		    Enumeration files = multipartRequest.getFileNames();            
		    // 取得文件详细信息   
		    while (files.hasMoreElements()) {   
		           name = (String)files.nextElement();  
		           type = multipartRequest.getContentType(name);   
		           filename = multipartRequest.getFilesystemName(name);   
		           originalFilename = multipartRequest.getOriginalFileName(name);            
		           File currentFile = multipartRequest.getFile(name); 
		           fileMap.put("filename", filename);
		    }  
		  //取得其它非文件字段  
		    Enumeration params = multipartRequest.getParameterNames();  
		    int i=0;
		    while (params.hasMoreElements()) {  
		        String aname = (String)params.nextElement();  
		        String value = multipartRequest.getParameter(aname); 
		        textMap.put(aname, value);
		    } 
		    new User().set("account",textMap.get("user.account"))
	          .set("password", textMap.get("user.password"))
	          .set("name", textMap.get("user.name"))
	          .set("img_src",webPath+fileMap.get("filename")).save();
		    
		} catch (Exception exception) {   
			exception.printStackTrace();
		}
		redirect("/user");
	}
	
	public void edit() {
		setAttr("user", User.me.findById(getParaToInt()));
	}
	
	@Before(UserValidator.class)
	public void update() {//processing
		getModel(User.class).update();
		redirect("/user");
	}
	
	public void delete() {
		User.me.deleteById(getParaToInt());
		redirect("/user");
	}
	public void checkLogin(){//finish
		HttpServletRequest r = getRequest();
		String uac = r.getParameter("uac");
		String upw = r.getParameter("upw");
		List<User> user = User.me.find("select * from user where account="+uac+"and password="+upw);
		if(user!=null){
			renderText("1");
		}else{
			renderText("0");
		}	
	}
		public void findAccount(){//finish
			HttpServletRequest r = getRequest();
			String uac = r.getParameter("uac");
			List<User> ac = User.me.find("select * from user where account="+uac);
			if(ac!=null){
				renderText("1");
			}else{
				renderText("0");
			}	
		}
		public void createUser(){//finish
			HttpServletRequest r = getRequest();
			String uac = r.getParameter("uac");
			String upw = r.getParameter("upw");
			new User().set("account", uac).set("password", upw).save();
			renderText("1");
		}
		public void fetchUserCollection(){//processing
			HttpServletRequest r = getRequest();
			String uac = r.getParameter("account");
			List<Collection> idl = Collection.me.find("select blog_id from collection where account="+uac);
			renderJson(idl);
		}
		public void setComment(){//finish
			HttpServletRequest r = getRequest();
			String content = r.getParameter("content");
			String blog_id = r.getParameter("id");
			String account = r.getParameter("account");
			new Comment().set("content", content).set("blog_id", blog_id).set("account",account).save();
			renderText("1");
		}
		public void setImg(){//finish
			HttpServletRequest r = getRequest();
			UploadFile files =getFile("uploadfile","c:/Users/Administrator/workspace/Xiaocaidao/WebRoot/img/");
			files.getFileName();
			String account = r.getParameter("account");
			List<User> id = User.me.find("select id from user where account="+account);
			User.me.findByIdLoadColumns(id.get(0).getId(),"img_src")
				   .set("img_src",webPath)
				   .update();
			renderText("1");
		}
		public void findImg(){//finish
			HttpServletRequest r = getRequest();
			String account = r.getParameter("account");
			List<User> im = User.me.find("select img_src from user where account="+account);
			renderText(im.get(0).getImgSrc());
		}
		public void setName(){//finish
			HttpServletRequest r = getRequest();
			String name = r.getParameter("name");
			String account = r.getParameter("account");
			List<User> id  = User.me.find("select id from user where account="+account);
			User.me.findByIdLoadColumns(id.get(0).getId(),"name")
				   .set("name",name)
				   .update();
			renderText("1");
		}
		public void findName(){//finish
			HttpServletRequest r = getRequest();
			String account = r.getParameter("account");
			List<User> name = User.me.find("select name from user where account="+account);
			renderText(name.get(0).getName());
		}
		public void setPassword(){//finish
			HttpServletRequest r = getRequest();
			String password = r.getParameter("password");
			String account = r.getParameter("account");
			List<User> id = User.me.find("select id from user where account="+account);
			User.me.findByIdLoadColumns(id.get(0).getId(),"password")
				   .set("password",password)
				   .update();
			renderText("1");
		}
		public void findFollowNum(){//finish
			HttpServletRequest r = getRequest();
			String account = r.getParameter("account");
			List<User> fo = User.me.find("select follows_num from user where account="+account);
			renderText(fo.get(0).getFollowNum().toString());
		}
		public void findFansNum(){//finish
			HttpServletRequest r = getRequest();
			String account = r.getParameter("account");
			List<User> fa = User.me.find("select fans_num from user where account="+account);
			renderText(fa.get(0).getFansNum().toString());
		}
			
		//1、url访问：
		//本机访问：http://localhost/blog/test  http://127.0.0.1/blog/test
		//内网访问：http://内网ip/blog/test
		//2、服务器端获取请求后，返回结果
		//2.1返回字符串：renderText方法。
		//renderText("<person sex=\"female\"><firstname>Anna</firstname><lastname>Smith</lastname></person>");
		//renderText("{\"firstName\":\"Bill\" , \"lastName\":\"Gates\"}");
		//2.2返回html：render(view);
//		setAttr("blogPage", Blog.me.paginate(getParaToInt(0, 1), 10));
//		render("blog.html");
		//2.3返回xml：
		//无参数：renderXml("blog.xml");
		//有参数：
		//setAttr("blogPage", Blog.me.paginate(getParaToInt(0, 1), 10));
		//renderXml("blog.xml");
		//2.4返回json：
		//List<Blog> lb = Blog.me.find("select * from blog");
		//renderJson(lb);
		//3、获取客户端请求参数
		//3.1 jfinal框架支持的url映射的方式：http://10.7.88.190/blog/edit/21
		//获取方法：getParaToInt()
		//3.2 传统web请求方式：
		//HttpServletRequest r = getRequest();
		//String uname = r.getParameter("username");
	
		//表关联操作
		/*public void relation() {
		String sql = "select b.*, u.user_name from blog b inner
				join user u on b.user_id=u.id where b.id=?";
				Blog blog = Blog.dao.findFirst(sql, 123);
				String name = blog.getStr("user_name");
				}
		*/
		/*
		 *  public class Blog extends Model<Blog>{
			public static final Blog dao = new Blog();
			public User getUser() {
			return User.dao.findById(get("user_id"));
			}
			}
			public class User extends Model<User>{
			public static final User dao = new User();
			public List<Blog> getBlogs() {
			return Blog.dao.find("select * from blog where user_id=?",
			get("id"));
			}
			}
			*/
}


