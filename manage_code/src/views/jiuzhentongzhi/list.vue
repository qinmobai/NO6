<template>
	<div>
		<div class="center_view">
			<div class="list_search_view">
				<el-form :model="searchQuery" class="search_form" >
					<div class="search_view">
						<div class="search_label">
							医生账号：
						</div>
						<div class="search_box">
							<el-input class="search_inp" v-model="searchQuery.yishengzhanghao" placeholder="医生账号"
								clearable>
							</el-input>
						</div>
					</div>
					<div class="search_view">
						<div class="search_label">
							账号：
						</div>
						<div class="search_box">
							<el-input class="search_inp" v-model="searchQuery.zhanghao" placeholder="账号"
								clearable>
							</el-input>
						</div>
					</div>
					<div class="search_view">
						<div class="search_label">
							发送状态：
						</div>
						<div class="search_box">
							<el-select class="search_inp" v-model="searchQuery.sendStatus" placeholder="发送状态" clearable>
								<el-option label="全部" value=""></el-option>
								<el-option label="待发送" :value="0"></el-option>
								<el-option label="发送成功" :value="1"></el-option>
								<el-option label="发送失败" :value="2"></el-option>
							</el-select>
						</div>
					</div>
					<div class="search_btn_view">
						<el-button class="search_btn" type="primary" @click="searchClick()" size="small">搜索</el-button>
					</div>
				</el-form>
				<div class="btn_view">
					<el-button class="add_btn" type="success" @click="addClick" v-if="btnAuth('jiuzhentongzhi','新增')">
						<i class="iconfont icon-xinzeng1"></i>
						新增
					</el-button>
					<el-button class="del_btn" type="danger" :disabled="selRows.length?false:true" @click="delClick(null)"  v-if="btnAuth('jiuzhentongzhi','删除')">
						<i class="iconfont icon-shanchu4"></i>
						删除
					</el-button>
					<el-button class="retry_btn" type="warning" :disabled="selRows.length?false:true" @click="retryBatchClick"  v-if="btnAuth('jiuzhentongzhi','重试')">
						<i class="iconfont icon-xinxi"></i>
						批量重试
					</el-button>
					<el-button class="stat_btn" type="info" @click="statisticsClick"  v-if="btnAuth('jiuzhentongzhi','统计')">
						<i class="iconfont icon-tongji"></i>
						查看统计
					</el-button>
				</div>
			</div>
			<el-table
				v-loading="listLoading"
				border
				:stripe='false'
				@selection-change="handleSelectionChange"
				ref="table"
				v-if="btnAuth('jiuzhentongzhi','查看')"
				:data="list"
				@row-click="listChange">
				<el-table-column :resizable='true' align="left" header-align="left" type="selection" width="55" />
				<el-table-column label="序号" width="70" :resizable='true' align="left" header-align="left">
					<template #default="scope">{{ (listQuery.page-1)*listQuery.limit+scope.$index + 1}}</template>
				</el-table-column>
				<el-table-column min-width="120"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="tongzhibianhao"
					label="通知编号">
					<template #default="scope">
						{{scope.row.tongzhibianhao}}
					</template>
				</el-table-column>
				<el-table-column min-width="100"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="tongzhiType"
					label="通知类型">
					<template #default="scope">
						{{scope.row.tongzhiType}}
					</template>
				</el-table-column>
				<el-table-column min-width="100"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="yishengzhanghao"
					label="医生账号">
					<template #default="scope">
						{{scope.row.yishengzhanghao}}
					</template>
				</el-table-column>
				<el-table-column min-width="100"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="zhanghao"
					label="账号">
					<template #default="scope">
						{{scope.row.zhanghao}}
					</template>
				</el-table-column>
				<el-table-column min-width="120"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="jiuzhenshijian"
					label="就诊时间">
					<template #default="scope">
						{{scope.row.jiuzhenshijian}}
					</template>
				</el-table-column>
				<el-table-column min-width="120"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="tongzhishijian"
					label="通知时间">
					<template #default="scope">
						{{scope.row.tongzhishijian}}
					</template>
				</el-table-column>
				<el-table-column min-width="90"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="sendStatus"
					label="发送状态">
					<template #default="scope">
						<el-tag v-if="scope.row.sendStatus == 0" type="info">待发送</el-tag>
						<el-tag v-else-if="scope.row.sendStatus == 1" type="success">发送成功</el-tag>
						<el-tag v-else-if="scope.row.sendStatus == 2" type="danger">发送失败</el-tag>
					</template>
				</el-table-column>
				<el-table-column min-width="80"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="retryCount"
					label="重试次数">
					<template #default="scope">
						{{scope.row.retryCount}} / {{scope.row.maxRetry}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="lastSendTime"
					label="最后发送时间">
					<template #default="scope">
						{{scope.row.lastSendTime}}
					</template>
				</el-table-column>
				<el-table-column min-width="150"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="failReason"
					label="失败原因">
					<template #default="scope">
						<el-tooltip v-if="scope.row.failReason" :content="scope.row.failReason" placement="top">
							<span style="cursor: pointer; max-width: 140px; display: inline-block; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">{{scope.row.failReason}}</span>
						</el-tooltip>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="400" :resizable='true' :sortable='true' align="left" header-align="left">
					<template #default="scope">
						<el-button class="view_btn" type="info" v-if=" btnAuth('jiuzhentongzhi','查看')" @click="infoClick(scope.row.id)">
							<i class="iconfont icon-sousuo2"></i>
							查看
						</el-button>
						<el-button class="edit_btn" type="primary" @click="editClick(scope.row.id)" v-if=" btnAuth('jiuzhentongzhi','修改')">
							<i class="iconfont icon-xiugai5"></i>
							修改
						</el-button>
						<el-button class="retry_btn" type="warning" @click="retryClick(scope.row.id)" v-if="btnAuth('jiuzhentongzhi','重试') && scope.row.sendStatus == 2">
							<i class="iconfont icon-xinxi"></i>
							重试
						</el-button>
						<el-button class="del_btn" type="danger" @click="delClick(scope.row.id)"  v-if="btnAuth('jiuzhentongzhi','删除')">
							<i class="iconfont icon-shanchu4"></i>
							删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<el-pagination
				background
				:layout="layouts.join(',')"
				:total="total"
				:page-size="listQuery.limit"
                v-model:current-page="listQuery.page"
				prev-text="上一页"
				next-text="下一页"
				:hide-on-single-page="false"
				:style='{}'
				:page-sizes="[10, 20, 30, 40, 50, 100]"
				@size-change="sizeChange"
				@current-change="currentChange"  />
		</div>
		<formModel ref="formRef" @formModelChange="formModelChange"></formModel>
		<jiuzhenqiandaoFormModel ref="jiuzhenqiandaoFormModelRef" @formModelChange="formModelChange"></jiuzhenqiandaoFormModel>
		
		<el-dialog v-model="statisticsDialogVisible" title="通知发送统计" width="500px">
			<el-descriptions :column="2" border v-if="statisticsData">
				<el-descriptions-item label="总通知数">{{statisticsData.total}}</el-descriptions-item>
				<el-descriptions-item label="发送成功">
					<el-tag type="success">{{statisticsData.success}}</el-tag>
				</el-descriptions-item>
				<el-descriptions-item label="发送失败">
					<el-tag type="danger">{{statisticsData.fail}}</el-tag>
				</el-descriptions-item>
				<el-descriptions-item label="待发送">
					<el-tag type="info">{{statisticsData.pending}}</el-tag>
				</el-descriptions-item>
			</el-descriptions>
		</el-dialog>
	</div>
</template>
<script setup>
	import axios from 'axios'
    import moment from "moment"
	import {
		reactive,
		ref,
		getCurrentInstance,
		nextTick,
		onMounted,
		watch,
		computed,
	} from 'vue'
	import {
		useRoute,
		useRouter
	} from 'vue-router'
	import {
		ElMessageBox
	} from 'element-plus'
	import {
		useStore
	} from 'vuex';
	const store = useStore()
	const user = computed(()=>store.getters['user/session'])
	const avatar = ref(store.state.user.avatar)
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	import formModel from './formModel.vue'
	
	const tableName = 'jiuzhentongzhi'
	const formName = '就诊通知'
	const route = useRoute()
	
	onMounted(()=>{
	})
	
	const list = ref(null)
	const table = ref(null)
	const listQuery = ref({
		page: 1,
		limit: 10,
		sort: 'id',
		order: 'desc'
	})
	const searchQuery = ref({})
	const selRows = ref([])
	const listLoading = ref(false)
	
	const statisticsDialogVisible = ref(false)
	const statisticsData = ref(null)
	
	const listChange = (row) =>{
		nextTick(()=>{
			table.value.toggleRowSelection(row)
		})
	}
	
	const getList = () => {
		listLoading.value = true
		let params = JSON.parse(JSON.stringify(listQuery.value))
		params['sort'] = 'id'
		params['order'] = 'desc'
		if(searchQuery.value.yishengzhanghao&&searchQuery.value.yishengzhanghao!=''){
			params['yishengzhanghao'] = '%' + searchQuery.value.yishengzhanghao + '%'
		}
		if(searchQuery.value.zhanghao&&searchQuery.value.zhanghao!=''){
			params['zhanghao'] = '%' + searchQuery.value.zhanghao + '%'
		}
		if(searchQuery.value.sendStatus!=null&&searchQuery.value.sendStatus!=''){
			params['sendStatus'] = searchQuery.value.sendStatus
		}
		context.$http({
			url: `${tableName}/page`,
			method: 'get',
			params: params
		}).then(res => {
			listLoading.value = false
			list.value = res.data.data.list
			total.value = Number(res.data.data.total)
		})
	}
	
	const delClick = (id) => {
		let ids = ref([])
		if (id) {
			ids.value = [id]
		} else {
			if (selRows.value.length) {
				for (let x in selRows.value) {
					ids.value.push(selRows.value[x].id)
				}
			} else {
				return false
			}
		}
		ElMessageBox.confirm(`是否删除选中${formName}`, '提示', {
			confirmButtonText: '是',
			cancelButtonText: '否',
			type: 'warning',
		}).then(() => {
			context.$http({
				url: `${tableName}/delete`,
				method: 'post',
				data: ids.value
			}).then(res => {
				context?.$toolUtil.message('删除成功', 'success',()=>{
					getList()
				})
			})
		}).catch(_ => {})
	}
	
	const retryClick = (id) => {
		ElMessageBox.confirm('是否重试发送此通知？', '提示', {
			confirmButtonText: '是',
			cancelButtonText: '否',
			type: 'warning',
		}).then(() => {
			context.$http({
				url: `${tableName}/retry/${id}`,
				method: 'post'
			}).then(res => {
				context?.$toolUtil.message('重试成功', 'success',()=>{
					getList()
				})
			})
		}).catch(_ => {})
	}
	
	const retryBatchClick = () => {
		if (!selRows.value.length) {
			context?.$toolUtil.message('请选择要重试的通知', 'error')
			return
		}
		let ids = selRows.value.filter(row => row.sendStatus == 2).map(row => row.id)
		if (!ids.length) {
			context?.$toolUtil.message('请选择发送失败的通知进行重试', 'error')
			return
		}
		ElMessageBox.confirm(`是否批量重试选中的${ids.length}条失败通知？`, '提示', {
			confirmButtonText: '是',
			cancelButtonText: '否',
			type: 'warning',
		}).then(() => {
			context.$http({
				url: `${tableName}/retryBatch`,
				method: 'post',
				data: ids
			}).then(res => {
				context?.$toolUtil.message('批量重试成功', 'success',()=>{
					getList()
				})
			})
		}).catch(_ => {})
	}
	
	const statisticsClick = () => {
		context.$http({
			url: `${tableName}/statistics`,
			method: 'get'
		}).then(res => {
			statisticsData.value = res.data.data
			statisticsDialogVisible.value = true
		})
	}
	
	const handleSelectionChange = (e) => {
		selRows.value = e
	}
	
	const total = ref(0)
	const layouts = ref(["total","prev","pager","next","sizes","jumper"])
	const sizeChange = (size) => {
		listQuery.value.limit = size
		getList()
	}
	const currentChange = (page) => {
		listQuery.value.page = page
		getList()
	}
	
	const btnAuth = (e,a)=>{
		return context?.$toolUtil.isAuth(e,a)
	}
	
	const searchClick = () => {
		listQuery.value.page = 1
		getList()
	}
	
	const formRef = ref(null)
	const formModelChange=()=>{
		searchClick()
	}
	const addClick = ()=>{
		formRef.value.init()
	}
	const editClick = (id=null)=>{
		if(id){
			formRef.value.init(id,'edit')
			return
		}
		if(selRows.value.length){
			formRef.value.init(selRows.value[0].id,'edit')
		}
	}

	const infoClick = (id=null)=>{
		if(id){
			formRef.value.init(id,'info')
		}
		else if(selRows.value.length){
			formRef.value.init(selRows.value[0].id,'info')
		}
	}
	
	const preClick = (file) =>{
		if(!file){
			context?.$toolUtil.message('文件不存在','error')
		}
		window.open(context?.$config.url + file)
	}
	
	const download = (file) => {
		if(!file){
			context?.$toolUtil.message('文件不存在','error')
		}
		let arr = file.replace(new RegExp('file/', "g"), "")
		axios.get((location.href.split(context?.$config.name).length>1 ? location.href.split(context?.$config.name)[0] :'') + context?.$config.name + '/file/download?fileName=' + arr, {
			headers: {
				token: context?.$toolUtil.storageGet('Token')
			},
			responseType: "blob"
		}).then(({
			data
		}) => {
			const binaryData = [];
			binaryData.push(data);
			const objectUrl = window.URL.createObjectURL(new Blob(binaryData, {
				type: 'application/pdf;chartset=UTF-8'
			}))
			const a = document.createElement('a')
			a.href = objectUrl
			a.download = arr
			a.dispatchEvent(new MouseEvent('click', {
				bubbles: true,
				cancelable: true,
				view: window
			}))
			window.URL.revokeObjectURL(data)
		})
	}
	
	import jiuzhenqiandaoFormModel from '@/views/jiuzhenqiandao/formModel'
	const jiuzhenqiandaoFormModelRef = ref(null)
    const jiuzhenqiandaoCrossAddOrUpdateHandler = (row,type,crossOptAudit,crossOptPay,statusColumnName,tips,statusColumnValue) => {
		if(statusColumnName!=''&&!statusColumnName.startsWith("[")) {
			var obj = row
			for (var o in obj){
				if(o==statusColumnName && obj[o]==statusColumnValue){
					context?.$toolUtil.message(tips,'error')
					return;
				}
			}
		}
		nextTick(()=>{
			jiuzhenqiandaoFormModelRef.value.init(row.id,'cross','签到',row,'jiuzhentongzhi',statusColumnName,tips,statusColumnValue)
		})
    }
	
	const init = () => {
		getList()
	}
	init()
</script>
<style lang="scss" scoped>

	.list_search_view {
		.search_form {
			.search_view {
				.search_label {
				}
				.search_box {
					:deep(.search_inp) {
					}
				}
			}
			.search_btn_view {
				.search_btn {
				}
				.search_btn:hover {
				}
			}
		}
		.btn_view {
			:deep(.el-button--default){
			}
			:deep(.el-button--default:hover){
			}
			:deep(.el-button--success){
			}
			:deep(.el-button--success:hover){
			}
			:deep(.el-button--danger){
			}
			:deep(.el-button--danger:hover){
			}
			:deep(.el-button--warning){
			}
			:deep(.el-button--warning:hover){
			}
			:deep(.el-button--info){
			}
			:deep(.el-button--info:hover){
			}
		}
	}
	.el-table {
		:deep(.el-table__header-wrapper) {
			thead {
				tr {
					th {
						.cell {
						}
					}
				}
			}
		}
		:deep(.el-table__body-wrapper) {
			tbody {
				tr {
					td {
						.cell {
							.el-button--primary {
							}
							.el-button--primary:hover {
							}
							.el-button--info {
							}
							.el-button--info:hover {
							}
							.el-button--danger {
							}
							.el-button--danger:hover {
							}
							.el-button--success {
							}
							.el-button--success:hover {
							}
							.el-button--warning {
							}
							.el-button--warning:hover {
							}
						}
					}
				}
				tr:hover {
					td {
					}
				}
			}
		}
	}
	.el-pagination {
		:deep(.el-pagination__total) {
		}
		:deep(.btn-prev) {
		}
		:deep(.btn-next) {
		}
		:deep(.btn-prev:disabled) {
		}
		:deep(.btn-next:disabled) {
		}
		:deep(.el-pager) {
			.number {
			}
			.number:hover {
			}
			.number.is-active {
			}
		}
		:deep(.el-pagination__sizes) {
			display: inline-block;
			vertical-align: top;
			font-size: 13px;
			line-height: 28px;
			height: 28px;
			.el-select {
			}
		}
		:deep(.el-pagination__jump) {
			.el-input {
			}
		}
	}
</style>
