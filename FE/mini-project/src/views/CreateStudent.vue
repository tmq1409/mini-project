<template>
  <div class="form-container">
    <h2 class="form-title">Tạo Sinh Viên Mới</h2>

    <el-form
  ref="studentForm"
  :model="form"
  :rules="rules"
  label-position="top"
  @submit.prevent
>
  <el-form-item label="Họ" prop="firstName">
    <el-input v-model="form.firstName" />
  </el-form-item>

  <el-form-item label="Tên" prop="lastName">
    <el-input v-model="form.lastName" />
  </el-form-item>

  <el-form-item label="Ngày Sinh" prop="dateOfBirth">
    <el-date-picker
      v-model="form.dateOfBirth"
      type="date"
      placeholder="Chọn ngày sinh"
      format="YYYY-MM-DD"
      value-format="YYYY-MM-DD"
      style="width: 100%"
    />
  </el-form-item>

  <el-form-item label="Email" prop="email">
    <el-input v-model="form.email" type="email" />
  </el-form-item>

  <el-form-item label="Số điện thoại" prop="phoneNumber">
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
import axios from 'axios';
import { ElMessage } from 'element-plus';
import { reactive, ref, watch } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const form = reactive({
  firstName: '',
  lastName: '',
  dateOfBirth: '',
  email: '',
  phoneNumber: '',
  avatar: null
});

const rules = {
  firstName: [
    { required: true, message: 'Họ không được bỏ trống', trigger: 'blur' }
  ],
  lastName: [
    { required: true, message: 'Tên không được bỏ trống', trigger: 'blur' }
  ],
  dateOfBirth: [
    { required: true, message: 'Ngày sinh không được bỏ trống', trigger: 'change' }
  ],
  email: [
    { required: true, message: 'Email không được bỏ trống', trigger: 'blur' },
    { type: 'email', message: 'Email không hợp lệ', trigger: ['blur', 'change'] }
  ],
  phoneNumber: [
    { required: true, message: 'Số điện thoại không được bỏ trống', trigger: 'blur' },
    {
      pattern: /^\d{10}$/,
      message: 'Số điện thoại phải đúng 10 chữ số',
      trigger: ['blur', 'change']
    }
  ]
};


const avatarFile = ref(null);
const avatarPreview = ref(null);

const onFileChange = (uploadFile) => {
  avatarFile.value = uploadFile.raw;
  avatarPreview.value = URL.createObjectURL(uploadFile.raw);
};

const studentForm = ref();

const submitForm = () => {
  studentForm.value.validate(async (valid) => {
    if (!valid) {
      ElMessage.error("Vui lòng sửa các lỗi trong biểu mẫu.");
      return;
    }

    const formData = new FormData();
    formData.append("firstName", form.firstName);
    formData.append("lastName", form.lastName);
    formData.append("dateOfBirth", form.dateOfBirth);
    formData.append("email", form.email);
    formData.append("phoneNumber", form.phoneNumber);
    if (avatarFile.value) {
      formData.append("avatar", avatarFile.value);
    }

    try {
      await axios.post("http://localhost:8079/api/v1/students", formData, {
        headers: { "Content-Type": "multipart/form-data" }
      });
      ElMessage.success("Tạo sinh viên thành công!");
      router.push("/student");
    } catch (err) {
      ElMessage.error(err.response?.data?.message || "Tạo sinh viên thất bại!");
    }
  });
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
