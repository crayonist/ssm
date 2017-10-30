package com.labi.mapper;

import java.util.List;

import com.labi.pojo.ItemsCustom;
import com.labi.pojo.ItemsQueryVo;

public interface ItemsCustomMapper {

	public List<ItemsCustom> getItemsList(ItemsQueryVo itemsQueryVo);
	
}
