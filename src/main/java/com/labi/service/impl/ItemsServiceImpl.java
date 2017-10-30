package com.labi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.labi.mapper.ItemsCustomMapper;
import com.labi.pojo.ItemsCustom;
import com.labi.pojo.ItemsQueryVo;
import com.labi.service.ItemsService;

@Service
public class ItemsServiceImpl implements ItemsService {

	@Autowired
	private ItemsCustomMapper itemsCustomMapper;
	
	public List<ItemsCustom> getItemsList(ItemsQueryVo itemsQueryVo) {
		return itemsCustomMapper.getItemsList(itemsQueryVo);
	}

}
