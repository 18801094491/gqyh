<template>
    <div class="margin12">
        <div style="margin:0 0 10px 0 ">
            <a-card :bordered="false">
                <a-divider>每日累计用水量分析</a-divider>
                <div class="accumulatedWaterConsumptionBox">
                    <a-radio-group :value="barType" @change="statisticst" style="margin-bottom:25px;">
                        <a-radio-button value="7">7天</a-radio-button>
                        <a-radio-button value="14">14天</a-radio-button>
                        <a-radio-button value="30">30天</a-radio-button>
                    </a-radio-group>
                    <div id="container"></div>
                </div>
            </a-card>
        </div>
        <div style="margin:0 0 10px 0 ">
            <a-card :bordered="false">
                <a-divider>产销差分析</a-divider>
                <div class="productionSalesBalanceBox">
                    <div class="clearfix">
                        <a-radio-group :value="productionSalesBalance" @change="productionSalesBalanceChange" style="margin-bottom:25px;">
                            <a-radio-button value="14">7天</a-radio-button>
                            <a-radio-button value="30">本月</a-radio-button>
                            <a-radio-button value="365">本年</a-radio-button>
                        </a-radio-group>
                        <a-range-picker style="margin-left:25px"  v-model="productionSalesBalanceTime"  @change="productionSalesBalanceTimeChange"/>
                        <a-radio-group name="radioGroup" :value="dateType" @change="dateTypeChange" style="margin-left:25px">
                            <a-radio :value="1">天</a-radio>
                            <a-radio :value="2">月</a-radio>
                            <a-radio :value="3">年</a-radio>
                        </a-radio-group>
                        <a-button type="primary" @click="productionSalesBalanceSearchQuery" icon="search">查询</a-button> 
                    </div>

                    
                    <div id="container2"></div>
                </div>
            </a-card>
        </div>
        <a-card :bordered="false">
            <a-divider>水表用水分析</a-divider>
            
            
            <div class="customerWaterConsumption">
                <!-- 表格显示区域 -->
                <a-table
                    ref="table"
                    bordered
                    size="middle"
                    rowKey="id"
                    :columns="columns"
                    :dataSource="dataSource"
                    :pagination="ipagination"
                    :loading="loading"
                >
                    
                    
                </a-table>
            </div>
        </a-card>
    </div>
</template>

<script>
import '../../../../assets/less/dataCenter/dayAnalysis.less'
import index from '../../../../api/dataCenter/dayAnalysis.js'

export default{
    ...index
}
</script>