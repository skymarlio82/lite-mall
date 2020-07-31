<template>
  <div>
    <van-nav-bar @click-left="goback" left-arrow left-text="返回" title="收货地址"/>
    <van-address-list :list="addressList" @add="onAdd" @edit="onEdit" @select="onSelect" v-model="chosenAddressId"/>
  </div>
</template>

<script>
import {addressList} from '@/api/api';
import {AddressList, NavBar} from 'vant';
import {setLocalStorage} from '@/utils/local-storage';

export default {
  data() {
    return {
      chosenAddressId: -1,
      addressList: []
    };
  },
  created() {
    this.loadAddress();
  },
  methods: {
    onAdd() {
      this.$router.push({name: 'address-edit', query: {addressId: -1}});
    },
    onEdit(item, index) {
      this.$router.push({name: 'address-edit', query: {addressId: item.id}});
    },
    onSelect(item, index) {
      setLocalStorage({AddressId: item.id});
    },
    goback() {
      // this.$router.go(-1);
      this.$router.push({name: 'user'});
    },
    loadAddress() {
      addressList().then(res => {
        var list = res.data.data.list;
        for (var i = 0; i < list.length; i++) {
          var item = list[i];
          this.addressList.push({
            id: item.id,
            name: item.name,
            tel: item.tel,
            address: item.province + item.city + item.county + " " + item.addressDetail
          });
        }
      });
    }
  },
  components: {
    [NavBar.name]: NavBar,
    [AddressList.name]: AddressList
  }
};
</script>

<style lang="scss" scoped>
.addressGroup {
  margin-bottom: 10px;

  &:last-child {
    margin-bottom: 0;
  }
}

.bottom_btn {
  position: fixed;
  bottom: 0;
}
</style>
