package cn.wolfcode.business.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.wolfcode.common.core.domain.entity.SysDictData;
import cn.wolfcode.common.core.domain.entity.SysUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.wolfcode.common.annotation.Log;
import cn.wolfcode.common.core.controller.BaseController;
import cn.wolfcode.common.core.domain.AjaxResult;
import cn.wolfcode.common.enums.BusinessType;
import cn.wolfcode.business.domain.CustomerReturnVisit;
import cn.wolfcode.business.service.ICustomerReturnVisitService;
import cn.wolfcode.common.utils.poi.ExcelUtil;
import cn.wolfcode.common.core.page.TableDataInfo;

/**
 * 客户回访记录Controller
 * 
 * @author lin
 * @date 2023-11-05
 */
@RestController
@RequestMapping("/business/CustomerVisit")
public class CustomerReturnVisitController extends BaseController
{
    @Autowired
    private ICustomerReturnVisitService customerReturnVisitService;

    /**
     * 查询客户回访记录列表
     */
    @PreAuthorize("@ss.hasPermi('business:CustomerVisit:list')")
    @GetMapping("/list")
    public TableDataInfo list(CustomerReturnVisit customerReturnVisit)
    {
        startPage();
        List<CustomerReturnVisit> list = customerReturnVisitService.selectCustomerReturnVisitList(customerReturnVisit);
        return getDataTable(list);
    }

    /**
     * 导出客户回访记录列表
     */
    @PreAuthorize("@ss.hasPermi('business:CustomerVisit:export')")
    @Log(title = "客户回访记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CustomerReturnVisit customerReturnVisit)
    {
        List<CustomerReturnVisit> list = customerReturnVisitService.selectCustomerReturnVisitList(customerReturnVisit);
        ExcelUtil<CustomerReturnVisit> util = new ExcelUtil<CustomerReturnVisit>(CustomerReturnVisit.class);
        util.exportExcel(response, list, "客户回访记录数据");
    }

    /**
     * 获取客户回访记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:CustomerVisit:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(customerReturnVisitService.selectCustomerReturnVisitById(id));
    }

    /**
     * 新增客户回访记录
     */
    @PreAuthorize("@ss.hasPermi('business:CustomerVisit:add')")
    @Log(title = "客户回访记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CustomerReturnVisit customerReturnVisit)
    {
        return toAjax(customerReturnVisitService.insertCustomerReturnVisit(customerReturnVisit));
    }

    /**
     * 修改客户回访记录
     */
    @PreAuthorize("@ss.hasPermi('business:CustomerVisit:edit')")
    @Log(title = "客户回访记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CustomerReturnVisit customerReturnVisit)
    {
        return toAjax(customerReturnVisitService.updateCustomerReturnVisit(customerReturnVisit));
    }

    /**
     * 删除客户回访记录
     */
    @PreAuthorize("@ss.hasPermi('business:CustomerVisit:remove')")
    @Log(title = "客户回访记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(customerReturnVisitService.deleteCustomerReturnVisitByIds(ids));
    }


    /**
     * 删除客户回访记录
     */
//    @PreAuthorize("@ss.hasPermi('business:CustomerVisit:remove')")
//    @Log(title = "客户回访记录", businessType = BusinessType.DELETE)
    @GetMapping("/getUserList")
    public AjaxResult getUserList()
    {
        List<String> data = customerReturnVisitService.getUserList();
        return success(data);
    }
}
