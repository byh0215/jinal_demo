package com.demo.orderf;

import com.demo.common.model.Blog;
import com.demo.common.model.Orderf;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * BlogValidator.
 */
public class BlogValidator extends Validator {
	
	protected void validate(Controller controller) {
		validateRequiredString("orderf.infor", "inforMsg", "请输入订单信息!");
		validateRequiredString("orderf.pos", "posMsg", "请输入地址信息!");
		validateRequiredString("orderf.price", "priceMsg", "请输入订单总价!");	
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(Orderf.class);
		
		String actionKey = getActionKey();
		if (actionKey.equals("/orderf/save"))
			controller.render("add.html");
		else if (actionKey.equals("/orderf/update"))
			controller.render("edit.html");
	}
}
