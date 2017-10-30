package com.labi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.labi.pojo.ItemsCustom;
import com.labi.pojo.ItemsQueryVo;
import com.labi.service.ItemsService;

@Controller
@RequestMapping("/items")
public class ItemsController {
	
	@Autowired
	private ItemsService itemsService;
	
	@RequestMapping("/getItemsList")
	public String getItemsList(ItemsQueryVo itemsQueryVo, Model model) {
		itemsQueryVo.setIsSort("false".equals(itemsQueryVo.getIsSort()) ? "true" : "false");
		List<ItemsCustom> itemsList = itemsService.getItemsList(itemsQueryVo);
		model.addAttribute("itemsList", itemsList);
		model.addAttribute("itemsQueryVo", itemsQueryVo); // 前台用value=""来显示
		return "index";
	}
	
}
