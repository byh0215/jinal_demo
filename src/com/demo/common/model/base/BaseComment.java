package com.demo.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseComment<M extends BaseComment<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setContent(java.lang.String content) {
		set("content", content);
	}

	public java.lang.String getContent() {
		return get("content");
	}

	public void setAccount(java.lang.String account) {
		set("account", account);
	}

	public java.lang.String getAccount() {
		return get("account");
	}

	public void setBlogId(java.lang.Integer blogId) {
		set("blog_id", blogId);
	}

	public java.lang.Integer getBlogId() {
		return get("blog_id");
	}

}
