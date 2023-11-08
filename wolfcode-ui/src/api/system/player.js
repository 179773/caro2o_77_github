import request from '@/utils/request'

// 查询玩家管理3列表
export function listPlayer(query) {
  return request({
    url: '/system/player/list',
    method: 'get',
    params: query
  })
}

// 查询玩家管理3详细
export function getPlayer(id) {
  return request({
    url: '/system/player/' + id,
    method: 'get'
  })
}

// 新增玩家管理3
export function addPlayer(data) {
  return request({
    url: '/system/player',
    method: 'post',
    data: data
  })
}

// 修改玩家管理3
export function updatePlayer(data) {
  return request({
    url: '/system/player',
    method: 'put',
    data: data
  })
}

// 删除玩家管理3
export function delPlayer(id) {
  return request({
    url: '/system/player/' + id,
    method: 'delete'
  })
}
