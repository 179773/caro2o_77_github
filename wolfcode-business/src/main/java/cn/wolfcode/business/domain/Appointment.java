package cn.wolfcode.business.domain;

import java.util.Date;

import cn.wolfcode.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import cn.wolfcode.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;

/**
 * 养修信息预约对象 bus_appointment
 * 
 * @author lin
 * @date 2023-10-28
 */
public class Appointment extends BaseEntity
{

    public static final Integer STATUS_APPOINTMENT = 0;//预约中
    public static final Integer STATUS_ARRIVAL = 1;//已到店
    public static final Integer STATUS_CANCEL = 2;//用户取消
    public static final Integer STATUS_OVERTIME = 3;//超时取消
    public static final Integer STATUS_SETTLE  = 4;//结算单生成
    public static final Integer STATUS_PAYED  = 5;//已支付

    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 客户姓名 */
    @Excel(name = "客户姓名")
    @NotBlank
    private String customerName;

    /** 客户联系方式 */
    @Excel(name = "客户联系方式")
    private String customerPhone;

    /** 预约时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "预约时间", width = 30, dateFormat = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date appointmentTime;

    /** 实际到店时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "实际到店时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actualArrivalTime;

    /** 车牌号码 */
    @Excel(name = "车牌号码")
    private String licensePlate;

    /** 汽车类型 */
    @Excel(name = "汽车类型")
    private String carSeries;

    /** 服务类型【维修0/保养1】 */
    @Excel(name = "服务类型【维修0/保养1】")
    private Integer serviceType;

    /** 备注信息 */
    @Excel(name = "备注信息")
    private String info;

    /** 状态【预约中0/已到店1/用户取消2/超时取消3/已结算4/已支付5】 */
    @Excel(name = "状态【预约中0/已到店1/用户取消2/超时取消3/已结算4/已支付5】")
    private Integer status;

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
    public void setAppointmentTime(Date appointmentTime) 
    {
        this.appointmentTime = appointmentTime;
    }

    public Date getAppointmentTime() 
    {
        return appointmentTime;
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
    public void setInfo(String info) 
    {
        this.info = info;
    }

    public String getInfo() 
    {
        return info;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }


}
