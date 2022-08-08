// 合并单元格
var last = {};
const mergeMyCells = (groupBy, data, columns, index) => {
    let conIdColumn = groupBy + columns;
    if (last[conIdColumn] && index === 0) {
        last = {};
    }
    if (last[conIdColumn]) return 0;
    last[conIdColumn] = 1;
    let list = data.filter(item => {
        return item.customerId === groupBy;
    })
    return list.length;
}
export function getTableHead(list, _this) {
    return [
        {
            title: "客户名称",
            align: "left",
            dataIndex: "customerName",
            customRender: (value, row, index) => {
                const obj = {
                    children: value,
                    attrs: {},
                }
                obj.attrs.rowSpan = mergeMyCells(row.customerId, list, 'customerId', index)
                return obj
            },
        },
        {
            title: "客户类型",
            align: "left",
            dataIndex: "customerType",
            customRender: (value, row, index) => {
                const obj = {
                    children: value,
                    attrs: {},
                }
                obj.attrs.rowSpan = mergeMyCells(row.customerId, list, 'customerType', index)
                return obj
            }
        },
        {
            title: "签约次数",
            align: "center",
            dataIndex: "signCount",

            customRender: (value, row, index) => {
                let child = _this.$createElement("a", {
                    domProps: {
                        innerHTML: value
                    },
                    on: {
                        click: function () {
                            _this.signingTimes(row, index);
                        }
                    }
                });
                const obj = {
                    children: child,
                    attrs: {},
                }
                obj.attrs.rowSpan = mergeMyCells(row.customerId, list, 'signCount', index)
                return obj
            }
        },
        {
            title: "合同名称",
            align: "center",
            scopedSlots: {
                customRender: 'contractName'
            }
        },
        {
            title: "合同起始时间",
            align: "center",
            dataIndex: "contractValidateDate"
        },
        {
            title: "合同终止时间",
            align: "center",
            dataIndex: "contractInvalidateDate"
        },
        {
            title: "水表信息",
            align: "center",
            dataIndex: "equipmentInfo"
        },
        {
            title: "区域",
            align: "center",
            dataIndex: "equipmentDistrictName"
        },
        {
            title: "本次抄表时间",
            align: "center",
            dataIndex: "currentFlowDate"
        },
        {
            title: "本次表底",
            align: "center",
            dataIndex: "currentFlow"
        },
        {
            title: "上次抄表时间",
            align: "center",
            dataIndex: "previousFlowDate"
        },
        {
            title: "上次表底",
            align: "center",
            dataIndex: "previousFlow"
        },
        {
            title: "本次用水量",
            align: "center",
            dataIndex: "currentUsedFlow"
        },
        {
            title: "本期水价",
            align: "center",
            dataIndex: "currentWaterPrice"
        },
        {
            title: "本次应缴费用",
            align: "center",
            dataIndex: "currentCost"
        },
        {
            title: "结算结果",
            align: "center",
            dataIndex: "status"
        }

    ]
}

//tableHead.js 这个合并混入到了right.js