<template>
    <a-modal
            :title="title"
            :width="800"
            :visible="visible"
            :confirmLoading="confirmLoading"
            @ok="handleOk"
            @cancel="handleCancel"
            cancelText="关闭"
    >
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="变量名称">
                    <a-input placeholder="请输入变量名称如：FM00#zt" v-decorator="['variableName', {}]"/>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="变量标题">
                    <a-input placeholder="请输入变量标题" v-decorator="['variableTitle', {}]"/>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="变量类型">
                    <a-select v-decorator="['variableType', {}]" placeholder="请选择变量类型">
                        <a-select-option
                                :value="item.code"
                                v-for="(item,index) in inequipmentTypeList"
                                :key="index"
                        >{{ item.title }}
                        </a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="单位">
                    <a-input placeholder="请输入单位" v-decorator="['variableUnit', {}]"/>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="读写状态">
                    <a-select v-decorator="['readType', {}]" placeholder="请选择读写状态" @change="changeRule">
                        <a-select-option
                                :value="item.code"
                                v-for="(item,index) in readType_List"
                                :key="index"
                        >{{ item.title }}
                        </a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="数据类型">
                    <a-select v-decorator="['dataType', {}]" placeholder="请选择数据类型" @change="dataTypeChange">
                        <a-select-option
                                :value="item.code"
                                v-for="(item,index) in dataType_List"
                                :key="index"
                        >{{ item.title }}
                        </a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item v-if="floatNumOk" :labelCol="labelCol" :wrapperCol="wrapperCol" label="小数位数">
                    <a-input-number
                            :min="0"
                            v-decorator="['scale', {}]"
                            @keyup="validateNum"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="备注">
                    <a-input placeholder="请输入备注" v-decorator="['remark', {}]"/>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="状态">
                    <!-- <a-input placeholder="请输入状态值1-启用,0-停用" v-decorator="['workingStatus', {}]" /> -->
                    <a-select v-decorator="['woStValue', {}]" placeholder="请选择状态">
                        <a-select-option value="0">停用</a-select-option>
                        <a-select-option value="1">启用</a-select-option>
                    </a-select>
                </a-form-item>
            </a-form>
        </a-spin>
    </a-modal>
</template>

