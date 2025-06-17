<script setup>
import { RouterLink } from 'vue-router'
</script>

<template>
  <header>
    <div class="wrapper">
      <nav>
        <RouterLink to="/student">Students</RouterLink>
      </nav>
    </div>
  </header>

  <router-view></router-view>
</template>

<style scoped>

</style>
