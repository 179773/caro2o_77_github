package cn.wolfcode.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.wolfcode.system.mapper.EmployeeMapper;
import cn.wolfcode.system.domain.Employee;
import cn.wolfcode.system.service.IEmployeeService;

/**
 * 员工管理2Service业务层处理
 * 
 * @author lin
 * @date 2023-10-26
 */
@Service
public class EmployeeServiceImpl implements IEmployeeService 
{
    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 查询员工管理2
     * 
     * @param id 员工管理2主键
     * @return 员工管理2
     */
    @Override
    public Employee selectEmployeeById(Long id)
    {
        return employeeMapper.selectEmployeeById(id);
    }

    /**
     * 查询员工管理2列表
     * 
     * @param employee 员工管理2
     * @return 员工管理2
     */
    @Override
    public List<Employee> selectEmployeeList(Employee employee)
    {
        return employeeMapper.selectEmployeeList(employee);
    }

    /**
     * 新增员工管理2
     * 
     * @param employee 员工管理2
     * @return 结果
     */
    @Override
    public int insertEmployee(Employee employee)
    {
        return employeeMapper.insertEmployee(employee);
    }

    /**
     * 修改员工管理2
     * 
     * @param employee 员工管理2
     * @return 结果
     */
    @Override
    public int updateEmployee(Employee employee)
    {
        return employeeMapper.updateEmployee(employee);
    }

    /**
     * 批量删除员工管理2
     * 
     * @param ids 需要删除的员工管理2主键
     * @return 结果
     */
    @Override
    public int deleteEmployeeByIds(Long[] ids)
    {
        return employeeMapper.deleteEmployeeByIds(ids);
    }

    /**
     * 删除员工管理2信息
     * 
     * @param id 员工管理2主键
     * @return 结果
     */
    @Override
    public int deleteEmployeeById(Long id)
    {
        return employeeMapper.deleteEmployeeById(id);
    }
}
