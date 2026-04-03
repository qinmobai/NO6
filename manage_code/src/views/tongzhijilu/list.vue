<template>
	<div>
		<div class="center_view">
			<div class="list_search_view">
				<el-form :model="searchQuery" class="search_form" >
					<div class="search_view">
						<div class="search_label">
							预约编号：
						</div>
						<div class="search_box">
							<el-input class="search_inp" v-model="searchQuery.yuyuebianhao" placeholder="预约编号"
								clearable>
							</el-input>
						</div>
					</div>
					<div class="search_view">
						<div class="search_label">
							用户账号：
						</div>
						<div class="search_box">
							<el-input class="search_inp" v-model="searchQuery.zhanghao" placeholder="用户账号"
								clearable>
							</el-input>
						</div>
					</div>
					<div class="search_view">
						<div class="search_label">
							通知类型：
						</div>
						<div class="search_box">
							<el-select v-model="searchQuery.tongzhileixing" placeholder="请选择通知类型" clearable>
								<el-option
									v-for="item in notificationTypes"
									:key="item.code"
									:label="item.name"
									:value="item.name">
								</el-option>
							</el-select>
						</div>
					</div>
					<div class="search_view">
						<div class="search_label">
							接收状态：
						</div>
						<div class="search_box">
							<el-select v-model="searchQuery.jieshouzhuangtai" placeholder="请选择接收状态" clearable>
								<el-option label="待发送" value="0"></el-option>
								<el-option label="发送成功" value="1"></el-option>
								<el-option label="发送失败" value="2"></el-option>
								<el-option label="已接收" value="3"></el-option>
							</el-select>
						</div>
					</div>
					<div class="search_view">
						<div class="search_label">
							处理状态：
						</div>
						<div class="search_box">
							<el-select v-model="searchQuery.chulizhuangtai" placeholder="请选择处理状态" clearable>
								<el-option label="未处理" value="0"></el-option>
								<el-option label="已处理" value="1"></el-option>
								<el-option label="已忽略" value="2"></el-option>
							</el-select>
						</div>
					</div>
					<div class="search_btn_view">
						<el-button class="search_btn" type="primary" @click="searchClick()" size="small">搜索</el-button>
						<el-button class="search_btn" type="warning" @click="showFailedOnly()" size="small">仅看失败</el-button>
					</div>
				</el-form>
				<div class="btn_view">
					<el-button class="add_btn" type="success" @click="batchRetryClick" v-if="btnAuth('tongzhijilu','批量重试')">
						<i class="iconfont icon-zhongshi"></i>
						批量重试失败通知
					</el-button>
					<el-button class="del_btn" type="danger" :disabled="selRows.length?false:true" @click="delClick(null)"  v-if="btnAuth('tongzhijilu','删除')">
						<i class="iconfont icon-shanchu4"></i>
						删除
					</el-button>
				</div>
			</div>
			<el-table
				v-loading="listLoading"
				border
				:stripe='false'
				@selection-change="handleSelectionChange"
				ref="table"
				v-if="btnAuth('tongzhijilu','查看')"
				:data="list"
				@row-click="listChange">
				<el-table-column :resizable='true' align="left" header-align="left" type="selection" width="55" />
				<el-table-column label="序号" width="70" :resizable='true' align="left" header-align="left">
					<template #default="scope">{{ (listQuery.page-1)*listQuery.limit+scope.$index + 1}}</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="yuyuebianhao"
					label="预约编号">
					<template #default="scope">
						{{scope.row.yuyuebianhao}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="zhanghao"
					label="用户账号">
					<template #default="scope">
						{{scope.row.zhanghao}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="shouji"
					label="用户手机">
					<template #default="scope">
						{{scope.row.shouji}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
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
				<el-table-column min-width="160"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="tongzhileixing"
					label="通知类型">
					<template #default="scope">
						<el-tag :type="getNotificationTypeTag(scope.row.tongzhileixing)">
							{{scope.row.tongzhileixing}}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column min-width="200"
					:resizable='true'
					align="left"
					header-align="left"
					prop="tongzhineirong"
					label="通知内容"
					show-overflow-tooltip>
					<template #default="scope">
						{{scope.row.tongzhineirong}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="fasongshijian"
					label="发送时间">
					<template #default="scope">
						{{scope.row.fasongshijian}}
					</template>
				</el-table-column>
				<el-table-column min-width="120"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="jieshouzhuangtai"
					label="接收状态">
					<template #default="scope">
						<el-tag :type="getStatusTagType(scope.row.jieshouzhuangtai)">
							{{getStatusText(scope.row.jieshouzhuangtai)}}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column min-width="100"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="zhongshicishu"
					label="重试次数">
					<template #default="scope">
						<el-tag :type="scope.row.zhongshicishu > 0 ? 'warning' : 'info'">
							{{scope.row.zhongshicishu || 0}}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column min-width="120"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="chulizhuangtai"
					label="处理状态">
					<template #default="scope">
						<el-tag :type="getHandleStatusTagType(scope.row.chulizhuangtai)">
							{{getHandleStatusText(scope.row.chulizhuangtai)}}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="280" :resizable='true' :sortable='true' align="left" header-align="left">
					<template #default="scope">
						<el-button class="view_btn" type="info" v-if=" btnAuth('tongzhijilu','查看')" @click="infoClick(scope.row.id)">
							<i class="iconfont icon-sousuo2"></i>
							查看
						</el-button>
						<el-button class="edit_btn" type="primary" @click="handleClick(scope.row)" v-if=" btnAuth('tongzhijilu','处理') && scope.row.jieshouzhuangtai == '2'">
							<i class="iconfont icon-chuli"></i>
							处理
						</el-button>
						<el-button class="edit_btn" type="warning" @click="retryClick(scope.row)" v-if=" btnAuth('tongzhijilu','重试') && scope.row.jieshouzhuangtai == '2'">
							<i class="iconfont icon-zhongshi"></i>
							重试
						</el-button>
						<el-button class="del_btn" type="danger" @click="delClick(scope.row.id)"  v-if="btnAuth('tongzhijilu','删除')">
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
		
		<!-- 处理对话框 -->
		<el-dialog title="处理通知记录" v-model="handleDialogVisible" width="500px">
			<el-form :model="handleForm" label-width="100px">
				<el-form-item label="处理状态">
					<el-select v-model="handleForm.chulizhuangtai" placeholder="请选择处理状态">
						<el-option label="已处理" value="1"></el-option>
						<el-option label="已忽略" value="2"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="处理备注">
					<el-input type="textarea" v-model="handleForm.chulibeizhu" rows="4" placeholder="请输入处理备注"></el-input>
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="handleDialogVisible = false">取消</el-button>
				<el-button type="primary" @click="submitHandle">确定</el-button>
			</template>
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
	import formModel from './formModel.vue'
	
	const store = useStore()
	const user = computed(()=>store.getters['user/session'])
	const avatar = ref(store.state.user.avatar)
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	
	//基础信息
	const tableName = 'tongzhijilu'
	const formName = '通知发送记录'
	const route = useRoute()
	
	//通知类型列表
	const notificationTypes = ref([])
	
	//处理对话框
	const handleDialogVisible = ref(false)
	const handleForm = reactive({
		id: null,
		chulizhuangtai: '1',
		chulibeizhu: ''
	})
	
	//基础信息
	onMounted(()=>{
		getNotificationTypes()
	})
	
	//获取通知类型列表
	const getNotificationTypes = () => {
		context.$http({
			url: `${tableName}/types`,
			method: 'get'
		}).then(res => {
			if(res.data.code === 0) {
				notificationTypes.value = res.data.data
			}
		})
	}
	
	//获取状态标签类型
	const getStatusTagType = (status) => {
		switch(status) {
			case '0': return 'info'
			case '1': return 'success'
			case '2': return 'danger'
			case '3': return 'success'
			default: return 'info'
		}
	}
	
	//获取状态文本
	const getStatusText = (status) => {
		switch(status) {
			case '0': return '待发送'
			case '1': return '发送成功'
			case '2': return '发送失败'
			case '3': return '已接收'
			default: return '未知'
		}
	}
	
	//获取处理状态标签类型
	const getHandleStatusTagType = (status) => {
		switch(status) {
			case '0': return 'info'
			case '1': return 'success'
			case '2': return 'warning'
			default: return 'info'
		}
	}
	
	//获取处理状态文本
	const getHandleStatusText = (status) => {
		switch(status) {
			case '0': return '未处理'
			case '1': return '已处理'
			case '2': return '已忽略'
			default: return '未处理'
		}
	}
	
	//获取通知类型标签
	const getNotificationTypeTag = (type) => {
		if(type && type.includes('成功')) return 'success'
		if(type && type.includes('提醒')) return 'warning'
		return 'info'
	}
	
	//列表数据
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
	const listChange = (row) =>{
		nextTick(()=>{
			table.value.toggleRowSelection(row)
		})
	}
	
	//列表
	const getList = () => {
		listLoading.value = true
		let params = JSON.parse(JSON.stringify(listQuery.value))
		params['sort'] = 'id'
		params['order'] = 'desc'
		if(searchQuery.value.yuyuebianhao && searchQuery.value.yuyuebianhao != ''){
			params['yuyuebianhao'] = '%' + searchQuery.value.yuyuebianhao + '%'
		}
		if(searchQuery.value.zhanghao && searchQuery.value.zhanghao != ''){
			params['zhanghao'] = '%' + searchQuery.value.zhanghao + '%'
		}
		if(searchQuery.value.tongzhileixing && searchQuery.value.tongzhileixing != ''){
			params['tongzhileixing'] = searchQuery.value.tongzhileixing
		}
		if(searchQuery.value.jieshouzhuangtai && searchQuery.value.jieshouzhuangtai != ''){
			params['jieshouzhuangtai'] = searchQuery.value.jieshouzhuangtai
		}
		if(searchQuery.value.chulizhuangtai && searchQuery.value.chulizhuangtai != ''){
			params['chulizhuangtai'] = searchQuery.value.chulizhuangtai
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
	
	//仅看失败
	const showFailedOnly = () => {
		searchQuery.value.jieshouzhuangtai = '2'
		searchQuery.value.chulizhuangtai = '0'
		listQuery.value.page = 1
		getList()
	}
	
	//删
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
	
	//多选
	const handleSelectionChange = (e) => {
		selRows.value = e
	}
	
	//列表数据
	//分页
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
	//分页
	
	//权限验证
	const btnAuth = (e,a)=>{
		return context?.$toolUtil.isAuth(e,a)
	}
	
	//搜索
	const searchClick = () => {
		listQuery.value.page = 1
		getList()
	}
	
	//表单
	const formRef = ref(null)
	const formModelChange=()=>{
		searchClick()
	}
	
	const infoClick = (id=null)=>{
		if(id){
			formRef.value.init(id,'info')
		}
		else if(selRows.value.length){
			formRef.value.init(selRows.value[0].id,'info')
		}
	}
	
	//处理点击
	const handleClick = (row) => {
		handleForm.id = row.id
		handleForm.chulizhuangtai = '1'
		handleForm.chulibeizhu = ''
		handleDialogVisible.value = true
	}
	
	//提交处理
	const submitHandle = () => {
		context.$http({
			url: `${tableName}/handle`,
			method: 'post',
			data: handleForm
		}).then(res => {
			if(res.data.code === 0) {
				context?.$toolUtil.message('处理成功', 'success')
				handleDialogVisible.value = false
				getList()
			} else {
				context?.$toolUtil.message(res.data.msg || '处理失败', 'error')
			}
		})
	}
	
	//重试点击
	const retryClick = (row) => {
		ElMessageBox.confirm('确定要重试发送该通知吗？', '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning'
		}).then(() => {
			context.$http({
				url: `${tableName}/retry/${row.id}`,
				method: 'post'
			}).then(res => {
				if(res.data.code === 0) {
					context?.$toolUtil.message('重试发送成功', 'success')
					getList()
				} else {
					context?.$toolUtil.message(res.data.msg || '重试发送失败', 'error')
				}
			})
		}).catch(_ => {})
	}
	
	//批量重试
	const batchRetryClick = () => {
		ElMessageBox.confirm('确定要批量重试所有失败的通知吗？', '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning'
		}).then(() => {
			context.$http({
				url: `${tableName}/retryBatch`,
				method: 'post'
			}).then(res => {
				if(res.data.code === 0) {
					const data = res.data.data
					context?.$toolUtil.message(`批量重试完成，成功：${data.successCount}，失败：${data.failCount}`, 'success')
					getList()
				} else {
					context?.$toolUtil.message(res.data.msg || '批量重试失败', 'error')
				}
			})
		}).catch(_ => {})
	}
	
	//初始化
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
							.el-button--info {
							}
							.el-button--info:hover {
							}
							.el-button--primary {
							}
							.el-button--primary:hover {
							}
							.el-button--danger {
							}
							.el-button--danger:hover {
							}
							.el-button--warning {
							}
							.el-button--warning:hover {
							}
						}
					}
				}
			}
		}
	}
</style>
