package cn.wolfcode.business.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.wolfcode.common.annotation.Log;
import cn.wolfcode.common.core.controller.BaseController;
import cn.wolfcode.common.core.domain.AjaxResult;
import cn.wolfcode.common.enums.BusinessType;
import cn.wolfcode.business.domain.Appointment;
import cn.wolfcode.business.service.IAppointmentService;
import cn.wolfcode.common.utils.poi.ExcelUtil;
import cn.wolfcode.common.core.page.TableDataInfo;

/**
 * 养修信息预约Controller
 * 
 * @author lin
 * @date 2023-10-28
 */
@RestController
@RequestMapping("/business/appointment")
public class AppointmentController extends BaseController
{
    @Autowired
    private IAppointmentService appointmentService;

    /**
     * 查询养修信息预约列表
     */
    @PreAuthorize("@ss.hasPermi('business:appointment:list')")
    @GetMapping("/list")
    public TableDataInfo list(Appointment appointment)
    {
        startPage();
        List<Appointment> list = appointmentService.selectAppointmentList(appointment);
        return getDataTable(list);
    }

    /**
     * 导出养修信息预约列表
     */
    @PreAuthorize("@ss.hasPermi('business:appointment:export')")
    @Log(title = "养修信息预约", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Appointment appointment)
    {
        List<Appointment> list = appointmentService.selectAppointmentList(appointment);
        ExcelUtil<Appointment> util = new ExcelUtil<Appointment>(Appointment.class);
        util.exportExcel(response, list, "养修信息预约数据");
    }

    /**
     * 获取养修信息预约详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:appointment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(appointmentService.selectAppointmentById(id));
    }

    /**
     * 新增养修信息预约
     */
    @PreAuthorize("@ss.hasPermi('business:appointment:add')")
    @Log(title = "养修信息预约", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Appointment appointment)
    {
        return toAjax(appointmentService.insertAppointment(appointment));
    }

    /**
     * 修改养修信息预约
     */
    @PreAuthorize("@ss.hasPermi('business:appointment:edit')")
    @Log(title = "养修信息预约", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Appointment appointment)
    {
        return toAjax(appointmentService.updateAppointment(appointment));
    }

    /**
     * 删除养修信息预约
     */
    @PreAuthorize("@ss.hasPermi('business:appointment:remove')")
    @Log(title = "养修信息预约", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(appointmentService.deleteAppointmentByIds(ids));
    }

    /**
     * 到店
     */
    @PreAuthorize("@ss.hasPermi('business:appointment:arrival')")
    @Log(title = "养修信息预约", businessType = BusinessType.DELETE)
    @PutMapping("/arrivalShop/{id}")
    public AjaxResult arrivalShop(@PathVariable Long id)
    {
        return toAjax(appointmentService.arrivalShop(id));
    }

    /**
     * 取消
     */
    @PreAuthorize("@ss.hasPermi('business:appointment:cancel')")
    @Log(title = "养修信息预约", businessType = BusinessType.DELETE)
    @PutMapping("/cancel/{id}")
    public AjaxResult cancel(@PathVariable Long id)
    {
        return toAjax(appointmentService.cancel(id));
    }

    /**
     *
     */
    @PreAuthorize("@ss.hasPermi('business:appointment:cancel')")
    @Log(title = "养修信息预约", businessType = BusinessType.DELETE)
    @PostMapping("/generate/{id}")
    public AjaxResult generate(@PathVariable Long id)
    {
        return AjaxResult.success(appointmentService.generateSettleDoc(id));
    }
}
