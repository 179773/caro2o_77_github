import request from '@/utils/request'

// 查询套餐审核列表
export function listCarPackageAudit(query) {
  return request({
    url: '/business/carPackageAudit/list',
    method: 'get',
    params: query
  })
}

// 查询套餐审核详细
export function getCarPackageAudit(id) {
  return request({
    url: '/business/carPackageAudit/' + id,
    method: 'get'
  })
}

// 新增套餐审核
export function addCarPackageAudit(data) {
  return request({
    url: '/business/carPackageAudit',
    method: 'post',
    data: data
  })
}

// 修改套餐审核
export function updateCarPackageAudit(data) {
  return request({
    url: '/business/carPackageAudit',
    method: 'put',
    data: data
  })
}

// 删除套餐审核
export function delCarPackageAudit(id) {
  return request({
    url: '/business/carPackageAudit/' + id,
    method: 'delete'
  })
}

// 套餐审核进度图
export function getAuditProcess(id) {
  return request({
    url: '/business/carPackageAudit/process/' + id,
    method: 'get'
  })
}

// 套餐审核撤销
export function auditCancel(id) {
  return request({
    url: '/business/carPackageAudit/cancel/' + id,
    method: 'post'
  })
}

// 套餐审核历史list
export function getAuditHistory(instanceId) {
  return request({
    url: '/business/carPackageAudit/history/' + instanceId,
    method: 'get'
  })
}

// 查询套餐审核列表
export function listTodo(query) {
  return request({
    url: '/business/carPackageAudit/todo',
    method: 'get',
    params: query
  })
}

// 查询套餐审核列表
export function listDone(query) {
  return request({
    url: '/business/carPackageAudit/done',
    method: 'get',
    params: query
  })
}

// 新增套餐审核
export function audit(data) {
  return request({
    url: '/business/carPackageAudit/audit',
    method: 'post',
    data: data
  })
}