import request from '@/utils/request'

// 查询客户信息列表
export function listCustomerInfo(query) {
  return request({
    url: '/business/CustomerInfo/list',
    method: 'get',
    params: query
  })
}

// 查询客户信息详细
export function getCustomerInfo(id) {
  return request({
    url: '/business/CustomerInfo/' + id,
    method: 'get'
  })
}

// 新增客户信息
export function addCustomerInfo(data) {
  return request({
    url: '/business/CustomerInfo',
    method: 'post',
    data: data
  })
}

// 修改客户信息
export function updateCustomerInfo(data) {
  return request({
    url: '/business/CustomerInfo',
    method: 'put',
    data: data
  })
}

// 删除客户信息
export function delCustomerInfo(id) {
  return request({
    url: '/business/CustomerInfo/' + id,
    method: 'delete'
  })
}
