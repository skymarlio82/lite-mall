<template>
  <div>
    <navi-back :pageName="'user-information'"/>
    <van-cell-group>
      <van-field label="新密码" placeholder="请输入新密码" type="password" v-model="password"/>
      <van-field @click-icon="getCode" label="验证码" placeholder="请输入验证码" v-model="code">
				<span :class="{verifi_code_counting: counting}" @click="getCode" class="verifi_code red" slot="right-icon">
					<countdown :time="60000" @countdownend="countdownend" v-if="counting">
					  <template slot-scope="props">{{ +props.seconds || 60 }}秒后获取</template>
					</countdown>
					<span v-else>获取验证码</span>
				</span>
      </van-field>
    </van-cell-group>
    <div class="bottom_btn">
      <van-button @click="modifypassword" size="large" type="danger">保存</van-button>
    </div>
  </div>
</template>

<script>
import {authCaptcha, authLogout, authReset} from '@/api/api';
import {Field} from 'vant';
import NaviBack from '@/components/navi-back/';

export default {
  data: () => ({
    password: '',
    mobile: '',
    code: '',
    counting: false
  }),
  methods: {
    modifypassword() {
      if (this.passwordValid()) {
        authReset({
          password: this.password,
          mobile: this.mobile,
          code: this.code
        }).then(() => {
          this.$dialog.alert({message: '保存成功, 请重新登录.'});
          authLogout();
        });
      }
    },
    passwordValid() {
      return true;
    },
    getCode() {
      if (this.mobile === '') {
        this.$toast.fail('请输入号码');
        return;
      }
      if (!this.counting) {
        authCaptcha({
          mobile: this.mobile,
          type: 'change-password'
        }).then(() => {
          this.$toast.success('发送成功');
          this.counting = true;
        }).catch(error => {
          this.$toast.fail(error.data.errmsg);
          this.counting = false;
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

<style lang="scss" scoped>
@import '../../../../assets/scss/var';
@import '../../../../assets/scss/mixin';

.bottom_btn {
  padding: 30px 15px 0 15px;
}

.verifi_code {
  @include one-border;
  padding-left: 10px;

  &::after {
    border-bottom: 0;
    border-left: 1px solid $border-color;
  }

  &_counting {
    color: $font-color-gray;
  }
}
</style>
