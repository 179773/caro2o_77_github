<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="回访方式" prop="visitMethod">
        <el-select v-model="queryParams.visitMethod" placeholder="请选择回访方式" clearable>
          <el-option
            v-for="dict in dict.type.cus_return"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="回访日期">
        <el-date-picker
          v-model="daterangeVisitDate"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      
      <el-form-item label="录入人" prop="entryUser">
          <el-select
            v-model="queryParams.entryUser"
            placeholder="请选择录入人"
            clearable
            style="width: 240px;"
            
          >
            <el-option
              v-for="user in userList"
              :key="user"
              :label="user"
              :value="user"
            />
          </el-select>
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
          v-hasPermi="['business:CustomerVisit:add']"
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
          v-hasPermi="['business:CustomerVisit:edit']"
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
          v-hasPermi="['business:CustomerVisit:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['business:CustomerVisit:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="CustomerVisitList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="回访客户" align="center" prop="customerName" />
      <el-table-column label="回访方式" align="center" prop="visitMethod">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.cus_return" :value="scope.row.visitMethod"/>
        </template>
      </el-table-column>
      <el-table-column label="回访原因" align="center" prop="visitReason" />
      <el-table-column label="回访结果" align="center" prop="visitResult" />
      <el-table-column label="回访日期" align="center" prop="visitDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.visitDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="录入人" align="center" prop="entryUser" />
      <el-table-column label="录入时间" align="center" prop="entryTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.entryTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:CustomerVisit:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:CustomerVisit:remove']"
          >删除</el-button>
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

    <!-- 添加或修改客户回访记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="回访客户姓名" prop="customerName">
          <el-input v-model="form.customerName" placeholder="请输入客户姓名" />
        </el-form-item>
        <el-form-item label="回访方式" prop="visitMethod">
          <el-select v-model="form.visitMethod" placeholder="请选择回访方式">
            <el-option
              v-for="dict in dict.type.cus_return"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="回访原因" prop="visitReason">
          <el-input v-model="form.visitReason" placeholder="请输入回访原因" />
        </el-form-item>
        <el-form-item label="回访结果" prop="visitResult">
          <el-input v-model="form.visitResult" placeholder="请输入回访结果" />
        </el-form-item>
        <el-form-item label="回访日期" prop="visitDate">
          <el-date-picker clearable
            v-model="form.visitDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择回访日期">
          </el-date-picker>
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
import { getUserList,listCustomerVisit, getCustomerVisit, delCustomerVisit, addCustomerVisit, updateCustomerVisit } from "@/api/business/CustomerVisit";

export default {
  name: "CustomerVisit",
  dicts: ['cus_return'],
  userList: ['userList'],
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
      // 客户回访记录表格数据
      CustomerVisitList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 回访日期时间范围
      daterangeVisitDate: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        customerName: null,
        visitMethod: null,
        visitDate: null,
        entryUser: null,
        
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        customerName: [
          { required: true, message: "回访客户不能为空", trigger: "change" }
        ],
        visitMethod: [
          { required: true, message: "回访方式不能为空", trigger: "change" }
        ],
        visitReason: [
          { required: true, message: "回访原因不能为空", trigger: "blur" }
        ],
        visitResult: [
          { required: true, message: "回访结果不能为空", trigger: "blur" }
        ],
        visitDate: [
          { required: true, message: "回访日期不能为空", trigger: "blur" }
        ],
      }

    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询客户回访记录列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeVisitDate && '' != this.daterangeVisitDate) {
        this.queryParams.params["beginVisitDate"] = this.daterangeVisitDate[0];
        this.queryParams.params["endVisitDate"] = this.daterangeVisitDate[1];
      }
      
      listCustomerVisit(this.queryParams).then(response => {
        this.CustomerVisitList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
      getUserList().then(response => {
        console.log(response);
        console.log(response.data);
        this.userList = response.data;
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
        visitMethod: null,
        visitReason: null,
        visitResult: null,
        visitDate: null,
        entryUser: null,
        entryTime: null
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
      this.daterangeVisitDate = [];
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
      this.title = "添加客户回访记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCustomerVisit(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改客户回访记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCustomerVisit(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCustomerVisit(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除客户回访记录编号为"' + ids + '"的数据项？').then(function() {
        return delCustomerVisit(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('business/CustomerVisit/export', {
        ...this.queryParams
      }, `CustomerVisit_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
