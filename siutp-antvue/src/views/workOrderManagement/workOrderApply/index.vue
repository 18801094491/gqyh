<!-- 运营中心-工单管理-工单录入 -->
<template>
  <div class="workOrderApplyBox margin12">
    <a-card :bordered="false">
      <a-divider>运维部管廊作业工单</a-divider>
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8" :offset="18">
              <a-form-item label="编号">
                <a-input placeholder="请输入编号"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :md="8" :sm="8">
              <a-form-item label="派工人">
                <a-input placeholder="请输入派工人"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="8">
              <a-form-item label="带队人">
                <a-input placeholder="请输入带队人"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="8">
              <a-form-item label="作业人">
                <a-input placeholder="请输入作业人"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </div>
      <div class="workOrderApplyTableBox">
        <table>
          <tbody>
            <tr>
              <td rowspan="3" class="tdone">程序检查</td>
              <td class="tdtwo">交底事项</td>
              <td colspan="4">
                <a-textarea class="textareaInput" placeholder="" />
              </td>

              <td class="tdEnd">确认签字</td>
            </tr>
            <tr>
              <td class="tdtwo">是否涉及特种作业</td>
              <td class="tdthree">是</td>
              <td>
                <a-checkbox-group style="width: 100%">
                  <a-row style="margin-bottom: 15px">
                    <a-col :span="8"
                      ><a-checkbox value="A">有限空间作业</a-checkbox></a-col
                    >
                    <a-col :span="8"
                      ><a-checkbox value="B">吊装作业</a-checkbox></a-col
                    >
                  </a-row>
                  <a-row>
                    <a-col :span="8"
                      ><a-checkbox value="C">电焊作业</a-checkbox></a-col
                    >
                    <a-col :span="8"
                      ><a-checkbox value="D">电器作业</a-checkbox></a-col
                    >
                  </a-row>
                </a-checkbox-group>
              </td>
              <td>交底事项</td>
              <td>3</td>
              <td class="tdEnd"></td>
            </tr>
            <tr>
              <td class="tdtwo">是否需要流程审批</td>
              <td class="tdthree">是</td>
              <td>
                <a-checkbox-group style="width: 100%">
                  <a-row style="margin-bottom: 15px">
                    <a-col :span="8"
                      ><a-checkbox value="A">人员证件</a-checkbox></a-col
                    >
                    <a-col :span="8"
                      ><a-checkbox value="B">作业方案</a-checkbox></a-col
                    >
                  </a-row>
                  <a-row>
                    <a-col :span="8"
                      ><a-checkbox value="C">电焊作业</a-checkbox></a-col
                    >
                    <a-col :span="8"
                      ><a-checkbox value="D">电器作业</a-checkbox></a-col
                    >
                  </a-row>
                </a-checkbox-group>
              </td>
              <td>交底事项</td>
              <td>3</td>
              <td class="tdEnd"></td>
            </tr>
          </tbody>
        </table>
      </div>
    </a-card>
  </div>
</template>

<script>
import "./style.less";
import index from "./index.js";
export default {
  ...index,
};
</script>