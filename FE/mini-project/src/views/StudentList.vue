<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import axios from 'axios';
import { ElMessageBox, ElMessage } from 'element-plus'
import { useRouter } from 'vue-router';

const router = useRouter();

const students = ref([]);
const searchText = ref('');
const currentPage = ref(0);
const pageSize = ref(5);
const totalPages = ref(0);

const getStudents = async() => {
    try {
        const response = await axios.get('http://localhost:8079/api/v1/students', {
        params: {
            page: currentPage.value,
            size: pageSize.value,
            keyword: searchText.value || ''
        } 
    });
    students.value = response.data.content;
    totalPages.value = response.data.totalPages;
    } catch (error) {
        console.error('Lỗi khi gọi API:', error);
    }
}

onMounted(getStudents);
watch(searchText, getStudents);

const searchStudents = () => {
  currentPage.value = 0;
  getStudents();
};

const redirectToCreate = () => {
  router.push('/student/create');
};

const confirmDelete = async (id) => {
  try {
    await ElMessageBox.confirm(
      'Bạn có chắc chắn muốn xóa sinh viên này?',
      'Xác nhận xóa',
      {
        confirmButtonText: 'Xóa',
        cancelButtonText: 'Hủy',
        type: 'warning',
      }
    );

    await axios.delete(`http://localhost:8079/api/v1/students/${id}`);
    ElMessage.success('Xóa sinh viên thành công');
    await getStudents();
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error);
      ElMessage.error('Xóa thất bại');
    }
  }
};

const handlePageChange = (page) => {
  currentPage.value = page - 1;
  getStudents();
};
</script>

<template>
  <div class="container">
    <h1 class="title">Danh sách sinh viên</h1>

    <div class="search-box">
      <el-row :gutter="10" class="search-row">
        <el-col :span="18">
          <el-input
            v-model="searchText"
            placeholder="Tìm sinh viên theo tên, sđt, email..."
            clearable
          />
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="searchStudents">Tìm</el-button>
        </el-col>
      </el-row>
      <el-button type="success" @click="redirectToCreate">Thêm mới</el-button>
    </div>

    <div class="table-container">
      <el-table :data="students" style="width: 100%">
        <el-table-column label="Họ Tên" prop="fullName">
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
            <el-button type="danger" @click="confirmDelete(row.id)">Xóa</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-pagination
        layout="prev, pager, next"
        :total="totalPages * pageSize"
        :page-size="pageSize"
        :current-page="currentPage + 1"
        @current-change="handlePageChange"
        background
        class="pagination"
    />
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
  color: #2c3e50; /* Màu đậm sang trọng */
  margin-bottom: 30px;
  position: relative;
  padding-bottom: 12px;
}

.title::after {
  content: '';
  width: 80px;
  height: 4px;
  background-color: #409eff; /* Màu chính của Element Plus */
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

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 16px;
  gap: 16px;
}

.pagination button {
  padding: 6px 12px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.pagination button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>
