package com.ucar.smadmin.common.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 分页实体
 *
 * @author 吴佰川（baichuan.wu@ucarinc.com）创建
 * @date 2018/10/25 17:47
 * @version 1.0 2018/10/25 17:47
 */
public class PageVO<T>  implements Serializable {
    private static final long serialVersionUID = -3623596575707376133L;
    /**
     * 页码
     */
    private Integer pageIndex;

    /**
     * 页码
     */
    private Integer pageSize;

    /**
     * 结果列表
     */
    private List<T> dataList;

    /**
     * 总条数
     */
    private Long total;

    public PageVO() {
    }

    public PageVO(Long total, List<T> dataList) {
        this.total = total;
        this.dataList = dataList;
    }

    public PageVO(Long total, Integer pageIndex, Integer pageSize, List<T> dataList) {
        this.total = total;
        this.pageIndex = pageIndex;
        this.dataList = dataList;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "PageVO{" +
                "pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                ", dataList=" + dataList +
                ", total=" + total +
                '}';
    }
}
