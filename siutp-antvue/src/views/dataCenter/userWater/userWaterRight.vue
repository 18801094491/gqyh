<template>
  <div class="screenCommonBox">
    <div class="table-page-search-wrapper" style="padding: 10px 12px">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :md="19" :sm="8">
            <a-range-picker
              v-model="hdLLBTime"
              format="YYYY-MM-DD"
              @change="hdLLBSearchQuery"
            ></a-range-picker>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div style="margin: 0 0 10px 0">
      <a-card :bordered="false">
        <div class="accumulatedWaterConsumptionBox" style="margin-top: 10px">
          <div class="chartDivBox">
            <div id="container7" class="container"></div>
          </div>
        </div>
      </a-card>
    </div>
    <div style="margin: 0 0 10px 0">
      <a-card :bordered="false">
        <a-divider
          >客户水价统计({{
            this.treeInfo == "0" ? "全部" : this.treeInfo.title
          }})</a-divider
        >
        <div class="accumulatedWaterConsumptionBox">
          <a-card
            class="j-address-list-right-card-box"
            :loading="cardLoading"
            :bordered="false"
          >
            <a-table
              ref="table"
              size="middle"
              bordered
              rowKey="userId"
              :pagination="ipagination"
              :columns="columns"
              :dataSource="dataSource"
              :loading="loading"
              @change="handleTableChange"
            ></a-table>
          </a-card>
        </div>
      </a-card>
    </div>
  </div>
</template>

<script>
import { getAction } from "@/api/manage";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { axios } from "@/utils/request";

