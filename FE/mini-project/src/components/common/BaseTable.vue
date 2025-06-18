<template>
  <div class="base-table-wrapper">
    <el-table :data="data" style="width: 100%">
      <slot />
    </el-table>

    <el-pagination
      layout="prev, pager, next"
      :total="total"
      :page-size="pageSize"
      :current-page="currentPage"
      @current-change="onPageChange"
      background
      class="pagination"
    />
  </div>
</template>

<script setup>
const props = defineProps({
  data: Array,
  total: Number,
  pageSize: Number,
  currentPage: Number
});

const emit = defineEmits(['page-change']);

const onPageChange = (page) => {
  emit('page-change', page);
};
</script>

<style scoped>
.base-table-wrapper {
  width: 100%;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 16px;
  gap: 16px;
  margin-bottom: 16px;
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
