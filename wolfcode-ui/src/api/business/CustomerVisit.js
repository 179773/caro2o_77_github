import request from '@/utils/request'

// 查询客户回访记录列表
export function listCustomerVisit(query) {
  return request({
    url: '/business/CustomerVisit/list',
    method: 'get',
    params: query
  })
}

// 查询客户回访记录详细
export function getCustomerVisit(id) {
  return request({
    url: '/business/CustomerVisit/' + id,
    method: 'get'
  })
}

// 新增客户回访记录
export function addCustomerVisit(data) {
  return request({
    url: '/business/CustomerVisit',
    method: 'post',
    data: data
  })
}

// 修改客户回访记录
export function updateCustomerVisit(data) {
  return request({
    url: '/business/CustomerVisit',
    method: 'put',
    data: data
  })
}

// 删除客户回访记录
export function delCustomerVisit(id) {
  return request({
    url: '/business/CustomerVisit/' + id,
    method: 'delete'
  })
}


// getUserList
export function getUserList(query) {
  return request({
    url: '/business/CustomerVisit/getUserList',
    method: 'get',
    params: query
  })
}