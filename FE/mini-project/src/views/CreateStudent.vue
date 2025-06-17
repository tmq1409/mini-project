<template>
  <div class="form-container">
    <h2 class="form-title">Tạo Sinh Viên Mới</h2>

    <el-form @submit.prevent="submitForm" label-position="top" :model="form">
      <el-form-item label="Họ" required>
        <el-input v-model="form.firstName" />
      </el-form-item>

      <el-form-item label="Tên" required>
        <el-input v-model="form.lastName" />
      </el-form-item>

      <el-form-item label="Ngày Sinh" required>
        <el-date-picker
          v-model="form.dateOfBirth"
          type="date"
          placeholder="Chọn ngày sinh"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="Email" required>
        <el-input v-model="form.email" type="email" />
      </el-form-item>

      <el-form-item label="Số điện thoại" required>
        <el-input v-model="form.phoneNumber" />
      </el-form-item>

      <el-form-item label="Avatar">
        <el-upload
          class="upload-demo"
          :auto-upload="false"
          :show-file-list="false"
          accept="image/*"
          :on-change="onFileChange"
        >
          <el-button type="primary">Chọn ảnh</el-button>
        </el-upload>
        <div v-if="avatarPreview" class="preview">
          <img :src="avatarPreview" alt="Avatar Preview" />
        </div>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm">Tạo Sinh Viên</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

const form = ref({
  firstName: '',
  lastName: '',
  dateOfBirth: '',
  email: '',
  phoneNumber: '',
});

const avatarFile = ref(null);
const avatarPreview = ref(null);

const onFileChange = (uploadFile) => {
  avatarFile.value = uploadFile.raw;
  avatarPreview.value = URL.createObjectURL(uploadFile.raw);
};

const submitForm = async () => {
  const formData = new FormData();
  formData.append('firstName', form.value.firstName);
  formData.append('lastName', form.value.lastName);
  formData.append('dateOfBirth', form.value.dateOfBirth);
  formData.append('email', form.value.email);
  formData.append('phoneNumber', form.value.phoneNumber);
  if (avatarFile.value) {
    formData.append('avatar', avatarFile.value);
  }

  try {
    await axios.post('http://localhost:8079/api/v1/students', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    ElMessage.success('Tạo sinh viên thành công!');
  } catch (err) {
    if (err.response?.data?.message) {
      ElMessage.error(`${err.response.data.message}`);
    } else {
      ElMessage.error('Tạo sinh viên thất bại!');
    }
    console.error(err);
  }
};
</script>

<style scoped>
.form-container {
  max-width: 500px;
  margin: auto;
  margin-top: 100px;
  padding: 30px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-family: sans-serif;
  background-color: #fafafa;
}

.form-title {
  text-align: center;
  font-size: 26px;
  font-weight: 600;
  color: #34495e;
  margin-bottom: 24px;
  position: relative;
  padding-bottom: 10px;
}

.form-title::after {
  content: '';
  width: 60px;
  height: 3px;
  background-color: #409eff ; 
  display: block;
  margin: 8px auto 0;
  border-radius: 2px;
}

.preview {
  margin-top: 8px;
  margin-left: 20px;
}

.preview img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 50%;
  border: 1px solid #aaa;
}
</style>
