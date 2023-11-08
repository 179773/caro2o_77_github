import request from '@/utils/request'

// 查询流程定义明细列表
export function listBpmnInfo(query) {
  return request({
    url: '/business/bpmnInfo/list',
    method: 'get',
    params: query
  })
}

// 查询流程定义明细详细
export function getBpmnInfo(id) {
  return request({
    url: '/business/bpmnInfo/' + id,
    method: 'get'
  })
}

// 新增流程定义明细
export function addBpmnInfo(data) {
  return request({
    url: '/business/bpmnInfo',
    method: 'post',
    data: data
  })
}

// 修改流程定义明细
export function updateBpmnInfo(data) {
  return request({
    url: '/business/bpmnInfo',
    method: 'put',
    data: data
  })
}

// 删除流程定义明细
export function delBpmnInfo(id) {
  return request({
    url: '/business/bpmnInfo/' + id,
    method: 'delete'
  })
}

 

// 查看 图片 xml
export function getBpmnInfoFile(params){
  return request({
    url:`/business/bpmnInfo/${params.type}/${params.id}`,
    method:'get'
  })
}
