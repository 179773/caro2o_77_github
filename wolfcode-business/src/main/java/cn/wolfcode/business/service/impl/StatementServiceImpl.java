package cn.wolfcode.business.service.impl;

import java.util.List;

import cn.wolfcode.business.domain.Appointment;
import cn.wolfcode.business.domain.BusStatementItem;
import cn.wolfcode.business.mapper.AppointmentMapper;
import cn.wolfcode.business.mapper.BusStatementItemMapper;
import cn.wolfcode.common.utils.DateUtils;
import cn.wolfcode.common.utils.SecurityUtils;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.wolfcode.business.mapper.StatementMapper;
import cn.wolfcode.business.domain.Statement;
import cn.wolfcode.business.service.IStatementService;

/**
 * 结算单Service业务层处理
 * 
 * @author lin
 * @date 2023-10-31
 */
@Service
public class StatementServiceImpl implements IStatementService 
{
    @Autowired
    private StatementMapper statementMapper;
    @Autowired
    private AppointmentMapper appointmentMapper;
    @Autowired
    private BusStatementItemMapper statementItemMapper;

    /**
     * 查询结算单
     * 
     * @param id 结算单主键
     * @return 结算单
     */
    @Override
    public Statement selectStatementById(Long id)
    {
        return statementMapper.selectStatementById(id);
    }

    /**
     * 查询结算单列表
     * 
     * @param statement 结算单
     * @return 结算单
     */
    @Override
    public List<Statement> selectStatementList(Statement statement)
    {
        return statementMapper.selectStatementList(statement);
    }

    /**
     * 新增结算单
     * 
     * @param statement 结算单
     * @return 结果
     */
    @Override
    public int insertStatement(Statement statement)
    {
        Assert.notNull(statement,"非法操作");
        Statement statement2 = statementMapper.selectStatementById(statement.getId());
        //做手机号码校验
        //车牌号码校验
        //做日期校验
        statement.setCreateTime(DateUtils.getNowDate());
        return statementMapper.insertStatement(statement);
    }

    /**
     * 修改结算单
     * 
     * @param statement 结算单
     * @return 结果
     */
    @Override
    public int updateStatement(Statement statement)
    {
        return statementMapper.updateStatement(statement);
    }

    /**
     * 批量删除结算单
     * 
     * @param ids 需要删除的结算单主键
     * @return 结果
     */
    @Override
    public int deleteStatementByIds(Long[] ids)
    {
        Assert.notNull(ids,"非法操作");
        // 使用假删除
        for (Long id : ids) {
            Statement statement = statementMapper.selectStatementById(id);
            Assert.notNull(statement,"非法操作");
            // 必须是消费中才能删除
            Integer status = statement.getStatus();
            boolean flagStatus = Statement.STATUS_CONSUME.equals(status);
            Assert.state(!flagStatus,"消费中才能删除");
            // 结算单：将is_delete，改为已删除
            statementMapper.setIsDelete(id,Statement.IS_DEL_YES);
            // 结算单明细：直接删除
            statementItemMapper.deleteItemsByStatementId(id);
            // 预约单：改为到店状态
            if (statement.getAppointmentId() != null) {
                appointmentMapper.updateStatus(statement.getAppointmentId(), Appointment.STATUS_ARRIVAL);
            }
        }
        return 1/*statementMapper.deleteStatementByIds(ids)*/;
    }

    /**
     * 删除结算单信息
     * 
     * @param id 结算单主键
     * @return 结果
     */
    @Override
    public int deleteStatementById(Long id)
    {

        return statementMapper.deleteStatementById(id);
    }

    @Override
    public int payStatement(Long id) {
        // 1 校验
        Statement statement = statementMapper.selectStatementById(id);
        // 参数校验-id
        Assert.notNull(id,"参数异常");
        // 状态校验-status
        boolean flagStatus = Statement.STATUS_PAID.equals(statement.getStatus());
        Assert.state(!flagStatus,"状态异常");
        // 2 更新结算单---状态-支付时间-收款人
        statement.setStatus(Statement.STATUS_PAID);
        statement.setPayTime(DateUtils.getNowDate());
        statement.setPayeeId(SecurityUtils.getUserId());

        // TODO 如果后续做预约单，修改预约状态为已支付
        Long appointmentId = statement.getAppointmentId();
        if (appointmentId != null) {
            appointmentMapper.updateStatus(appointmentId, Appointment.STATUS_PAYED);
        }

        return statementMapper.updateStatement(statement);
    }
}
