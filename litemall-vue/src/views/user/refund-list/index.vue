<template>
  <div class="refund_list">
    <van-tabs :active="activeIndex" :swipe-threshold="5" @click="handleTabClick" sticky>
      <van-tab :key="tabIndex" :title="tab.name" v-for="(tab, tabIndex) in tabsItem">
        <van-list :finished="finished"
                  :immediate-check="false"
                  @load="getRefundList"
                  finished-text="没有更多了"
                  v-model="loading">
          <van-panel
              :key="i"
              :status="getStatusText(el.status)"
              :title="'订单编号: ' + el.id"
              class="order_list--panel"
              v-for="(el, i) in tab.items"
          >
            <div>
              <van-card
                  :desc="el.orderItem.sku_props_str"
                  :key="i"
                  :num="10000"
                  :price="(el.orderItem.price / 100).toFixed(2)"
                  :thumb="el.orderItem.pic_url"
                  :title="el.orderItem.item_name"
                  class="order_list--van-card"
              />
              <div
                  class="order_list--total"
              >合计: {{ el.refund_fee | yuan }}（含运费{{ el.refund_post_fee | yuan }}）
              </div>
            </div>
            <div slot="footer" style="text-align: right;">
              <van-button
                  @click="refund_handle(i)"
                  size="small"
              >{{ el.status == 10 ? "撤销申请" : "查看详情" }}
              </van-button>
            </div>
          </van-panel>
        </van-list>
      </van-tab>
    </van-tabs>
  </div>
</template>

<script>
import {REFUND_LIST} from '@/api/api';

import {Card, List, Panel, Tab, Tabs} from 'vant';

const STATUS_TEXT = {
  10: '退款中',
  50: '退款关闭',
  60: '退款成功'
};

export default {
  name: 'order-list',

  data() {
    return {
      listApi: REFUND_LIST,
      activeIndex: 0,
      items: [],
      page: 0,
      limit: 10,
      loading: false,
      finished: false,
      tabsItem: [
        {
          name: '全部',
          status: 0,
          items: []
        },
        {
          name: '退款中',
          status: 10,
          items: []
        },
        {
          name: '退款成功',
          status: 60,
          items: []
        }
      ]
    };
  },

  methods: {
    onLoad(i, items) {
      this.tabsItem[i].items.push(...items);
    },
    refund_handle(i) {
      const item = this.items[i];
      if (item.status == 10) {
        this.$dialog
            .confirm({
              message: '撤销后将不能再次发起申请，确定要撤销该申请吗？'
            })
            .then(() => {
              this.$toast('已撤销该退款申请');
              this.items[i].status = 50;
            });
      } else {
        // 跳转退款详情
      }
    },
    handleTabClick(index) {
      if (this.activeIndex != index) {
        this.activeIndex = index;
      }
    },
    getStatusText(status) {
      return STATUS_TEXT[status] || '';
    },
    getRefundList() {

    }
  },
  components: {
    [Tab.name]: Tab,
    [Tabs.name]: Tabs,
    [Panel.name]: Panel,
    [Card.name]: Card,
    [List.name]: List
  }
};
</script>

<style lang="scss" scoped>
.refund_list {
  padding-bottom: 0;

  &--footer_btn {
    text-align: right;
  }

  &--panel {
    margin-bottom: 10px;
  }

  &--van-card {
    background-color: #fafafa;
  }

  &--total {
    text-align: right;
    padding: 10px;
  }
}
</style>
