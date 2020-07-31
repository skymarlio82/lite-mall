<template>
  <div class="tab_home">
    <div class="tal_class_searchBox">
      <van-search @click="$router.push({ name: 'search' })"
                  placeholder="点击前往搜索"/>
      <div class="tal_class_searchMask"></div>
    </div>
    <van-swipe :autoplay="3000"
               indicator-color="white">
      <van-swipe-item :key="index"
                      v-for="(ban, index) in shopInfos.banner">
        <img :src="ban.url"
             style="height:230px">
      </van-swipe-item>
    </van-swipe>
    <div class="goods-channel">
      <div :key="index"
           @click="changeTabbar(chan)"
           class="item"
           v-for="(chan, index) in shopInfos.channel">
        <img :src="chan.iconUrl"
             background-size="cover"/>
        <span>{{ chan.name }}</span>
      </div>
    </div>
    <van-panel style=" padding-bottom: 10px;"
               title="优惠券">
      <div :key="index"
           @click="getCoupon(coupon.id)"
           class="van-coupon-item"
           v-for="(coupon, index) in shopInfos.couponList">
        <div class="van-coupon-item__content">
          <div class="van-coupon-item__head">
            <h2>
              <span>¥</span>
              {{ coupon.discount }} 元
            </h2>
            <p>{{ coupon.desc }} - {{ coupon.tag }}</p>
          </div>
          <div class="van-coupon-item__body">
            <h2>{{ coupon.name }}</h2>
            <p>有效期：{{ coupon.days }} 天</p>
          </div>
        </div>
      </div>
    </van-panel>
    <van-panel>
      <van-card :desc="grouponGood.brief"
                :key="index"
                :origin-price="grouponGood.retailPrice"
                :price="grouponGood.grouponPrice + '.00'"
                :thumb="grouponGood.picUrl"
                :thumb-link="goDetail(grouponGood.id)"
                :title="grouponGood.name"
                @native-click="goDetail(grouponGood.id)"
                v-for="(grouponGood, index) in shopInfos.grouponList">
        <div class="card__tags"
             slot="tags">
          <van-tag plain
                   type="primary">
            {{ grouponGood.grouponMember }}人成团
          </van-tag>
          <van-tag plain
                   type="danger">
            {{ grouponGood.grouponDiscount }}元再减
          </van-tag>
        </div>
      </van-card>
      <div slot='header'>
        <van-cell-group>
          <van-cell isLink
                    title="团购专区">
            <router-link class="text-desc"
                         to="/items/groupon">更多团购商品
            </router-link>
          </van-cell>
        </van-cell-group>
      </div>
    </van-panel>
    <van-panel>
      <van-grid :column-num="2"
                clickable>
        <van-grid-item :key="index"
                       :text="brand.name"
                       :url="goBrand(brand.id)"
                       v-for="(brand, index) in shopInfos.brandList">
          <img :src="brand.picUrl"
               style="width: 80%;">
          <div style="font-size:16px;">{{ brand.name }}</div>
        </van-grid-item>
      </van-grid>
      <div slot='header'>
        <van-cell-group>
          <van-cell isLink
                    title="品牌商直供">
            <router-link class="text-desc"
                         to="/items/brand-list">更多品牌商
            </router-link>
          </van-cell>
        </van-cell-group>
      </div>
    </van-panel>
    <van-panel>
      <van-row gutter>
        <van-col :key="index"
                 span="12"
                 v-for="(newGood, index) in shopInfos.newGoodsList">
          <router-link :to="{ path: `/items/detail/${newGood.id}` }">
            <img :src="newGood.picUrl"
                 style="width:180px;height:180px;">
          </router-link>
          <span style="padding-left: 20px;position: relative;bottom: 10px; color: rgb(123, 116, 116);white-space: nowrap;">{{ newGood.name }}</span>
          <span style="padding-left: 80px;position: relative;bottom: 10px; color:#ab956d">￥ {{ newGood.retailPrice }}</span>
        </van-col>
      </van-row>
      <div slot='header'>
        <van-cell-group>
          <van-cell isLink
                    title="新品首发">
            <router-link class="text-desc"
                         to="/items/new">更多新品首发
            </router-link>
          </van-cell>
        </van-cell-group>
      </div>
    </van-panel>
    <van-panel>
      <van-card :desc="groupGood.brief"
                :key="index"
                :origin-price="groupGood.counterPrice"
                :price="groupGood.retailPrice + '.00'"
                :thumb="groupGood.picUrl"
                :thumb-link="goDetail(groupGood.id)"
                :title="groupGood.name"
                @native-click="goDetail(groupGood.id)"
                v-for="(groupGood, index) in shopInfos.hotGoodsList">
        <!-- <div slot="footer">添加日期 {{item.addTime}}</div> -->
      </van-card>
      <div slot='header'>
        <van-cell-group>
          <van-cell isLink
                    title="人气推荐">
            <router-link class="text-desc"
                         to="/items/hot">更多人气推荐
            </router-link>
          </van-cell>
        </van-cell-group>
      </div>
    </van-panel>
    <van-panel>
      <van-grid :column-num="2"
                clickable>
        <van-grid-item :key="index"
                       :url="goTopic(topic.id)"
                       v-for="(topic ,index) in shopInfos.topicList">
          <img :src="topic.picUrl"
               style="width: 90%; max-height: 150px;">
          <div style="font-size:14px;color:#ab956d;">{{ topic.title }}</div>
          <div style="font-size:10px;color:#ab956d;">{{ topic.subtitle }}</div>
        </van-grid-item>
      </van-grid>
      <div slot='header'>
        <van-cell-group>
          <van-cell isLink
                    title="专题精选">
            <router-link class="text-desc"
                         to="/items/topic-list">更多专题精选
            </router-link>
          </van-cell>
        </van-cell-group>
      </div>
    </van-panel>
  </div>
