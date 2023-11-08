package cn.wolfcode.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import cn.wolfcode.business.mapper.CustomerInfoMapper;
import cn.wolfcode.common.core.domain.entity.SysUser;
import cn.wolfcode.common.utils.SecurityUtils;
import cn.wolfcode.system.mapper.SysUserMapper;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.wolfcode.business.mapper.CustomerReturnVisitMapper;
import cn.wolfcode.business.domain.CustomerReturnVisit;
import cn.wolfcode.business.service.ICustomerReturnVisitService;

/**
 * 客户回访记录Service业务层处理
 * 
 * @author lin
 * @date 2023-11-05
 */
@Service
public class CustomerReturnVisitServiceImpl implements ICustomerReturnVisitService 
{
    @Autowired
    private CustomerReturnVisitMapper customerReturnVisitMapper;
    @Autowired
    private CustomerInfoMapper customerInfoMapper;
    @Autowired
    private SysUserMapper userMapper;

    /**
     * 查询客户回访记录
     * 
     * @param id 客户回访记录主键
     * @return 客户回访记录
     */
    @Override
    public CustomerReturnVisit selectCustomerReturnVisitById(Long id)
    {
        return customerReturnVisitMapper.selectCustomerReturnVisitById(id);
    }

    /**
     * 查询客户回访记录列表
     * 
     * @param customerReturnVisit 客户回访记录
     * @return 客户回访记录
     */
    @Override
    public List<CustomerReturnVisit> selectCustomerReturnVisitList(CustomerReturnVisit customerReturnVisit)
    {
        return customerReturnVisitMapper.selectCustomerReturnVisitList(customerReturnVisit);
    }

    /**
     * 新增客户回访记录
     * 
     * @param customerReturnVisit 客户回访记录
     * @return 结果
     */
    @Override
    public int insertCustomerReturnVisit(CustomerReturnVisit customerReturnVisit)
    {

        String username = SecurityUtils.getUsername();
        // 回访用户
//        customerReturnVisit.setCustomerName(username);
        // 录入人
        customerReturnVisit.setEntryUser(username);
        return customerReturnVisitMapper.insertCustomerReturnVisit(customerReturnVisit);
    }

    /**
     * 修改客户回访记录
     * 
     * @param customerReturnVisit 客户回访记录
     * @return 结果
     */
    @Override
    public int updateCustomerReturnVisit(CustomerReturnVisit customerReturnVisit)
    {
        return customerReturnVisitMapper.updateCustomerReturnVisit(customerReturnVisit);
    }

    /**
     * 批量删除客户回访记录
     * 
     * @param ids 需要删除的客户回访记录主键
     * @return 结果
     */
    @Override
    public int deleteCustomerReturnVisitByIds(Long[] ids)
    {
        return customerReturnVisitMapper.deleteCustomerReturnVisitByIds(ids);
    }

    /**
     * 删除客户回访记录信息
     * 
     * @param id 客户回访记录主键
     * @return 结果
     */
    @Override
    public int deleteCustomerReturnVisitById(Long id)
    {
        return customerReturnVisitMapper.deleteCustomerReturnVisitById(id);
    }

    @Override
    public List<String> getUserList() {
        List<SysUser> sysUsers = userMapper.selectUserList2();
        List<String> list = new ArrayList<>();
        for (SysUser sysUser : sysUsers) {
            String userName = sysUser.getUserName();
            list.add(userName);
        }
        return list;
    }


}