<script>
    import {axios} from '@/utils/request';
    import {httpAction} from "@/api/manage";
    import pick from "lodash.pick";
    //import {getA16} from "@/api/dict.js";
    import {getA16} from "@/api/collection-t/variableinfo.js";

    export default {
        name: "IotVariableInfoModal",
        data() {
            return {
                title: "操作",
                visible: false,
                model: {},
                labelCol: {
                    xs: {span: 24},
                    sm: {span: 5}
                },
                wrapperCol: {
                    xs: {span: 24},
                    sm: {span: 16}
                },
                isShow: true,
                confirmLoading: false,
                form: this.$form.createForm(this),
                validatorRules: {},
                url: {
                    add: "/iot/varinfo/add",
                    edit: "/iot/varinfo/edit"
                },
                alarm_rulesList: [],
                readType_List: [],
                dataType_List: [],
                floatNumOk: false,
                inequipmentTypeList: []
            };
        },
        created() {
        },
        mounted() {
            this.getDictValueByReadType();
            this.getReadType(this);
            this.getDataType(this);
            getA16(this);
        },
        methods: {
            getReadType(_this) {
                axios.get('/sys/dict/getDictValue/read_type')
                    .then(res => {
                        if (res.code * 1 == 200) {
                            _this.readType_List = res.result;
                        } else {
                            _this.$message.info(res.message);
                        }
                    })
            },
            getDataType(_this) {
                axios.get('/sys/dict/getDictValue/data_type')
                    .then(res => {
                        if (res.code * 1 == 200) {
                            _this.dataType_List = res.result;
                        } else {
                            _this.$message.info(res.message);
                        }
                    })
            },
            // 获取字典信息
            getDictValue(codeValue, parameter) {
                return axios({
                    url: `/sys/dict/getDictValue/${codeValue}`,
                    method: 'get',
                    params: parameter
                })
            },
            add() {
                this.edit();
            },
            // 获取字典数据
            getDictValueByReadType() {
                let _this = this;
                this.getDictValue("read_type", null)
                    .then(res => {
                        console.log(res);
                        if (res.code * 1 == 200) {
                            let list = res.result;
                            _this.readType_List = [];
                            list.map(index => {
                                _this.readType_List.push({
                                    code: index.code,
                                    title: index.title
                                });
                            });
                        } else {
                            _this.$message.info(res.message);
                        }
                    })
                    .catch(err => {
                        this.$message.error(err);
                    });
                //read_type
            },
            /**
             * 编辑对话框
             */
            edit(record) {
                this.isShow = true;
                if (record) {
                    if (record.threshold) {
                        if (record.threshold.indexOf("-") != -1) {
                            record.threshold1 = record.threshold.split("-")[0];
                            record.threshold2 = record.threshold.split("-")[1];
                            this.isShow = false;
                        } else {
                            this.isShow = true;
                        }
                    }
                }
                if (record) {
                    if (record.dataType == 3 || record.dataType == 4) {

                        this.floatNumOk = true;
                    }
                }

                console.log(record);
                this.form.resetFields();
                this.model = Object.assign({}, record);
                console.log(this.model);
                this.visible = true;
                this.$nextTick(() => {
                    this.form.setFieldsValue(
                        pick(
                            this.model,
                            "variableName",
                            "variableTitle",
                            "workingStatus",
                            "rule",
                            "threshold",
                            "remark",
                            "ruleValue",
                            "woStValue",
                            "readType",
                            "dataType",
                            "variableUnit",
                            'scale',
                            'variableType'
                        )
                    );
                });
            },
            close() {
                this.$emit("close");
                this.visible = false;
                this.floatNumOk = false;
            },
            /**
             * 提交
             */
            handleOk() {
                const that = this;
                // 触发表单验证
                this.form.validateFields((err, values) => {
                    if (!values.variableName) {
                        this.$message.info("变量名称不能为空!");
                        return;
                    }
                    if (!values.variableType) {
                        this.$message.info("变量类型不能为空!");
                        return;
                    }
                    //read_type
                    if (!values.woStValue) {
                        this.$message.info("状态不能为空!");
                        return;
                    }
                    if (that.isShow) {
                        if (values.ruleValue) {
                            if (!values.threshold) {
                                this.$message.info("阈值不能为空!");
                                return;
                            }
                        }
                    } else {
                        if (values.ruleValue) {
                            if (!that.model.threshold1 || !that.model.threshold2) {
                                this.$message.info("阈值不能为空!");
                                return;
                            }
                        }
                    }
                    if (this.floatNumOk) {
                        if (!values.scale && values.scale != 0) {
                            this.$message.info("小数位数不能为空!");
                            return;
                        }
                    }
                    if (!err) {
                        that.confirmLoading = true;
                        let httpurl = "";
                        let method = "";
                        if (!this.model.id) {
                            httpurl += this.url.add;
                            method = "post";
                        } else {
                            httpurl += this.url.edit;
                            method = "put";
                        }
                        console.log(values);
                        let formData = Object.assign({id: this.model.id}, values);
                        //时间格式化
                        if (!that.isShow) {
                            formData.threshold =
                                this.model.threshold1 + "-" + this.model.threshold2;
                        }
                        if (!this.floatNumOk) {
                            formData.scale = 0;
                        }
                        console.log(formData);
                        httpAction(httpurl, formData, method)
                            .then(res => {
                                if (res.success) {
                                    that.$message.success(res.message);
                                    that.$emit("ok");
                                } else {
                                    that.$message.warning(res.message);
                                }
                            })
                            .finally(() => {
                                that.confirmLoading = false;
                                that.close();
                            });
                    }
                });
            },
            handleCancel() {
                this.close();
            },
            // 改变规则
            changeRule(data) {
                console.log(data);
                if (data == 6 || data == 7) {
                    this.isShow = false;
                } else {
                    this.isShow = true;
                }
            },
            /**
             * 日期改变
             */
            dataTypeChange(data) {
                console.log(data);
                if (data == 3 || data == 4) {
                    this.floatNumOk = true;
                } else {
                    this.floatNumOk = false;
                }
            },
            validateNum(e) {
                console.log(e.target.value)
            }
        }
    };
</script>

<style lang="less" scoped>
</style>