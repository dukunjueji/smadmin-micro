package com.uc.training.smadmin.ord.dao;
import com.uc.training.smadmin.ord.model.OrderGoods;import java.util.List;
/**
 * @author kun.du01@ucarinc.com
 * @date 2018-10-17 星期三 16:28
 * @description:OrderGoodsDao数据库操作接口类
 */
public interface OrderGoodsDao{

	/**
	 * 通过主键来查找
	 * @param id
	 * @return
	 */
	  List<OrderGoods>  getOrderGoodsByOrderId(Integer id);

	/**
	 * 查询列表
	 * @return
	 */
	  List<OrderGoods>  queryOrderGoodsList();

	/**
	 * 查找数据总记录数
	 * @return
	 */
	 Integer queryOrderGoodsCount();

	/**
	 * 保存
	 * @param record
	 * @return
	 */
	 Long insertOrderGoods(OrderGoods record);

	/**
	 * 更新
	 * @param record
	 * @return
	 */
	 int updateOrderGoodsById(OrderGoods record);

	/**
	 * 通过商品属性id获取待支付的商品属性数量
	 * @param propertyId
	 * @return
	 */
    Integer getUnPayGoodsPropertyCountByPropertyId(Long propertyId);

}