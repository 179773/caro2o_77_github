package cn.wolfcode.system.service;

import java.util.List;
import cn.wolfcode.system.domain.Employee;

/**
 * 员工管理2Service接口
 * 
 * @author lin
 * @date 2023-10-26
 */
public interface IEmployeeService 
{
    /**
     * 查询员工管理2
     * 
     * @param id 员工管理2主键
     * @return 员工管理2
     */
    public Employee selectEmployeeById(Long id);

    /**
     * 查询员工管理2列表
     * 
     * @param employee 员工管理2
     * @return 员工管理2集合
     */
    public List<Employee> selectEmployeeList(Employee employee);

    /**
     * 新增员工管理2
     * 
     * @param employee 员工管理2
     * @return 结果
     */
    public int insertEmployee(Employee employee);

    /**
     * 修改员工管理2
     * 
     * @param employee 员工管理2
     * @return 结果
     */
    public int updateEmployee(Employee employee);

    /**
     * 批量删除员工管理2
     * 
     * @param ids 需要删除的员工管理2主键集合
     * @return 结果
     */
    public int deleteEmployeeByIds(Long[] ids);

    /**
     * 删除员工管理2信息
     * 
     * @param id 员工管理2主键
     * @return 结果
     */
    public int deleteEmployeeById(Long id);
}
