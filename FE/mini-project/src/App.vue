<script setup>
import { RouterLink } from 'vue-router'
</script>

<template>
  <header>
    <div class="wrapper">
      <nav>
        <RouterLink to="/studentList">Students</RouterLink>
        <RouterLink to="/create">Create</RouterLink>
      </nav>
    </div>
  </header>

  <router-view></router-view>
</template>

<style scoped>

</style>
