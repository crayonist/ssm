package com.labi.service.impl;

import java.io.IOException;
import java.util.List;

import javax.swing.text.StyledEditorKit.BoldAction;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.HighlightParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.labi.mapper.ItemsCustomMapper;
import com.labi.pojo.ItemsCustom;
import com.labi.pojo.ItemsQueryVo;
import com.labi.service.ItemsService;
import com.labi.solr.dao.SolrSearchDao;

@Service
public class ItemsServiceImpl implements ItemsService {

	@Autowired
	private ItemsCustomMapper itemsCustomMapper;

	@Autowired
	private SolrClient solrClient;

	@Autowired
	private SolrSearchDao solrSearchDao;

	/**
	 * 导入Solr索引库
	 */
	public boolean importItems() {
		try {
			// 查询商品列表
			List<ItemsCustom> list = itemsCustomMapper.getItemsList(null);
			// 把商品信息写入索引库
			for (ItemsCustom item : list) {
				// 创建一个SolrInputDocument对象
				SolrInputDocument document = new SolrInputDocument();
				document.setField("id", item.getId());
				document.setField("items_name", item.getName());
				document.setField("items_detail", item.getDetail());
				document.setField("items_price", item.getPrice());
				// 写入索引库
				solrClient.add(document);
			}
			// 提交修改
			solrClient.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 查询（Solr）
	 */
	public List<ItemsCustom> getItemsListBySolr(ItemsQueryVo itemsQueryVo) throws Exception {
		String queryString = itemsQueryVo.getItemsCustom().getName();
		if (!StringUtils.isEmpty(queryString)) {
			// 创建查询对象
			SolrQuery query = new SolrQuery();
			// 设置查询条件
			// queryString = "{!term f=items_name}" + queryString;
			query.setQuery(queryString);
			query.set("df", "items_name");
			//设置高亮显示
			query.setHighlight(true);
			query.addHighlightField("items_name");
			query.addHighlightField("items_detail");
			query.setHighlightSimplePre("<em style=\"color:red\">");
			query.setHighlightSimplePost("</em>");

			// 执行查询
			List<ItemsCustom> itemlist = solrSearchDao.getItemsListBySolr(query);

			return itemlist;
		}
		return null;
	}

	public List<ItemsCustom> getItemsList(ItemsQueryVo itemsQueryVo) {
		return itemsCustomMapper.getItemsList(itemsQueryVo);
	}

}
