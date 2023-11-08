package cn.wolfcode.business.domain;

import java.util.Date;

import cn.wolfcode.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import cn.wolfcode.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 客户回访记录对象 customer_return_visit
 * 
 * @author lin
 * @date 2023-11-05
 */
public class CustomerReturnVisit extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 回访客户 */
    @Excel(name = "回访客户")
    private String customerName;

    /** 回访方式 */
    @Excel(name = "回访方式")
    private String visitMethod;

    /** 回访原因 */
    @Excel(name = "回访原因")
    private String visitReason;

    /** 回访结果 */
    @Excel(name = "回访结果")
    private String visitResult;

    /** 回访日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "回访日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date visitDate;

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
    public void setVisitMethod(String visitMethod) 
    {
        this.visitMethod = visitMethod;
    }

    public String getVisitMethod() 
    {
        return visitMethod;
    }
    public void setVisitReason(String visitReason) 
    {
        this.visitReason = visitReason;
    }

    public String getVisitReason() 
    {
        return visitReason;
    }
    public void setVisitResult(String visitResult) 
    {
        this.visitResult = visitResult;
    }

    public String getVisitResult() 
    {
        return visitResult;
    }
    public void setVisitDate(Date visitDate) 
    {
        this.visitDate = visitDate;
    }

    public Date getVisitDate() 
    {
        return visitDate;
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
            .append("visitMethod", getVisitMethod())
            .append("visitReason", getVisitReason())
            .append("visitResult", getVisitResult())
            .append("visitDate", getVisitDate())
            .append("entryUser", getEntryUser())
            .append("entryTime", getEntryTime())
            .toString();
    }
}
