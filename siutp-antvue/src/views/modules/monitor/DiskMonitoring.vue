<!-- 系统监控-性能监控-磁盘控制 -->
<template>
  <div class="margin12">
    <a-card title="磁盘监控">
      <a-row>
        <template v-if="diskInfo && diskInfo.length > 0">
          <a-col
            :span="8"
            v-for="(item, index) in diskInfo"
            :key="'diskInfo' + index"
          >
            <dash-chart-demo
              :title="item.name"
              :datasource="item.restPPT"
            ></dash-chart-demo>
          </a-col>
        </template>
      </a-row>
    </a-card>
  </div>
</template>

<script>
import { getAction } from "@/api/manage";
import DashChartDemo from "@/components/chart/DashChartDemo";
import ARow from "ant-design-vue/es/grid/Row";

export default {
  name: "DiskMonitoring",
  components: {
    ARow,
    DashChartDemo
  },
  data() {
    return {
      description: "磁盘监控",
      //数据集
      diskInfo: [],
      url: {
        queryDiskInfo: "actuator/redis/queryDiskInfo"
      }
    };
  },
  created() {
    getAction(this.url.queryDiskInfo).then(res => {
      if (res.success) {
        for (var i = 0; i < res.result.length; i++) {
          res.result[i].restPPT = res.result[i].restPPT / 10;
        }
        this.diskInfo = res.result;
      }
    });
  }
};
</script>

<style scoped></style>
