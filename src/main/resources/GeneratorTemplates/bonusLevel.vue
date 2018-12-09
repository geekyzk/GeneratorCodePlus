<template>
  <div class="app-container">
    <!-- 查询参数过滤 -->
    <div class="filter-container">
      <el-row :gutter="10">
        <el-form>
          <el-col :span="2">
            <el-button class="filter-item" style="width:100%;padding: 10px 0px;"  @click="handleCreate" type="primary" icon="el-icon-plus">{{$t('table.add')}}</el-button>
          </el-col>
          <el-col :span="2">
            <el-button :disabled="total == 0" type="primary" style="width:100%;padding: 10px 0px;" :loading="downloadLoading"  icon="el-icon-download" @click="handleDownload">{{$t('table.export')}}</el-button>
          </el-col>
          <el-col :span="4">
            <el-form-item label="名称" label-width="50px">
              <el-input @keyup.enter.native="handleFilter" style="width: 100%;" class="filter-item" placeholder="名称" v-model="listParams['name.contains']"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="2">
            <el-button class="filter-item" style="width:100%;padding: 10px 0px;"  type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('table.search')}}</el-button>
          </el-col>
        </el-form>
      </el-row>
    </div>
    <el-row>
      <div class='chart-container'>
        <el-table :data="data" @sort-change="sortSignTime" v-loading="loading" @row-dblclick="openDetails" border fit highlight-current-row style="width: 100%;">
          <!-- 操作 -->
          <el-table-column align="center" :label="$t('table.actions')" width="80px" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button type="text" title="编辑"  @click="handleUpdate(scope.row)"><i class="el-icon-edit"></i></el-button>
              <el-button type="text" title="删除"  @click="deleteData(scope.row.id)"><i class="el-icon-delete"></i></el-button>
            </template>
          </el-table-column>
          <el-table-column type="index" align="center" width="50"></el-table-column>
          <% for(item in model.items) {%>
          <el-table-column prop="${item.name}" label="${item.remark}" align="center" :sortable="'custom'">
            <template slot-scope="scope">
              {{ scope.row.${item.name} }}
            </template>
          </el-table-column>
          <%}%>
          <el-table-column prop="createAt" label="创建时间" align="center" :sortable="'custom'">
            <template slot-scope="scope">
              {{ scope.row.createAt | formatDate }}
            </template>
          </el-table-column>
          <el-table-column prop="updateAt" label="更新时间" align="center" :sortable="'custom'">
            <template slot-scope="scope">
              {{ scope.row.updateAt | formatDate }}
            </template>
          </el-table-column>
        </el-table>
      </div>
      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listParams.page" 
          :page-sizes="pageSize" :page-size="listParams.size" layout="total, sizes, prev, pager, next, jumper" :total="total">
        </el-pagination>
      </div>
    </el-row>

    <!-- 对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="38%" top="7vh">
      <el-form class="form-add" :model="${lowerFirst(model.entityName)}Temp" :rules="rules" ref="BonusLevelForm" label-width="100px" style="width: 100%; padding-left:10px;padding-right:10px">
        <% for(item in model.items) {%>
        <el-form-item label="${item.remark}" prop="${item.name}">
          <el-input v-model="${lowerFirst(model.entityName)}Temp.${item.name}" style="width:100%"></el-input>
        </el-form-item>
        <%}%>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">{{$t('table.cancel')}}</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData" :loading="buttonLoading">{{$t('table.confirm')}}</el-button>
        <el-button v-else type="primary" @click="updateData" :loading="buttonLoading">{{$t('table.confirm')}}</el-button>
      </span>
    </el-dialog>
    <el-dialog title="详情" :visible.sync="dialogDetailVisible" width="40%" top="7vh" style="width: 100%; padding-left:10px;padding-right:10px">
      <el-form ref="form" :model="showData" label-width="90px" label-position="left" style="padding-left: 20px">
        <% for(item in model.items) {%>
        <el-form-item label="${item.remark}">
          <span> {{ showData.${item.name} }}</span>
        </el-form-item label>
        <%}%>
        <el-form-item label="创建时间">
          <span>{{ showData.createAt | formatDate }}</span>
        </el-form-item>
        <el-form-item label="更新时间">
          <span>{{ showData.updateAt | formatDate }}</span>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { list${model.entityName}, create${model.entityName}, update${model.entityName}, delet${model.entityName} } from '@/api/${model.entityName}'
import waves from '@/directive/waves'
import { formatDate } from '@/utils'

