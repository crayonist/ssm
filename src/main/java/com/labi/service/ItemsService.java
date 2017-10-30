package com.labi.service;

import java.util.List;

import com.labi.pojo.ItemsCustom;
import com.labi.pojo.ItemsQueryVo;

public interface ItemsService {
	
	public List<ItemsCustom> getItemsList(ItemsQueryVo itemsQueryVo);
	
}
