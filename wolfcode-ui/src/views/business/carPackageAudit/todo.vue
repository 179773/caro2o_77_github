<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.flow_bpmn_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="daterangeCreateTime"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['business:carPackageAudit:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['business:carPackageAudit:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['business:carPackageAudit:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['business:carPackageAudit:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="carPackageAuditList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="套餐名称" align="center" prop="serviceItemName" />
      
      <el-table-column label="套餐价格" align="center" prop="serviceItemPrice" />
      <el-table-column label="套餐备注" align="center" prop="serviceItemInfo" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span><br>
          <span>{{ parseTime(scope.row.createTime, '{h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
     
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.pck_audit_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleHistory(scope.row.instanceId)"
          >审批历史</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-info"
            @click="handleProcess(scope.row.id)"
          >进度查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleAudit(scope.row.id)"
          >审批</el-button>
          >
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改套餐审核对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 流程进度图 -->
    <el-dialog
      title="流程进度图"
      :visible.sync="processOpen"
      width="1200px"
      append-to-body
    >
      <div v-html="processImg"></div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeProcess">关 闭</el-button>
      </div>
    </el-dialog>
    <!-- 审核历史 -->
    <el-dialog
      title="审批历史"
      :visible.sync="historyOpen"
      width="1200px"
      append-to-body
    >
      <el-table
        ref="tables"
        v-loading="history.loading"
        :data="history.list"
      >
        <el-table-column label="任务名称" align="center" prop="taskName"  width="180"/>
        <el-table-column label="开始时间" align="center" prop="startTime" />
        <el-table-column
          label="结束时间"
          align="center"
          prop="endTime"
          width="180"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="耗时"
          align="center"
          prop="durationInMillis"
          width="180"
        >
        </el-table-column>
        <el-table-column
          label="审批意见"
          align="center"
          prop="comment"
          width="180"
        >
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeHistory">关 闭</el-button>
      </div>
    </el-dialog>
    <!-- 审批--模态框 -->
    <el-dialog
      title="流程审核"
      :visible.sync="auditOpen"
      width="500px"
      append-to-body
    >
      <el-form
        label-width="80px"
      >
        <el-col :span="12">
          <el-form-item label="审批意见" prop="auditStatus">
            <el-select
              v-model="auditStatus"
              placeholder="请选择审批意见"
            >
              <el-option value="2" label="同意"></el-option>
              <el-option value="1" label="拒绝"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="批注" prop="auditInfo">
            <el-input
              v-model="auditInfo"
              type="textarea"
              placeholder="请输入批注"
              maxlength="250"
              rows="4"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAudit">确 定</el-button>
        <el-button @click="auditCancel">取 消</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>
import { audit,listTodo,getAuditHistory,getAuditProcess,listCarPackageAudit, getCarPackageAudit, delCarPackageAudit, addCarPackageAudit, updateCarPackageAudit } from "@/api/business/carPackageAudit";

export default {
  name: "CarPackageAudit",
  dicts: ['pck_audit_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 套餐审核表格数据
      carPackageAuditList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 创建时间时间范围
      daterangeCreateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        status: null,
        createTime: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      // 是否显示弹出层-进程
      processOpen:false,
      //显示内容
      processImg:null,
       // 是否显示弹出层-历史
       historyOpen: false,
      //列表数据
      history:{
        loading:true,
        // taskName startTime endTime durtionInMillis comment
        list:[]
      },
      // 是否显示弹出层-审核
      auditOpen:false,
      auditStatus:'1', //默认同意
      auditInfo:'',  //审核备注 
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询套餐审核列表 */
    getList() {
    this.loading = true;
    this.queryParams.params = {};
    if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
    }
    listTodo(this.queryParams).then(response => {
        this.carPackageAuditList = response.rows;
        this.total = response.total;
        this.loading = false;
    });
  },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        serviceItemId: null,
        serviceItemName: null,
        serviceItemInfo: null,
        serviceItemPrice: null,
        instanceId: null,
        creatorId: null,
        info: null,
        status: null,
        createTime: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeCreateTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加套餐审核";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCarPackageAudit(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改套餐审核";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCarPackageAudit(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCarPackageAudit(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除套餐审核编号为"' + ids + '"的数据项？').then(function() {
        return delCarPackageAudit(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('business/carPackageAudit/export', {
        ...this.queryParams
      }, `carPackageAudit_${new Date().getTime()}.xlsx`)
    },
    handleProcess(id){
        getAuditProcess(id).then(response => {
            this.processImg = response;
            this.processOpen = true
        });
    },
    closeProcess(){
        this.processOpen = false;
    } ,
    
    handleHistory(instanceId){
      getAuditHistory(instanceId).then(response => {
        this.historyOpen = true;
        this.history.list = response.data
        this.history.loading = false
      });
    },
    closeHistory(){
      this.historyOpen = false
      
    },
    handleAudit(id){
      this.id = id;
      this.auditOpen = true
      this.auditInfo = '',
      this.auditStatus = ""
    },
    submitAudit(){
      let param = {
        id:this.id,
        status : this.auditStatus,
        info : this.auditInfo
      }
      audit(param).then(response => {
        this.auditOpen = false
        this.getList()
      });
    },
    auditCancel(){
      this.auditOpen = false
    }

  }
};
</script>
