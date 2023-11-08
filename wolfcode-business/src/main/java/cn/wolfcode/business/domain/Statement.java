package cn.wolfcode.business.domain;

import java.math.BigDecimal;
import java.util.Date;

import cn.wolfcode.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import cn.wolfcode.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;

/**
 * 结算单对象 bus_statement
 * 
 * @author lin
 * @date 2023-10-31
 */
public class Statement extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    public static final Integer STATUS_CONSUME = 0;//消费中
    public static final Integer STATUS_PAID = 1;//已支付
    public static final Integer IS_DEL_YES = 1;//已删除
    public static final Integer IS_DEL_NO = 0;//未删除

    /** $column.columnComment */
    private Long id;

    /** 客户姓名 */
    @NotBlank(message = "非法操作Statement.domain")
    @Excel(name = "客户姓名")
    private String customerName;

    /** 客户联系方式 */
    @NotBlank(message = "非法操作Statement.domain")
    @Excel(name = "客户联系方式")
    private String customerPhone;

    /** 实际到店时间 */
    @NotBlank(message = "非法操作Statement.domain")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "实际到店时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actualArrivalTime;

    /** 车牌号码 */
    @NotBlank(message = "非法操作Statement.domain")
    @Excel(name = "车牌号码")
    private String licensePlate;

    /** 汽车类型 */
    @NotBlank(message = "非法操作Statement.domain")
    @Excel(name = "汽车类型")
    private String carSeries;

    /** 服务类型 */
    @NotBlank(message = "非法操作Statement.domain")
    @Excel(name = "服务类型")
    private Integer serviceType;

    /** 预约单ID */
    @Excel(name = "预约单ID")
    private Long appointmentId;

    /** 结算状态 */
    @Excel(name = "结算状态")
    private Integer status;

    /** 收款时间 */
    private Date payTime;

    /** 收款人id */
    private Long payeeId;

    /** 总消费金额 */
    private BigDecimal totalAmount;

    /** 服务项数量 */
    private BigDecimal totalQuantity;

    /** 折扣金额 */
    private BigDecimal discountAmount;

    /** 备注信息 */
    private String info;

    /** 0没有删除/1删除 */
    private Integer isDelete = 0;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCustomerName(String customerName) 
    {
        this.customerName = customerName;
    }

    public String getCustomerName() 
    {
        return customerName;
    }
    public void setCustomerPhone(String customerPhone) 
    {
        this.customerPhone = customerPhone;
    }

    public String getCustomerPhone() 
    {
        return customerPhone;
    }
    public void setActualArrivalTime(Date actualArrivalTime) 
    {
        this.actualArrivalTime = actualArrivalTime;
    }

    public Date getActualArrivalTime() 
    {
        return actualArrivalTime;
    }
    public void setLicensePlate(String licensePlate) 
    {
        this.licensePlate = licensePlate;
    }

    public String getLicensePlate() 
    {
        return licensePlate;
    }
    public void setCarSeries(String carSeries) 
    {
        this.carSeries = carSeries;
    }

    public String getCarSeries() 
    {
        return carSeries;
    }
    public void setServiceType(Integer serviceType) 
    {
        this.serviceType = serviceType;
    }

    public Integer getServiceType() 
    {
        return serviceType;
    }
    public void setAppointmentId(Long appointmentId) 
    {
        this.appointmentId = appointmentId;
    }

    public Long getAppointmentId() 
    {
        return appointmentId;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setPayTime(Date payTime) 
    {
        this.payTime = payTime;
    }

    public Date getPayTime() 
    {
        return payTime;
    }
    public void setPayeeId(Long payeeId) 
    {
        this.payeeId = payeeId;
    }

    public Long getPayeeId() 
    {
        return payeeId;
    }
    public void setTotalAmount(BigDecimal totalAmount) 
    {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmount() 
    {
        return totalAmount;
    }
    public void setTotalQuantity(BigDecimal totalQuantity) 
    {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalQuantity() 
    {
        return totalQuantity;
    }
    public void setDiscountAmount(BigDecimal discountAmount) 
    {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getDiscountAmount() 
    {
        return discountAmount;
    }
    public void setInfo(String info) 
    {
        this.info = info;
    }

    public String getInfo() 
    {
        return info;
    }
    public void setIsDelete(Integer isDelete) 
    {
        this.isDelete = isDelete;
    }

    public Integer getIsDelete() 
    {
        return isDelete;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("customerName", getCustomerName())
            .append("customerPhone", getCustomerPhone())
            .append("actualArrivalTime", getActualArrivalTime())
            .append("licensePlate", getLicensePlate())
            .append("carSeries", getCarSeries())
            .append("serviceType", getServiceType())
            .append("appointmentId", getAppointmentId())
            .append("status", getStatus())
            .append("payTime", getPayTime())
            .append("payeeId", getPayeeId())
            .append("totalAmount", getTotalAmount())
            .append("totalQuantity", getTotalQuantity())
            .append("discountAmount", getDiscountAmount())
            .append("createTime", getCreateTime())
            .append("info", getInfo())
            .append("isDelete", getIsDelete())
            .toString();
    }
}
