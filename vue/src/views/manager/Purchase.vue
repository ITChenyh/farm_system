<template>
  <div>

    <div class="card" style="margin-bottom: 5px;">
      <el-button :type="(data.activeCategoryId === null) ? 'primary' : 'default'" @click="loadCategoryGoods(null)">全部</el-button>
      <el-button :type="(data.activeCategoryId === item.id) ? 'primary' : 'default'" @click="loadCategoryGoods(item.id)" v-for="item in data.categoryList" :key="item.id">
        {{item.name}}</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.name" style="width: 300px; margin-right: 10px" placeholder="请输入农产品名称查询"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="info" style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <el-row :gutter="10">
      <el-col :span="6" v-for="item in data.tableData" :key="item.id">
        <div class="card">
          <el-row :gutter="5">
            <el-col :span="12">
              <img :src="item.img" alt="" style="width: 100%; height: 200px">
            </el-col>
            <el-col :span="12">
              <div style="font-size: 25px; margin: 50px 10px 5px 10px; color: #333">
                {{item.name}}
                <el-tooltip v-if="item.description.length > 40" :content="item.description" effect="light" placement="top">
                  <div style="margin: 5px 0; color: #666; font-size: 14px; height: 40px">{{ item.description }}</div>
                </el-tooltip>
                <div v-else style="margin: 10px 0px 5px 0; color: #666; font-size: 16px; height: 20px;">{{ item.description }}</div>
              </div>
            </el-col>
          </el-row>
          <div style="margin: 3px 3px 0 0">
            <el-tag type="success">{{ item.specials }}</el-tag>
          </div>
          <div style="margin: 10px 0; display: flex; align-items: center; color: #666">
            <div style="flex: 1">
              <strong style="color: red; font-size: 18px">￥{{ item.price }}</strong>/{{ item.unit }}
            </div>
            <div style="flex:1; text-align: center">库存:{{ item.store }}</div>
            <el-input-number @change="handleCal(item)" v-model="item.num" style="width: 120px" :min="0" :max="item.store"></el-input-number>
          </div>
          <div style="display: flex; align-items: center; justify-content: flex-end" v-if="item.num > 0">
            总价: <strong style="margin-right: 5px; display: inline-block; min-width: 50px; font-size: 18px; color: red">￥{{ item.total }}</strong>
            <el-button type="primary" @click="buy(item)">购买</el-button>
          </div>
        </div>
      </el-col>
    </el-row>


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
  pageSize: 8,
  total: 0,
  formVisible: false,
  form: {},
  tableData: [],
  name: null,
  categoryList: [],
  activeCategoryId : null,
  user: JSON.parse(localStorage.getItem('user-info') || '{}')
})

const loadCategoryGoods = (id) => {
  data.activeCategoryId = id;
  load()
}

//获取所有分类
request.get('category/selectAll').then(res =>{
  data.categoryList = res.data;
})

// 分页查询
const load = () => {
  request.get('/goods/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      categoryId: data.activeCategoryId
    }
  }).then(res => {
    console.log(res.data)
    data.tableData = res.data?.list
    data.total = res.data?.total

  })
}

// 计算总价
const handleCal= (item) => {
  item.total = (item.price * item.num).toFixed(2);
}

// 购买
const buy= (item) => {
  let order = {goodsId: item.id, num: item.num, userId: data.user.id, status: "待支付"}
  request.post('orders/add', order).then(res => {
    if (res.code === '200') {
      load()
      ElMessage.success('购买成功')
    } else {
      ElMessage.error(res.msg)
    }
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
  request.post('/goods/add', data.form).then(res => {
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
  request.put('/goods/update', data.form).then(res => {
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
    request.delete('/goods/delete/' + id).then(res => {
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