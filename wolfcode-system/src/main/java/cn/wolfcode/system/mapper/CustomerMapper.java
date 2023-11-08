package cn.wolfcode.system.mapper;

import java.util.List;

import cn.wolfcode.system.domain.Customer;

/**
 * 客户管理1Mapper接口
 * 
 * @author lin
 * @date 2023-10-26
 */
public interface CustomerMapper 
{
    /**
     * 查询客户管理1
     * 
     * @param id 客户管理1主键
     * @return 客户管理1
     */
    public Customer selectCustomerById(Long id);

    /**
     * 查询客户管理1列表
     * 
     * @param customer 客户管理1
     * @return 客户管理1集合
     */
    public List<Customer> selectCustomerList(Customer customer);

    /**
     * 新增客户管理1
     * 
     * @param customer 客户管理1
     * @return 结果
     */
    public int insertCustomer(Customer customer);

    /**
     * 修改客户管理1
     * 
     * @param customer 客户管理1
     * @return 结果
     */
    public int updateCustomer(Customer customer);

    /**
     * 删除客户管理1
     * 
     * @param id 客户管理1主键
     * @return 结果
     */
    public int deleteCustomerById(Long id);

    /**
     * 批量删除客户管理1
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCustomerByIds(Long[] ids);
}
