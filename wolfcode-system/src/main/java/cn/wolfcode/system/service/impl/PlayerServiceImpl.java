package cn.wolfcode.system.service.impl;

import java.util.List;

import cn.wolfcode.system.domain.Player;
import cn.wolfcode.system.mapper.PlayerMapper;
import cn.wolfcode.system.service.IPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 玩家管理3Service业务层处理
 * 
 * @author lin
 * @date 2023-10-26
 */
@Service
public class PlayerServiceImpl implements IPlayerService
{
    @Autowired
    private PlayerMapper playerMapper;

    /**
     * 查询玩家管理3
     * 
     * @param id 玩家管理3主键
     * @return 玩家管理3
     */
    @Override
    public Player selectPlayerById(Long id)
    {
        return playerMapper.selectPlayerById(id);
    }

    /**
     * 查询玩家管理3列表
     * 
     * @param player 玩家管理3
     * @return 玩家管理3
     */
    @Override
    public List<Player> selectPlayerList(Player player)
    {
        return playerMapper.selectPlayerList(player);
    }

    /**
     * 新增玩家管理3
     * 
     * @param player 玩家管理3
     * @return 结果
     */
    @Override
    public int insertPlayer(Player player)
    {
        return playerMapper.insertPlayer(player);
    }

    /**
     * 修改玩家管理3
     * 
     * @param player 玩家管理3
     * @return 结果
     */
    @Override
    public int updatePlayer(Player player)
    {
        return playerMapper.updatePlayer(player);
    }

    /**
     * 批量删除玩家管理3
     * 
     * @param ids 需要删除的玩家管理3主键
     * @return 结果
     */
    @Override
    public int deletePlayerByIds(Long[] ids)
    {
        return playerMapper.deletePlayerByIds(ids);
    }

    /**
     * 删除玩家管理3信息
     * 
     * @param id 玩家管理3主键
     * @return 结果
     */
    @Override
    public int deletePlayerById(Long id)
    {
        return playerMapper.deletePlayerById(id);
    }
}
