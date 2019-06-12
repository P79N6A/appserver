package com.idcq.appserver.controller.test;

import java.io.Serializable;

import com.idcq.appserver.common.annotation.Check;


public class TestModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3849949938728754799L;
	
/*	@Check(required = false,sensitive=true) 如果非必填字段，属性名增加此注释*/
	@Check(sensitive=true)/*必填字段增加此注释*/
	private String checkSensiviWords;

	public String getCheckSensiviWords() {
		return checkSensiviWords;
	}

	public void setCheckSensiviWords(String checkSensiviWords) {
		this.checkSensiviWords = checkSensiviWords;
	}
}