</template>

<script>
import {couponReceive, getHome, goodsCategory} from '@/api/api';
import scrollFixed from '@/mixin/scroll-fixed';

import {Card, Col, CouponCell, CouponList, Grid, GridItem, Lazyload, List, Panel, Row, Search, Swipe, SwipeItem, Tabbar, TabbarItem, Tag, Toast} from 'vant';

export default {
  mixins: [scrollFixed],

  data() {
    return {
      shopInfos: [],
      isLoading: false
    };
  },

  created() {
    this.initViews();
  },

  methods: {
    goDetail(id) {
      return `#/items/detail/${id}`;
    },
    goBrand(id) {
      return `#/items/brand/${id}`;
    },
    goTopic(id) {
      return `#/items/topic/${id}`;
    },
    getCoupon(id) {
      couponReceive({couponId: id}).then(res => {
        Toast.success('领取成功');
      });
    },
    changeTabbar(o) {
      goodsCategory({id: o.id}).then(res => {
        let categoryId = res.data.data.currentCategory.id;
        this.$router.replace({
          name: 'category',
          query: {itemClass: categoryId}
        });
      });
    },
    initViews() {
      getHome().then(res => {
        this.shopInfos = res.data.data;
      });
    }
  },

  components: {
    [Row.name]: Row,
    [Col.name]: Col,
    [Card.name]: Card,
    [Toast.name]: Toast,
    [CouponCell.name]: CouponCell,
    [CouponList.name]: CouponList,
    [Search.name]: Search,
    [Panel.name]: Panel,
    [List.name]: List,
    [Swipe.name]: Swipe,
    [SwipeItem.name]: SwipeItem,
    [Tabbar.name]: Tabbar,
    [TabbarItem.name]: TabbarItem,
    [Tag.name]: Tag,
    [Grid.name]: Grid,
    [GridItem.name]: GridItem,
    [Lazyload.name]: Lazyload
  }
};
</script>

<style lang="scss" scoped>
.interval_bot {
  margin-bottom: 10px;
}

.van-panel {
  margin-top: 20px;
}

.goods-channel {
  background: #fff;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  padding-bottom: 0px;
  padding-top: 10px;
}

.goods-channel .item {
  width: 50px;
  height: 50px;
  margin-left: 10px;
}

.goods-channel img {
  display: block;
  width: 20px;
  height: 20px;
  margin: 0 auto;
}

.goods-channel span {
  display: block;
  font-size: 14px;
  text-align: center;
  margin-top: 5px;
  margin-bottom: 10px;
  color: #333;
}

.van-coupon-cell--selected {
  color: #323233;
}

.van-coupon-list {
  height: 100%;
  position: relative;
  background-color: #f8f8f8;
}

.van-coupon-list__field {
  padding: 7px 15px;
}

.van-coupon-list__exchange {
  height: 32px;
  line-height: 30px;
}

.van-coupon-list__list {
  overflow-y: auto;
  padding: 15px 0;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  -webkit-overflow-scrolling: touch;
}

.van-coupon-list__close {
  left: 0;
  bottom: 0;
  position: absolute;
  font-weight: 500;
}

.van-coupon-list__empty {
  padding-top: 100px;
  text-align: center;
}

.van-coupon-list__empty p {
  color: #969799;
  margin: 15px 0;
  font-size: 14px;
  line-height: 20px;
}

.van-coupon-list__empty img {
  width: 80px;
  height: 84px;
}

.van-coupon-item {
  overflow: hidden;
  border-radius: 4px;
  margin: 0 15px 15px;
  background-color: #fff;
  -webkit-box-shadow: 0 0 4px rgba(0, 0, 0, 0.1);
  box-shadow: 0 0 4px rgba(0, 0, 0, 0.1);
}

.van-coupon-item:active {
  background-color: #e8e8e8;
}

.van-coupon-item__content {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  height: 100px;
  padding: 24px 0 0 15px;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  border: 1px solid red;
}

.van-coupon-item h2,
.van-coupon-item p {
  margin: 0;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.van-coupon-item h2 {
  height: 34px;
  font-weight: 500;
  line-height: 34px;
}

.van-coupon-item p {
  font-size: 12px;
  line-height: 16px;
  color: #969799;
}

.van-coupon-item__head {
  min-width: 90px;
}

.van-coupon-item__head h2 {
  color: #f44;
  font-size: 24px;
}

.van-coupon-item__head h2 span {
  font-size: 50%;
}

.van-coupon-item__body {
  -webkit-box-flex: 1;
  -ms-flex: 1;
  flex: 1;
  position: relative;
  border-radius: 0 4px 4px 0;
}

.van-coupon-item__body h2 {
  font-size: 16px;
}

.van-coupon-item__corner {
  top: 16px;
  right: 15px;
  position: absolute;
}

.van-coupon-item__corner .van-icon {
  border-color: #f44;
  background-color: #f44;
}

.van-coupon-item__reason {
  padding: 7px 15px;
  border-top: 1px dashed #ebedf0;
  background-color: #fafafa;
}

.van-coupon-item--disabled:active {
  background-color: #fff;
}

.van-coupon-item--disabled .van-coupon-item__content {
  height: 90px;
}

.van-coupon-item--disabled h2,
.van-coupon-item--disabled p,
.van-coupon-item--disabled span {
  color: #969799;
}
</style>
