package cn.wolfcode.business.mapper;

import java.util.List;
import cn.wolfcode.business.domain.BpmnInfo;

/**
 * 流程定义明细Mapper接口
 * 
 * @author lin
 * @date 2023-11-01
 */
public interface BpmnInfoMapper 
{
    /**
     * 查询流程定义明细
     * 
     * @param id 流程定义明细主键
     * @return 流程定义明细
     */
    public BpmnInfo selectBpmnInfoById(Long id);

    /**
     * 查询流程定义明细列表
     * 
     * @param bpmnInfo 流程定义明细
     * @return 流程定义明细集合
     */
    public List<BpmnInfo> selectBpmnInfoList(BpmnInfo bpmnInfo);

    /**
     * 新增流程定义明细
     * 
     * @param bpmnInfo 流程定义明细
     * @return 结果
     */
    public int insertBpmnInfo(BpmnInfo bpmnInfo);

    /**
     * 修改流程定义明细
     * 
     * @param bpmnInfo 流程定义明细
     * @return 结果
     */
    public int updateBpmnInfo(BpmnInfo bpmnInfo);

    /**
     * 删除流程定义明细
     * 
     * @param id 流程定义明细主键
     * @return 结果
     */
    public int deleteBpmnInfoById(Long id);

    /**
     * 批量删除流程定义明细
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBpmnInfoByIds(Long[] ids);

    BpmnInfo queryByType(Integer flowAuditType);
}
