<template>
  <div>
    <navi-back :pageName="'user-server'"/>
    <van-collapse :accordion="true" v-model="activeNames">
      <van-collapse-item :key="index" :name="index" :title="issue.question" v-for="(issue, index) in issueList">
        {{ issue.answer }}
      </van-collapse-item>
    </van-collapse>
  </div>
</template>

<script>
import {Collapse, CollapseItem} from 'vant';
import NaviBack from '@/components/navi-back/';
import {issueList} from '@/api/api';

export default {
  data: () => ({
    activeNames: [-1],
    issueList: []
  }),
  created() {
    this.getIssueList();
  },
  methods: {
    getIssueList() {
      issueList().then(res => {
        this.issueList = res.data.data.list
      });
    }
  },
  components: {
    [Collapse.name]: Collapse,
    [CollapseItem.name]: CollapseItem,
    [NaviBack.name]: NaviBack
  }
};
</script>
