package cn.wolfcode.business.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.wolfcode.business.domain.vo.DeployVo;
import cn.wolfcode.common.utils.StringUtils;
import cn.wolfcode.common.utils.file.FileUploadUtils;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.wolfcode.business.mapper.BpmnInfoMapper;
import cn.wolfcode.business.domain.BpmnInfo;
import cn.wolfcode.business.service.IBpmnInfoService;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 流程定义明细Service业务层处理
 * 
 * @author lin
 * @date 2023-11-01
 */
@Service
public class BpmnInfoServiceImpl implements IBpmnInfoService 
{
    @Resource
    private BpmnInfoMapper bpmnInfoMapper;
    @Autowired
    private RepositoryService repositoryService;

    /**
     * 查询流程定义明细
     * 
     * @param id 流程定义明细主键
     * @return 流程定义明细
     */
    @Override
    public BpmnInfo selectBpmnInfoById(Long id)
    {
        return bpmnInfoMapper.selectBpmnInfoById(id);
    }

    /**
     * 查询流程定义明细列表
     * 
     * @param bpmnInfo 流程定义明细
     * @return 流程定义明细
     */
    @Override
    public List<BpmnInfo> selectBpmnInfoList(BpmnInfo bpmnInfo)
    {
        return bpmnInfoMapper.selectBpmnInfoList(bpmnInfo);
    }

    /**
     * 新增流程定义明细
     * 
     * @param bpmnInfo 流程定义明细
     * @return 结果
     */
    @Override
    public int insertBpmnInfo(BpmnInfo bpmnInfo)
    {
        return bpmnInfoMapper.insertBpmnInfo(bpmnInfo);
    }

    /**
     * 修改流程定义明细
     * 
     * @param bpmnInfo 流程定义明细
     * @return 结果
     */
    @Override
    public int updateBpmnInfo(BpmnInfo bpmnInfo)
    {
        return bpmnInfoMapper.updateBpmnInfo(bpmnInfo);
    }

    /**
     * 批量删除流程定义明细
     * 
     * @param ids 需要删除的流程定义明细主键
     * @return 结果
     */
    @Override
    public int deleteBpmnInfoByIds(Long[] ids)
    {
        return bpmnInfoMapper.deleteBpmnInfoByIds(ids);
    }

    /**
     * 删除流程定义明细信息
     * 
     * @param id 流程定义明细主键
     * @return 结果
     */
    @Override
    public int deleteBpmnInfoById(Long id)
    {
        return bpmnInfoMapper.deleteBpmnInfoById(id);
    }

    @Override
    public void deploy(DeployVo vo) {
        //参数校验
        if(vo.getFile() == null){
            throw new RuntimeException("上传文件不能为空");
        }
        if(StringUtils.isEmpty(vo.getBpmnLabel()) ||
                StringUtils.isEmpty(vo.getBpmnType()+"") ||
                StringUtils.isEmpty(vo.getInfo())){
            throw new RuntimeException("非法参数");
        }
        //校验文件格式是否为 bpmn 格式文件
        String ext = FileUploadUtils.getExtension(vo.getFile());
        if(!"bpmn".equals(ext)){
            throw new RuntimeException("文件格式必须为 bpmn 格式");
        }
        //把 bpmn 文件部署到 actviti 中
        Deployment deploy = null;
        try {
            deploy = repositoryService.
                    createDeployment().
                    addInputStream(vo.getBpmnLabel(), vo.getFile().getInputStream()).
                    deploy();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //查询流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploy.getId())
                .singleResult();
        //把 bpmnInfo 信息保存到数据库中
        BpmnInfo busBpmnInfo = new BpmnInfo();
        busBpmnInfo.setBpmnLabel(vo.getBpmnLabel());
        busBpmnInfo.setBpmnType(vo.getBpmnType());
        busBpmnInfo.setDeployTime(new Date());
        busBpmnInfo.setInfo(vo.getInfo());
        busBpmnInfo.setProcessDefinitionKey(processDefinition.getKey());
        busBpmnInfo.setVersion(Integer.parseInt(processDefinition.getVersion()+""));
        bpmnInfoMapper.insertBpmnInfo(busBpmnInfo);
    }

    @Override
    public InputStream getBpmn(String type, Long id) {

        //参数校验
        if(StringUtils.isEmpty(type) || StringUtils.isEmpty(id+"")){
            throw new  RuntimeException("非法操作");
        }
        BpmnInfo bpmnInfo = bpmnInfoMapper.selectBpmnInfoById(id);
        if(bpmnInfo == null){
            throw new RuntimeException("非法操作");
        }
        InputStream inputStream = null;
        //判断类型是 xml 还是 png,如果是 xml 响应 xml 如果是图片响应图片
        //查询流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().
                processDefinitionKey(bpmnInfo.getProcessDefinitionKey()).
                processDefinitionVersion(bpmnInfo.getVersion().intValue()).singleResult();
        if("xml".equals(type)){
            inputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getResourceName());

        }else if("png".equals(type)){
            DefaultProcessDiagramGenerator processDiagramGenerator = new DefaultProcessDiagramGenerator();
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
            /**
             * 第一个参数: 流程定义模型
             * 第二个参数: 高亮节点集合
             * 第三个参数: 高亮连线集合
             */
            inputStream = processDiagramGenerator.generateDiagram(bpmnModel,
                    Collections.emptyList(),
                    Collections.emptyList(),
                    "宋体",
                    "宋体",
                    "宋体");

        }else{
            throw new RuntimeException("非法操作");
        }
        return inputStream;
    }

    @Override
    public void bpmnCancel(Long id) {
        if(StringUtils.isEmpty(id+"")){
            throw new RuntimeException("非法操作");
        }
        //删除 bpmnInfo 信息
        BpmnInfo bpmnInfo = bpmnInfoMapper.selectBpmnInfoById(id);
        // bpmnInfo--流程定义id
        String processDefinitionKey = bpmnInfo.getProcessDefinitionKey();
        // bpmnInfo--流程定义版本
        int processDefinitionVersion = bpmnInfo.getVersion().intValue();
        // 获取流程定义对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .processDefinitionVersion(processDefinitionVersion)
                .singleResult();
        /**.
         * TODO: 要查看这个流程定义下是否有流程实例,如果有需要找到流程实例
         * 根据流程实例找到 businessKey , 然后在把数据修改成初始化状态
         */
        bpmnInfoMapper.deleteBpmnInfoById(id);
        // 删除流程定义信息--连关系一起
        // 通过流程定义对象，获取流程部署id
        String deploymentId = processDefinition.getDeploymentId();
        repositoryService.deleteDeployment(deploymentId,true);

    }
}






