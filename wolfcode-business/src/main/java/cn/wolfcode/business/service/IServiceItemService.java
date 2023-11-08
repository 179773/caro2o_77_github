package cn.wolfcode.business.service;

import java.util.List;
import cn.wolfcode.business.domain.ServiceItem;
import cn.wolfcode.business.domain.vo.AuditInfoVO;
import cn.wolfcode.business.domain.vo.AuditVO;

/**
 * 服务项Service接口
 * 
 * @author lin
 * @date 2023-10-29
 */
public interface IServiceItemService 
{
    /**
     * 查询服务项
     * 
     * @param id 服务项主键
     * @return 服务项
     */
    public ServiceItem selectServiceItemById(Long id);

    /**
     * 查询服务项列表
     * 
     * @param serviceItem 服务项
     * @return 服务项集合
     */
    public List<ServiceItem> selectServiceItemList(ServiceItem serviceItem);

    /**
     * 新增服务项
     * 
     * @param serviceItem 服务项
     * @return 结果
     */
    public int insertServiceItem(ServiceItem serviceItem);

    /**
     * 修改服务项
     * 
     * @param serviceItem 服务项
     * @return 结果
     */
    public int updateServiceItem(ServiceItem serviceItem);

    /**
     * 批量删除服务项
     * 
     * @param ids 需要删除的服务项主键集合
     * @return 结果
     */
    public int deleteServiceItemByIds(Long[] ids);

    /**
     * 删除服务项信息
     * 
     * @param id 服务项主键
     * @return 结果
     */
    public int deleteServiceItemById(Long id);

    /**
     * 上架
     * @param id
     * @return
     */
    int listing(Long id);

    int delisting(Long id);

    AuditInfoVO auditInfo(Long id);

    void startAudit(AuditVO vo);
}
