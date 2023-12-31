package cn.wolfcode.business.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.wolfcode.common.annotation.Log;
import cn.wolfcode.common.core.controller.BaseController;
import cn.wolfcode.common.core.domain.AjaxResult;
import cn.wolfcode.common.enums.BusinessType;
import cn.wolfcode.business.domain.Statement;
import cn.wolfcode.business.service.IStatementService;
import cn.wolfcode.common.utils.poi.ExcelUtil;
import cn.wolfcode.common.core.page.TableDataInfo;

/**
 * 结算单Controller
 * 
 * @author lin
 * @date 2023-10-31
 */
@RestController
@RequestMapping("/business/statement")
public class StatementController extends BaseController
{
    @Autowired
    private IStatementService statementService;

    /**
     * 查询结算单列表
     */
    @PreAuthorize("@ss.hasPermi('business:statement:list')")
    @GetMapping("/list")
    public TableDataInfo list(Statement statement)
    {
        startPage();
        List<Statement> list = statementService.selectStatementList(statement);
        return getDataTable(list);
    }

    /**
     * 导出结算单列表
     */
    @PreAuthorize("@ss.hasPermi('business:statement:export')")
    @Log(title = "结算单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Statement statement)
    {
        List<Statement> list = statementService.selectStatementList(statement);
        ExcelUtil<Statement> util = new ExcelUtil<Statement>(Statement.class);
        util.exportExcel(response, list, "结算单数据");
    }

    /**
     * 获取结算单详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:statement:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(statementService.selectStatementById(id));
    }

    /**
     * 新增结算单
     */
    @PreAuthorize("@ss.hasPermi('business:statement:add')")
    @Log(title = "结算单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Statement statement)
    {
        return toAjax(statementService.insertStatement(statement));
    }

    /**
     * 修改结算单
     */
    @PreAuthorize("@ss.hasPermi('business:statement:edit')")
    @Log(title = "结算单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Statement statement)
    {
        return toAjax(statementService.updateStatement(statement));
    }

    /**
     * 删除结算单
     */
    @PreAuthorize("@ss.hasPermi('business:statement:remove')")
    @Log(title = "结算单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(statementService.deleteStatementByIds(ids));
    }

    /**
     * 支付结算单
     */
    @PreAuthorize("@ss.hasPermi('business:statement:pay')")
    @Log(title = "结算单", businessType = BusinessType.DELETE)
    @PostMapping("/pay/{id}")
    public AjaxResult payStatement(@PathVariable Long id)
    {
        return toAjax(statementService.payStatement(id));
    }
}
