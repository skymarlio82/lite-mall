<template>
  <div class="set_nickname">
    <navi-back :pageName="'user-information'"/>
    <van-cell-group>
      <van-field label="昵称" v-model="nickName"/>
    </van-cell-group>
    <div class="bottom_btn">
      <van-button @click="saveNick" size="large" type="danger">保存</van-button>
    </div>
  </div>
</template>

<script>
import {authProfile} from '@/api/api';
import {Field} from 'vant';
import NaviBack from '@/components/navi-back/';

export default {
  data() {
    return {
      nickName: ''
    };
  },
  created() {
    this.getNick();
  },
  methods: {
    getNick() {
      this.nickName = localStorage.getItem('nickName') || '';
    },
    saveNick() {
      if (true) {
        authProfile({nickname: this.nickName})
            .then(res => {
              localStorage.setItem('nickName', res.data.data.nickName);
              return this.$dialog.alert({message: '昵称保存成功'});
            })
            .then(() => {
              this.$router.go(-1);
            });
      }
    }
  },
  components: {
    [Field.name]: Field,
    [NaviBack.name]: NaviBack
  }
};
</script>

<style scoped>
.bottom_btn {
  padding: 30px 15px 0 15px;
}
</style>
