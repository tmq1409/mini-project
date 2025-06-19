<template>
  <div class="base-table-wrapper">
    <el-table :data="data" v-loading="loading" style="width: 100%">
      <slot />
    </el-table>

    <div class="pagination-wrapper">
    

      <el-pagination
        layout="sizes, prev, pager, next"
        :total="total"
        :page-size="pageSize"
        :current-page="currentPage"
        @size-change="onPageSizeChange"
        @current-change="onPageChange"
        background
        class="pagination"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';

const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const data = ref([]);

const loading = ref(false);

const props = defineProps({
  getData: { type: Function, required: true },
});

watch(() => props.searchKey, () => {
  currentPage.value = 1;
  fetchData();
});

const fetchData = async () => {
  loading.value = true;
  try {
    const res = await props.getData(currentPage.value, pageSize.value);
    data.value = res.content;
    total.value = res.totalElements || res.total || 0;
  } catch (e) {
    alert('Loi load data');
  } finally {
    loading.value = false;
  }
};

const onPageChange = (page) => {
  currentPage.value = page;
};

const onPageSizeChange = (size) => {
  pageSize.value = size;
  currentPage.value = 1;
};


watch([currentPage, pageSize], fetchData);
onMounted(fetchData);

defineExpose({fetchData});
</script>

<style scoped>
.base-table-wrapper {
  width: 100%;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 16px;
  margin-bottom: 16px;
  flex-wrap: wrap;
  gap: 12px;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
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
