<template>
<!-- 资产中心-资产管理-资产录入-bom清单-新增 -->
    <a-drawer
      :title="drawerAddTitle"
      :width="720"
      @close="closeModal"
      :visible="visible"
      :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
    >
      <a-form layout="vertical" hideRequiredMark>
				<a-row :gutter="16">
					<a-col :span="12">
						<a-form-item label="类型">
							<a-select v-model="bomType" placeholder="请选择类型">
								<a-select-option :value="item.code" v-for="(item,index) in partList" :key="index">{{item.title}}</a-select-option>
							</a-select>
						</a-form-item>
					</a-col>
					<a-col :span="12">
						<a-form-item label="备品备件信息">
							<a-select v-model="sparepartsId" placeholder="请选择备品备件信息">
								<a-select-option :value="item.id" v-for="(item,index) in sparePartsList" :key="index">{{item.sparepartsName}}</a-select-option>
							</a-select>
						</a-form-item>
					</a-col>
				</a-row>
			</a-form>
			<div :style="{
                    position: 'absolute',
                    left: 0,
                    bottom: 0,
                    width: '100%',
                    borderTop: '1px solid #e9e9e9',
                    padding: '10px 16px',
                    background: '#fff',
                    textAlign: 'right',
                }">

				<a-button :style="{marginRight: '8px'}" @click="closeModal">
					关闭
				</a-button>
				<a-button @click="addSubmit" type="primary">提交</a-button>
			</div>
    </a-drawer>
    
</template>

<script>
	import bomAddModal from '@/assets/js/equipmentAccount/basicInformation/modules/BOMAddModal.js';
	export default{
	    ...bomAddModal
	}
</script>
