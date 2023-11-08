package cn.wolfcode.system.service;

import java.util.List;

import cn.wolfcode.system.domain.Player;

/**
 * 玩家管理3Service接口
 * 
 * @author lin
 * @date 2023-10-26
 */
public interface IPlayerService 
{
    /**
     * 查询玩家管理3
     * 
     * @param id 玩家管理3主键
     * @return 玩家管理3
     */
    public Player selectPlayerById(Long id);

    /**
     * 查询玩家管理3列表
     * 
     * @param player 玩家管理3
     * @return 玩家管理3集合
     */
    public List<Player> selectPlayerList(Player player);

    /**
     * 新增玩家管理3
     * 
     * @param player 玩家管理3
     * @return 结果
     */
    public int insertPlayer(Player player);

    /**
     * 修改玩家管理3
     * 
     * @param player 玩家管理3
     * @return 结果
     */
    public int updatePlayer(Player player);

    /**
     * 批量删除玩家管理3
     * 
     * @param ids 需要删除的玩家管理3主键集合
     * @return 结果
     */
    public int deletePlayerByIds(Long[] ids);

    /**
     * 删除玩家管理3信息
     * 
     * @param id 玩家管理3主键
     * @return 结果
     */
    public int deletePlayerById(Long id);
}
