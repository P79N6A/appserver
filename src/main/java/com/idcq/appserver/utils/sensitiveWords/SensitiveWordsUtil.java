package com.idcq.appserver.utils.sensitiveWords;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.processor.SensitiveWordsProcessor;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.utils.BeanFactory;

public class SensitiveWordsUtil {
	private final static Logger logger = LoggerFactory.getLogger(SensitiveWordsUtil.class);
	private final static SensitiveWordsProcessor sensitiveWordsProcessor = (SensitiveWordsProcessor)BeanFactory.getBean("sensitiveWordsProcessor");
	
	public static void checkSensitiveWords(String filedName,Object filedValue) throws Exception {
		if (StringUtils.isBlank(filedName)) {
			logger.info("filedName不能为空");
			return;
		}
		
		if (filedValue == null) {
			logger.info("filedValue不能为空");
			return;
		}
		
		Long startMatchTime = System.currentTimeMillis();
		Set<String> containSensitiveWords = sensitiveWordsProcessor.getSensitiveWords(filedValue.toString());
		Long endMatchTime = System.currentTimeMillis();
		logger.info("检索字段：{} 敏感词用时{}ms",filedName,endMatchTime - startMatchTime);
		if (!CollectionUtils.isEmpty(containSensitiveWords)) {
			throw new ValidateException(CodeConst.CODE_SENSITIVE_WORDS_ERROR,filedName+"字段包含如下敏感信息："+containSensitiveWords);
		}
	}
	
	public static Set<String> getSensitiveWords(String words) {
		return sensitiveWordsProcessor.getSensitiveWords(words);
	}
}
