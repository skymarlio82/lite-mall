<template>
  <div>
    <van-cell-group>
      <van-cell isLink title="我的订单">
        <router-link class="text-desc" to="/user/order/list/0">全部订单</router-link>
      </van-cell>
    </van-cell-group>
    <van-row class="order_status">
      <van-col span="6">
        <div @click="$router.push({path: '/user/order/list/1'})" class="order_status_icon">
          <van-icon :info="order.unpaid > 0 ? order.unpaid : ''" name="daifukuan"/>
        </div>
        <div>待付款</div>
      </van-col>
      <van-col span="6">
        <div @click="$router.push({path: '/user/order/list/2'})" class="order_status_icon">
          <van-icon :info="order.unship > 0 ? order.unship : ''" name="daifahuo"/>
        </div>
        <div>待发货</div>
      </van-col>
      <van-col span="6">
        <div @click="$router.push({path: '/user/order/list/3'})" class="order_status_icon">
          <van-icon :info="order.unrecv > 0 ? order.unrecv : ''" name="wuliu"/>
        </div>
        <div>待收货</div>
      </van-col>
      <van-col span="6">
        <div @click="$router.push({path: '/user/order/list/4'})" class="order_status_icon">
          <van-icon :info="order.uncomment > 0 ? order.uncomment : ''" name="shouhouguanli"/>
        </div>
        <div>已完成</div>
      </van-col>
    </van-row>
  </div>
</template>

<script>
import {Col, Row} from 'vant';
import {userIndex} from '@/api/api';

export default {
  name: 'order-group',
  data() {
    return {
      order: []
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      userIndex().then(res => {
        this.order = res.data.data.order;
      });
    }
  },
  components: {
    [Row.name]: Row,
    [Col.name]: Col
  }
};
</script>

<style lang="scss" scoped>
@import '../../assets/scss/mixin';

.order_status {
  background-color: #fff;
  text-align: center;
  padding: 10px 0;
  font-size: 12px;

  > div {
    @include one-border;

    &::after {
      top: 50%;
      left: 50%;
      border-bottom: 0;
      border-right: 1px solid $border-color;
      height: 150%;
      transform: scale(0.5) translate3d(-50%, -50%, 0);
      transform-origin: 0 0;
    }

    &:last-child::after {
      border: 0;
    }
  }

  .order_status_icon {
    position: relative;
    width: 36px;
    height: 36px;
    border-radius: 50%;
    display: inline-block;

    i {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate3d(-50%, -50%, 0);
      font-size: 24px;
      color: #000;
    }
  }
}
</style>
