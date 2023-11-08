<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="模糊查询" prop="customerName">
        <el-input
          v-model="queryParams.customerName"
          placeholder="请输入客户姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="联系方式" prop="customerPhone">
        <el-input
          v-model="queryParams.customerPhone"
          placeholder="请输入客户联系方式"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车牌号码" prop="licensePlate">
        <el-input
          v-model="queryParams.licensePlate"
          placeholder="请输入车牌号码"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['business:appointment:add']"
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
          v-hasPermi="['business:appointment:edit']"
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
          v-hasPermi="['business:appointment:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['business:appointment:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="appointmentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="客户名称" align="center" prop="customerName" />
      <el-table-column label="联系方式" align="center" prop="customerPhone" />
      <el-table-column label="预约时间" align="center" prop="appointmentTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.appointmentTime, '{y}-{m}-{d}') }}</span><br>
          <span>{{ parseTime(scope.row.appointmentTime, '{h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="到店时间" align="center" prop="actualArrivalTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.actualArrivalTime, '{y}-{m}-{d}') }}</span><br>
          <span>{{ parseTime(scope.row.actualArrivalTime, '{h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="车牌号码" align="center" prop="licensePlate" />
      <el-table-column label="汽车类型" align="center" prop="carSeries" />
      <el-table-column label="服务类型" align="center" prop="serviceType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.appoint_serv_type" :value="scope.row.serviceType"/>
        </template>
      </el-table-column>
      <el-table-column label="备注信息" align="center" prop="info" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.appoint_serv_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:appointment:edit']"
            v-if="scope.row.status != 1 && scope.row.status != 2 && scope.row.status != 4 && scope.row.status != 5"
          >编辑</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleArrival(scope.row)"
            v-hasPermi="['business:appointment:arrival']"
            v-if="scope.row.status != 1 && scope.row.status != 2 && scope.row.status != 4 && scope.row.status != 5"
          >到店</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handlerSettle(scope.row)"
            v-hasPermi="['business:appointment:edit']"
            v-if="scope.row.status != 0 && scope.row.status != 2"
            
          >结算单</el-button>
          <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)" v-hasPermi="['system:user:resetPwd', 'system:user:edit']">
                <el-button size="mini" type="text" icon="el-icon-d-arrow-right" v-if="scope.row.status != 1 && scope.row.status != 4 && scope.row.status != 5">更多</el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="handleCancel" icon="el-icon-key"
                    v-hasPermi="['business:appointment:cancel']"  v-if="scope.row.status != 2">取消</el-dropdown-item>
                  <el-dropdown-item command="handleDelete" icon="el-icon-circle-check"
                    v-hasPermi="['business:appointment:delete']">删除</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
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

    <!-- 添加或修改养修信息预约对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="客户姓名" prop="customerName">
          <el-input v-model="form.customerName" placeholder="请输入客户姓名" />
        </el-form-item>
        <el-form-item label="客户联系方式" prop="customerPhone">
          <el-input v-model="form.customerPhone" placeholder="请输入客户联系方式" />
        </el-form-item>
        <el-form-item label="预约时间" prop="appointmentTime">
          <el-date-picker clearable
            v-model="form.appointmentTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择预约时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="车牌号码" prop="licensePlate">
          <el-input v-model="form.licensePlate" placeholder="请输入车牌号码" />
        </el-form-item>
        <el-form-item label="汽车类型" prop="carSeries">
          <el-input v-model="form.carSeries" placeholder="请输入汽车类型" />
        </el-form-item>
        <el-form-item label="服务类型" prop="serviceType">
          <el-select v-model="form.serviceType" placeholder="请选择服务类型">
            <el-option
              v-for="dict in dict.type.appoint_serv_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注信息" prop="info">
          <el-input v-model="form.info" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { generateSettlementDoc,AppointmentCancel,arrivalShop,listAppointment, getAppointment, delAppointment, addAppointment, updateAppointment } from "@/api/business/appointment";

export default {
  name: "Appointment",
  dicts: ['appoint_serv_status', 'appoint_serv_type'],
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
      // 养修信息预约表格数据
      appointmentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        customerName: null,
        customerPhone: null,
        licensePlate: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        customerName:[
            {required:true, message:'客户姓名不能为空', trigger:'blur'}
        ],
        customerPhone:[
            {required:true, message:'手机号不能为空', trigger:'blur'}
        ],
        appointmentTime:[
            {required:true, message:'预约时间不能为空', trigger:'blur'}
        ],
        licensePlate:[
            {required:true, message:'车牌号不能为空', trigger:'blur'}
        ],
        carSeries:[
            {required:true, message:'汽车类型不能为空', trigger:'blur'}
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询养修信息预约列表 */
    getList() {
      this.loading = true;
      listAppointment(this.queryParams).then(response => {
        this.appointmentList = response.rows;
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
        customerName: null,
        customerPhone: null,
        appointmentTime: null,
        actualArrivalTime: null,
        licensePlate: null,
        carSeries: null,
        serviceType: null,
        createTime: null,
        info: null,
        status: null
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
      this.title = "添加养修信息预约";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAppointment(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改养修信息预约";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAppointment(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAppointment(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除养修信息预约编号为"' + ids + '"的数据项？').then(function() {
        return delAppointment(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('business/appointment/export', {
        ...this.queryParams
      }, `appointment_${new Date().getTime()}.xlsx`)
    },
    // 更多操作触发
    handleCommand(command, row) {
        switch (command) {
            case "handleCancel":
                this.handleCancel(row);
                break;
            case "handleDelete":
                this.handleDelete(row);
                break;

            default:
                break;
        }
    },
    handleArrival(row){
      const ids = row.id || this.ids;
      this.$modal.confirm('客户是否到店？').then(function() {
        return arrivalShop(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("操作成功");
      }).catch(() => {});
    },
    handleCancel(row){
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认取消？').then(function() {
        return AppointmentCancel(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("取消成功");
      }).catch(() => {});
    },
    handlerSettle(row){
      // 思考：结算单id怎么获取

      // 1 创建结算单
      // 2 查询结算单
      const ids = row.id || this.ids;
      let _this = this
      this.$modal.confirm('是否查看结算单？').then(function() {
        // 生成结算单并返回结算单id / 直接返回结算单id
        generateSettlementDoc(ids).then(response => {
          console.log(response);
            // 返回值：结算单id
            // 页面跳转结算单明细，需要准备结算单id
            _this.$router.push({
              path:"/business/statementItem/index/" + response.data
            })
        })
      })
    }
  }
  
};
</script>
