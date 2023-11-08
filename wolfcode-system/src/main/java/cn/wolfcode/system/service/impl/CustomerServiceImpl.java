package cn.wolfcode.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.wolfcode.system.mapper.CustomerMapper;
import cn.wolfcode.system.domain.Customer;
import cn.wolfcode.system.service.ICustomerService;

/**
 * 客户管理1Service业务层处理
 * 
 * @author lin
 * @date 2023-10-26
 */
@Service
public class CustomerServiceImpl implements ICustomerService 
{
    @Autowired
    private CustomerMapper customerMapper;

    /**
     * 查询客户管理1
     * 
     * @param id 客户管理1主键
     * @return 客户管理1
     */
    @Override
    public Customer selectCustomerById(Long id)
    {
        return customerMapper.selectCustomerById(id);
    }

    /**
     * 查询客户管理1列表
     * 
     * @param customer 客户管理1
     * @return 客户管理1
     */
    @Override
    public List<Customer> selectCustomerList(Customer customer)
    {
        return customerMapper.selectCustomerList(customer);
    }

    /**
     * 新增客户管理1
     * 
     * @param customer 客户管理1
     * @return 结果
     */
    @Override
    public int insertCustomer(Customer customer)
    {
        return customerMapper.insertCustomer(customer);
    }

    /**
     * 修改客户管理1
     * 
     * @param customer 客户管理1
     * @return 结果
     */
    @Override
    public int updateCustomer(Customer customer)
    {
        return customerMapper.updateCustomer(customer);
    }

    /**
     * 批量删除客户管理1
     * 
     * @param ids 需要删除的客户管理1主键
     * @return 结果
     */
    @Override
    public int deleteCustomerByIds(Long[] ids)
    {
        return customerMapper.deleteCustomerByIds(ids);
    }

    /**
     * 删除客户管理1信息
     * 
     * @param id 客户管理1主键
     * @return 结果
     */
    @Override
    public int deleteCustomerById(Long id)
    {
        return customerMapper.deleteCustomerById(id);
    }
}
