package cn.wolfcode.business.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.wolfcode.business.domain.BpmnInfo;
import cn.wolfcode.business.domain.CarPackageAudit;
import cn.wolfcode.business.domain.vo.AuditInfoVO;
import cn.wolfcode.business.domain.vo.AuditVO;
import cn.wolfcode.business.mapper.BpmnInfoMapper;
import cn.wolfcode.business.mapper.CarPackageAuditMapper;
import cn.wolfcode.common.core.domain.entity.SysUser;
import cn.wolfcode.common.exception.ServiceException;
import cn.wolfcode.common.utils.DateUtils;
import cn.wolfcode.common.utils.SecurityUtils;
import cn.wolfcode.system.service.ISysConfigService;
import cn.wolfcode.system.service.ISysUserService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.wolfcode.business.mapper.ServiceItemMapper;
import cn.wolfcode.business.domain.ServiceItem;
import cn.wolfcode.business.service.IServiceItemService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 服务项Service业务层处理
 * 
 * @author lin
 * @date 2023-10-29
 */
@Service
public class ServiceItemServiceImpl implements IServiceItemService 
{
    @Resource
    private ServiceItemMapper serviceItemMapper;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysConfigService sysConfigService;
    @Resource
    private CarPackageAuditMapper carPackageAuditMapper;
    @Resource
    private BpmnInfoMapper bpmnInfoMapper;
    @Resource
    private RuntimeService runtimeService;

    /**
     * 查询服务项
     * 
     * @param id 服务项主键
     * @return 服务项
     */
    @Override
    public ServiceItem selectServiceItemById(Long id)
    {
        return serviceItemMapper.selectServiceItemById(id);
    }

    /**
     * 查询服务项列表
     * 
     * @param serviceItem 服务项
     * @return 服务项
     */
    @Override
    public List<ServiceItem> selectServiceItemList(ServiceItem serviceItem)
    {
        return serviceItemMapper.selectServiceItemList(serviceItem);
    }

    /**
     * 新增服务项
     * 
     * @param serviceItem 服务项
     * @return 结果
     */
    @Override
    public int insertServiceItem(ServiceItem serviceItem)
    {
        // 1 判断参数合法性--是否为null
        if (serviceItem == null) {
            throw new ServiceException("参数异常");
        }
        // 2 判断参数合法性--折扣价小于原价
        int result = serviceItem.getDiscountPrice().compareTo(serviceItem.getOriginalPrice());
        if (result > 0) {
            throw new ServiceException("折扣价不能大于原价");
        }
        // 3 默认状态设置
        //    3.1 上下架状态
        serviceItem.setSaleStatus(ServiceItem.SALESTATUS_OFF);
        //    3.2 审核状态
        boolean flag = ServiceItem.CARPACKAGE_YES.equals(serviceItem.getCarPackage());
        if (flag) {
            //      3.2.1 是套餐---初始化
            serviceItem.setAuditStatus(ServiceItem.AUDITSTATUS_INIT);
        } else {
            //      3.2.2 不是套餐--无需审核
            serviceItem.setAuditStatus(ServiceItem.AUDITSTATUS_NO_REQUIRED);
        }

        // 创建时间
        serviceItem.setCreateTime(DateUtils.getNowDate());
        return serviceItemMapper.insertServiceItem(serviceItem);
    }

    /**
     * 修改服务项
     * 
     * @param serviceItem 服务项
     * @return 结果
     */
    @Override
    public int updateServiceItem(ServiceItem serviceItem)
    {
        // 1 判断参数合法性--是否为null
        if (serviceItem == null) {
            throw new ServiceException("参数异常");
        }
        // 2 判断参数合法性--折扣价小于原价
        int result = serviceItem.getDiscountPrice().compareTo(serviceItem.getOriginalPrice());
        if (result > 0) {
            throw new ServiceException("折扣价不能大于原价");
        }
        // 考虑编辑状态
        // 审核中 上架--不能使用编辑
        ServiceItem item = serviceItemMapper.selectServiceItemById(serviceItem.getId());
        if (ServiceItem.AUDITSTATUS_AUDITING.equals(item.getAuditStatus())
                || ServiceItem.SALESTATUS_ON.equals(item.getSaleStatus())) {
            throw new ServiceException("服务在审核中 上架--不能使用编辑");
        }


        // 修改之后状态变化怎么考虑
        // 单项--无需审核
        // 套餐--初始化
        // @TODO 是套餐情况下，审核通过后，修改内容，导致状态变动
        if (ServiceItem.CARPACKAGE_YES.equals(item.getCarPackage())) {
            serviceItem.setAuditStatus(ServiceItem.AUDITSTATUS_INIT);
        }
        // @TODO 能不能改成单项该套餐--不能
        if (!item.getCarPackage().equals(serviceItem.getCarPackage())) {
            throw new ServiceException("服务单项不能与套餐相互转换");
        }

        return serviceItemMapper.updateServiceItem(serviceItem);
    }

    /**
     * 批量删除服务项
     * 
     * @param ids 需要删除的服务项主键
     * @return 结果
     */
    @Override
    public int deleteServiceItemByIds(Long[] ids)
    {
        return serviceItemMapper.deleteServiceItemByIds(ids);
    }

    /**
     * 删除服务项信息
     * 
     * @param id 服务项主键
     * @return 结果
     */
    @Override
    public int deleteServiceItemById(Long id)
    {
        return serviceItemMapper.deleteServiceItemById(id);
    }

