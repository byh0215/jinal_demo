package com.demo.user;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
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
	public static String webPath=null;
	
	public void index() {
		setAttr("userPage", User.me.paginate(getParaToInt(0, 1), 10));
		render("user.html");
	}
	
	public void add() {
	}
	public void save() {
		 String Path="C:/Users/Administrator/workspace/Xiaocaidao/WebRoot/upload/";
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
	          .set("img_src",Path+fileMap.get("filename")).save();
		    
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
	public void checkIp() throws Exception{
		HttpServletRequest r = getRequest();
		Enumeration<NetworkInterface> nifs;
		nifs = NetworkInterface.getNetworkInterfaces();
        while (nifs.hasMoreElements()) {
            NetworkInterface nif = nifs.nextElement();	 
            // 获得与该网络接口绑定的 IP 地址，一般只有一个
            Enumeration<InetAddress> addresses = nif.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
        
                if (addr instanceof Inet4Address) { // 只关心 IPv4 地址
                    if(nif.getName().equals("eth4")){
                    	System.out.println("网卡接口名称：" + nif.getName());
                    	System.out.println("网卡接口地址：" + addr.getHostAddress());
                    	webPath="http://"+addr.getHostAddress()+":8008/";
                    } 
                }
            }
        }
	}
	public void checkLogin(){//检查登陆状态,finished
		HttpServletRequest r = getRequest();
		String uac = r.getParameter("account");
		String upw = r.getParameter("password");
		List<User> user = User.me.find("select * from user where account="+"'"+uac+"'"+" and password="+"'"+upw+"'");
		if(user.size()!=0){
			renderText("1");
		}else{
			renderText("0");
		}	
	}
		public void checkAccount(){//检测用户名是否存在,finished
			HttpServletRequest r = getRequest();
			String uac = r.getParameter("account");
			List<User> ac = User.me.find("select * from user where account="+"'"+uac+"'");
			if(ac.size()!=0){
				renderText("1");
			}else{
				renderText("0");
			}	
		}
		public void createUser(){//创建新用户,finished
			HttpServletRequest r = getRequest();
			String uac = r.getParameter("account");
			String upw = r.getParameter("password");
			new User().set("account", uac).set("password", upw).save();
			renderText("1");
		}
		public void fetchUserCollection(){//抓取用户收藏列表
			HttpServletRequest r = getRequest();
			String uac = r.getParameter("account");
			List<Collection> idl = Collection.me.find("select blog_id from collection where account="+"'"+uac+"'");
			renderJson(idl);
		}
		public void setImg(){//设置头像
			UploadFile files =getFile("uploadfile");
			String Path="C:/Users/Administrator/workspace/Xiaocaidao/WebRoot/upload/";
			String fileName=files.getFileName();
			String account = getPara("account");		
			List<User> im = User.me.find("select img_src from user where account="+"'"+account+"'");
			List<User> id = User.me.find("select id from user where account="+"'"+account+"'");
			String fim=Path+fileName;
//			if(im.get(0).getImgSrc().indexOf("touxiang")<0){
//				deleteFile(fim);
//			}
			User.me.findById(id.get(0).getId()).set("img_src",fim).update();
			renderText("1");
		}
		public void findImg(){//查找头像
			HttpServletRequest r = getRequest();
			String account = r.getParameter("account");
			List<User> im = User.me.find("select img_src from user where account="+"'"+account+"'");
			renderText(getWebPath(im.get(0).getImgSrc()));
		}
		public void setName(){//设置用户名
			HttpServletRequest r = getRequest();
			String name = r.getParameter("name");
			String account = r.getParameter("account");
			List<User> id  = User.me.find("select id from user where account="+"'"+account+"'");
			User.me.findById(id.get(0).getId()).set("name",name).update();
			renderText("1");
		}
		public void findName(){//finish
			HttpServletRequest r = getRequest();
			String account = r.getParameter("account");
			List<User> name = User.me.find("select name from user where account="+"'"+account+"'");
			renderText(name.get(0).getName());
		}
		public void setPassword(){//finish
			HttpServletRequest r = getRequest();
			String password = r.getParameter("password");
			String account = r.getParameter("account");
			List<User> id = User.me.find("select id from user where account="+"'"+account+"'");
			User.me.findById(id.get(0).getId()).set("password",password).update();
			renderText("1");
		}
		public void findFollowNum(){//finish
			HttpServletRequest r = getRequest();
			String account = r.getParameter("account");
			List<User> follows = User.me.find("select follows_num from user where account="+"'"+account+"'");
			renderText(follows.get(0).getFollowsNum()+"");
		}
		public void findFansNum(){//finish
			HttpServletRequest r = getRequest();
			String account = r.getParameter("account");
			List<User> fans = User.me.find("select fans_num from user where account="+"'"+account+"'");
			renderText(fans.get(0).getFansNum()+"");
		}
		 /** 
		  * 删除单个文件 
		  *  
		  * @param fileName 
		  *            要删除的文件的文件名 
		  * @return 单个文件删除成功返回true，否则返回false 
		  */  
		 public static boolean deleteFile(String fileName) {  
		  File file = new File(fileName);  
		  // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除  
		  if (file.exists() && file.isFile()) {  
		   if (file.delete()) {  
		    System.out.println("删除单个文件" + fileName + "成功！");  
		    return true;  
		   } else {  
		    System.out.println("删除单个文件" + fileName + "失败！");  
		    return false;  
		   }  
		  } else {  
		   System.out.println("删除单个文件失败：" + fileName + "不存在！");  
		   return false;  
		  }  
		 }  
		 public static String getWebPath(String aPath){
			 String finalPath=null;
			 String Path="C:/Users/Administrator/workspace/Xiaocaidao/WebRoot/upload/";
			 System.out.println("传过来的路径： "+aPath);
			 System.out.println("截取后的路径： "+aPath.substring(Path.length()-7));
			 finalPath=webPath+aPath.substring(Path.length()-7);
			 return finalPath;
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


