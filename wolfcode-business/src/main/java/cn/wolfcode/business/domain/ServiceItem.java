package cn.wolfcode.business.domain;

import java.math.BigDecimal;
import cn.wolfcode.common.annotation.Excel;
import cn.wolfcode.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 服务项对象 bus_service_item
 * 
 * @author lin
 * @date 2023-10-29
 */
public class ServiceItem extends BaseEntity
{
    public static final Integer CARPACKAGE_NO = 0;//不是套餐
    public static final Integer CARPACKAGE_YES = 1;//是套餐

    public static final Integer AUDITSTATUS_INIT = 0;//初始化
    public static final Integer AUDITSTATUS_AUDITING = 1;//审核中
    public static final Integer AUDITSTATUS_APPROVED = 2;//审核通过
    public static final Integer AUDITSTATUS_REPLY = 3;//重新调整
    public static final Integer AUDITSTATUS_NO_REQUIRED = 4;//无需审核

    public static final Integer SALESTATUS_OFF = 0;//下架
    public static final Integer SALESTATUS_ON = 1;//上架

    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 服务项名称 */
    @Excel(name = "服务项名称")
    private String name;

    /** 服务项原价 */
    @Excel(name = "服务项原价")
    private BigDecimal originalPrice;

    /** 服务项折扣价 */
    @Excel(name = "服务项折扣价")
    private BigDecimal discountPrice;

    /** 是否套餐 */
    @Excel(name = "是否套餐")
    private Integer carPackage;

    /** 备注信息 */
    @Excel(name = "备注信息")
    private String info;

    /** 服务分类 */
    @Excel(name = "服务分类")
    private Long serviceCatalog;

    /** 审核状态 */
    @Excel(name = "审核状态")
    private Integer auditStatus;

    /** 上架状态【1已上架/0未上架】 */
    @Excel(name = "上架状态【1已上架/0未上架】")
    private Integer saleStatus;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setOriginalPrice(BigDecimal originalPrice) 
    {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getOriginalPrice() 
    {
        return originalPrice;
    }
    public void setDiscountPrice(BigDecimal discountPrice) 
    {
        this.discountPrice = discountPrice;
    }

    public BigDecimal getDiscountPrice() 
    {
        return discountPrice;
    }
    public void setCarPackage(Integer carPackage)
    {
        this.carPackage = carPackage;
    }

    public Integer getCarPackage()
    {
        return carPackage;
    }
    public void setInfo(String info) 
    {
        this.info = info;
    }

    public String getInfo() 
    {
        return info;
    }
    public void setServiceCatalog(Long serviceCatalog) 
    {
        this.serviceCatalog = serviceCatalog;
    }

    public Long getServiceCatalog() 
    {
        return serviceCatalog;
    }
    public void setAuditStatus(Integer auditStatus)
    {
        this.auditStatus = auditStatus;
    }

    public Integer getAuditStatus()
    {
        return auditStatus;
    }
    public void setSaleStatus(Integer saleStatus)
    {
        this.saleStatus = saleStatus;
    }

    public Integer getSaleStatus()
    {
        return saleStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("originalPrice", getOriginalPrice())
            .append("discountPrice", getDiscountPrice())
            .append("carPackage", getCarPackage())
            .append("info", getInfo())
            .append("createTime", getCreateTime())
            .append("serviceCatalog", getServiceCatalog())
            .append("auditStatus", getAuditStatus())
            .append("saleStatus", getSaleStatus())
            .toString();
    }
}
