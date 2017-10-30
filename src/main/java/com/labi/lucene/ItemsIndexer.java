package com.labi.lucene;

import java.io.StringReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;

import com.labi.pojo.ItemsCustom;
import com.labi.service.ItemsService;
import com.labi.util.StringUtil;

public class ItemsIndexer {

	@Autowired
	private ItemsService itemsService;

	public IndexWriter getIndexWriter() throws Exception {
		Path path = FileSystems.getDefault().getPath("", "index");
		Directory directory = FSDirectory.open(path);// 打开要索引存储的文件
		// 定义分词器
		// Analyzer analyzer = new StandardAnalyzer();
		SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer(); // 中文分词器
		// 索引文件创建的配置
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer)
				.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		// 创建IndexWriter
		IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

		return indexWriter;
	}

	public void createIndex() throws Exception {
		IndexWriter indexWriter = getIndexWriter();
		List<Document> docs = new ArrayList<Document>();
		List<ItemsCustom> itemsList = itemsService.getItemsList(null);
		// 索引和查询的单元
		Document document = null;
		for (ItemsCustom itemsCustom : itemsList) {
			// 定义文档
			// 定义文档字段
			document = new Document();
			document.add(new StringField("id", itemsCustom.getId().toString(), Field.Store.YES));
			document.add(new TextField("name", itemsCustom.getName(), Field.Store.YES));
			document.add(new TextField("detail", itemsCustom.getDetail(), Field.Store.YES));
			docs.add(document);
			List<IndexableField> fields = document.getFields();
			for (IndexableField indexableField : fields) {
				System.out.println(indexableField.toString());
			}
		}

		// 写入数据
		indexWriter.addDocuments(docs);
		// 提交
		indexWriter.commit();
		// 关闭
		indexWriter.close();

	}

	public List<ItemsCustom> searchItems(String q) throws Exception {
		Path path = FileSystems.getDefault().getPath("index");
		Directory directory = FSDirectory.open(path);
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(reader);
		// Analyzer analyzer=new StandardAnalyzer(); // 标准分词器
		SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
		// 查询解析器
		QueryParser parser = new QueryParser("name", analyzer);
		Query query1 = parser.parse(q);
		QueryParser parser2 = new QueryParser("detail", analyzer);
		Query query2 = parser2.parse(q);
	 	// 构造了一个BooleanQuery的对象，并将两个Query当成它的查询子句加入Boolean查询中。
		Builder builder = new BooleanQuery.Builder();
	 	builder.add(query1, BooleanClause.Occur.SHOULD);
	 	builder.add(query2, BooleanClause.Occur.SHOULD);
		long start = System.currentTimeMillis();
		// 查询前100条
		TopDocs hits = indexSearcher.search(builder.build(), 100);
		long end = System.currentTimeMillis();
		System.out.println("匹配 " + q + " ，总共花费" + (end - start) + "毫秒" + "查询到" + hits.totalHits + "个记录");

		// 定义QueryScorer，通过找到的独特的查询词数来分出文本片段
		QueryScorer scorer = new QueryScorer(query1);
		// Fragmenter切片，实现将文本分解为多个片段的策略
		Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
		// 这里可以根据自己的需要来自定义查找关键字高亮时的样式。
		SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color='red'>", "</font></b>");
		// 关键字高亮
		Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
		highlighter.setTextFragmenter(fragmenter);

		List<ItemsCustom> itemsCustomList = new ArrayList<ItemsCustom>();
		ScoreDoc[] scoreDocs = hits.scoreDocs;
		for (ScoreDoc scoreDoc : scoreDocs) { //去除索引库中的列设置到ItemsCustom对应的属性值中
			Document doc = indexSearcher.doc(scoreDoc.doc);
			ItemsCustom itemsCustom = new ItemsCustom();
			itemsCustom.setId(Integer.parseInt(doc.get(("id"))));
			itemsCustom.setName(doc.get("name"));
			itemsCustom.setDetail(doc.get(("detail")));
			String name = doc.get("name");
			String detail = doc.get("detail");
			// 高亮的处理
			if (name != null) {
				TokenStream tokenStream = analyzer.tokenStream("name", new StringReader(name));
				String husername = highlighter.getBestFragment(tokenStream, name);
				if (StringUtil.isEmpty(husername)) {
					itemsCustom.setName(name);
				} else {
					itemsCustom.setName(husername);
				}
			}
			if (detail != null) {
				TokenStream tokenStream = analyzer.tokenStream("detail", new StringReader(detail));
				String hContent = highlighter.getBestFragment(tokenStream, detail);
				if (StringUtil.isEmpty(hContent)) {
					if (detail.length() <= 200) {
						itemsCustom.setDetail(detail);
					} else {
						itemsCustom.setDetail(detail.substring(0, 200));
					}
				} else {
					itemsCustom.setDetail(hContent);
				}
			}

			itemsCustomList.add(itemsCustom);
		}
		return itemsCustomList;
	}

}
