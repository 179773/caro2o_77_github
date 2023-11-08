package cn.wolfcode.business.mapper;

import java.math.BigDecimal;
import java.util.List;
import cn.wolfcode.business.domain.Statement;
import org.apache.ibatis.annotations.Param;

/**
 * 结算单Mapper接口
 * 
 * @author lin
 * @date 2023-10-31
 */
public interface StatementMapper 
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
     * 删除结算单
     * 
     * @param id 结算单主键
     * @return 结果
     */
    public int deleteStatementById(Long id);

    /**
     * 批量删除结算单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStatementByIds(Long[] ids);

    int updateAmount(@Param("statementId") Long statementId,
                     @Param("totalAmount") BigDecimal totalAmount,
                     @Param("totalQuantity") Long totalQuantity,
                     @Param("discountPrice") BigDecimal discountPrice);

    void setIsDelete(@Param("id") Long id, @Param("isDelYes") Integer isDelYes);
}
