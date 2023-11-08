import request from '@/utils/request'

// 查询服务项列表
export function listServiceItem(query) {
  return request({
    url: '/business/serviceItem/list',
    method: 'get',
    params: query
  })
}

// 查询服务项详细
export function getServiceItem(id) {
  return request({
    url: '/business/serviceItem/' + id,
    method: 'get'
  })
}

// 新增服务项
export function addServiceItem(data) {
  return request({
    url: '/business/serviceItem',
    method: 'post',
    data: data
  })
}

// 修改服务项
export function updateServiceItem(data) {
  return request({
    url: '/business/serviceItem',
    method: 'put',
    data: data
  })
}

// 删除服务项
export function delServiceItem(id) {
  return request({
    url: '/business/serviceItem/' + id,
    method: 'delete'
  })
}

// 上架服务项
export function Listing(id) {
  return request({
    url: '/business/serviceItem/listing/' + id,
    method: 'post'
  })
}

// 下架服务项
export function Delisting(id) {
  return request({
    url: '/business/serviceItem/delisting/' + id,
    method: 'post'
  })
}

// 获取审核信息-回显
export function serviceItemAuditInfo(id) {
  return request({
    url: '/business/serviceItem/auditInfo/' + id,
    method: 'get'
  })
}

// 发起审核-确认
export function startAudit(data) {
  return request({
    url: '/business/serviceItem/audit',
    method: 'post',
    data : data
  })
}