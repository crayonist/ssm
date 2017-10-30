package com.labi;

import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class TestDemo {
	public static void main(String[] args) throws Exception {
		// 下面这个分词器，是经过修改支持同义词的分词器
		SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
		String text = "小米笔记本";
		TokenStream ts = analyzer.tokenStream("field", new StringReader(text));
		CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
		ts.reset();// 重置做准备
		while (ts.incrementToken()) {
			System.out.println(term.toString());
		}
		ts.end();//
		ts.close();// 关闭流

	}
}
