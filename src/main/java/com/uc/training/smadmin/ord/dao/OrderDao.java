package com.uc.training.smadmin.ord.dao;
import com.uc.training.smadmin.ord.model.Order;
import com.uc.training.smadmin.ord.re.OrderRe;
import com.uc.training.smadmin.ord.vo.OrdOrderVo;

import java.util.List;
/**
 * @author kun.du01@ucarinc.com
 * @date 2018-10-17 星期三 16:28
 * @description:OrderDao数据库操作接口类
 */
public interface OrderDao{

	/**
	 * 通过主键来查找查找
	 */
	 public List<Order>  getOrderById(Long id);

	/**
	 * 查询列表
	 */
	 public List<Order>  queryOrderList();

	/**
	 * 查找指定会员订单总记录数
	 */
	public Integer queryOrderCount(Long memberId);

	/**
	 * 保存
	 */
	public Long insertOrder(Order record);

	/**
	 * 更新
	 */
	public int updateOrderById(Order record);

/**
 *获取查询分页
 */
  List<OrderRe> getOrderPage (OrdOrderVo orderVo);

	/**
	 * 返回订单表的总记录数
	 * @return
	 */

	Integer getOrderTotal(OrdOrderVo orderVo);

	/**
	 * 逻辑删除订单
	 * @param list
	 * @return
	 */
	int logicDelOrder(List<OrderRe> list);

	/**
	 * 更新订单状态
	 * @param ordOrderVo
	 */
	void updateOrder(OrdOrderVo ordOrderVo);
}