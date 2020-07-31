<template>
  <div class="item_search">
    <navi-back :pageName="'home'"/>
    <form @submit="disabledSubmit" action="/search">
      <van-search @search="enterSearch" autofocus placeholder="请输入商品名称" v-model="keyword"/>
    </form>
    <div class="item_search_content">
      <div class="item_search_text clearfix">
        <div class="float-l">历史搜索</div>
        <div @click="clearHistory" class="float-r">
          <van-icon name="lajitong" style="font-size: 12px;margin-right: 3px"/>
          清空历史记录
        </div>
      </div>
      <div class="item_search_history">
        <van-tag :key="i" @click="clickSearch(his)" plain v-for="(his, i) in wordHistory">{{ his }}</van-tag>
      </div>
    </div>
    <!-- <van-list v-model="loading" :finished="finished" :immediate-check="false" @load="loadMore"> -->
    <van-card :desc="item.brief"
              :key="i"
              :origin-price="item.counterPrice"
              :price="item.retailPrice"
              :thumb="item.picUrl"
              :title="item.name"
              @click="itemClick(item.id)"
              v-for="(item, i) in list"/>
    <!-- </van-list> -->
    <is-empty v-if="!list.length">抱歉, 没有找到符合条件商品</is-empty>
  </div>
</template>

<script>
import {Card, List, Search, Tag} from 'vant';
import NaviBack from '@/components/navi-back/';
import {goodsList} from '@/api/api';
import IsEmpty from '@/components/is-empty/';

export default {
  data() {
    return {
      keyword: '',
      focusStatus: true,
      wordHistory: [],
      list: [],
      page: 1,
      limit: 10,
      pages: 0,
      loading: false,
      finished: false,
      isEmpty: false
    };
  },
  methods: {
    enterSearch() {
      console.log({[Search.name]: 1});
      this.reset();
      this.searchGoods();
      this.pushHistoryTolocal(this.keyword);
    },
    clickSearch(word) {
      this.keyword = word.trim();
      this.reset();
      this.searchGoods();
    },
    reset() {
      this.list = [];
      this.page = 1;
      this.limit = 10;
      this.total = 0;
      this.loading = false;
      this.finished = false;
      this.isEmpty = false;
    },
    pushHistoryTolocal(keyword) {
      const wordHistory = this.wordHistory;
      const historyKeyWord = this.getKeyWordHistory();
      if (!!keyword.trim() && historyKeyWord.indexOf(keyword) < 0) {
        wordHistory.push(keyword);
        window.localStorage.setItem('keyword', wordHistory.join('|'));
      }
    },
    getKeyWordHistory() {
      const listWord = window.localStorage.getItem('keyword');
      return listWord ? listWord.split('|') : [];
    },
    clearHistory() {
      this.$dialog
          .confirm({
            message: '是否清空历史记录'
          })
          .then(() => {
            window.localStorage.setItem('keyword', '');
            this.wordHistory = [];
          });
    },
    disabledSubmit() {
      return false;
    },
    searchGoods() {
      goodsList({
        keyword: this.keyword,
        page: this.page,
        limit: this.limit,
        categoryId: 0
      }).then(res => {
        var data = res.data.data;
        this.list.push(...data.list);
        this.page = data.page;
        this.limit = data.limit;
        this.pages = data.pages;
      });
    },
    async loadMore() {
      this.loading = false;
      this.page += 1;
      await this.searchGoods();
      this.loading = false;
      if (this.pages <= this.page) {
        this.finished = true;
      }
    },
    itemClick(id) {
      this.$router.push(`/items/detail/${id}`);
    }
  },
  activated() {
    this.wordHistory = this.getKeyWordHistory();
  },
  components: {
    [Search.name]: Search,
    [Card.name]: Card,
    [Tag.name]: Tag,
    [List.name]: List,
    [IsEmpty.name]: IsEmpty,
    [NaviBack.name]: NaviBack
  }
};
</script>

<style lang="scss" scoped>
.item_search {
  background-color: #fff;
}

.item_search_content {
  padding: 15px 10px 0;
}

.item_search_text {
  font-size: $font-size-normal;
  color: $font-color-gray;
  margin-bottom: 20px;
}

.item_search_history > span {
  margin-right: 10px;
  margin-bottom: 10px;
}
</style>
