import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: () => import('@/views/Manager.vue'),
      redirect: '/home',
      children: [
        { path: 'person', component: () => import('@/views/manager/Person.vue')},
        { path: 'password', component: () => import('@/views/manager/Password.vue')},
        { path: 'home', component: () => import('@/views/manager/Home.vue')},
        { path: 'admin', component: () => import('@/views/manager/Admin.vue')},
        { path: 'notice', component: () => import('@/views/manager/Notice.vue')},
        { path: 'category', component: () => import('@/views/manager/Category.vue')},
        { path: 'goods', component: () => import('@/views/manager/Goods.vue')},
        { path: 'goodsStock', component: () => import('@/views/manager/GoodsStock.vue')},
        { path: 'user', component: () => import('@/views/manager/User.vue')},
        { path: 'purchase', component: () => import('@/views/manager/Purchase.vue')},
        { path: 'orders', component: () => import('@/views/manager/Orders.vue')},
        { path: 'seckill', component: () => import('@/views/manager/Seckill.vue')},
        { path: 'purchaseSeckill', component: () => import('@/views/manager/purchaseSeckill.vue')},
      ]
    },
    { path: '/login', component: () => import('@/views/Login.vue')},
    { path: '/register', component: () => import('@/views/Register.vue')},
    { path: '/register', component: () => import('@/views/Register.vue')},

  ]
})

export default router
