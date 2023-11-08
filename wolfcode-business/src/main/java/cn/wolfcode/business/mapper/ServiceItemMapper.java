package cn.wolfcode.business.mapper;

import java.util.List;
import cn.wolfcode.business.domain.ServiceItem;
import org.apache.ibatis.annotations.Param;

/**
 * 服务项Mapper接口
 * 
 * @author lin
 * @date 2023-10-29
 */
public interface ServiceItemMapper 
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
     * 删除服务项
     * 
     * @param id 服务项主键
     * @return 结果
     */
    public int deleteServiceItemById(Long id);

    /**
     * 批量删除服务项
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteServiceItemByIds(Long[] ids);

    void updateAuitStatus(@Param("id") Long id, @Param("status") Integer status);
}
