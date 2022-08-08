<template>
    <a-modal
            :title="title"
            :width="500"
            :visible="visible"
            :confirmLoading="confirmLoading"
            @ok="handleOk"
            @cancel="handleCancel"
            cancelText="关闭">

        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item
                        :labelCol="labelCol"
                        :wrapperCol="wrapperCol"
                        label="客户名称">
                    <a-select
                            showSearch
                            placeholder="请输入客户名称"
                            optionFilterProp="children"
                            style="width: 100%"

                            v-decorator="[ 'customerId', {}]"
                            @change="handleChange"

                    >
                        <a-select-option v-for="(item,index) in dataSource" :key="index" :value="item.customerId">
                            {{item.constomerName}}
                        </a-select-option>

                    </a-select>
                </a-form-item>
                <a-form-item
                        :labelCol="labelCol"
                        :wrapperCol="wrapperCol"
                        label="报装时间">
                    <el-date-picker
                            style="width:100%"
                            v-model="form.lotTime"
                            placeholder="请选择时间"
                            type="datetime"
                            format="yyyy-MM-dd HH:mm"
                    ></el-date-picker>
                </a-form-item>
                <a-form-item
                        :labelCol="labelCol"
                        :wrapperCol="wrapperCol"
                        label="报装内容">
                    <a-input placeholder="请输入报装内容" v-decorator="['lotContent', {}]"/>
                </a-form-item>
                <a-form-item
                        :labelCol="labelCol"
                        :wrapperCol="wrapperCol"
                        label="用途">
                    <a-input placeholder="请输入用途" v-decorator="['purpose', {}]"/>
                </a-form-item>

            </a-form>
        </a-spin>
    </a-modal>
</template>

<script>
    import index from '@assets/js/operationCenter/userInstallation/SettleUseralotModal.js';

    export default {
        ...index
    }
</script>

