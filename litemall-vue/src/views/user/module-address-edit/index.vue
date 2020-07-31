<template>
  <div>
    <van-nav-bar @click-left="goback" left-arrow left-text="返回" title="编辑地址"/>
    <van-address-edit
        :addressInfo="addressInfo"
        :areaList="areaList"
        @delete="onDelete"
        @save="onSave"
        show-delete
        show-set-default
        style="background-color: #fff;"
    />
  </div>
</template>

<script>
import {AddressEdit, NavBar} from 'vant';
import areaList from './area.json';
import {addressDelete, addressDetail, addressSave} from '@/api/api';
import {removeLocalStorage} from '@/utils/local-storage';

export default {
  name: 'address-edit',
  data: () => ({
    areaList,
    addressId: 0,
    addressInfo: {}
  }),
  created() {
    this.addressId = this.$route.query.addressId;
    if (this.addressId !== -1 && this.addressId !== 0) {
      this.init();
    }
  },
  methods: {
    init() {
      addressDetail({id: this.addressId}).then(res => {
        this.addressInfo = res.data.data;
      });
    },
    onSave(content) {
      addressSave(content).then(res => {
        this.$toast('成功');
        this.$router.push({name: 'address'});
      });
    },
    onDelete(content) {
      addressDelete({id: content.id});
      removeLocalStorage('AddressId');
      this.$router.push({name: 'address'});
    },
    goback() {
      // this.$router.go(-1);
      this.$router.push({name: 'address'});
    }
  },
  components: {
    [NavBar.name]: NavBar,
    [AddressEdit.name]: AddressEdit
  }
};
</script>
