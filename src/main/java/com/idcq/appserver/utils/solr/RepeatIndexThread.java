package com.idcq.appserver.utils.solr;

import java.util.Collection;

import org.apache.solr.common.SolrInputDocument;

/**
 * 
 * @ClassName: RepeatIndexThread 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年7月2日 上午9:48:49 
 *
 */
public class RepeatIndexThread extends Thread{
	private Collection<SolrInputDocument>docs;
	
	private String ip;
	public RepeatIndexThread(Collection<SolrInputDocument>docs,String ip)
	{
		this.docs=docs;
		this.ip=ip;
	}
	public void run() {
		
		try {
			sleep(5000);
			SolrContext.getInstance().repeatAddDocs(docs, ip);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
 
}
