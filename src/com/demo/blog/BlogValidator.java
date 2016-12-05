package com.demo.blog;

import com.demo.common.model.Blog;
import com.demo.common.model.Blog;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * BlogValidator.
 */
public class BlogValidator extends Validator {
	
	protected void validate(Controller controller) {
		validateRequiredString("blog.name", "nameMsg", "请输入帖子名称!");
		validateRequiredString("blog.label", "labelMsg", "请选择帖子标签!");
		validateRequiredString("blog.content", "contentMsg", "请输入帖子内容!");	
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(Blog.class);
		
		String actionKey = getActionKey();
		if (actionKey.equals("/blog/save"))
			controller.render("add.html");
		else if (actionKey.equals("/blog/update"))
			controller.render("edit.html");
	}
}
