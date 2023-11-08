package cn.wolfcode.business.domain.vo;

import cn.wolfcode.business.domain.BusStatementItem;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class StatementItemVO {
    private Long statementId;
    private List<BusStatementItem> busStatementItems;
    private BigDecimal discountPrice;

}