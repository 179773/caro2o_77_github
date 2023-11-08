package cn.wolfcode.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.wolfcode.common.core.controller.BaseController;
import cn.wolfcode.common.core.domain.AjaxResult;
import cn.wolfcode.common.core.page.TableDataInfo;
import cn.wolfcode.common.enums.BusinessType;
import cn.wolfcode.system.domain.Employee;
import cn.wolfcode.system.service.IEmployeeService;
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
import cn.wolfcode.common.utils.poi.ExcelUtil;

/**
 * 员工管理2Controller
 * 
 * @author lin
 * @date 2023-10-26
 */
@RestController
@RequestMapping("/system/employee")
public class EmployeeController extends BaseController
{
    @Autowired
    private IEmployeeService employeeService;

    /**
     * 查询员工管理2列表
     */
    @PreAuthorize("@ss.hasPermi('system:employee:list')")
    @GetMapping("/list")
    public TableDataInfo list(Employee employee)
    {
        startPage();
        List<Employee> list = employeeService.selectEmployeeList(employee);
        return getDataTable(list);
    }

    /**
     * 导出员工管理2列表
     */
    @PreAuthorize("@ss.hasPermi('system:employee:export')")
    @Log(title = "员工管理2", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Employee employee)
    {
        List<Employee> list = employeeService.selectEmployeeList(employee);
        ExcelUtil<Employee> util = new ExcelUtil<Employee>(Employee.class);
        util.exportExcel(response, list, "员工管理2数据");
    }

    /**
     * 获取员工管理2详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:employee:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(employeeService.selectEmployeeById(id));
    }

    /**
     * 新增员工管理2
     */
    @PreAuthorize("@ss.hasPermi('system:employee:add')")
    @Log(title = "员工管理2", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Employee employee)
    {
        return toAjax(employeeService.insertEmployee(employee));
    }

    /**
     * 修改员工管理2
     */
    @PreAuthorize("@ss.hasPermi('system:employee:edit')")
    @Log(title = "员工管理2", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Employee employee)
    {
        return toAjax(employeeService.updateEmployee(employee));
    }

    /**
     * 删除员工管理2
     */
    @PreAuthorize("@ss.hasPermi('system:employee:remove')")
    @Log(title = "员工管理2", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(employeeService.deleteEmployeeByIds(ids));
    }
}