    @Override
    public int listing(Long id) {

        // 参数校验 （id，状态为已上架）
        ServiceItem serviceItem = serviceItemMapper.selectServiceItemById(id);
        if (serviceItem == null) {
            throw new ServiceException("参数异常");
        }
        if (ServiceItem.SALESTATUS_ON.equals(serviceItem.getSaleStatus())) {
            throw new ServiceException("参数异常");
        }
        // 满足上架条件判断
        Integer auditStatus = serviceItem.getAuditStatus();
        System.out.println(auditStatus + "test123123");
        if (!ServiceItem.AUDITSTATUS_APPROVED.equals(auditStatus)
            && !ServiceItem.AUDITSTATUS_NO_REQUIRED.equals(auditStatus)){
            throw new ServiceException("审核通过才可上架");
        }
        // 直接上架-状态改为上架
        serviceItem.setSaleStatus(ServiceItem.SALESTATUS_ON);
        return serviceItemMapper.updateServiceItem(serviceItem);
    }

    @Override
    public int delisting(Long id) {
        ServiceItem item = serviceItemMapper.selectServiceItemById(id);
        if (item == null) {
            throw new ServiceException("参数异常");
        }
        if (ServiceItem.SALESTATUS_OFF.equals(item.getSaleStatus())) {
            throw new ServiceException("参数异常");
        }
        item.setSaleStatus(ServiceItem.SALESTATUS_OFF);
        return serviceItemMapper.updateServiceItem(item);
    }

    @Override
    public AuditInfoVO auditInfo(Long id) {
        if(id == null){
            throw new RuntimeException("非法操作");
        }
        ServiceItem serviceItem = serviceItemMapper.selectServiceItemById(id);
        if(serviceItem == null){
            throw new RuntimeException("非法操作");
        }
        //判断是否是套餐
        if(!ServiceItem.CARPACKAGE_YES.equals(serviceItem.getCarPackage())){
            throw new RuntimeException("必须是套餐才可以审核");
        }
        //把数据封装到 vo 对象中
        AuditInfoVO vo = new AuditInfoVO();
        //判断是否处于初始化或者是重新调整
        if(ServiceItem.AUDITSTATUS_INIT.equals(serviceItem.getAuditStatus())||
                ServiceItem.AUDITSTATUS_REPLY.equals(serviceItem.getAuditStatus())){

            vo.setServiceItem(serviceItem);
            //查询店长信息
            List<SysUser> shopOnwers = sysUserService.queryByRoleKey("shopOwner");
            vo.setShopOwners(shopOnwers);
            //设置折扣金额
            String obj = sysConfigService.selectConfigByKey("discountPriceLimit");
            vo.setDiscountPrice(new BigDecimal(obj));

            if(serviceItem.getDiscountPrice().compareTo(new BigDecimal(obj)) > 0){
                //查询财务信息
                List<SysUser> finances = sysUserService.queryByRoleKey("finance");
                vo.setFinances(finances);
            }

        }else{
            throw new RuntimeException("非法操作");
        }

        return vo;
    }

    @Override
    @Transactional
    public void startAudit(AuditVO vo) {
        //做参数校验
        if(vo == null){
            throw new RuntimeException("非法操作");
        }

        //根据 id 查询数据
        ServiceItem busServiceItem = serviceItemMapper.selectServiceItemById(vo.getId());
        if(busServiceItem == null){
            throw new RuntimeException("非法操作");
        }
        //判断是否是套餐
        if(!ServiceItem.CARPACKAGE_YES.equals(busServiceItem.getCarPackage())){
            throw new RuntimeException("必须是套餐才可以审核");
        }

        //判断是否处于初始化或者是重新调整
        if(ServiceItem.AUDITSTATUS_INIT.equals(busServiceItem.getAuditStatus())||
                ServiceItem.AUDITSTATUS_REPLY.equals(busServiceItem.getAuditStatus())){
            // 根据对应的流程类型，查询 bpmnInfo 信息
            BpmnInfo busBpmnInfo = bpmnInfoMapper.queryByType(CarPackageAudit.FLOW_AUDIT_TYPE);
            // 创建 CarPackageAudit 保存到业务表中
            CarPackageAudit carPackageAudit = new CarPackageAudit();
            carPackageAudit.setCreatorId(SecurityUtils.getUserId().toString());
            carPackageAudit.setInfo(vo.getInfo());
            carPackageAudit.setServiceItemId(busServiceItem.getId());
            carPackageAudit.setCreateTime(new Date());
            carPackageAudit.setServiceItemInfo(busServiceItem.getInfo());
            carPackageAudit.setServiceItemName(busServiceItem.getName());
            carPackageAudit.setServiceItemPrice(busServiceItem.getDiscountPrice());
            carPackageAudit.setStatus(CarPackageAudit.STATUS_IN_PROCESS);
            carPackageAuditMapper.insertCarPackageAudit(carPackageAudit);
            // 设置流程变量
            Map<String,Object> map = new HashMap<>();
            map.put("shopOwnerId",vo.getShopOwnerId());
            if(vo.getFinanceId() !=null){
                map.put("financeId",vo.getFinanceId());
            }
            map.put("disCountPrice",busServiceItem.getDiscountPrice().longValue());
            // 启动流程实例
            ProcessInstance processInstance = runtimeService.
                    startProcessInstanceByKey(busBpmnInfo.getProcessDefinitionKey(),
                            carPackageAudit.getId().toString(), map);

            carPackageAudit.setInstanceId(processInstance.getId());
            carPackageAuditMapper.updateCarPackageAudit(carPackageAudit);

            // 修改单项状态为审核中状态
            serviceItemMapper.updateAuitStatus(vo.getId(),ServiceItem.AUDITSTATUS_AUDITING);

        }else{
            throw new RuntimeException("非法操作");
        }
    }
}