export default {
  name: "userWaterRight",
  mixins: [JeecgListMixin],
  components: {},
  props: ["value"],
  data() {
    return {
      description: "用户信息",
      cardLoading: true,
      positionInfo: {},
      columns: [
        {
          title: "序号",
          dataIndex: "",
          key: "rowIndex",
          width: 60,
          align: "center",
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          },
        },
        {
          title: "客户名称",
          align: "left",
          width: 100,
          dataIndex: "customerName",
        },
        {
          title: "客户编码",
          align: "center",
          width: 100,
          dataIndex: "customerSn",
        },
        {
          title: "合同名称",
          align: "left",
          width: 100,
          dataIndex: "contractName",
        },
        {
          title: "合同编码",
          align: "center",
          width: 100,
          dataIndex: "contractSn",
        },
        {
          title: "水表编号",
          align: "center",
          width: 100,
          dataIndex: "equipmentSn",
        },
        {
          title: "日期",
          align: "center",
          width: 100,
          dataIndex: "staticsDate",
        },
        {
          title: "水单价",
          align: "center",
          width: 100,
          dataIndex: "waterPrice",
        },
      ],
      url: {
        list: "/equipment/meterFlow/queryAllWaterMonth",
        listByPosition: "/sys/position/list",
      },

      hdLLBTime: [],
      treeInfo: null,
    };
  },
  watch: {
    value: {
      immediate: true,
      handler(data) {
        this.treeInfo = data;
        this.dataSource = [];
        this.loadData(1, data.id);
        let data1 = {
          customerId: data.id,
          startDate: "",
          endDate: "",
        };
        this.getEchart(data1);
      },
    },
  },
  created() {
    setTimeout(() => {
      this.loadData(1, "0");
      let data = {
        customerId: "0",
        startDate: "",
        endDate: "",
      };
      this.getEchart(data);
    }, 500);
  },
  methods: {
    hdLLBSearchQuery() {
      let data = {
        customerId: this.treeInfo.id,
        startDate: this.hdLLBTime.length
          ? this.moment(this.hdLLBTime[0]).format("YYYY-MM-DD")
          : "",
        endDate: this.hdLLBTime.length
          ? this.moment(this.hdLLBTime[1]).format("YYYY-MM-DD")
          : "",
      };
      this.loadData(1, this.treeInfo.id);
      this.getEchart(data);
    },
    getEchart(data) {
      axios
        .get("/equipment/meterFlow/queryAllDateWaterFlow", {
          params: data,
        })
        .then((res) => {
          if (res.code * 1 == 200) {
            let list = res.result;
            this.dataChart(list);
          } else {
            this.$message.info(res.message);
          }
        });
    },
    dataChart(data) {
      let chartsTitle = this.treeInfo == "0" ? "全部" : this.treeInfo.title;

      let arr = [];
      let arr2 = [];
      for (let i = 0; i < data.length; i++) {
        arr.push(this.moment(data[i][0]).format("YYYY-MM-DD"));
        arr2.push(Number(data[i][1]));
      }
      Highcharts.setOptions({
        colors: [
          "#1E90FF",
          "#00CDCD",
          "#97FFFF",
          "#76EEC6",
          "#FFB90F",
          "#EE6363",
          "#FF00FF",
          "#DDA0DD",
          "#9370DB",
          "#8A2BE2",
          "#ADD8E6",
        ],
      });
      var chart = Highcharts.chart("container7", {
        chart: {
          zoomType: "x",
        },
        title: {
          text: `${chartsTitle}用水量`,
        },
        xAxis: {
          type: "datetime",
          categories: arr,
        },
        tooltip: {
          dateTimeLabelFormats: {
            millisecond: "%H:%M:%S.%L",
            second: "%H:%M:%S",
            minute: "%H:%M",
            hour: "%H:%M",
            day: "%Y-%m-%d",
            week: "%m-%d",
            month: "%Y-%m",
            year: "%Y",
          },
        },
        yAxis: {
          title: {
            text: "用水量(m³)",
          },
        },
        legend: {
          enabled: false,
        },
        plotOptions: {
          area: {
            fillColor: {
              linearGradient: {
                x1: 0,
                y1: 0,
                x2: 0,
                y2: 1,
              },
              stops: [
                [0, new Highcharts.getOptions().colors[0]],
                [
                  1,
                  new Highcharts.Color(Highcharts.getOptions().colors[0])
                    .setOpacity(0)
                    .get("rgba"),
                ],
              ],
            },
            marker: {
              radius: 2,
            },
            lineWidth: 1,
            states: {
              hover: {
                lineWidth: 1,
              },
            },
            threshold: null,
          },
        },
        series: [
          {
            type: "area",
            name: "用户用水量",
            data: arr2,
          },
        ],
      });
    },
    loadData(pageNum, customerId) {
      //加载数据 若传入参数1则加载第一页的内容
      if (pageNum === 1) {
        this.ipagination.current = 1;
      }
      this.loading = true;
      getAction(this.url.list, {
        customerId,
        ...this.getQueryParams(),
      })
        .then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
        })
        .finally(() => {
          this.loading = false;
          this.cardLoading = false;
        });
    },

    searchQuery() {
      this.loadData(1, this.value);
    },
    searchReset() {
      this.queryParam = {};
      this.loadData(1, this.value);
    },

    handleTableChange(pagination, filters, sorter) {
      if (Object.keys(sorter).length > 0) {
        this.isorter.column = sorter.field;
        this.isorter.order = "ascend" === sorter.order ? "asc" : "desc";
      }
      this.ipagination = pagination;
      console.log(this.value, "this.valuethis.value");
      this.loadData(null, this.value == "0" ? this.value : this.value.id);
    },

    // 查询职务信息
    queryPositionInfo() {
      // status--1:启用,   新加参数--停用的职务不显示
      getAction(this.url.listByPosition, { pageSize: 99999, status: "1" }).then(
        (res) => {
          if (res.success) {
            let positionInfo = {};
            res.result.records.forEach((record) => {
              positionInfo[record["code"]] = record["name"];
            });
            this.positionInfo = positionInfo;
          }
        }
      );
    },
  },
};
</script>
<style scoped>
.j-address-list-right-card-box .ant-table-placeholder {
  min-height: 46px;
}

.j-address-list-right-card-box {
  height: 100%;
  min-height: 300px;
}
</style>