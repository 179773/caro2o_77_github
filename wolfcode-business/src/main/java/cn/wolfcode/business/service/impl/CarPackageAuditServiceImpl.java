package cn.wolfcode.business.service.impl;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import cn.wolfcode.business.domain.BpmnInfo;
import cn.wolfcode.business.domain.ServiceItem;
import cn.wolfcode.business.domain.vo.CarPackageAuditVO;
import cn.wolfcode.business.domain.vo.HistoryVO;
import cn.wolfcode.business.mapper.BpmnInfoMapper;
import cn.wolfcode.business.mapper.ServiceItemMapper;
import cn.wolfcode.common.utils.DateUtils;
import cn.wolfcode.common.utils.PageUtils;
import cn.wolfcode.common.utils.SecurityUtils;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.wolfcode.business.mapper.CarPackageAuditMapper;
import cn.wolfcode.business.domain.CarPackageAudit;
import cn.wolfcode.business.service.ICarPackageAuditService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 套餐审核Service业务层处理
 * 
 * @author lin
 * @date 2023-11-03
 */
@Service
public class CarPackageAuditServiceImpl implements ICarPackageAuditService 
{
    @Autowired
    private CarPackageAuditMapper carPackageAuditMapper;
    @Autowired
    private BpmnInfoMapper bpmnInfoMapper;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ServiceItemMapper serviceItemMapper;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private TaskService taskService;

    /**
     * 查询套餐审核
     * 
     * @param id 套餐审核主键
     * @return 套餐审核
     */
    @Override
    public CarPackageAudit selectCarPackageAuditById(Long id)
    {
        return carPackageAuditMapper.selectCarPackageAuditById(id);
    }

    /**
     * 查询套餐审核列表
     * 
     * @param carPackageAudit 套餐审核
     * @return 套餐审核
     */
    @Override
    public List<CarPackageAudit> selectCarPackageAuditList(CarPackageAudit carPackageAudit)
    {
        return carPackageAuditMapper.selectCarPackageAuditList(carPackageAudit);
    }

    /**
     * 新增套餐审核
     * 
     * @param carPackageAudit 套餐审核
     * @return 结果
     */
    @Override
    public int insertCarPackageAudit(CarPackageAudit carPackageAudit)
    {
        carPackageAudit.setCreateTime(DateUtils.getNowDate());
        return carPackageAuditMapper.insertCarPackageAudit(carPackageAudit);
    }

    /**
     * 修改套餐审核
     * 
     * @param carPackageAudit 套餐审核
     * @return 结果
     */
    @Override
    public int updateCarPackageAudit(CarPackageAudit carPackageAudit)
    {
        return carPackageAuditMapper.updateCarPackageAudit(carPackageAudit);
    }

    /**
     * 批量删除套餐审核
     * 
     * @param ids 需要删除的套餐审核主键
     * @return 结果
     */
    @Override
    public int deleteCarPackageAuditByIds(Long[] ids)
    {
        return carPackageAuditMapper.deleteCarPackageAuditByIds(ids);
    }

    /**
     * 删除套餐审核信息
     * 
     * @param id 套餐审核主键
     * @return 结果
     */
    @Override
    public int deleteCarPackageAuditById(Long id)
    {
        return carPackageAuditMapper.deleteCarPackageAuditById(id);
    }

    /**
     * 流程--查看进度
     * @param id
     * @return
     */
    @Override
    public InputStream process(Long id) {

        //参数校验
        if(id == null){
            throw new RuntimeException("非法操作");
        }
        //根据 id 查询数据
        CarPackageAudit carPackageAudit = carPackageAuditMapper.selectCarPackageAuditById(id);
        if(carPackageAudit == null){
            throw new RuntimeException("非法操作");
        }

        BpmnInfo busBpmnInfo = bpmnInfoMapper.queryByType(CarPackageAudit.FLOW_AUDIT_TYPE);
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().
                processDefinitionKey(busBpmnInfo.getProcessDefinitionKey()).
                processDefinitionVersion(busBpmnInfo.getVersion()).singleResult();

        // 高亮显示当前 流程所在节点坐标
        List<String> activeActivityIds = new ArrayList<>();
        if(carPackageAudit.getStatus().equals(CarPackageAudit.STATUS_IN_PROCESS)){
            activeActivityIds = runtimeService.getActiveActivityIds(carPackageAudit.getInstanceId());
        }else{
            activeActivityIds = Collections.emptyList();
        }


        DefaultProcessDiagramGenerator processDiagramGenerator = new DefaultProcessDiagramGenerator();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());


