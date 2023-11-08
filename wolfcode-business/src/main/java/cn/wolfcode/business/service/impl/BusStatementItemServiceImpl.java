package cn.wolfcode.business.service.impl;

import java.math.BigDecimal;
import java.util.List;

import cn.wolfcode.business.domain.BusStatementItem;
import cn.wolfcode.business.domain.Statement;
import cn.wolfcode.business.domain.vo.StatementItemVO;
import cn.wolfcode.business.mapper.StatementMapper;
import cn.wolfcode.common.exception.ServiceException;
import cn.wolfcode.common.utils.StringUtils;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.wolfcode.business.mapper.BusStatementItemMapper;
import cn.wolfcode.business.service.IBusStatementItemService;

import javax.annotation.Resource;

/**
 * 结算单明细Service业务层处理
 * 
 * @author lin
 * @date 2023-10-31
 */
@Service
public class BusStatementItemServiceImpl implements IBusStatementItemService 
{
    @Autowired
    private BusStatementItemMapper busStatementItemMapper;
    @Autowired
    private StatementMapper statementMapper;

    /**
     * 查询结算单明细
     * 
     * @param id 结算单明细主键
     * @return 结算单明细
     */
    @Override
    public BusStatementItem selectBusStatementItemById(Long id)
    {
        return busStatementItemMapper.selectBusStatementItemById(id);
    }

    /**
     * 查询结算单明细列表
     * 
     * @param busStatementItem 结算单明细
     * @return 结算单明细
     */
    @Override
    public List<BusStatementItem> selectBusStatementItemList(BusStatementItem busStatementItem)
    {
        return busStatementItemMapper.selectBusStatementItemList(busStatementItem);
    }

    /**
     * 新增结算单明细
     * 
     * @param busStatementItem 结算单明细
     * @return 结果
     */
    @Override
    public int insertBusStatementItem(BusStatementItem busStatementItem)
    {
        return busStatementItemMapper.insertBusStatementItem(busStatementItem);
    }

    /**
     * 修改结算单明细
     * 
     * @param busStatementItem 结算单明细
     * @return 结果
     */
    @Override
    public int updateBusStatementItem(BusStatementItem busStatementItem)
    {
        return busStatementItemMapper.updateBusStatementItem(busStatementItem);
    }

    /**
     * 批量删除结算单明细
     * 
     * @param ids 需要删除的结算单明细主键
     * @return 结果
     */
    @Override
    public int deleteBusStatementItemByIds(Long[] ids)
    {
        return busStatementItemMapper.deleteBusStatementItemByIds(ids);
    }

    /**
     * 删除结算单明细信息
     * 
     * @param id 结算单明细主键
     * @return 结果
     */
    @Override
    public int deleteBusStatementItemById(Long id)
    {
        return busStatementItemMapper.deleteBusStatementItemById(id);
    }

    @Override
    public int saveItem(StatementItemVO vo) {
        /**
         * vo 内容
         */
        // 结算单id
        Long statementId = vo.getStatementId();
        // 结算单集合
        List<BusStatementItem> statementItems = vo.getBusStatementItems();
        // 结算单折扣价
        BigDecimal discountPrice = vo.getDiscountPrice();

        //1.做参数校验
        Assert.notNull(vo,"非法操作");
        Assert.notNull(statementId,"非法操作");
        Assert.notNull(statementItems,"非法操作");
        Assert.notNull(discountPrice,"非法操作");

        //2.根据结算单 id 查询数据
        Statement statement = statementMapper.selectStatementById(statementId);
        Assert.notNull(statement,"非法操作");

        //3.判断状态是否是消费中
        boolean flagStatus = Statement.STATUS_CONSUME.equals(statement.getStatus());
        Assert.state(flagStatus,"状态必须为消费中状态");

        //4 根据 结算单id 去查询是否有明细,如果有就删除
        List<BusStatementItem> items = busStatementItemMapper.selectItemsByStatementId(statementId);
        if (!StringUtils.isEmpty(items)) {
            busStatementItemMapper.deleteItemsByStatementId(statementId);
        }
        //5 初始化 总金额，总量
        BigDecimal totalAmount = new BigDecimal("0");
        Long totalQuantity = 0L;
        //6 循环遍历
        for (BusStatementItem item : statementItems) {
            //5.1.计算总金额
            BigDecimal itemPrice = item.getItemPrice();
            Long itemQuantity = item.getItemQuantity();
            totalAmount = totalAmount.add(itemPrice.multiply(new BigDecimal(itemQuantity)));
            //5.2.计算总数量
            totalQuantity += item.getItemQuantity();
            //5.3.插入明细
            busStatementItemMapper.insertBusStatementItem(item);
        };
        //6.判断折扣金额不能大于总金额
        int count = discountPrice.compareTo(totalAmount);
        if (count > 0) {
            throw new ServiceException("折扣金额不能大于总金额");
        }
        //7.更新结算单,总金额,总数量,折扣金额

        return statementMapper.updateAmount(statementId,totalAmount,totalQuantity,discountPrice);
    }
}












