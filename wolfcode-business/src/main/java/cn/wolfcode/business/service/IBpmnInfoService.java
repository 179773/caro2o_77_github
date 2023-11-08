package cn.wolfcode.business.service;

import java.io.InputStream;
import java.util.List;
import cn.wolfcode.business.domain.BpmnInfo;
import cn.wolfcode.business.domain.vo.DeployVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 流程定义明细Service接口
 * 
 * @author lin
 * @date 2023-11-01
 */
public interface IBpmnInfoService 
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
     * 批量删除流程定义明细
     * 
     * @param ids 需要删除的流程定义明细主键集合
     * @return 结果
     */
    public int deleteBpmnInfoByIds(Long[] ids);

    /**
     * 删除流程定义明细信息
     * 
     * @param id 流程定义明细主键
     * @return 结果
     */
    public int deleteBpmnInfoById(Long id);

    void deploy(DeployVo vo);

    InputStream getBpmn(String type, Long id);

    void bpmnCancel(Long id);
}
