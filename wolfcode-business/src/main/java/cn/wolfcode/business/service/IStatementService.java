package cn.wolfcode.business.service;

import java.util.List;
import cn.wolfcode.business.domain.Statement;

/**
 * 结算单Service接口
 * 
 * @author lin
 * @date 2023-10-31
 */
public interface IStatementService 
{
    /**
     * 查询结算单
     * 
     * @param id 结算单主键
     * @return 结算单
     */
    public Statement selectStatementById(Long id);

    /**
     * 查询结算单列表
     * 
     * @param statement 结算单
     * @return 结算单集合
     */
    public List<Statement> selectStatementList(Statement statement);

    /**
     * 新增结算单
     * 
     * @param statement 结算单
     * @return 结果
     */
    public int insertStatement(Statement statement);

    /**
     * 修改结算单
     * 
     * @param statement 结算单
     * @return 结果
     */
    public int updateStatement(Statement statement);

    /**
     * 批量删除结算单
     * 
     * @param ids 需要删除的结算单主键集合
     * @return 结果
     */
    public int deleteStatementByIds(Long[] ids);

    /**
     * 删除结算单信息
     * 
     * @param id 结算单主键
     * @return 结果
     */
    public int deleteStatementById(Long id);

    int payStatement(Long id);
}
