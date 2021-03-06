package com.demo.common.model;

import java.util.List;

import com.demo.common.model.base.BaseUser;
import com.jfinal.plugin.activerecord.Page;

/**
 * Generated by JFinal.
 * Blog model.
 * 数据库字段名建议使用驼峰命名规则，便于与 java 代码保持一致，如字段名： userId
 */
@SuppressWarnings("serial")
public class User extends BaseUser<User> {
	
	public static final User me = new User();
	
	/**
	 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
	 */
	public Page<User> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select *", "from user order by id asc");
	}
	public void setFollowNum(String account){
		List<Fans> fid = Fans.me.find("select id from fans where account="+"'"+account+"'");
		List<User> uid = User.me.find("select id from user where account="+"'"+account+"'");
		User.me.findById(uid.get(0).getId())
		   .set("follows_num",fid.size())
		   .update();
		System.out.println("follow"+fid.size());
	}
	public void setFansNum(String account){
		List<Fans> fid = Fans.me.find("select id from fans where follows="+"'"+account+"'");
		List<User> uid = User.me.find("select id from user where account="+"'"+account+"'");
		User.me.findById(uid.get(0).getId())
		   .set("fans_num",fid.size())
		   .update();
		System.out.println("fans"+fid.size());
	}
}


