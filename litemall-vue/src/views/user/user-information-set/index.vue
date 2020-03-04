<template>
  <div class="user_information">
    <van-cell-group>
      <van-cell title="头像" class="cell_middle">
        <van-uploader :before-read="avatarBeforeRead" :afterRead="avatarAfterRead">
          <div class="user_avatar_upload">
            <!-- <img :src="avatar + '?x-oss-process=image/resize,m_fill,h_50,w_50'" alt="你的头像" v-if="avatar"> -->
            <img :src="avatar" alt="你的头像" width="50" height="50" v-if="avatar">
            <van-icon name="camera_full" v-else></van-icon>
          </div>
        </van-uploader>
      </van-cell>
      <van-cell title="昵称" to="/user/information/setNickname" :value="nickName" isLink/>
      <van-cell title="性别" :value="genderText" @click="showSex = true" isLink/>
      <van-cell title="密码" to="/user/information/setPassword" isLink/>
      <van-cell title="手机" to="/user/information/setMobile" :value="mobile" isLink/>
    </van-cell-group>
    <van-button size="large" class="user_quit" @click="loginOut">退出</van-button>
    <van-popup v-model="showSex" position="bottom">
      <van-picker showToolbar :columns="sexColumns" title="选择性别" @cancel="showSex = false" @confirm="onSexConfirm" />
    </van-popup>
  </div>
</template>

<script>
import { Uploader, Picker, Popup, Button } from 'vant';
import { getLocalStorage, setLocalStorage, removeLocalStorage } from '@/utils/local-storage';
import { authInfo, authLogout, authProfile } from '@/api/api';

export default {
  data() {
    return {
      sexColumns: [
        {
          values: ['保密', '男', '女'],
          defaultIndex: 0
        }
      ],
      showSex: false,
      avatar: '',
      nickName: '',
      gender: 0,
      mobile: ''
    };
  },
  computed: {
    genderText() {
      const text = ['保密', '男', '女'];
      return text[this.gender] || '';
    }
  },
  created() {
    this.getUserInfo();
  },
  methods: {
    avatarBeforeRead(file) {
      return new Promise((resolve, reject) => {
        if (file.type !== 'image/png') {
          this.$dialog.alert({ message: '上传图片格式PNG' });
          reject();
        } else if (file.size > 10240) {
          this.$dialog.alert({ message: '图片SIZE不可大于10K' });
          reject();
        } else {
          resolve();
        }
      });
    },
    avatarAfterRead(file) {
      authProfile({
        avatar: file.content,
      }).then(res => {
        setLocalStorage({ avatar: file.content });
        this.avatar = file.content;
        return this.$dialog.alert({ message: '头像保存成功' });
      });
    },
    onSexConfirm(value, index) {
      authProfile({
        gender: index[0],
      }).then(res => {
        this.gender = index[0];
        this.showSex = false;
        return this.$dialog.alert({ message: '性别保存成功' });
      });
    },
    getUserInfo() {
      authInfo().then(res => {
        this.avatar = res.data.data.avatar;
        this.nickName = res.data.data.nickName;
        this.gender = res.data.data.gender;
        this.mobile = res.data.data.mobile;
      });
    },
    loginOut() {
      authLogout().then(res => {
        removeLocalStorage('Authorization');
        removeLocalStorage('avatar');
        removeLocalStorage('nickName');
        this.$router.push({ name: 'home' });
      });
    }
  },
  components: {
    [Button.name]: Button,
    [Uploader.name]: Uploader,
    [Picker.name]: Picker,
    [Popup.name]: Popup
  }
};
</script>

<style lang="scss" scoped>
.user_information {
  .user_avatar_upload {
    position: relative;
    width: 50px;
    height: 50px;
    border: 1px solid $border-color;
    img {
      max-width: 100%;
      max-height: 100%;
    }
    i {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      font-size: 20px;
      color: $border-color;
    }
  }
  .user_quit {
    margin-top: 20px;
  }
}
</style>
