<template>
  <div>
    <van-nav-bar left-text="返回" left-arrow @click-left="goback"/>
  </div>
</template>

<script>
import { NavBar } from 'vant';

export default {
  name: 'navi-back',
  props: {
    pageName: {
      type: String,
      default: 'home'
    }
  },
  methods: {
    goback() {
      this.$router.push({name: this.pageName});
    }
  },
  components: {
    [NavBar.name]: NavBar
  }
};
</script>

<style>
</style>
