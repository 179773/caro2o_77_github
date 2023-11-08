package cn.wolfcode.business.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.wolfcode.business.domain.vo.CarPackageAuditVO;
import cn.wolfcode.business.domain.vo.HistoryVO;
import org.apache.commons.io.IOUtils;
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
import cn.wolfcode.business.domain.CarPackageAudit;
import cn.wolfcode.business.service.ICarPackageAuditService;
import cn.wolfcode.common.utils.poi.ExcelUtil;
import cn.wolfcode.common.core.page.TableDataInfo;

/**
 * 套餐审核Controller
 * 
 * @author lin
 * @date 2023-11-03
 */
@RestController
@RequestMapping("/business/carPackageAudit")
public class CarPackageAuditController extends BaseController
{
    @Autowired
    private ICarPackageAuditService carPackageAuditService;

    /**
     * 查询套餐审核列表
     */
    @PreAuthorize("@ss.hasPermi('business:carPackageAudit:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarPackageAudit carPackageAudit)
    {
        startPage();
        List<CarPackageAudit> list = carPackageAuditService.selectCarPackageAuditList(carPackageAudit);
        return getDataTable(list);
    }

    /**
     * 获取套餐审核详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:carPackageAudit:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(carPackageAuditService.selectCarPackageAuditById(id));
    }

    /**
     * 新增套餐审核
     */
    @PreAuthorize("@ss.hasPermi('business:carPackageAudit:add')")
    @Log(title = "套餐审核", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CarPackageAudit carPackageAudit)
    {
        return toAjax(carPackageAuditService.insertCarPackageAudit(carPackageAudit));
    }

    /**
     * 修改套餐审核
     */
    @PreAuthorize("@ss.hasPermi('business:carPackageAudit:edit')")
    @Log(title = "套餐审核", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CarPackageAudit carPackageAudit)
    {
        return toAjax(carPackageAuditService.updateCarPackageAudit(carPackageAudit));
    }

    /**
     * 删除套餐审核
     */
    @PreAuthorize("@ss.hasPermi('business:carPackageAudit:remove')")
    @Log(title = "套餐审核", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(carPackageAuditService.deleteCarPackageAuditByIds(ids));
    }

    /**
     * 流程--查看进度
     * @param id
     * @param response
     * @throws IOException
     */
    @GetMapping("/process/{id}")
    public void process(@PathVariable Long id,HttpServletResponse response) throws IOException {
        InputStream inputStream = carPackageAuditService.process(id);
        IOUtils.copy(inputStream,response.getOutputStream());
    }

    /**
     * 流程--撤销
     * @param id
     * @return
     */
    @PostMapping("/cancel/{id}")
    public AjaxResult cancel(@PathVariable Long id){
        carPackageAuditService.cancel(id);
        return AjaxResult.success();
    }

    /**
     * 套餐审核历史
     */
    @PreAuthorize("@ss.hasPermi('business:carPackageAudit:history')")
    @Log(title = "套餐审核", businessType = BusinessType.OTHER)
    @GetMapping("/history/{instanceId}")
    public AjaxResult history(@PathVariable Long instanceId)
    {
        List<HistoryVO> list = carPackageAuditService.queryHistory(instanceId);
        return AjaxResult.success(list);
    }

    /**
     * 查询套餐审核列表-todo
     */
//    @PreAuthorize("@ss.hasPermi('business:carPackageAudit:todo')")
    @GetMapping("/todo")
    public TableDataInfo todo(CarPackageAudit carPackageAudit)
    {
        List<CarPackageAudit> list = carPackageAuditService.todoQuery(carPackageAudit);
        if(list == null){
            return new TableDataInfo(Collections.emptyList(),0);
        }else{
            return getDataTable(list);
        }
    }

    /**
     * 审核 接口
     * @param vo
     * @return
     */
    @PostMapping("/audit")
    public AjaxResult audit(@RequestBody CarPackageAuditVO vo){
        carPackageAuditService.audit(vo);
        return AjaxResult.success();
    }

    /**
     * 查询套餐审核列表-done
     */
    @PreAuthorize("@ss.hasPermi('business:carPackageAudit:done')")
    @GetMapping("/done")
    public TableDataInfo done(CarPackageAudit carPackageAudit)
    {
        List<CarPackageAudit> list = carPackageAuditService.doneQuery(carPackageAudit);
        if(list == null){
            return new TableDataInfo(Collections.emptyList(),0);
        }else{
            return getDataTable(list);
        }
    }
}
