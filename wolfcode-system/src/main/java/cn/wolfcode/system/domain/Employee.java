package cn.wolfcode.system.domain;

import java.math.BigDecimal;
import java.util.Date;

import cn.wolfcode.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.wolfcode.common.annotation.Excel;

/**
 * 员工管理2对象 employee
 * 
 * @author lin
 * @date 2023-10-26
 */
public class Employee extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 员工ID */
    private Long id;

    /** 员工姓名 */
    @Excel(name = "员工姓名")
    private String name;

    /** 员工手机号 */
    @Excel(name = "员工手机号")
    private String phone;

    /** 员工年龄 */
    @Excel(name = "员工年龄")
    private Long age;

    /** 员工邮箱 */
    @Excel(name = "员工邮箱")
    private String email;

    /** 员工地址 */
    @Excel(name = "员工地址")
    private String address;

    /** 员工职位 */
    @Excel(name = "员工职位")
    private String position;

    /** 所属部门 */
    @Excel(name = "所属部门")
    private String department;

    /** 入职日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入职日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date hireDate;

    /** 薪水 */
    @Excel(name = "薪水")
    private BigDecimal salary;

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
    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }
    public void setAge(Long age) 
    {
        this.age = age;
    }

    public Long getAge() 
    {
        return age;
    }
    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }
    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }
    public void setPosition(String position) 
    {
        this.position = position;
    }

    public String getPosition() 
    {
        return position;
    }
    public void setDepartment(String department) 
    {
        this.department = department;
    }

    public String getDepartment() 
    {
        return department;
    }
    public void setHireDate(Date hireDate) 
    {
        this.hireDate = hireDate;
    }

    public Date getHireDate() 
    {
        return hireDate;
    }
    public void setSalary(BigDecimal salary) 
    {
        this.salary = salary;
    }

    public BigDecimal getSalary() 
    {
        return salary;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("phone", getPhone())
            .append("age", getAge())
            .append("email", getEmail())
            .append("address", getAddress())
            .append("position", getPosition())
            .append("department", getDepartment())
            .append("hireDate", getHireDate())
            .append("salary", getSalary())
            .toString();
    }
}
