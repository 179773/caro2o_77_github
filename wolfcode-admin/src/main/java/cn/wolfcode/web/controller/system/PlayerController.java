package cn.wolfcode.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.wolfcode.common.core.controller.BaseController;
import cn.wolfcode.common.core.domain.AjaxResult;
import cn.wolfcode.common.enums.BusinessType;
import cn.wolfcode.system.domain.Player;
import cn.wolfcode.system.service.IPlayerService;
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
 * 玩家管理3Controller
 * 
 * @author lin
 * @date 2023-10-26
 */
@RestController
@RequestMapping("/system/player")
public class PlayerController extends BaseController
{
    @Autowired
    private IPlayerService playerService;

    /**
     * 查询玩家管理3列表
     */
    @PreAuthorize("@ss.hasPermi('system:player:list')")
    @GetMapping("/list")
    public AjaxResult list(Player player)
    {
        List<Player> list = playerService.selectPlayerList(player);
        return success(list);
    }

    /**
     * 导出玩家管理3列表
     */
    @PreAuthorize("@ss.hasPermi('system:player:export')")
    @Log(title = "玩家管理3", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Player player)
    {
        List<Player> list = playerService.selectPlayerList(player);
        ExcelUtil<Player> util = new ExcelUtil<Player>(Player.class);
        util.exportExcel(response, list, "玩家管理3数据");
    }

    /**
     * 获取玩家管理3详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:player:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(playerService.selectPlayerById(id));
    }

    /**
     * 新增玩家管理3
     */
    @PreAuthorize("@ss.hasPermi('system:player:add')")
    @Log(title = "玩家管理3", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Player player)
    {
        return toAjax(playerService.insertPlayer(player));
    }

    /**
     * 修改玩家管理3
     */
    @PreAuthorize("@ss.hasPermi('system:player:edit')")
    @Log(title = "玩家管理3", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Player player)
    {
        return toAjax(playerService.updatePlayer(player));
    }

    /**
     * 删除玩家管理3
     */
    @PreAuthorize("@ss.hasPermi('system:player:remove')")
    @Log(title = "玩家管理3", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(playerService.deletePlayerByIds(ids));
    }
}
