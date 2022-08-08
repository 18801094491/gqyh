import BomAddModal from '@/views/equipmentAccount/basicInformation/modules/BOMAddModal'
import { getBOMLIst, getCategory } from '@/api/equipmentAccount-t/BOMListModal.js'

export default {
	name: "BOMListModal",
	props: ['BOMListVisible', 'BOMListRecord'],
	components: {
		BomAddModal
	},
	data() {
		return {
			stateRuleTitle: 'BOM清单',
			queryParam: {},
			modalVisible: false,
			openAddVisible: false,
			dataSource: [],
			ipagination: {
				current: 1,
				pageSize: 5,
				pageSizeOptions: ['10', '20', '30'],
				showTotal: (total, range) => {
					return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
				},
				showQuickJumper: true,
				// showSizeChanger: true,
				total: 0
			},
			columns: [{
				title: '序号',
				dataIndex: '',
				key: 'rowIndex',
				width: 60,
				align: "center",
				customRender: function (t, r, index) {
					return parseInt(index) + 1;
				}
			},
			{
				title: '类型',
				align: "center",
				dataIndex: 'bomTypeName'
			},
			{
				title: '备品备件',
				align: "center",
				dataIndex: 'sparepartsName'
			},
			{
				title: '备品备件型号',
				align: "center",
				dataIndex: 'sparepartsSpcesName',
			},
			{
				title: '备品备件规格',
				align: "center",
				dataIndex: 'sparepartsModelName'
			},
			{
				title: '操作',
				dataIndex: 'action',
				align: "center",
				scopedSlots: { customRender: 'action' },
			}
			],
			loading: false,
			bidSegmentList: [], // 类型
			basicId: '', // 基础信息id        
			BOMAddVisible: false,
			drawerAddTitle: '',
			BOMAddRecord: null,
			BOMAddId: '',
		}
	},
	watch: {
		BOMListVisible(val) {
			this.modalVisible = val
		},
		BOMListRecord: {
			handler: function (newVal, oldVal) {
				this.BOMRecord = newVal;
				this.updata(this.BOMRecord);
				this.basicId = newVal.id
			},
			deep: true,
		}
	},
	mounted() {
		getCategory({ pcode: 'A19' }, this, 'bidSegmentList')
	},
	methods: {
		/**
		 * 关闭bom
		 */
		closeBOMListModal() {
			this.$emit('closeBOMListModal', 'BOMListVisible')
		},
		/**
		 * 关闭添加
		 */
		closeAddModal(modalVisible) {
			this[modalVisible] = false
		},
		//新增/修改弹框
		handleAdd(type, data) {
			this.openAddVisible = true;
			this.$refs.BOMAddRef.getList();
			if (type == 'add') {
				this.drawerAddTitle = '新增BOM清单';
			} else {
				this.drawerAddTitle = '修改BOM清单';
				this.BOMAddRecord = Object.assign({}, this.BOMAddRecord, data);
				this.BOMAddId = data.id;
			}
		},
		/**
		 * 修改
		 */
		updata(record) {
			let data = {
				basicId: record.id, // 基础信息id
				bomType: this.queryParam.bomType, // BOM类型
				sparepartsModelName: this.queryParam.sparepartsModelName, // 备件规格名称
				sparepartsName: this.queryParam.sparepartsName, // 备件名称
				sparepartsSpcesName: this.queryParam.sparepartsSpcesName, // 备件型号名称
				pageNo: this.ipagination.current,
				pageSize: this.ipagination.pageSize
			}
			this.loading = true;
			getBOMLIst(data, this)
		},
		/**
		 * 查询
		 */
		searchQuery() {
			this.updata(this.BOMListRecord);
		},
		/**
		 * 重置
		 */
		searchReset() {
			this.queryParam = {};
			this.updata(this.BOMListRecord);
		},
	}
}
//basicInformation/BOMListModal页面组件混入的basicInformation/BOMListModal.js