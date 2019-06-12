package com.idcq.appserver.utils.solr;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

import com.idcq.appserver.common.CommonConst;
/**
 * 搜索服务
* @ClassName: SearchServer 
* @Description: TODO
* @author 张鹏程 
* @date 2015年6月10日 上午9:41:07 
*
 */
@SuppressWarnings("serial")
public class SearchServer extends HttpSolrServer{
	
	/**
	 * 节点状态
	 */
	private Integer status;
	/**
	 * 故障发生时间
	 */
	private String happendTime;
	
	/**
	 * 总条数
	 */
	private Integer totalNum;
	
	/**
	 * 已经索引的条数
	 */
	private Integer indexdNum;
	
	private String solrUrl;
	
	private String tag;
	public SearchServer(String solrUrl) {
		super(solrUrl);
		this.solrUrl=solrUrl;
	}

	public Integer getStatus() {
		if(status==null)
		{
			status=CommonConst.SOLR_NODE_NORMAL;
		}
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getHappendTime() {
		return happendTime;
	}

	public void setHappendTime(String happendTime) {
		this.happendTime = happendTime;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	public Integer getIndexdNum() {
		if(indexdNum==null)
		{
			indexdNum=0;
		}
		return indexdNum;
	}

	public synchronized void setIndexdNum(Integer indexdNum) {
		this.indexdNum = indexdNum;
	}

	public String getSolrUrl() {
		return solrUrl;
	}

	public void setSolrUrl(String solrUrl) {
		this.solrUrl = solrUrl;
		solrUrl.split(".");
	}

	public String getTag() {
		if(solrUrl!=null)
		{
			String []array=solrUrl.split("\\.");
			if(array.length>3)
			{
				String a=array[3];
				array=a.split(":");
				tag=array[0];
			}
		}
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	
}
