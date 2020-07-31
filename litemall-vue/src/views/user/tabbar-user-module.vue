<template>
  <div class="user_module">
    <van-cell-group>
      <van-cell icon="shoucang" isLink title="我的收藏" to="/user/collect"/>
      <van-cell icon="dingwei" isLink title="收货地址" to="/user/address"/>
      <van-cell icon="kefu" isLink title="服务中心" to="/user/server"/>
    </van-cell-group>
  </div>
</template>

<script>
export default {
  name: 'user-module'
};
</script>

<style lang="scss" scoped>
.user_module {
  background-color: #fff;
}
</style>
