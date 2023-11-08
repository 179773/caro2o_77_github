package cn.wolfcode.business.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.wolfcode.business.domain.vo.AuditInfoVO;
import cn.wolfcode.business.domain.vo.AuditVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.wolfcode.common.annotation.Log;
import cn.wolfcode.common.core.controller.BaseController;
import cn.wolfcode.common.core.domain.AjaxResult;
import cn.wolfcode.common.enums.BusinessType;
import cn.wolfcode.business.domain.ServiceItem;
import cn.wolfcode.business.service.IServiceItemService;
import cn.wolfcode.common.utils.poi.ExcelUtil;
import cn.wolfcode.common.core.page.TableDataInfo;

/**
 * 服务项Controller
 * 
 * @author lin
 * @date 2023-10-29
 */
@RestController
@RequestMapping("/business/serviceItem")
public class ServiceItemController extends BaseController
{
    @Autowired
    private IServiceItemService serviceItemService;

    /**
     * 查询服务项列表
     */
    @PreAuthorize("@ss.hasPermi('business:serviceItem:list')")
    @GetMapping("/list")
    public TableDataInfo list(ServiceItem serviceItem)
    {
        startPage();
        List<ServiceItem> list = serviceItemService.selectServiceItemList(serviceItem);
        return getDataTable(list);
    }

    /**
     * 导出服务项列表
     */
    @PreAuthorize("@ss.hasPermi('business:serviceItem:export')")
    @Log(title = "服务项", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ServiceItem serviceItem)
    {
        List<ServiceItem> list = serviceItemService.selectServiceItemList(serviceItem);
        ExcelUtil<ServiceItem> util = new ExcelUtil<ServiceItem>(ServiceItem.class);
        util.exportExcel(response, list, "服务项数据");
    }

    /**
     * 获取服务项详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:serviceItem:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(serviceItemService.selectServiceItemById(id));
    }

    /**
     * 新增服务项
     */
    @PreAuthorize("@ss.hasPermi('business:serviceItem:add')")
    @Log(title = "服务项", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ServiceItem serviceItem)
    {
        return toAjax(serviceItemService.insertServiceItem(serviceItem));
    }

    /**
     * 修改服务项
     */
    @PreAuthorize("@ss.hasPermi('business:serviceItem:edit')")
    @Log(title = "服务项", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ServiceItem serviceItem)
    {
        return toAjax(serviceItemService.updateServiceItem(serviceItem));
    }

    /**
     * 删除服务项
     */
    @PreAuthorize("@ss.hasPermi('business:serviceItem:remove')")
    @Log(title = "服务项", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(serviceItemService.deleteServiceItemByIds(ids));
    }

    /**
     * 上架服务项
     */
    @PreAuthorize("@ss.hasPermi('business:serviceItem:listing')")
    @Log(title = "服务项", businessType = BusinessType.DELETE)
    @PostMapping("/listing/{id}")
    public AjaxResult listing(@PathVariable Long id)
    {
        return toAjax(serviceItemService.listing(id));
    }

    /**
     * 下架服务项
     */
    @PreAuthorize("@ss.hasPermi('business:serviceItem:delisting')")
    @Log(title = "服务项", businessType = BusinessType.DELETE)
    @PostMapping("/delisting/{id}")
    public AjaxResult delisting(@PathVariable Long id)
    {
        return toAjax(serviceItemService.delisting(id));
    }

    /**
     * 获取审核信息--用于回显
     * @param id
     * @return
     */
    @GetMapping("/auditInfo/{id}")
    public AjaxResult auditInfo(@PathVariable Long id){
        AuditInfoVO auditServiceItemVO = serviceItemService.auditInfo(id);
        return AjaxResult.success(auditServiceItemVO);
    }

    /**
     * 发起审核--确认
     * @param vo
     * @return
     */
    @PostMapping("/audit")
    public AjaxResult startAudit(@RequestBody AuditVO vo){
        serviceItemService.startAudit(vo);
        return AjaxResult.success();
    }

}
