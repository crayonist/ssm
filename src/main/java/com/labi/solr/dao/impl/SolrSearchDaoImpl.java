package com.labi.solr.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.labi.pojo.ItemsCustom;
import com.labi.solr.dao.SolrSearchDao;

public class SolrSearchDaoImpl implements SolrSearchDao {

	@Autowired
	private SolrClient solrClient;

	public List<ItemsCustom> getItemsListBySolr(SolrQuery query) throws Exception {
		// 根据查询条件查询索引库
		QueryResponse queryResponse = solrClient.query(query);
		// 取查询结果
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		// 取查询结果总数量
		// 商品列表
		List<ItemsCustom> itemList = new ArrayList<ItemsCustom>();
		// 取高亮显示
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		// 取商品列表
		for (SolrDocument solrDocument : solrDocumentList) {
			// 创建一商品对象
			ItemsCustom item = new ItemsCustom();
			item.setId(Integer.parseInt(solrDocument.get("id").toString()));
			// 取高亮显示的结果
			List<String> list = highlighting.get(solrDocument.get("id")).get("items_name");
			String name = "";
			if (list != null && list.size() > 0) {
				name = list.get(0);
			} else {
				name = (String) solrDocument.get("items_name");
			}
			item.setName(name);
			item.setPrice((Float)solrDocument.get("items_price"));
			List<String> list1 = highlighting.get(solrDocument.get("id")).get("items_detail");
			String detail = "";
			if (list1 != null && list.size() > 0) {
				detail = list1.get(0);
			} else {
				detail = (String) solrDocument.get("items_detail");
			}
			item.setDetail(detail);
			// 添加的商品列表
			itemList.add(item);
		}
		return itemList;
	}
}
