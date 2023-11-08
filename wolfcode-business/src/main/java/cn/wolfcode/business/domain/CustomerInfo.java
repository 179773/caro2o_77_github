package cn.wolfcode.business.domain;

import java.util.Date;

import cn.wolfcode.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import cn.wolfcode.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 客户信息对象 customer_info
 * 
 * @author Lin
 * @date 2023-11-05
 */
public class CustomerInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 客户姓名 */
    @Excel(name = "客户姓名")
    private String customerName;

    /** 电话 */
    @Excel(name = "电话")
    private String phone;

    /** 性别 */
    @Excel(name = "性别")
    private Integer gender;

    /** 录入人 */
    @Excel(name = "录入人")
    private String entryUser;

    /** 录入时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "录入时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date entryTime;

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
    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }
    public void setGender(Integer gender) 
    {
        this.gender = gender;
    }

    public Integer getGender() 
    {
        return gender;
    }
    public void setEntryUser(String entryUser) 
    {
        this.entryUser = entryUser;
    }

    public String getEntryUser() 
    {
        return entryUser;
    }
    public void setEntryTime(Date entryTime) 
    {
        this.entryTime = entryTime;
    }

    public Date getEntryTime() 
    {
        return entryTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("customerName", getCustomerName())
            .append("phone", getPhone())
            .append("gender", getGender())
            .append("entryUser", getEntryUser())
            .append("entryTime", getEntryTime())
            .toString();
    }
}
