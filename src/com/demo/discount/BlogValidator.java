package com.demo.discount;

import com.demo.common.model.Discount;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * BlogValidator.
 */
public class BlogValidator extends Validator {
	
	protected void validate(Controller controller) {
		validateRequiredString("discount.name", "nameMsg", "请输入活动名称!");
		validateRequiredString("discount.infor", "inforMsg", "请输入活动信息!");
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(Discount.class);
		String actionKey = getActionKey();
		if (actionKey.equals("/discount/save"))
			controller.render("add.html");
		else if (actionKey.equals("/discount/update"))
			controller.render("edit.html");
	}
}
