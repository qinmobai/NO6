<template>
	<el-dialog :title="title" v-model="formVisible" :before-close="clear">
		<el-form ref="formRef" :model="form" label-width="120px">
			<el-form-item label="预约编号">
				<el-input v-model="form.yuyuebianhao" readonly></el-input>
			</el-form-item>
			<el-form-item label="用户账号">
				<el-input v-model="form.zhanghao" readonly></el-input>
			</el-form-item>
			<el-form-item label="用户手机">
				<el-input v-model="form.shouji" readonly></el-input>
			</el-form-item>
			<el-form-item label="医生账号">
				<el-input v-model="form.yishengzhanghao" readonly></el-input>
			</el-form-item>
			<el-form-item label="医生电话">
				<el-input v-model="form.dianhua" readonly></el-input>
			</el-form-item>
			<el-form-item label="通知类型">
				<el-input v-model="form.tongzhileixing" readonly></el-input>
			</el-form-item>
			<el-form-item label="通知内容">
				<el-input type="textarea" v-model="form.tongzhineirong" rows="4" readonly></el-input>
			</el-form-item>
			<el-form-item label="发送时间">
				<el-input v-model="form.fasongshijian" readonly></el-input>
			</el-form-item>
			<el-form-item label="接收状态">
				<el-tag :type="getStatusTagType(form.jieshouzhuangtai)">
					{{getStatusText(form.jieshouzhuangtai)}}
				</el-tag>
			</el-form-item>
			<el-form-item label="失败原因" v-if="form.jieshouzhuangtai === '2'">
				<el-input type="textarea" v-model="form.shibaiyuanyin" rows="3" readonly></el-input>
			</el-form-item>
			<el-form-item label="重试次数">
				<el-tag :type="form.zhongshicishu > 0 ? 'warning' : 'info'">
					{{form.zhongshicishu || 0}}
				</el-tag>
			</el-form-item>
			<el-form-item label="最后重试时间" v-if="form.zuihouzhongshishijian">
				<el-input v-model="form.zuihouzhongshishijian" readonly></el-input>
			</el-form-item>
			<el-form-item label="处理状态">
				<el-tag :type="getHandleStatusTagType(form.chulizhuangtai)">
					{{getHandleStatusText(form.chulizhuangtai)}}
				</el-tag>
			</el-form-item>
			<el-form-item label="处理备注" v-if="form.chulibeizhu">
				<el-input type="textarea" v-model="form.chulibeizhu" rows="3" readonly></el-input>
			</el-form-item>
			<el-form-item label="处理人" v-if="form.chuliren">
				<el-input v-model="form.chuliren" readonly></el-input>
			</el-form-item>
			<el-form-item label="处理时间" v-if="form.chulishijian">
				<el-input v-model="form.chulishijian" readonly></el-input>
			</el-form-item>
			<el-form-item label="创建时间">
				<el-input v-model="form.addtime" readonly></el-input>
			</el-form-item>
		</el-form>
		<template #footer>
			<el-button @click="clear">关闭</el-button>
		</template>
	</el-dialog>
</template>

<script setup>
import {
	ref,
	reactive,
	getCurrentInstance
} from 'vue'

const context = getCurrentInstance()?.appContext.config.globalProperties;
const emit = defineEmits(['formModelChange'])

const formVisible = ref(false)
const title = ref('')
const form = reactive({
	id: null,
	yuyueid: null,
	yuyuebianhao: '',
	zhanghao: '',
	shouji: '',
	yishengzhanghao: '',
	dianhua: '',
	tongzhileixing: '',
	tongzhineirong: '',
	fasongshijian: '',
	jieshouzhuangtai: '',
	shibaiyuanyin: '',
	zhongshicishu: 0,
	zuihouzhongshishijian: '',
	chulizhuangtai: '',
	chulibeizhu: '',
	chuliren: '',
	chulishijian: '',
	addtime: ''
})

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

const init = (id, type) => {
	if(id) {
		title.value = '查看通知发送记录'
		getInfo(id)
	}
	formVisible.value = true
}

const getInfo = (id) => {
	context.$http({
		url: `tongzhijilu/info/${id}`,
		method: 'get'
	}).then(res => {
		if(res.data.code === 0) {
			Object.assign(form, res.data.data)
		}
	})
}

const clear = () => {
	formVisible.value = false
	Object.assign(form, {
		id: null,
		yuyueid: null,
		yuyuebianhao: '',
		zhanghao: '',
		shouji: '',
		yishengzhanghao: '',
		dianhua: '',
		tongzhileixing: '',
		tongzhineirong: '',
		fasongshijian: '',
		jieshouzhuangtai: '',
		shibaiyuanyin: '',
		zhongshicishu: 0,
		zuihouzhongshishijian: '',
		chulizhuangtai: '',
		chulibeizhu: '',
		chuliren: '',
		chulishijian: '',
		addtime: ''
	})
}

defineExpose({
	init
})
</script>

<style lang="scss" scoped>
</style>
