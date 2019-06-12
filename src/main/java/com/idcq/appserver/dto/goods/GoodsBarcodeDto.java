package com.idcq.appserver.dto.goods;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.idcq.appserver.dto.column.ColumnDto;


/**s
 * 商品dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月20日
 * @time 下午7:31:37
 */
public class GoodsBarcodeDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -9195673049643467558L;
	/**
	 * 
	 */
	
	  private Integer id; // '条码ID',
	  private String barcode; // '条码号',
	  private String goodsName; //  '商品名',
	  private String unitName; //  '单位',
	  private String goodsSize; //  '规格',
	  private Double inPrice ; // '进货价格',
	  private Double salePrice ; // '销售价格',
	  private String productName; //  '生产地',
	  
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getGoodsSize() {
		return goodsSize;
	}
	public void setGoodsSize(String goodsSize) {
		this.goodsSize = goodsSize;
	}
	public Double getInPrice() {
		return inPrice;
	}
	public void setInPrice(Double inPrice) {
		this.inPrice = inPrice;
	}
	public Double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	  

}