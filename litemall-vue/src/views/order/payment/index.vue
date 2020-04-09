<template>
  <div class="payment">
    <div class="time_down payment_group">请在<span class="red">半小时内</span>完成付款，否则系统自动取消订单</div>
    <van-cell-group class="payment_group">
      <van-cell title="订单编号" :value="order.orderInfo.orderSn"/>
      <van-cell title="实付金额">
        <span class="red">{{order.orderInfo.actualPrice*100 | yuan}}</span>
      </van-cell>
    </van-cell-group>
    <div class="pay_way_group">
      <div class="pay_way_title">选择支付方式</div>
      <van-radio-group v-model="payWay">
        <van-cell-group>
          <!-- <van-cell>
            <template slot="title">
              <img src="../../../assets/images/ali_pay.png" alt="支付宝" width="82" height="29">
            </template>
            <van-radio name="ali"/>
          </van-cell> -->
          <van-cell>
            <template slot="title">
              <img src="../../../assets/images/wx_pay.png" alt="微信支付" width="113" height="23">
            </template>
            <van-radio name="wx"/>
          </van-cell>
          <!-- <van-cell>
            <center>
              <img :src="orderWsQrCodeUrl" v-show="!isWeixin" alt="wx-qr-code" width="180" height="180" style="">
            </center>
          </van-cell> -->
        </van-cell-group>
      </van-radio-group>
    </div>
    <!-- <van-button class="pay_submit" @click="pay" type="primary" bottomAction>支付结果</van-button> -->
    <van-button class="pay_submit" @click="try2Pay" type="primary" bottomAction>去支付</van-button>
  </div>
</template>

<script>
import { Radio, RadioGroup, Dialog } from 'vant';
import { orderDetail, orderWxQrCode, orderWxPayResult, orderWxPayDummy, orderPrepay } from '@/api/api';
import _ from 'lodash';

export default {
  name: 'payment',
  data() {
    return {
      payWay: 'wx',
      order: {
        orderInfo: {},
        orderGoods: []
      },
      orderId: 0,
      orderWsQrCodeUrl: '',
      timer_obj: null,
      isWeixin: false
    };
  },
  created() {
    if (_.has(this.$route.params, 'orderId')) {
      this.orderId = this.$route.params.orderId;
      this.getOrder(this.orderId);
      // this.applyOrderWxQrCode(this.orderId);
    }
  },
  methods: {
    getOrder(orderId) {
      orderDetail({orderId: orderId}).then(res => {
        this.order = res.data.data;
      });
    },
    applyOrderWxQrCode(orderId) {
      this.isWeixin = navigator.userAgent.toLowerCase().indexOf('micromessenger') != -1;
      if (!this.isWeixin) {
        orderWxQrCode({orderId: orderId}).then(res => {
          this.orderWsQrCodeUrl = process.env.VUE_APP_BASE_API + 'wx/order/qrc?code_url=' + res.data.data.code_url;
        });
      }
    },
    pay() {
      this.timer_obj = setInterval(() => {
        orderDetail({orderId: this.orderId}).then(res => {
          if (res.data.data.orderInfo.orderStatusText !== '未付款') {
            clearInterval(this.timer_obj);
            orderWxPayResult({orderId: this.orderId}).then(res => {
              this.$router.push({
                name: 'paymentStatus',
                params: {
                  status: res.data.data.result_code
                }
              });
            });
          }
        });
      }, 3000);
    },
    try2Pay() {
      // if (this.isWeixin) {
        // orderWxPayDummy({orderId: this.orderId}).then(res => {
          // let url = "http://www.changchunamy.com/Home/wxPay?orderId=" + this.order.orderInfo.orderSn + "&totalFee=" + this.order.orderInfo.actualPrice;
          let url = "http://www.changchunamy.com/Home/wxPay?orderId=" + this.order.orderInfo.orderSn + "&totalFee=1";
          window.location.assign(url);
        // });
      // } else {
      //   this.pay();
      // }
    }
  },
  components: {
    [Radio.name]: Radio,
    [RadioGroup.name]: RadioGroup,
    [Dialog.name]: Dialog
  }
};
</script>

<style lang="scss" scoped>
.payment_group {
  margin-bottom: 10px;
}
.time_down {
  background-color: #fffeec;
  padding: 10px 15px;
}
.pay_submit {
  position: fixed;
  bottom: 0;
  width: 100%;
}
.pay_way_group img {
  vertical-align: middle;
}
.pay_way_title {
  padding: 15px;
  background-color: #fff;
}
</style>
