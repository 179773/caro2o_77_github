<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="服务项名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入服务项名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否套餐" prop="carPackage">
        <el-select v-model="queryParams.carPackage" placeholder="请选择是否套餐" clearable>
          <el-option
            v-for="dict in dict.type.item_serv_package"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="服务分类" prop="serviceCatalog">
        <el-select v-model="queryParams.serviceCatalog" placeholder="请选择服务分类" clearable>
          <el-option
            v-for="dict in dict.type.item_serv_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="审核状态" prop="auditStatus">
        <el-select v-model="queryParams.auditStatus" placeholder="请选择审核状态" clearable>
          <el-option
            v-for="dict in dict.type.item_serv_audit_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="上架状态" prop="saleStatus">
        <el-select v-model="queryParams.saleStatus" placeholder="请选择上架状态" clearable>
          <el-option
            v-for="dict in dict.type.item_serv_up_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
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
          v-hasPermi="['business:serviceitem:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleAudit"
          :disabled="!canAudit"
          >发起审核</el-button
        >
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="serviceitemList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="服务项名称" align="center" prop="name" />
      <el-table-column label="服务项原价" align="center" prop="originalPrice" />
      <el-table-column label="服务项折扣价" align="center" prop="discountPrice" />
      <el-table-column label="是否套餐" align="center" prop="carPackage">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.item_serv_package" :value="scope.row.carPackage"/>
        </template>
      </el-table-column>
      <el-table-column label="备注信息" align="center" prop="info" />
      <el-table-column label="服务分类" align="center" prop="serviceCatalog">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.item_serv_type" :value="scope.row.serviceCatalog"/>
        </template>
      </el-table-column>
      <el-table-column label="审核状态" align="center" prop="auditStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.item_serv_audit_status" :value="scope.row.auditStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="上架状态" align="center" prop="saleStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.item_serv_up_status" :value="scope.row.saleStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:serviceItem:edit']"
            :disabled="(scope.row.saleStatus == 1 || scope.row.auditStatus == 1)"
          >修改</el-button>
          <!-- :disabled="(scope.row.saleStatus != 0 && scope.row.carPackage !=0 && )" -->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:serviceItem:remove']"
          >删除</el-button>
          <!-- 上架 -->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleListing(scope.row)"
            v-hasPermi="['business:serviceItem:listing']"
            :disabled="scope.row.saleStatus != 0"
          >上架</el-button>
          <!-- 下架 -->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelisting(scope.row)"
            v-hasPermi="['business:serviceItem:delisting']"
            :disabled="scope.row.saleStatus != 1"
          >下架</el-button>
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

    <!-- 添加或修改服务项对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="服务项名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入服务项名称" />
        </el-form-item>
        <el-form-item label="服务项原价" prop="originalPrice">
          <el-input v-model="form.originalPrice" placeholder="请输入服务项原价" />
        </el-form-item>
        <el-form-item label="服务项折扣价" prop="discountPrice">
          <el-input v-model="form.discountPrice" placeholder="请输入服务项折扣价" />
        </el-form-item>
        <el-form-item label="是否套餐" prop="carPackage">
          <el-select v-model="form.carPackage" placeholder="请选择是否套餐">
            <el-option
              v-for="dict in dict.type.item_serv_package"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="是否套餐" prop="carPackage">
            <el-radio-group v-model="form.carPackage">
                <el-radio v-for="dict in dict.type.item_serv_package" 
                :key="dict.value" 
                :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item> -->
        <el-form-item label="备注信息" prop="info">
          <el-input v-model="form.info" placeholder="请输入备注信息" />
        </el-form-item>
        <el-form-item label="服务分类" prop="serviceCatalog">
          <el-select v-model="form.serviceCatalog" placeholder="请选择服务分类">
            <el-option
              v-for="dict in dict.type.item_serv_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 发起审核对话框 -->
    <el-dialog 
      title="审核页面"
      :visible.sync="auditOpen"
      width="600px"
      append-to-body
      >
      <el-form
        ref="auditInfo"
        :model="auditInfo"
        :rules="auditRules"
        label-width="150px"
      >
        <el-form-item label="服务单项名称：" prop="name">
          <el-input disabled v-model="auditInfo.serviceItem.name" />
        </el-form-item>
        <el-form-item label="原价：" prop="originalPrice">
          <el-input disabled v-model="auditInfo.serviceItem.originalPrice" />
        </el-form-item>
        <el-form-item label="折扣价：" prop="discountPrice">
          <el-input disabled v-model="auditInfo.serviceItem.discountPrice" />
        </el-form-item>
        <el-form-item label="审核人(店长)：" prop="shopOwners">
          <el-select
            size="medium"
            v-model="shopOwnerId"
          >
            <el-option
              v-for="(item) in auditInfo.shopOwners"
              :key="item.userId"
              :label="item.nickName"
              :value="item.userId"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="审核人(财务)："
          v-if="auditInfo.serviceItem.discountPrice >= auditInfo.discountPrice"
          prop="finances"
        >
          <el-select 
            size="medium"
            v-model="financeId"
            >
            <el-option
              v-for="(item) in auditInfo.finances"
              :key="item.userId"
              :label="item.nickName"
              :value="item.userId"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注信息：" prop="info">
          <el-input v-model="info" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="auditSubmit">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { startAudit,serviceItemAuditInfo,Delisting,Listing,listServiceItem, getServiceItem, delServiceItem, addServiceItem, updateServiceItem } from "@/api/business/serviceItem";

