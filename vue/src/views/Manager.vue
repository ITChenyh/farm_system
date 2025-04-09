<template>
  <div>
    <div style="height: 60px; background-color: #fff; display: flex; align-items: center; border-bottom: 1px solid #ddd">
      <div style="flex: 1">
        <div style="padding-left: 20px; display: flex; align-items: center">
          <img src="@/assets/imgs/logo.png" alt="" style="width: 40px">
          <div style="font-weight: bold; font-size: 24px; margin-left: 5px">阿强农场</div>
        </div>
      </div>
      <div style="width: fit-content; padding-right: 10px; display: flex; align-items: center;">
        <img style="width: 40px; height: 40px; border-radius: 50%" :src="data.user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" alt="">
        <span style="margin-left: 5px">{{ data.user.name }}</span>
      </div>
    </div>

    <div style="display: flex">
      <div style="width: 200px; border-right: 1px solid #ddd; min-height: calc(100vh - 60px)">
        <el-menu
            router
            style="border: none"
            :default-active="router.currentRoute.value.path"
            :default-openeds="['/home', '2']"
        >
          <el-menu-item index="/home">
            <el-icon><HomeFilled /></el-icon>
            <span>公告</span>
          </el-menu-item>
          <el-menu-item index="/purchase" v-if="data.user.role == 'USER'">
            <el-icon><Goods /></el-icon>
            <span>产品购买</span>
          </el-menu-item>
          <el-menu-item index="/purchaseSeckill" v-if="data.user.role == 'USER'">
            <el-icon><Goods /></el-icon>
            <span>产品秒杀</span>
          </el-menu-item>
          <el-menu-item index="/notice" v-if="data.user.role == 'ADMIN'">
            <el-icon><Bell /></el-icon>
            <span>公告管理</span>
          </el-menu-item>
          <el-sub-menu index="1" v-if="data.user.role == 'ADMIN'">
            <template #title>
              <el-icon><Memo /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/admin">
              <el-icon><User /></el-icon>
              <span>管理员信息</span>
            </el-menu-item>
            <el-menu-item index="/user">
              <el-icon><User /></el-icon>
              <span>用户信息</span>
            </el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="2" v-if="data.user.role == 'ADMIN'">
            <template #title>
              <el-icon><Menu /></el-icon>
              <span>农产品管理</span>
            </template>
            <el-menu-item index="/category">
              <el-icon><Menu /></el-icon>
              <span>农产品分类管理</span>
            </el-menu-item>
            <el-menu-item index="/goods">
              <el-icon><Goods /></el-icon>
              <span>农产品信息管理</span>
            </el-menu-item>
            <el-menu-item index="/goodsStock">
              <el-icon><GoodsFilled /></el-icon>
              <span>农产品进货管理</span>
            </el-menu-item>
            <el-menu-item index="/seckill">
              <el-icon><Goods /></el-icon>
              <span>秒杀产品信息管理</span>
            </el-menu-item>
          </el-sub-menu>
          <!--TODO: index-->
          <el-menu-item index="/orders" v-if="data.user.role == 'USER'">
            <el-icon><Sell /></el-icon>
            <span>我的订单</span>
          </el-menu-item>
          <!--TODO: index-->
          <el-menu-item index="/orders" v-if="data.user.role == 'ADMIN'">
            <el-icon><SoldOut /></el-icon>
            <span>订单详情</span>
          </el-menu-item>
          <el-menu-item index="/person">
            <el-icon><User /></el-icon>
            <span>个人资料</span>
          </el-menu-item>
          <el-menu-item index="/password">
            <el-icon><Lock /></el-icon>
            <span>修改密码</span>
          </el-menu-item>
          <el-menu-item index="login" @click="logout">
            <el-icon><SwitchButton /></el-icon>
            <span>退出系统</span>
          </el-menu-item>
        </el-menu>
      </div>

      <div style="flex: 1; width: 0; background-color: #f8f8ff; padding: 10px">
        <router-view @updateUser="updateUser" />
      </div>
    </div>

  </div>
</template>

<script setup>
import { reactive } from "vue";
import router from "@/router";
import {ElMessage} from "element-plus";
import request from "@/utils/request";

const login = () => {
  formRef.value.validate((valid => {
    if (valid) {
      // 调用后台的接口
      request.post('/login', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success("登录成功")
          console.log("取消JSON.stringify" + res.data)
          localStorage.setItem('user-info', res.data)
          router.push('/')
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })).catch(error => {
    console.error(error)
  })
}

const data = reactive({
  user : JSON.parse(localStorage.getItem('user-info') || '{}')
})

if (!data.user?.id) {
  ElMessage.error('请登录！')
  router.push('/login')
}

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('user-info') || '{}')
}

const logout = () => {
  ElMessage.success('退出成功')
  localStorage.removeItem('user-info')
  router.push('/login')
}
</script>

<style scoped>
.el-menu-item.is-active {
  background-color: #e0edfd !important;
}
.el-menu-item:hover {
  color: #1967e3;
}
:deep(th)  {
  color: #333;
}
</style>