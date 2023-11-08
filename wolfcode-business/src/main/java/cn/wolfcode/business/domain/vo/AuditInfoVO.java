package cn.wolfcode.business.domain.vo;

import cn.wolfcode.business.domain.ServiceItem;
import cn.wolfcode.common.core.domain.entity.SysUser;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class AuditInfoVO {
    private ServiceItem serviceItem;
    List<SysUser> shopOwners;
    List<SysUser> finances;
    BigDecimal discountPrice;
}