export default {
  name: "ServiceItem",
  dicts: ['item_serv_package', 'item_serv_up_status', 'item_serv_audit_status', 'item_serv_type'],
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
      // 服务项表格数据
      serviceitemList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        carPackage: null,
        serviceCatalog: null,
        auditStatus: null,
        saleStatus: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      // 是否可审核
      // 是否可以发起审核
      canAudit: false,
      // 是否显示审核页面
      auditOpen:false,
      auditInfo:{
              serviceItem: {},
              shopOwners: [],
              finances:[],
              discountPrice:0
            },
      shopOwnerId:null,
      financeId:null,
      auditRules: {
            name: [
              { required: true, message: "服务名称不能为空", trigger: "blur" },
            ],
            originalPrice: [
              { required: true, message: "服务原价不能为空", trigger: "blur" },
            ],
            discountPrice: [
              { required: true, message: "服务折扣价不能为空", trigger: "blur" },
            ],
          },
      info : null,
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询服务项列表 */
    getList() {
      this.loading = true;
      listServiceItem(this.queryParams).then(response => {
        console.log(response);
        this.serviceitemList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.auditOpen = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        originalPrice: null,
        discountPrice: null,
        carPackage: null,
        info: null,
        createTime: null,
        serviceCatalog: null,
        auditStatus: null,
        saleStatus: null
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
    /* // 多选框选中数据
    handleSelectionChange(selection) {
      console.log(selection[0]);
      // let serviceItem = selection[0];

      // 可以发起编辑的状态
      // 1 发起单个审核
      if (selection.length == 0 || selection.length > 1) {
        this.canAudit == false;
        return;
      }
      // 2 是套餐 1
      let isPackage = selection[0].carPackage == 1;
      // 3 未上架 0
      let isNoOnSale = selection[0].saleStatus == 0;
      // 4 初始化状态 0
      let isInitial = selection[0].auditStatus == 0;
      if (isPackage && isNoOnSale && isInitial) {
        this.canAudit == true;
      }
      this.id = selection[0].id;

    }, */
    // 多选框选中数据
    handleSelectionChange(selection) {
      //this.ids = selection.map(item => item.id)
      //this.single = selection.length!==1
      //this.multiple = !selection.length




      //此处决定  发起审核 按钮是否可用
      let isSingle = selection.length == 1
      if(!isSingle){
        this.canAudit = false;
        return;
      }
      //必须是套餐
      let isCarPackage = selection[0].carPackage == 1
      //必须允许审核状态: 初始化  || 审核拒绝
      let isPassAudit = selection[0].auditStatus == 0 || selection[0].auditStatus == 3
      //必须是未上架状态
      let isOn = selection[0].saleStatus == 0
      if(isCarPackage && isPassAudit && isOn){
        this.canAudit = true
      }
      //当前选择行数据的id是多少
      this.id = selection[0].id;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加服务项";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getServiceItem(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改服务项";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateServiceItem(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addServiceItem(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除服务项编号为"' + ids + '"的数据项？').then(function() {
        return delServiceItem(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('business/serviceitem/export', {
        ...this.queryParams
      }, `serviceitem_${new Date().getTime()}.xlsx`)
    },
    /** 上架按钮操作 */
    handleListing(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认上架').then(function() {
        return Listing(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("上架成功");
      }).catch(() => {});
    },
    /** 下架按钮操作 */
    handleDelisting(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认下架').then(function() {
        return Delisting(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("上架成功");
      }).catch(() => {});
    },
    /** 发起审核处理 */
    handleAudit() {
        if(!this.canAudit){
            return;
        }
        this.auditOpen=true;
        this.reset();
        serviceItemAuditInfo(this.id).then((response)=>{
          // console.log(response.data);
            this.auditInfo=response.data;
        })
    },
    auditSubmit(){
        startAudit({id:this.id,financeId:this.financeId,shopOwnerId:this.shopOwnerId,info:this.info}).then(response => {
            this.$modal.msgSuccess("操作成功");
            this.auditOpen = false;
            this.getList();
        });
    }

  },
  
};
</script>
