<template>
  <div class="form-container">
    <h2 class="form-title">Tạo Sinh Viên Mới</h2>

    <el-form @submit.prevent="submitForm" label-position="top" :model="form">
      <el-form-item label="Họ" :error="errors.firstName">
        <el-input v-model="form.firstName" />
      </el-form-item>

      <el-form-item label="Tên" :error="errors.lastName">
        <el-input v-model="form.lastName" />
      </el-form-item>

      <el-form-item label="Ngày Sinh" :error="errors.dateOfBirth">
        <el-date-picker
          v-model="form.dateOfBirth"
          type="date"
          placeholder="Chọn ngày sinh"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          style="width: 100%"
      />
      </el-form-item>

      <el-form-item label="Email" :error="errors.email">
          <el-input v-model="form.email" type="email" />  
      </el-form-item>

      <el-form-item label="Số điện thoại" :error="errors.phoneNumber">
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

const errors = reactive({
  firstName: '',
  lastName: '',
  dateOfBirth: '',
  email: '',
  phoneNumber: ''
});

//#region  validate
watch(() => form.firstName, (value) => {
  errors.firstName = value.trim() ? '' : 'Họ không được bỏ trống';
});

watch(() => form.lastName, (value) => {
  errors.lastName = value.trim() ? '' : 'Tên không được bỏ trống';
});

watch(() => form.dateOfBirth, (value) => {
  errors.dateOfBirth = value ? '' : 'Ngày sinh không được bỏ trống';
});

watch(() => form.email, (value) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!value.trim()) {
    errors.email = 'Email không được bỏ trống';
  } else if (!emailRegex.test(value)) {
    errors.email = 'Email không hợp lệ';
  } else {
    errors.email = '';
  }
});

watch(() => form.phoneNumber, (value) => {
  const phoneRegex = /^\d{10}$/;
  if (!value.trim()) {
    errors.phoneNumber = 'Số điện thoại không được bỏ trống';
  } else if (!phoneRegex.test(value)) {
    errors.phoneNumber = 'Số điện thoại phải có đúng 10 chữ số';
  } else {
    errors.phoneNumber = '';
  }
});
//#endregion

const avatarFile = ref(null);
const avatarPreview = ref(null);

const onFileChange = (uploadFile) => {
  avatarFile.value = uploadFile.raw;
  avatarPreview.value = URL.createObjectURL(uploadFile.raw);
};

const submitForm = async () => {
  form.firstName = form.firstName;
  form.lastName = form.lastName;
  form.dateOfBirth = form.dateOfBirth;
  form.email = form.email;
  form.phoneNumber = form.phoneNumber;

  const hasError = Object.values(errors).some((err) => err);
  if (hasError) {
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
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    ElMessage.success("Tạo sinh viên thành công!");
    router.push('/student')
  } catch (err) {
    if (err.response?.data?.message) {
      ElMessage.error(`${err.response.data.message}`);
    } else {
      ElMessage.error("Tạo sinh viên thất bại!");
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
