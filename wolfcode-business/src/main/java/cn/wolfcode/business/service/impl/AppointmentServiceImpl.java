package cn.wolfcode.business.service.impl;

import java.util.Date;
import java.util.List;

import cn.wolfcode.business.domain.Statement;
import cn.wolfcode.business.mapper.StatementMapper;
import cn.wolfcode.business.util.RegexUtils;
import cn.wolfcode.business.util.VehiclePlateNoUtil;
import cn.wolfcode.common.exception.ServiceException;
import cn.wolfcode.common.utils.DateUtils;
import cn.wolfcode.common.utils.bean.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.wolfcode.business.mapper.AppointmentMapper;
import cn.wolfcode.business.domain.Appointment;
import cn.wolfcode.business.service.IAppointmentService;
import org.springframework.util.Assert;

/**
 * 养修信息预约Service业务层处理
 * 
 * @author lin
 * @date 2023-10-28
 */
@Service
public class AppointmentServiceImpl implements IAppointmentService 
{
    @Autowired
    private AppointmentMapper appointmentMapper;
    @Autowired
    private StatementMapper statementMapper;

    /**
     * 查询养修信息预约
     * 
     * @param id 养修信息预约主键
     * @return 养修信息预约
     */
    @Override
    public Appointment selectAppointmentById(Long id)
    {
        return appointmentMapper.selectAppointmentById(id);
    }

    /**
     * 查询养修信息预约列表
     * 
     * @param appointment 养修信息预约
     * @return 养修信息预约
     */
    @Override
    public List<Appointment> selectAppointmentList(Appointment appointment)
    {
        return appointmentMapper.selectAppointmentList(appointment);
    }

    /**
     * 新增养修信息预约
     * 
     * @param appointment 养修信息预约
     * @return 结果
     */
    /**
     * 新增养修信息预约
     *
     * @param appointment 养修信息预约
     * @return 结果
     */
    @Override
    public int insertAppointment(Appointment appointment)
    {
        //做非空判断
        Assert.notNull(appointment, "参数不能为 null");

        boolean phoneLegal = RegexUtils.isPhoneLegal(appointment.getCustomerPhone());
        if(!phoneLegal){
            throw new RuntimeException("手机号码不正确");
        }

        //做合理化校验,日期校验
        Date appointmentTime = appointment.getAppointmentTime();
        Date nowDate = DateUtils.getNowDate();
        boolean flag = appointmentTime.before(nowDate);
        //        Assert.state(flag,"预约时间不合理");
        if(flag){
            throw new RuntimeException("预约时间不合理");
        }
        //车牌号码校验
        VehiclePlateNoUtil.VehiclePlateNoEnum vehiclePlateNo = VehiclePlateNoUtil.getVehiclePlateNo(appointment.getLicensePlate());
        if(vehiclePlateNo == null){
            throw new RuntimeException("车牌号不正确");
        }
        //进行赋值操作
        Appointment busAppointment1 = new Appointment();
        BeanUtils.copyProperties(appointment, busAppointment1);

        appointment.setCreateTime(DateUtils.getNowDate());
        return appointmentMapper.insertAppointment(busAppointment1);
    }

    /**
     * 修改养修信息预约
     * 
     * @param appointment 养修信息预约
     * @return 结果
     */
    @Override
    public int updateAppointment(Appointment appointment)
    {
        //做非空判断
        Assert.notNull(appointment, "参数不能为 null");

        boolean phoneLegal = RegexUtils.isPhoneLegal(appointment.getCustomerPhone());
        if(!phoneLegal){
            throw new RuntimeException("手机号码不正确");
        }

        //做合理化校验,日期校验
        Date appointmentTime = appointment.getAppointmentTime();
        Date nowDate = DateUtils.getNowDate();
        boolean flag = appointmentTime.before(nowDate);
        //        Assert.state(flag,"预约时间不合理");
        if(flag){
            throw new RuntimeException("预约时间不合理");
        }
        //车牌号码校验
        VehiclePlateNoUtil.VehiclePlateNoEnum vehiclePlateNo = VehiclePlateNoUtil.getVehiclePlateNo(appointment.getLicensePlate());
        if(vehiclePlateNo == null){
            throw new RuntimeException("车牌号不正确");
        }
        //进行赋值操作
        Appointment busAppointment1 = new Appointment();
        BeanUtils.copyProperties(appointment, busAppointment1);

        // 必须在可编辑状态（0-预约中）才可编辑
        // 1 前端带
        // 2 数据库查--更安全
        Appointment appointment1 = appointmentMapper.selectAppointmentById(appointment.getId());
        Integer status = appointment1.getStatus();
        if (status != 0) {
            throw new ServiceException("状态错误,非法操作");
        }

        return appointmentMapper.updateAppointment(appointment);
    }

    /**
     * 批量删除养修信息预约
     * 
     * @param ids 需要删除的养修信息预约主键
     * @return 结果
     */
    @Override
    public int deleteAppointmentByIds(Long[] ids)
    {
        return appointmentMapper.deleteAppointmentByIds(ids);
    }

    /**
     * 删除养修信息预约信息
     * 
     * @param id 养修信息预约主键
     * @return 结果
     */
    @Override
    public int deleteAppointmentById(Long id)
    {
        return appointmentMapper.deleteAppointmentById(id);
    }

    @Override
    public int arrivalShop(Long id) {
        Appointment appointment = appointmentMapper.selectAppointmentById(id);
        if (appointment == null) {
            throw new ServiceException("参数异常");
        }
        // 是否满足到店状态
        if (!Appointment.STATUS_APPOINTMENT.equals(appointment.getStatus())) {
            throw new ServiceException("必须是预约状态才能到店");
        }
        // 设置到店时间
        appointment.setActualArrivalTime(new Date());
        appointment.setStatus(Appointment.STATUS_ARRIVAL);
        return appointmentMapper.updateAppointment(appointment);
    }

    @Override
    public int cancel(Long id) {
        Appointment appointment = appointmentMapper.selectAppointmentById(id);
        if (appointment == null) {
            throw new ServiceException("参数异常");
        }
        // 判断满足取消的操作状态：预约中
        if (!Appointment.STATUS_APPOINTMENT.equals(appointment.getStatus())) {
            throw new ServiceException("必须是预约状态才可以执行取消");
        }
        return appointmentMapper.updateStatus(id,Appointment.STATUS_CANCEL);
    }

    @Override
    public Long generateSettleDoc(Long id) {
        //判断参数是否为空
        Assert.notNull(id,"非法参数");
        //根据 id 查询数据
        Appointment appointment = appointmentMapper.selectAppointmentById(id);
        Assert.notNull(appointment,"非法操作");
        //判断状态是否为到店状态
        if(!(Appointment.STATUS_ARRIVAL.equals(appointment.getStatus())||
                Appointment.STATUS_PAYED.equals(appointment.getStatus())||
                Appointment.STATUS_SETTLE.equals(appointment.getStatus()))){
            throw new RuntimeException("非法操作");
        }
        //根据预约单 id 查询结算单数据,如果为空创建结算单
        Statement statement = appointmentMapper.queryByAppointmentId(id);
        if (statement == null) {
            statement = new Statement();
            BeanUtils.copyProperties(appointment,statement);
            statement.setId(null);
            statement.setStatus(Statement.STATUS_CONSUME);
            statement.setAppointmentId(id);
            statementMapper.insertStatement(statement);
            //把状态修改成结算单生成状态
            appointmentMapper.updateStatus(id,Appointment.STATUS_SETTLE);
        }

        return statement.getId();
    }
}
