package com.labi.pojo;

public class ItemsQueryVo {

	private Items items;
	
	private ItemsCustom itemsCustom;
	
	private String isSort = "false"; // 是否要进行排序


	public Items getItems() {
		return items;
	}

	public String getIsSort() {
		return isSort;
	}

	public void setIsSort(String isSort) {
		this.isSort = isSort;
	}

	public void setItems(Items items) {
		this.items = items;
	}

	public ItemsCustom getItemsCustom() {
		return itemsCustom;
	}

	public void setItemsCustom(ItemsCustom itemsCustom) {
		this.itemsCustom = itemsCustom;
	}

	
	
}
