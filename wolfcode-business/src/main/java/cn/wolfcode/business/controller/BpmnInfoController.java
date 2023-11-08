package cn.wolfcode.business.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.wolfcode.business.domain.vo.DeployVo;
import org.apache.commons.io.IOUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.wolfcode.common.annotation.Log;
import cn.wolfcode.common.core.controller.BaseController;
import cn.wolfcode.common.core.domain.AjaxResult;
import cn.wolfcode.common.enums.BusinessType;
import cn.wolfcode.business.domain.BpmnInfo;
import cn.wolfcode.business.service.IBpmnInfoService;
import cn.wolfcode.common.utils.poi.ExcelUtil;
import cn.wolfcode.common.core.page.TableDataInfo;

/**
 * 流程定义明细Controller
 * 
 * @author lin
 * @date 2023-11-01
 */
@RestController
@RequestMapping("/business/bpmnInfo")
public class BpmnInfoController extends BaseController
{
    @Autowired
    private IBpmnInfoService bpmnInfoService;

    /**
     * 查询流程定义明细列表
     */
    @PreAuthorize("@ss.hasPermi('business:bpmnInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(BpmnInfo bpmnInfo)
    {
        startPage();
        List<BpmnInfo> list = bpmnInfoService.selectBpmnInfoList(bpmnInfo);
        return getDataTable(list);
    }

    /**
     * 导出流程定义明细列表
     */
    @PreAuthorize("@ss.hasPermi('business:bpmnInfo:export')")
    @Log(title = "流程定义明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BpmnInfo bpmnInfo)
    {
        List<BpmnInfo> list = bpmnInfoService.selectBpmnInfoList(bpmnInfo);
        ExcelUtil<BpmnInfo> util = new ExcelUtil<BpmnInfo>(BpmnInfo.class);
        util.exportExcel(response, list, "流程定义明细数据");
    }

    /**
     * 获取流程定义明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:bpmnInfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bpmnInfoService.selectBpmnInfoById(id));
    }

    /**
     * 部署流程定义明细
     */
    @PreAuthorize("@ss.hasPermi('business:bpmnInfo:deploy')")
    @Log(title = "流程定义明细", businessType = BusinessType.INSERT)
    @PostMapping("/deploy")
    public AjaxResult deploy(DeployVo vo)
    {
        bpmnInfoService.deploy(vo);
        return AjaxResult.success();
    }

    /**
     * 修改流程定义明细
     */
    @PreAuthorize("@ss.hasPermi('business:bpmnInfo:edit')")
    @Log(title = "流程定义明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BpmnInfo bpmnInfo)
    {
        return toAjax(bpmnInfoService.updateBpmnInfo(bpmnInfo));
    }

    /**
     * 删除流程定义明细
     */
    @PreAuthorize("@ss.hasPermi('business:bpmnInfo:remove')")
    @Log(title = "审核流程定义", businessType = BusinessType.DELETE)
	@DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id)
    {

        bpmnInfoService.bpmnCancel(id);
        return  AjaxResult.success();
    }

    /**
     * 流程定义
     * @param type
     * @param id
     * @param response
     * @throws IOException
     */
    @GetMapping("/{type}/{id}")
    public void getBpmn(@PathVariable String type, @PathVariable Long id, HttpServletResponse response) throws IOException {
        InputStream inputStream = bpmnInfoService.getBpmn(type,id);
        IOUtils.copy(inputStream,response.getOutputStream());
    }


}
