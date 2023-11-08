package cn.wolfcode.business.service;

import java.util.List;
import cn.wolfcode.business.domain.CustomerReturnVisit;
import cn.wolfcode.common.core.domain.entity.SysUser;

/**
 * 客户回访记录Service接口
 * 
 * @author lin
 * @date 2023-11-05
 */
public interface ICustomerReturnVisitService 
{
    /**
     * 查询客户回访记录
     * 
     * @param id 客户回访记录主键
     * @return 客户回访记录
     */
    public CustomerReturnVisit selectCustomerReturnVisitById(Long id);

    /**
     * 查询客户回访记录列表
     * 
     * @param customerReturnVisit 客户回访记录
     * @return 客户回访记录集合
     */
    public List<CustomerReturnVisit> selectCustomerReturnVisitList(CustomerReturnVisit customerReturnVisit);

    /**
     * 新增客户回访记录
     * 
     * @param customerReturnVisit 客户回访记录
     * @return 结果
     */
    public int insertCustomerReturnVisit(CustomerReturnVisit customerReturnVisit);

    /**
     * 修改客户回访记录
     * 
     * @param customerReturnVisit 客户回访记录
     * @return 结果
     */
    public int updateCustomerReturnVisit(CustomerReturnVisit customerReturnVisit);

    /**
     * 批量删除客户回访记录
     * 
     * @param ids 需要删除的客户回访记录主键集合
     * @return 结果
     */
    public int deleteCustomerReturnVisitByIds(Long[] ids);

    /**
     * 删除客户回访记录信息
     * 
     * @param id 客户回访记录主键
     * @return 结果
     */
    public int deleteCustomerReturnVisitById(Long id);


    List<String> getUserList();
}
