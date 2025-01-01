<template>
  <div>系统公告</div>
  <el-timeline style="max-width: 600px">
    <el-timeline-item center :timestamp="item.time" placement="top" v-for="item in data.noticeList">
      <el-card>
        <h4 style="color: #ff0000; font-family: '黑体'; font-size: 30px">{{ item.title }}</h4>
        <p>{{ item.content }}</p>
      </el-card>
    </el-timeline-item>
  </el-timeline>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request";

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
  noticeList: []
})

request.get('notice/selectAll').then(res =>{
  data.noticeList = res.data
})
</script>