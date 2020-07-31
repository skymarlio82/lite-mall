<template>
  <div class="order_list">
    <navi-back :pageName="'user'"/>
    <van-tabs :swipe-threshold="5" @click="handleTabClick" v-model="activeIndex">
      <van-tab :key="index" :title="tabTitle" v-for="(tabTitle, index) in tabTitles">
        <van-list :finished="finished" :immediate-check="false" @load="getOrderList" finished-text="没有更多了"
                  v-model="loading">
          <van-panel :key="i" :status="el.orderStatusText" :title="'订单编号: ' + el.orderSn"
                     @click.native="toOrderDetail(el.id)"
                     v-for="(el, i) in orderList">
            <van-card :key="goodsI" :num="goods.number" :thumb="goods.picUrl" :title="goods.goodsName"
                      v-for="(goods, goodsI) in el.goodsList">
              <div slot="desc">
                <div class="desc">
                  <van-tag :key="index" plain style="margin-right:6px;" v-for="(spec, index) in goods.specifications">
                    {{ spec }}
                  </van-tag>
                </div>
              </div>
            </van-card>
            <div class="total">合计: {{ el.actualPrice * 100 | yuan }}（含运费{{ el.post_fee | yuan }}）</div>
            <div class="footer_btn" slot="footer">
              <van-button @click="cancelOrder(el.id)" size="small" v-if="el.handleOption.cancel">
                取消订单
              </van-button>
              <van-button @click.stop="toPay(el.id)" size="small" type="danger" v-if="el.handleOption.pay">
                去支付
              </van-button>
              <van-button @click="refundOrder(el.id)" size="small" type="danger" v-if="el.handleOption.refund">
                退款
              </van-button>
              <van-button @click="confirmOrder(el.id)" size="small" type="danger" v-if="el.handleOption.confirm">
                确认收货
              </van-button>
              <van-button @click="delOrder(el.id)" size="small" v-if="el.handleOption.delete">
                删除订单
              </van-button>
              <van-button @click="commentOrder(el.id)" size="small" v-if="el.handleOption.comment">
                去评价
              </van-button>
            </div>
          </van-panel>
        </van-list>
      </van-tab>
    </van-tabs>
  </div>
</template>

<script>
import {orderConfirm, orderDelete, orderList, orderRefund} from '@/api/api';
import {Card, List, Panel, Tab, Tabs, Tag} from 'vant';
import NaviBack from '@/components/navi-back/';

export default {
  name: 'order-list',

  props: {
    active: {
      type: [String, Number],
      default: 0
    }
  },
  created() {
    this.init();
  },
  data() {
    return {
      activeIndex: Number(this.active),
      tabTitles: ['全部', '待付款', '待发货', '待收货', '待评价'],
      orderList: [],
      page: 0,
      limit: 10,
      loading: false,
      finished: false
    };
  },

  methods: {
    init() {
      this.page = 0;
      this.orderList = [];
      this.getOrderList();
    },
    getOrderList() {
      this.page++;
      orderList({
        showType: this.activeIndex,
        page: this.page,
        limit: this.limit
      }).then(res => {
        this.orderList.push(...res.data.data.list);
        this.loading = false;
        this.finished = res.data.data.page >= res.data.data.pages;
      });
    },
    delOrder(id) {
      let that = this;
      this.$dialog
          .confirm({message: '确定要删除该订单吗?'})
          .then(() => {
            orderDelete({orderId: id}).then(() => {
              this.init();
              this.$toast('已删除订单');
            });
          })
          .catch(() => {
          });
    },
    cancelOrder(id) {
      this.$dialog
          .confirm({message: '确定要取消该订单吗?'})
          .then(() => {
            orderDelete({orderId: id}).then(() => {
              this.init();
              this.$toast('已取消该订单');
            });
          })
          .catch(() => {
          });
    },
    refundOrder(id) {
      this.$dialog
          .confirm({message: '确定要申请退款吗?'})
          .then(() => {
            orderRefund({orderId: id}).then(() => {
              this.init();
              this.$toast('已申请订单退款');
            });
          })
          .catch(() => {
          });
    },
    confirmOrder(id) {
      this.$dialog
          .confirm({
            message: '请确认收到货物, 确认收货后无法撤销!'
          })
          .then(() => {
            orderConfirm({orderId: id}).then(() => {
              this.init();
              this.$toast('已确认收货');
            });
          })
          .catch(() => {
          });
    },
    commentOrder(id) {
    },
    toPay(id) {
      this.$router.push({name: 'payment', params: {orderId: id}});
    },
    handleTabClick() {
      this.page = 0;
      this.orderList = [];
      this.getOrderList();
    },
    toOrderDetail(id) {
      this.$router.push({
        path: '/order/order-detail',
        query: {orderId: id}
      });
    }
  },
  components: {
    [Tab.name]: Tab,
    [Tabs.name]: Tabs,
    [Panel.name]: Panel,
    [Card.name]: Card,
    [List.name]: List,
    [Tag.name]: Tag,
    [NaviBack.name]: NaviBack
  }
};
</script>

<style lang="scss" scoped>
.order_list {
  .van-panel {
    margin-top: 20px;
  }

  .van-card {
    background-color: #fff;
  }

  .total {
    text-align: right;
    padding: 10px;
  }

  .footer_btn {
    text-align: right;

    .van-button {
      margin-left: 10px;
    }
  }
}
</style>