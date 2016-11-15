package com.demo.goods;

import com.demo.common.model.Goods;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * BlogValidator.
 */
public class BlogValidator extends Validator {
	
	protected void validate(Controller controller) {
		validateRequiredString("goods.good_name", "nameMsg", "请输入商品名称!");
		validateRequiredString("goods.good_price", "priceMsg", "请输入商品价格!");
		validateRequiredString("goods.good_infor", "contentMsg", "请输入商品信息!");
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(Goods.class);
		String actionKey = getActionKey();
		if (actionKey.equals("/goods/save"))
			controller.render("add.html");
		else if (actionKey.equals("/goods/update"))
			controller.render("edit.html");
	}
}
