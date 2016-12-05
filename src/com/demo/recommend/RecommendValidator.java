package com.demo.recommend;

import com.demo.common.model.Recommend;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * Validator.
 */
public class RecommendValidator extends Validator {
	
	protected void validate(Controller controller) {
		validateRequiredString("name", "nameMsg", "请输入推荐位名称!");
		validateRequiredString("img_src", "img_srcMsg", "请输入推荐位图片!");
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(Recommend.class);
		String actionKey = getActionKey();
		if (actionKey.equals("/recommend/save"))
			controller.render("add.html");
		else if (actionKey.equals("/recommend/update"))
			controller.render("edit.html");
	}
}