        /**
         * 第一个参数: 流程定义模型
         * 第二个参数: 高亮节点集合
         * 第三个参数: 高亮连线集合
         */
        InputStream inputStream = processDiagramGenerator.generateDiagram(bpmnModel,
                activeActivityIds,
                Collections.emptyList(),
                "宋体",
                "宋体",
                "宋体");
        return inputStream;
    }

    @Override
    public void cancel(Long id) {
        if(id == null){
            throw new RuntimeException("非法操作");
        }
        // 参数校验
        CarPackageAudit carPackageAudit = carPackageAuditMapper.selectCarPackageAuditById(id);
        if(!CarPackageAudit.STATUS_IN_PROCESS.equals(carPackageAudit.getStatus())){
            throw new RuntimeException("非法操作");
        }
        // 服务套餐--状态--初始化--撤销
        ServiceItem serviceItem = serviceItemMapper.selectServiceItemById(carPackageAudit.getServiceItemId());
        serviceItem.setAuditStatus(ServiceItem.AUDITSTATUS_INIT);
        serviceItemMapper.updateServiceItem(serviceItem);
        // 审核信息记录--状态--撤销
        carPackageAudit.setStatus(CarPackageAudit.STATUS_CANCEL);
        carPackageAuditMapper.updateCarPackageAudit(carPackageAudit);
        // 流程--流程结束--删除

/*        //修改 carPackageAudit 状态
        carPackageAuditMapper.updateStatus(id,CarPackageAudit.STATUS_CANCEL);*/
/*        //修改 item 转台
        serviceItemMapper.updateAuitStatus(carPackageAudit.getServiceItemId(), ServiceItem.AUDITSTATUS_INIT);*/
        //删除流程实例
        runtimeService.deleteProcessInstance(carPackageAudit.getInstanceId(),"审核被撤销");

    }

    @Override
    public List<HistoryVO> queryHistory(Long instanceId) {

        //参数校验
        if(instanceId == null){
            throw new RuntimeException("非法操作");
        }

        //查询 bpmnInfo 信息
        BpmnInfo bpmnInfo = bpmnInfoMapper.queryByType(CarPackageAudit.FLOW_AUDIT_TYPE);
        //根据流程实例 id 查询历史任务表
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().
                processInstanceId(instanceId.toString()).
                processDefinitionKey(bpmnInfo.getProcessDefinitionKey()).
                finished().
                list();

        List<HistoryVO> vos = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (HistoricTaskInstance task : list) {
            HistoryVO vo = new HistoryVO();
            vo.setTaskName(task.getName());
            vo.setStartTime(sdf.format(task.getStartTime()));
            vo.setEndTime(sdf.format(task.getEndTime()));
            // 间隔时间：endTime - startTime
            vo.setDurationInMillis(task.getDurationInMillis()/1000+"s");
            List<Comment> comments = taskService.getTaskComments(task.getId());
            if(comments != null || comments.size()>0){
                // 查询节点审核备注信息？
                StringBuilder sb = new StringBuilder(80);
                for (Comment comment : comments) {
                    sb.append(comment.getFullMessage());
                }
                vo.setComment(sb.toString());
            }
            vos.add(vo);
        }
        return vos;
    }

    @Override
    public List<CarPackageAudit> todoQuery(CarPackageAudit carPackageAudit) {
        // 先查询 bpmninfo 信息
        BpmnInfo busBpmnInfo = bpmnInfoMapper.queryByType(CarPackageAudit.FLOW_AUDIT_TYPE);
        if(busBpmnInfo == null){
            throw new RuntimeException("非法操作");
        }
        //查询待办任务
        List<Task> list = taskService.createTaskQuery().
                taskAssignee(SecurityUtils.getUserId().toString()).
                processDefinitionKey(busBpmnInfo.getProcessDefinitionKey()).list();

        if(list == null || list.size() == 0){
            return null;
        }
        //根据任务查询流程实例 id集合
        List<String> instanceIds = new ArrayList<>();
        for (Task task : list) {
            instanceIds.add(task.getProcessInstanceId());
        }
        //根据流程实例 id 查询,businesskey 结合
        PageUtils.startPage();
        //根据 userId 去查询
        List<CarPackageAudit> carPackageAudits = carPackageAuditMapper.queryByInstanceId(instanceIds);
        return carPackageAudits;
    }

    @Override
    @Transactional
    public void audit(CarPackageAuditVO vo) {
        //参数校验
        if(vo == null){
            throw new RuntimeException("非法操作");
        }
        //根据 id 查询 carPackgeAudit
        CarPackageAudit carPackageAudit = carPackageAuditMapper.selectCarPackageAuditById(vo.getId());
        if(carPackageAudit == null){
            throw new RuntimeException("非法操作");
        }
        //判断状态是否为审批中
        if(!CarPackageAudit.STATUS_IN_PROCESS.equals(carPackageAudit.getStatus())){
            throw new RuntimeException("非法操作");
        }
        //通过流程实例获取到当前任务节点
        Task currentTask = taskService.
                createTaskQuery().
                processInstanceId(carPackageAudit.getInstanceId()).
                singleResult();
        //添加批注
        String message = "";
        if(CarPackageAudit.STATUS_PASS.equals(vo.getStatus())){
            message ="审批人" +  SecurityUtils.getUsername() + "同意-审批意见[" + vo.getInfo() + "]";
        }else{
            message ="审批人" +  SecurityUtils.getUsername() + "拒绝-审批意见[" + vo.getInfo() + "]";
        }

        taskService.addComment(currentTask.getId(),carPackageAudit.getInstanceId(),message);

        // 设置环境变量
        Map<String,Object> map = new HashMap<>();
        map.put("shopOwner",vo.getStatus().equals(CarPackageAudit.STATUS_PASS));

        //调用activiti api 去完成任务
        taskService.complete(currentTask.getId(),map);
        //判断是同意还是拒绝
        if(vo.getStatus().equals(CarPackageAudit.STATUS_PASS)){
            //如果是统一, 看是否有下一个节点, 如果没有下一个节点,修改 状态为拒绝状态和单项状态为同意状态
            Task nextTask = taskService.
                    createTaskQuery().
                    processInstanceId(carPackageAudit.getInstanceId()).
                    singleResult();
            //如果有不做任何事情
            if(nextTask == null){
                //如果是拒绝,修改 carPackageAudit 状态为拒绝状态和单项状态为拒绝状态
                carPackageAuditMapper.updateStatus(vo.getId(),CarPackageAudit.STATUS_PASS);

                serviceItemMapper.updateAuitStatus(carPackageAudit.getServiceItemId(), ServiceItem.AUDITSTATUS_APPROVED);

            }

        }else{
            //如果是拒绝,修改 carPackageAudit 状态为拒绝状态和单项状态为拒绝状态
            carPackageAuditMapper.updateStatus(vo.getId(),CarPackageAudit.STATUS_REJECT);

            serviceItemMapper.updateAuitStatus(carPackageAudit.getServiceItemId(), ServiceItem.AUDITSTATUS_REPLY);
        }
    }

    @Override
    public List<CarPackageAudit> doneQuery(CarPackageAudit busServiceItem) {
        // 先查询 bpmninfo 信息
        BpmnInfo busBpmnInfo = bpmnInfoMapper.queryByType(CarPackageAudit.FLOW_AUDIT_TYPE);
        if(busBpmnInfo == null){
            throw new RuntimeException("非法操作");
        }
        //查询待办任务
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().
                taskAssignee(SecurityUtils.getUserId().toString()).
                processDefinitionKey(busBpmnInfo.getProcessDefinitionKey()).list();

        if(list == null || list.size() == 0){
            return null;
        }
        //根据任务查询流程实例 id集合
        List<String> instanceIds = new ArrayList<>();
        for (HistoricTaskInstance task : list) {
            instanceIds.add(task.getProcessInstanceId());
        }
        //根据流程实例 id 查询,businesskey 结合
        PageUtils.startPage();
        List<CarPackageAudit> carPackageAudits = carPackageAuditMapper.queryByInstanceId(instanceIds);
        return carPackageAudits;
    }
}
