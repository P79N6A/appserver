package com.idcq.appserver.utils.solr.analyzer;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class IkAnalyzerContext {
	
	private static IkAnalyzerContext context=new IkAnalyzerContext();
	private static IKAnalyzer analyzer=new 	IKAnalyzer();
	
	private IkAnalyzerContext()
	{
		
	}
	
	
	public static IkAnalyzerContext getInstance()
	{
		
		return context;
	}
	
	
	/**
	 * 获得最长的中文词元的长度
	 * @Title: getMaxCnLengthForAnalyzer 
	 * @param @param source
	 * @param @return
	 * @return int    返回类型 
	 * @throws
	 */
	public static int getMaxCnLengthForAnalyzer(String source)
	{
		  int maxWord=1;
		  TokenStream ts = null;
			try {
				ts = analyzer.tokenStream("myfield", new StringReader(source));
				//获取词元位置属性
			    OffsetAttribute  offset = ts.addAttribute(OffsetAttribute.class); 
			    //获取词元文本属性
			    CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
			    //获取词元文本属性
			    TypeAttribute type = ts.addAttribute(TypeAttribute.class);
			    //重置TokenStream（重置StringReader）
				ts.reset(); 
				//迭代获取分词结果
				while (ts.incrementToken()) {
				  String wordType=type.type();
				  if("CN_WORD".equals(wordType))
				  {
					  int length=term.toString().length();
					  if(length>maxWord)
					  {
						  maxWord=length;
					  }
				  }
				}
				//关闭TokenStream（关闭StringReader）
				ts.end();   // Perform end-of-stream operations, e.g. set the final offset.
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				//释放TokenStream的所有资源
				if(ts != null){
			      try {
					ts.close();
			      } catch (IOException e) {
					e.printStackTrace();
			      }
				}
		    }
		return maxWord;
	}
	
	
}
