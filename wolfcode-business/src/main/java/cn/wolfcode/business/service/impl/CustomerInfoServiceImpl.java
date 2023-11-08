package cn.wolfcode.business.service.impl;

import java.util.Date;
import java.util.List;

import cn.wolfcode.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.wolfcode.business.mapper.CustomerInfoMapper;
import cn.wolfcode.business.domain.CustomerInfo;
import cn.wolfcode.business.service.ICustomerInfoService;

/**
 * 客户信息Service业务层处理
 * 
 * @author Lin
 * @date 2023-11-05
 */
@Service
public class CustomerInfoServiceImpl implements ICustomerInfoService 
{
    @Autowired
    private CustomerInfoMapper customerInfoMapper;

    /**
     * 查询客户信息
     * 
     * @param id 客户信息主键
     * @return 客户信息
     */
    @Override
    public CustomerInfo selectCustomerInfoById(Long id)
    {
        return customerInfoMapper.selectCustomerInfoById(id);
    }

    /**
     * 查询客户信息列表
     * 
     * @param customerInfo 客户信息
     * @return 客户信息
     */
    @Override
    public List<CustomerInfo> selectCustomerInfoList(CustomerInfo customerInfo)
    {
        return customerInfoMapper.selectCustomerInfoList(customerInfo);
    }

    /**
     * 新增客户信息
     * 
     * @param customerInfo 客户信息
     * @return 结果
     */
    @Override
    public int insertCustomerInfo(CustomerInfo customerInfo)
    {
        String username = SecurityUtils.getUsername();
//        Date date = new Date();
        customerInfo.setEntryUser(username);
        /*customerInfo.setEntryTime(date);*/
        return customerInfoMapper.insertCustomerInfo(customerInfo);
    }

    /**
     * 修改客户信息
     * 
     * @param customerInfo 客户信息
     * @return 结果
     */
    @Override
    public int updateCustomerInfo(CustomerInfo customerInfo)
    {
        return customerInfoMapper.updateCustomerInfo(customerInfo);
    }

    /**
     * 批量删除客户信息
     * 
     * @param ids 需要删除的客户信息主键
     * @return 结果
     */
    @Override
    public int deleteCustomerInfoByIds(Long[] ids)
    {
        return customerInfoMapper.deleteCustomerInfoByIds(ids);
    }

    /**
     * 删除客户信息信息
     * 
     * @param id 客户信息主键
     * @return 结果
     */
    @Override
    public int deleteCustomerInfoById(Long id)
    {
        return customerInfoMapper.deleteCustomerInfoById(id);
    }
}
