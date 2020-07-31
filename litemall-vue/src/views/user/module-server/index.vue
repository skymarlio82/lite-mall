<template>
  <div>
    <navi-back :pageName="'user'"/>
    <van-cell-group>
      <van-cell @click="showKefu=true" isLink title="联系客服"></van-cell>
      <van-cell isLink title="意见反馈" to="/user/feedback"></van-cell>
      <van-cell isLink title="常见问题" to="/user/help"/>
    </van-cell-group>
    <van-popup v-model="showKefu">
      <van-cell-group>
        <van-cell title="项目名称" value="litemall"/>
        <van-cell title="项目地址" url="https://github.com/linlinjava/litemall" value="Github"/>
        <van-cell title="项目地址" url="https://gitee.com/linlinjava/litemall" value="Gitee"/>
        <van-cell title="联系电话" value="021-xxxx-xxxx"/>
        <van-cell title="联系QQ" value="738696120"/>
        <van-cell title="当前版本" value="V1.0"/>
        <van-cell title="开源协议" value="MIT"/>
      </van-cell-group>
    </van-popup>
  </div>
</template>

<script>
import {Cell, CellGroup, Popup} from 'vant';
import NaviBack from '@/components/navi-back/';

export default {
  data() {
    return {
      showKefu: false
    };
  },
  components: {
    [Popup.name]: Popup,
    [Cell.name]: Cell,
    [CellGroup.name]: CellGroup,
    [NaviBack.name]: NaviBack
  }
};
</script>

<style lang="scss" scoped>
.van-popup {
  width: 80%;
  padding: 20px;
  box-sizing: border-box;
}
</style>
