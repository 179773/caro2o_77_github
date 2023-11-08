package cn.wolfcode.business.service;

import java.util.List;
import cn.wolfcode.business.domain.Appointment;

/**
 * 养修信息预约Service接口
 * 
 * @author lin
 * @date 2023-10-28
 */
public interface IAppointmentService 
{
    /**
     * 查询养修信息预约
     * 
     * @param id 养修信息预约主键
     * @return 养修信息预约
     */
    public Appointment selectAppointmentById(Long id);

    /**
     * 查询养修信息预约列表
     * 
     * @param appointment 养修信息预约
     * @return 养修信息预约集合
     */
    public List<Appointment> selectAppointmentList(Appointment appointment);

    /**
     * 新增养修信息预约
     * 
     * @param appointment 养修信息预约
     * @return 结果
     */
    public int insertAppointment(Appointment appointment);

    /**
     * 修改养修信息预约
     * 
     * @param appointment 养修信息预约
     * @return 结果
     */
    public int updateAppointment(Appointment appointment);

    /**
     * 批量删除养修信息预约
     * 
     * @param ids 需要删除的养修信息预约主键集合
     * @return 结果
     */
    public int deleteAppointmentByIds(Long[] ids);

    /**
     * 删除养修信息预约信息
     * 
     * @param id 养修信息预约主键
     * @return 结果
     */
    public int deleteAppointmentById(Long id);

    int arrivalShop(Long id);

    int cancel(Long id);

    Long generateSettleDoc(Long id);
}
