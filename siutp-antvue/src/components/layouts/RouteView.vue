<!-- 路由缓存页面 -->
<template>
  <div class="main">
    <keep-alive>
      <router-view v-if="keepAlive" />
    </keep-alive>
    <router-view v-if="!keepAlive" />
  </div>
</template>

<script>
import Vue from 'vue'
export default {
  name: "RouteView",
  computed: {
    keepAlive() {
      console.log(this.$route);
      return this.$route.meta.keepAlive;
    },
  },
};
</script>