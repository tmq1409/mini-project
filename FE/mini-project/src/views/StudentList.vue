<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import BaseTable from '@/components/common/BaseTable.vue';
import MessageBox from '@/utils/MessageBox.vue';

const router = useRouter();
const baseTableRef = ref();
const searchText = ref('');
const selectedId = ref(null);
const confirmDelete = ref();

const showConfirmDelete = (id) => {
  selectedId.value = id;
  confirmDelete.value.execute(); 
};

const getStudents = async (page, size) => {
  const response = await axios.get('http://localhost:8079/api/v1/students', {
    params: {
      page: page - 1,
      size,
      keyword: searchText.value || ''
    }
  });

  return {
    content: response.data.content,
    total: response.data.totalElements
  };
};

const redirectToCreate = () => {
  router.push('/student/create');
};

watch(searchText, () => {
  baseTableRef.value?.fetchData();
});
</script>

<template>
  <div class="container">
    <h1 class="title">Danh sách sinh viên</h1>
          <MessageBox
            ref="confirmDelete"
            :title="'Xác nhận xóa'"
            :message="'Bạn có chắc chắn muốn xóa sinh viên này?'"
            :type="'warning'"
            :onConfirm="() => axios.delete(`http://localhost:8079/api/v1/students/${selectedId}`)"
            @success="getStudents"
            />
    <div class="search-box">
      <el-row :gutter="10" class="search-row">
        <el-col :span="20">
          <el-input
            v-model="searchText"
            placeholder="Tìm sinh viên theo tên, sđt, email..."
            clearable
          />
        </el-col>
      </el-row>
      <el-button type="success" @click="redirectToCreate">Thêm mới</el-button>
    </div>

    <div class="table-container">
      <BaseTable ref="baseTableRef" :get-data="getStudents" >
        <el-table-column label="Họ Tên">
          <template #default="{ row }">
            {{ row.firstName }} {{ row.lastName }}
          </template>
        </el-table-column>

        <el-table-column label="Ngày Sinh" prop="dateOfBirth" />

        <el-table-column label="SĐT" prop="phoneNumber" />

        <el-table-column label="Email" prop="email" min-width="150" />

        <el-table-column label="Avatar">
          <template #default="{ row }">
            <img :src="row.avatar" class="avatar" />
          </template>
        </el-table-column>

        <el-table-column label="Hành động">
          <template #default="{ row }">
            <el-button type="danger" @click="showConfirmDelete(row.id)">Xóa</el-button>
          </template>
        </el-table-column>
      </BaseTable>
    </div>

  </div>
</template>

<style scoped>
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px;
  font-family: sans-serif;
}

.title {
  text-align: center;
  font-size: 32px;
  font-weight: bold;
  color: #2c3e50;
  margin-bottom: 30px;
  position: relative;
  padding-bottom: 12px;
}

.title::after {
  content: '';
  width: 80px;
  height: 4px;
  background-color: #409eff;
  display: block;
  margin: 10px auto 0;
  border-radius: 2px;
}

.search-box {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.search-row {
  width: 100%;
  max-width: 600px;
}

.search-input {
  width: 100%;
  max-width: 400px;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 8px;
}

.table-container {
  overflow-x: auto;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
}

</style>
