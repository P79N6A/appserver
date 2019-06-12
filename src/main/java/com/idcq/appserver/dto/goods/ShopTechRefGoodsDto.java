package com.idcq.appserver.dto.goods;
/**
 * 商铺商品关联技师
 * @ClassName: ShopTechRefGoodsDto 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年7月30日 下午5:50:24 
 *
 */
public class ShopTechRefGoodsDto {
	
	/**
	 * 关联id
	 */
	private Long refGoodsId;
	
	/**
	 * 技师Id
	 */
	private Long techId;
	
	/**
	 * 商品族ID
	 * 
	 */
	private Long goodsGroupId;
	
	/**
	 * 技师名称
	 */
	private String techName;

	public Long getRefGoodsId() {
		return refGoodsId;
	}

	public void setRefGoodsId(Long refGoodsId) {
		this.refGoodsId = refGoodsId;
	}

	public Long getTechId() {
		return techId;
	}

	public void setTechId(Long techId) {
		this.techId = techId;
	}

	public Long getGoodsGroupId() {
		return goodsGroupId;
	}

	public void setGoodsGroupId(Long goodsGroupId) {
		this.goodsGroupId = goodsGroupId;
	}

	public String getTechName() {
		return techName;
	}

	public void setTechName(String techName) {
		this.techName = techName;
	}
}
