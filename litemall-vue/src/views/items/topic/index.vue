<template>
  <div class="goods_topic">
    <navi-back :pageName="'topicList'"/>
    <div class="topic-detail"
         v-html="topic.content">
    </div>

    <van-row gutter>
      <van-col :key="index"
               span="12"
               v-for="(goods ,index) in topicGoods">
        <router-link :to="{ path: `/items/detail/${goods.id}`}">
          <img :src="goods.picUrl"
               style="width:150px;height:150px;">
        </router-link>
        <div style="margin-left: 20px; rgb(123, 116, 116);">{{ goods.name }}</div>
        <div style="margin-left: 20px; color:#ab956d">￥ {{ goods.retailPrice }}</div>
      </van-col>
    </van-row>

  </div>
</template>

<script>
import {topicDetail, topicRelated} from '@/api/api';
import {Card, Col, Row} from 'vant';
import NaviBack from '@/components/navi-back/';

export default {
  props: {
    topicId: [String, Number]
  },
  data() {
    return {
      topic: {},
      topicGoods: [],
      topicRelated: []
    };
  },

  created() {
    this.init();
  },

  methods: {
    init() {
      topicDetail({
        id: this.topicId
      }).then(res => {
        this.topic = res.data.data.topic;
        this.topicGoods = res.data.data.goods;
      });

      topicRelated({
        id: this.topicId
      }).then(res => {
        this.topicRelated = res.data.data;
      });
    },
    itemClick(id) {
      this.$router.push(`/items/detail/${id}`);
    }
  },

  components: {
    [Card.name]: Card,
    [Row.name]: Row,
    [Col.name]: Col,
    [NaviBack.name]: NaviBack
  }
};
</script>

<style lang="scss" scoped>
.goods_topic {
  .topic-detail {
    /deep/ p {
      padding: 0 10px;
      margin-block-start: 0 !important;
      margin-block-end: 0 !important;
    }

    /deep/ img {
      max-width: 100%;
      width: 100% !important;
      height: 100% !important;
      display: block;
    }
  }
}
</style>