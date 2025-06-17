import { createRouter, createWebHistory } from 'vue-router'
import StudentList from '@/views/StudentList.vue'
import CreateStudent from '@/views/CreateStudent.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/student'
    },
    {
      path: '/student',
      name: 'studentList',
      component: StudentList
    },
    {
      path: '/student/create',
      name: 'create',
      component: CreateStudent
    },
  ],
})

export default router
