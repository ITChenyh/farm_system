<template>
  <div>

    <div class="card" style="margin-bottom: 5px">
      <div style="margin-bottom: 10px">
        <el-button type="primary" @click="handleAdd">新增</el-button>
      </div>
      <el-table :data="data.tableData" stripe>
        <el-table-column label="名称" prop="name"></el-table-column>
        <el-table-column label="图片" prop="img">
          <template #default="scope">
            <el-image :src="scope.row.img" style="width: 40px; height: 40px; border-radius: 20%" ></el-image>
          </template>
        </el-table-column>
        <el-table-column label="描述" prop="description"></el-table-column>
        <el-table-column label="特色" prop="specials"></el-table-column>
        <el-table-column label="单价" prop="price"></el-table-column>
        <el-table-column label="单位" prop="unit"></el-table-column>
        <el-table-column label="库存" prop="store"></el-table-column>
        <el-table-column label="种类" prop="categoryName"></el-table-column>
        <el-table-column label="操作" align="center" width="160">
          <template #default="scope">
            <el-button type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <el-pagination background layout="prev, pager, next" @current-change="load" v-model:page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
    </div>

    <el-dialog title="农产品信息" width="40%" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="data.form" label-width="100px" style="padding-right: 50px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="图片" prop="img">
          <el-upload :action="uploadUrl" list-type="picture" :on-success="handleImgSuccess" :headers="{ Authorization: data.token }">
            <el-button type="primary">上传图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="data.form.description" autocomplete="off" />
        </el-form-item>
        <el-form-item label="特色" prop="specials">
          <el-input v-model="data.form.specials" autocomplete="off" />
        </el-form-item>
        <el-form-item label="单价" prop="price">
          <el-input v-model="data.form.price" autocomplete="off" />
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="data.form.unit" autocomplete="off" />
        </el-form-item>
        <el-form-item label="库存" prop="store">
          <el-input v-model="data.form.store" autocomplete="off" />
        </el-form-item>
        <el-form-item label="种类" prop="categoryName">
          <el-select v-model="data.form.categoryName" placeholder="请选择分类" style="width: 240px">
            <el-option
                v-for="item in data.categoryList"
                :key="item.id"
                :label="item.name"
                :value="item.name"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="data.formVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">保 存</el-button>
      </span>
      </template>
    </el-dialog>

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
  categoryList: [],
  token: localStorage.getItem('token')
})

//获取所有分类
request.get('category/selectAll').then(res =>{
  data.categoryList = res.data;
})

// 分页查询
const load = () => {
  request.get('/seckill/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
    }
  }).then(res => {
    console.log("加载了...")
    data.tableData = res.data?.list
    data.total = res.data?.total
  })
}

// 新增
const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}

// 编辑
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

// 新增保存
const add = () => {
  request.post('/seckill/add', data.form).then(res => {
    if (res.code === '200') {
      load()
      ElMessage.success('操作成功')
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 编辑保存
const update = () => {
  request.put('/seckill/update', data.form).then(res => {
    if (res.code === '200') {
      load()
      ElMessage.success('操作成功')
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 弹窗保存
const save = () => {
  // data.form有id就是更新，没有就是新增
  data.form.id ? update() : add()
}

// 删除
const handleDelete = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/seckill/delete/' + id).then(res => {
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