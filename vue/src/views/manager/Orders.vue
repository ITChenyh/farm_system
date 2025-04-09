<template>
  <div>

    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.name" style="width: 300px; margin-right: 10px" placeholder="请输入农产品名称查询"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="info" style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" stripe>
        <el-table-column label="订单号" prop="orderNo"></el-table-column>
        <el-table-column label="商品名称" prop="goodsName"></el-table-column>
        <el-table-column label="数量" prop="num"></el-table-column>
        <el-table-column label="用户ID" prop="userId" v-if="data.user.role == 'ADMIN'"></el-table-column>
        <el-table-column label="状态" prop="status"></el-table-column>
        <el-table-column label="下单时间" prop="time"></el-table-column>
        <el-table-column label="操作" align="center" width="260">
          <template #default="scope">
            <el-button type="primary" @click="handleChangeStatus(scope.row)" v-if="data.user.role == 'ADMIN' && scope.row.status == '待发货'">发货</el-button>
            <el-button type="danger" @click="handleDelete(scope.row.id)">取消订单</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <el-pagination background layout="prev, pager, next" @current-change="load" v-model:page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
    </div>
    

  </div>
</template>

<script setup>
import request from "@/utils/request";
import {reactive} from "vue";
import {ElMessageBox, ElMessage} from "element-plus";

// 文件上传的接口地址
const uploadUrl = import.meta.env.VITE_BASE_URL + '/files/upload'

const data = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  formVisible: false,
  form: {},
  tableData: [],
  name: null,
  user: JSON.parse(localStorage.getItem('user-info') || '{}')
})


// 分页查询
const load = () => {
    request.get('/orders/selectPage', {
      params: {
        pageNum: data.pageNum,
        pageSize: data.pageSize,
        goodsName : data.name,
        role : data.user.role,
        userId : data.user.id
      }
    }).then(res => {
      data.tableData = res.data?.list
      data.total = res.data?.total
    })
}

// 改变订单状态
const handleChangeStatus = (row) => {
  row.status = "已发货"
  request.put('/orders/update', row).then(res => {
    if (res.code === '200') {
      load()
      ElMessage.success('操作成功')
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 删除
const handleDelete = (id) => {
  ElMessageBox.confirm('取消后订单无法恢复，您确定取消吗?', '确认取消', { type: 'warning' }).then(res => {
    request.delete('/orders/delete/' + id).then(res => {
      if (res.code === '200') {
        load()
        ElMessage.success('操作成功')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

// 重置
const reset = () => {
  data.name = null
  load()
}

// 处理文件上传的钩子
const handleImgSuccess = (res) => {
  data.form.img = res.data  // res.data就是文件上传返回的文件路径，获取到路径后赋值表单的属性
}

load()
</script>