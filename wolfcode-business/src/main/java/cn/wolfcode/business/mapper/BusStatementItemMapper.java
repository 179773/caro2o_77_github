package cn.wolfcode.business.mapper;

import java.util.List;
import cn.wolfcode.business.domain.BusStatementItem;

/**
 * 结算单明细Mapper接口
 * 
 * @author lin
 * @date 2023-10-31
 */
public interface BusStatementItemMapper 
{
    /**
     * 查询结算单明细
     * 
     * @param id 结算单明细主键
     * @return 结算单明细
     */
    public BusStatementItem selectBusStatementItemById(Long id);

    /**
     * 查询结算单明细列表
     * 
     * @param busStatementItem 结算单明细
     * @return 结算单明细集合
     */
    public List<BusStatementItem> selectBusStatementItemList(BusStatementItem busStatementItem);

    /**
     * 新增结算单明细
     * 
     * @param busStatementItem 结算单明细
     * @return 结果
     */
    public int insertBusStatementItem(BusStatementItem busStatementItem);

    /**
     * 修改结算单明细
     * 
     * @param busStatementItem 结算单明细
     * @return 结果
     */
    public int updateBusStatementItem(BusStatementItem busStatementItem);

    /**
     * 删除结算单明细
     * 
     * @param id 结算单明细主键
     * @return 结果
     */
    public int deleteBusStatementItemById(Long id);

    /**
     * 批量删除结算单明细
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusStatementItemByIds(Long[] ids);

    List<BusStatementItem> selectItemsByStatementId(Long statementId);

    void deleteItemsByStatementId(Long statementId);
}
