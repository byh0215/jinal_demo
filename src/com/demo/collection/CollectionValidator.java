package com.demo.collection;

import com.demo.common.model.Fans;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * BlogValidator.
 */
public class CollectionValidator extends Validator {
	
	protected void validate(Controller controller) {
		validateRequiredString("fans.account", "accountMsg", "请输入帐号!");
		validateRequiredString("fans.follows", "followsMsg", "请输入关注的帐号!");
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(Fans.class);
		String actionKey = getActionKey();
		if (actionKey.equals("/fans/save"))
			controller.render("add.html");
		else if (actionKey.equals("/fans/update"))
			controller.render("edit.html");
	}
}