export default {
  name: '${model.entityName}',
  directives: {
    waves
  },
  data() {
    return {
      loading: false,
      buttonLoading: false,
      pageSize: [10, 20, 30, 50],
      downloadLoading: false,
      dialogFormVisible: false,
      dialogDetailVisible: false,
      total: null,
      dialogStatus: '',
      textMap: {
        update: '编辑${model.remark}',
        create: '添加${model.remark}'
      },
      showData: '',
      listParams: {
        page: 1,
        size: 10,
        'name.contains': undefined
      },
      data: [],
      ${lowerFirst(model.entityName)}Temp: {
        <% for(item in model.items) {%>
        ${item.name}: undefined,
        <%}%>
        id: undefined
      },
      rules: {
        <%for(item in model.items) {%>
          <%if(item.isRequire){%>
        ${item.name}: [{required: true, message: '${item.remark}为必须参数', trigger: 'change'}],
        <%}}%>
      }
    }
  },
  created() {
    this.list()
  },
  methods: {
    list() {
      const params = {
        page: this.listParams.page,
        size: this.listParams.size,
        'sort': this.listParams['sort'] === '' ? null : this.listParams['sort'],
        'name.contains': this.listParams['name.contains']
      }
      this.loading = true
      list${model.entityName}(params).then(response => {
        this.loading = false
        this.data = response.data.data
        this.total = response.data.totalSize
      })
    },
    sortSignTime(column) {
      if (column.order === 'ascending') {
        this.listParams['sort'] = `${column.prop},asc`
      } else if (column.order === 'descending') {
        this.listParams['sort'] = `${column.prop},desc`
      } else {
        this.listParams['sort'] = null
      }
      this.page = 1
      this.list()
    },
    openDetails(row) {
      this.showData = row
      this.dialogDetailVisible = true
    },
    reset${lowerFirst(model.entityName)}Temp() {
      this.${lowerFirst(model.entityName)}Temp = {
        name: null,
        remark: null
      }
    },
    handleCreate() {
      this.reset${lowerFirst(model.entityName)}Temp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['BonusLevelForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['BonusLevelForm'].validate((valid) => {
        if (valid) {
          this.buttonLoading = true
          createBonusLevel(this.${lowerFirst(model.entityName)}Temp).then(response => {
            this.buttonLoading = false
            if (response.data.code === -1) {
              this.$message({
                title: '失败',
                message: response.data.message,
                type: 'error'
              })
            } else {
              this.list()
              this.dialogFormVisible = false
              this.$message({
                title: '成功',
                message: response.data.message,
                type: 'success'
              })
            }
          }).catch(() => {
            this.buttonLoading = false
          })
        }
      })
    },
    handleUpdate(row) {
      this.${lowerFirst(model.entityName)}Temp = {
        id: row.id,
        name: row.name,
        startBonus: row.startBonus,
        endBonus: row.endBonus,
        peoplePercent: row.peoplePercent,
        remark: row.remark
      }
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['BonusLevelForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['BonusLevelForm'].validate((valid) => {
        if (valid) {
          this.buttonLoading = true
          updateBonusLevel(this.${lowerFirst(model.entityName)}Temp).then(response => {
            this.buttonLoading = false
            if (response.data.code === -1) {
              this.$message({
                title: '失败',
                message: response.data.message,
                type: 'error'
              })
            } else {
              this.list()
              this.dialogFormVisible = false
              this.$message({
                title: '成功',
                message: response.data.message,
                type: 'success'
              })
            }
          }).catch(() => {
            this.buttonLoading = false
          })
        }
      })
    },
    deleteData(id) {
      this.$confirm('确认删除该积分等级, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteBonusLevel(id).then(response => {
          if (response.data.code === -1) {
            this.$message({
              title: '失败',
              message: response.data.message,
              type: 'error'
            })
          } else {
            this.list()
            this.$message({
              title: '成功',
              message: response.data.message,
              type: 'success'
            })
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    handleFilter() {
      this.listParams.page = 1
      this.list()
    },
    handleSizeChange(val) {
      this.listParams.size = val
      this.list()
    },
    handleCurrentChange(val) {
      this.listParams.page = val
      this.list()
    },
    // 导出数据为 excel
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['名称', '开始区间', '结束区间', '人数百分比', '备注', '创建时间', '更新时间']
        const filterVal = ['name', 'startBonus', 'endBonus', 'peoplePercent', 'remark', 'createAt', 'updateAt']
        const params = Object.assign({}, this.listParams)
        params.size = this.total
        listBonusLevels(params).then(response => {
          this.exportList = response.data.data
          const data = this.formatJson(filterVal, this.exportList)
          excel.export_json_to_excel({
            header: tHeader,
            data,
            filename: '积分等级列表'
          })
          this.downloadLoading = false
        })
      })
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => {
        if (j === 'createAt' || j === 'updateAt') {
          return formatDate(v[j])
        } else {
          return v[j]
        }
      }))
    }
  }
}
</script>

<style scoped>
</style>
