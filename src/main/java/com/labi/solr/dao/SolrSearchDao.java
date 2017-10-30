package com.labi.solr.dao;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;

import com.labi.pojo.ItemsCustom;

public interface SolrSearchDao {

	public List<ItemsCustom> getItemsListBySolr(SolrQuery query) throws Exception;
}
