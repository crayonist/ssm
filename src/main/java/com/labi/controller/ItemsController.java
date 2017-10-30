package com.labi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labi.lucene.ItemsIndexer;
import com.labi.pojo.ItemsCustom;
import com.labi.pojo.ItemsQueryVo;
import com.labi.service.ItemsService;

@Controller
@RequestMapping("/items")
public class ItemsController {
	
	@Autowired
	private ItemsService itemsService;
	
	@Autowired
	private ItemsIndexer itemsIndexer;
	
	@RequestMapping("/getItemsList")
	public String getItemsList(ItemsQueryVo itemsQueryVo, Model model) {
		itemsQueryVo.setIsSort("false".equals(itemsQueryVo.getIsSort()) ? "true" : "false");
		List<ItemsCustom> itemsList = itemsService.getItemsList(itemsQueryVo);
		model.addAttribute("itemsList", itemsList);
		model.addAttribute("itemsQueryVo", itemsQueryVo); // 前台用value=""来显示
		return "index";
	}
	
	/**
	 * 创建索引
	 * @throws Exception
	 */
	@RequestMapping("/addIndex")
	@ResponseBody
	public void addIndex() throws Exception {
		itemsIndexer.createIndex();
	}
	
	/**
	 * 全文检索
	 * @param itemsQueryVo 查询条件
	 * @param model
	 * @return
	 */
	@RequestMapping("/queryItems")
	public String queryItems(ItemsQueryVo itemsQueryVo, Model model) {
		String name = itemsQueryVo.getItemsCustom().getName();
		if (!StringUtils.isEmpty(name)) {
			List<ItemsCustom> itemsList;
			try {
				itemsList = itemsIndexer.searchItems(name);
				model.addAttribute("itemsList", itemsList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "index";
	}
	
}
