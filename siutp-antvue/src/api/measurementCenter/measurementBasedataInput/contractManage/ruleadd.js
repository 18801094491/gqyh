import {
    axios
} from '@/utils/request';
import pick from "lodash.pick";
export const getRuleTypeList = (_this) => {
    axios.get('/sys/dict/getDictValue/count_price_type', {})
        .then(res => {
            if (res.code * 1 === 200) {
                _this.ruleTypeList = res.result;
            } else {
                _this.$message.info(res.message);
            }
        })
}
//合同管理-计价规则新增-获取规则类型列表
export const getPricingRuleDetailById = (_this, data) => {
    axios.get('/settle/contractWaterRule/detail', {
        params: {ruleId: data.id}
    })
        .then(res => {
            if (res.code * 1 === 200) {
                _this.visible = true
                _this.form.resetFields();
                _this.model = Object.assign({}, res.result);
                _this.model.ruleTypeName = data.ruleTypeName
                _this.$nextTick(() => {
                    _this.form.setFieldsValue(pick(_this.model, 'ruleType', 'ruleName', 'benchPrice', 'setUp', 'setTime'))
                    _this.form.ruleType = _this.model.ruleType
                    if (_this.form.ruleType !== '1') {
                        _this.setInitItems()
                    }
                    _this.form = Object.assign({}, _this.form);
                })
            } else {
                _this.$message.info(res.message);
            }
        })
}