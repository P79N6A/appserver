package com.idcq.appserver.common.processor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.idcq.appserver.common.annotation.Processor;
import com.idcq.appserver.listeners.ContextInitListener;

@Processor("sensitiveWordsProcessor")
public class SensitiveWordsProcessor {
	private final static Logger logger = LoggerFactory.getLogger(SensitiveWordsProcessor.class);
	private  Map<String, Object> sensitivWordMap;
	
	@PostConstruct
	private void initSensitivWordsProcessor() throws Exception{
		logger.info("敏感词处理器初始化开始");
		Properties sensitiveWordPro = ContextInitListener.SENSITIV_WORDS;
		Set<String> sensitiveWords = getSensitiveWordsFromPro(sensitiveWordPro);
		Long startCreateIndex = System.currentTimeMillis();
		createSensitiveWordsIndex(sensitiveWords);
		Long endCreateIndex = System.currentTimeMillis();
		logger.info("敏感词处理器初始化结束 用时：{}ms",endCreateIndex - startCreateIndex);
	}
	
	@SuppressWarnings("unchecked")
	private Set<String> getSensitiveWordsFromPro(Properties sensitivWordPro) {
		return JSON.parseObject(JSON.toJSONString(sensitivWordPro.keySet()), Set.class);
	}
	
	@SuppressWarnings("unchecked")
	private void createSensitiveWordsIndex(Set<String> sensitivWords) {
		if (CollectionUtils.isEmpty(sensitivWords)) {
			sensitivWordMap = new HashMap<String, Object>();
			return;
		}
		sensitivWordMap = new HashMap<String, Object>(sensitivWords.size());
		for (String sensitivWord : sensitivWords) {
			sensitivWord = sensitivWord.trim();
			int sensitivWordLen = sensitivWord.length() -1;
			Map<String, Object> needCreateMap = sensitivWordMap;
			
			for (int i=0; i <= sensitivWordLen; i++) {
				String keyWord = String.valueOf(sensitivWord.charAt(i));
				Map<String, Object> keyWordMap = (Map<String, Object>)needCreateMap.get(keyWord);
				
				if (keyWordMap == null){
					keyWordMap = new HashMap<String, Object>();
					needCreateMap.put(keyWord, keyWordMap);
				}
				
				if (i != sensitivWordLen) {
					keyWordMap.put("end", false);
				}else {
					keyWordMap.put("end", true);
				}
				needCreateMap = keyWordMap;
			}
		}
	}
	
	public Set<String> getSensitiveWords(String words) {
		Set<String> containSensitivWordSet = new HashSet<String>();
		if (StringUtils.isNotBlank(words)) {
			int wordLen = words.length() - 1;
			for (int i=0; i <= wordLen; i++) {
				int sensitivWordLen = getSensitiveWordLength(words, i);
				if (sensitivWordLen > 0) {
					String sensitivWord = words.substring(i, i+sensitivWordLen);
					containSensitivWordSet.add(sensitivWord);
					i += sensitivWordLen - 1;
				}
			}
		}
		return containSensitivWordSet;
	}
	
	@SuppressWarnings("unchecked")
	private int getSensitiveWordLength(String words,int beginIndex) {
		int sensitivWordLen = 0;
		int wordLen = words.length() - 1;
		Boolean isMatch = false;
		Map<String, Object> sensitivWordIndexMap = sensitivWordMap;
		for (int i =beginIndex; i <= wordLen; i++) {
			String matchWord = String.valueOf(words.charAt(i));
			Map<String, Object> nextIndexMap = (Map<String, Object>)sensitivWordIndexMap.get(matchWord);
			if (nextIndexMap == null) {
				break;
			}
			
			sensitivWordLen++;
			Boolean isEnd = (Boolean)nextIndexMap.get("end");
			if (isEnd) {
				isMatch = true;
				break;
			}
			
			sensitivWordIndexMap = nextIndexMap;
		}
		
		if (!isMatch) {
			sensitivWordLen = 0;
		}
		return sensitivWordLen;
	}
}
