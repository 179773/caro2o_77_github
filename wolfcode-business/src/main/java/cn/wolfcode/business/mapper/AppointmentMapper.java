package cn.wolfcode.business.mapper;

import java.util.List;
import cn.wolfcode.business.domain.Appointment;
import cn.wolfcode.business.domain.Statement;
import org.apache.ibatis.annotations.Param;

/**
 * 养修信息预约Mapper接口
 * 
 * @author lin
 * @date 2023-10-28
 */
public interface AppointmentMapper 
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
     * 删除养修信息预约
     * 
     * @param id 养修信息预约主键
     * @return 结果
     */
    public int deleteAppointmentById(Long id);

    /**
     * 批量删除养修信息预约
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAppointmentByIds(Long[] ids);

    int updateStatus(@Param("id") Long id, @Param("status") Integer statusCancel);

    Statement queryByAppointmentId(Long id);

}
