import { getKnowledge,changeKnowlegeIdData } from '@/api/operationCenter-t/equipmentSearcher.js'
export default {
        name: "equiqKnowledge",
        props: {
            item: Object,
        },
        data() {
            return {
                knowledgeItem:this.item,
                columns:[
                	{
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
	                    title: '知识名称',
	                    align: "center",
	                    dataIndex: 'knowlegeName',	
	                    width:100
	                },
	                {
	                    title: '设备名称',
	                    align: "center",
	                    dataIndex: 'equipmentName',	
	                    width:200
	                },
	                {
	                    title: '设备类型',
	                    align: "center",
	                    dataIndex: 'equipmentTypeName',	
	                    width:100
	                },
	                {
	                    title: '规格',
	                    align: "center",
	                    dataIndex: 'equipmentSpecsName',	
	                    width:100
	                },
	                {
	                    title: '型号',
	                    align: "center",
	                    dataIndex: 'equipmentModelName',	
	                    width:200
	                },
	                {
	                    title: '供应商',
	                    align: "center",
	                    dataIndex: 'supplierName',	
	                    width:200
	                },
	                {
	                    title: '操作',
	                    align: "center",
	                    fixed: 'right',
	                    dataIndex: 'action',
	                    width:80,
	                    scopedSlots: {
	                        customRender: 'action'
	                    },	                    
	                },
                ],
                dataSource:[],
                ipagination:{
                	
                },
                loading:false,
                // 详情
                knowlegeItemVoList: [
	                {
	                    operationName: '',
	                    knowlegeOperationList: [
	                        {
	                            displayOrder: '',
	                            operationItem: ''
	                        }
	                    ],
	                    knowlegeCautionList: [
	                        {
	                            displayOrder: '',
	                            cautionItem: ''
	                        }
	                    ],
	                    knowlegeAttachList:[
	                        
	                    ]
	                }
	            ],
	            detailsvisible:false,
	            detailsTitle:'',
            }
        },
        created(){
        	this.updata();
        },
        methods:{
        	updata(){
        		this.loading = true;
        		getKnowledge(this.knowledgeItem.id,this)
        	},
        	getDetail(record){
        		this.detailsTitle=record.equipmentTypeName;
	            changeKnowlegeIdData({
	                id: record.id
	            }, this);
	            this.detailsvisible = true;
        	},
        	detailsClose(){
	            this.detailsvisible=false;
	        },
        }
	}
	//equipmentSearcher/modules/equiqKnowledge.vue 页面混入了equipmentSearcher/modules/equiqKnowledge.js