<template>
  <div class="goods_groupon">
    <navi-back :pageName="'home'"/>
    <div class="banner">
      <div class="title">团购列表</div>
    </div>

    <van-list :finished="finished"
              :immediate-check="false"
              @load="getGrouponList"
              finished-text="没有更多了"
              v-model="loading">
      <van-card :desc="item.brief"
                :key="i"
                :origin-price="item.counterPrice"
                :price="item.retailPrice"
                :thumb="item.picUrl"
                :title="item.name"
                @click="itemClick(item.id)"
                v-for="(item, i) in list">
        <div slot="tags">
          <van-tag plain
                   type="primary">
            {{ item.grouponMember }}人成团
          </van-tag>
          <van-tag plain
                   style="margin-left:5px;"
                   type="danger">
            {{ item.grouponDiscount }}元再减
          </van-tag>
        </div>
      </van-card>
    </van-list>

  </div>
</template>

<script>
import {grouponList} from '@/api/api';
import {Card, List, Tag} from 'vant';
import NaviBack from '@/components/navi-back/';
import scrollFixed from '@/mixin/scroll-fixed';

export default {
  mixins: [scrollFixed],

  data() {
    return {
      list: [],
      page: 0,
      limit: 10,
      loading: false,
      finished: false
    };
  },

  created() {
    this.init();
  },

  methods: {
    init() {
      this.page = 0;
      this.list = [];
      this.getGrouponList();
    },
    getGrouponList() {
      this.page++;
      grouponList({
        page: this.page,
        limit: this.limit
      }).then(res => {
        this.list.push(...res.data.data.list);
        this.loading = false;
        this.finished = res.data.data.page >= res.data.data.pages;
      });
    },
    itemClick(id) {
      this.$router.push(`/items/detail/${id}`);
    }
  },

  components: {
    [List.name]: List,
    [Tag.name]: Tag,
    [Card.name]: Card,
    [NaviBack.name]: NaviBack
  }
};
</script>

<style lang="scss" scoped>
.goods_groupon {
  padding: 20px;

  .banner {
    height: 250px;
    background-image: url('http://yanxuan.nosdn.127.net/8976116db321744084774643a933c5ce.png');
    background-size: cover;
    margin-bottom: 20px;

    .title {
      text-align: center;
      line-height: 200px;
      color: #ffffff;
      font-size: 40px;
    }
  }
}
</style>