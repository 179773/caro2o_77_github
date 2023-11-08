package cn.wolfcode.business.service;

import java.io.InputStream;
import java.util.List;
import cn.wolfcode.business.domain.CarPackageAudit;
import cn.wolfcode.business.domain.vo.CarPackageAuditVO;
import cn.wolfcode.business.domain.vo.HistoryVO;

/**
 * 套餐审核Service接口
 * 
 * @author lin
 * @date 2023-11-03
 */
public interface ICarPackageAuditService 
{
    /**
     * 查询套餐审核
     * 
     * @param id 套餐审核主键
     * @return 套餐审核
     */
    public CarPackageAudit selectCarPackageAuditById(Long id);

    /**
     * 查询套餐审核列表
     * 
     * @param carPackageAudit 套餐审核
     * @return 套餐审核集合
     */
    public List<CarPackageAudit> selectCarPackageAuditList(CarPackageAudit carPackageAudit);

    /**
     * 新增套餐审核
     * 
     * @param carPackageAudit 套餐审核
     * @return 结果
     */
    public int insertCarPackageAudit(CarPackageAudit carPackageAudit);

    /**
     * 修改套餐审核
     * 
     * @param carPackageAudit 套餐审核
     * @return 结果
     */
    public int updateCarPackageAudit(CarPackageAudit carPackageAudit);

    /**
     * 批量删除套餐审核
     * 
     * @param ids 需要删除的套餐审核主键集合
     * @return 结果
     */
    public int deleteCarPackageAuditByIds(Long[] ids);

    /**
     * 删除套餐审核信息
     * 
     * @param id 套餐审核主键
     * @return 结果
     */
    public int deleteCarPackageAuditById(Long id);

    InputStream process(Long id);

    void cancel(Long id);

    /**
     * 查询指定流程实例的审核历史
     * @param instanceId
     * @return
     */
    List<HistoryVO> queryHistory(Long instanceId);

    List<CarPackageAudit> todoQuery(CarPackageAudit carPackageAudit);

    void audit(CarPackageAuditVO vo);

    List<CarPackageAudit> doneQuery(CarPackageAudit carPackageAudit);
}
