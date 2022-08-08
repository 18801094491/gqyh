import request from '../common/request.js'
import moment from '../common/moment.js'

//获取图标数据(总接口)
export const getChartsData = (_this, data, update) => {
	request({
		method: 'GET',
		url: '/statistic/chartView/app/charts',
		data: {} //参数
	}).then(res => {
		if (res.code == 200) {
			if (update) {
				res.result.forEach((item, index) => {
					getChartData(item.id, index, data, _this, update)
				})
			} else {
				_this.idIndex = 0;
				_this.idList = res.result;
				_this.chartList = [];
				getChartData(_this.idList[0].id, _this.idIndex, data, _this, update, true)
			}
		}
	}).catch(err => {
		console.log(err, '总数据接口请求失败')
	})
}

//获取每个图表数据(接口)
export const getChartData = (id, index, data, _this, update, forEach=false) => {
	request({
		method: 'POST',
		url: '/statistic/chartView/app/'+id,
		data: data //参数
	}).then(res => {
		if (res.code*1 === 200) {
			let scroll = false;
			let s_data = [];
			let chartList = _this.chartList;
			let colors = ['#1E90FF','#00CDCD'];
			let chartData = {categories:[], series: []}
			let markLine = {type: 'dash', dashLength: 2, data: []};
			
			let series = res.result.series
			let charsValue = res.result.charsValue
			
			if (update) { //更新
				chartList.forEach(item => {
					if (item.title === res.result.title) { //数据替换
						item = {title: res.result.title, data_len: series[0].data.length}
					}
				}) 
			} else {
				chartList.push({title: res.result.title, data_len: series[0].data.length});
			}
			_this.chartList = chartList;
			// if (!series[0].data.length) return;
			charsValue.forEach((item, index)=> {
				markLine.data.push({
					value: item.max+'',
					yAxisIndex: item.max+'',
					lineColor: colors[index],
				},{
					value: item.min+'',
					yAxisIndex: item.min+'',
					lineColor: colors[index],
				},)
			})
			series.forEach((item, index) => {
				if (item.data.length > 5) scroll = true
				if (item.data.length) {
					item.data.forEach(item2 => {
					if (index===0) {
						chartData.categories.push(moment('m-d H:i', item2[0]/1000));
					}
					s_data.push(item2[1])
				}) 
				} else {
					s_data.push(0)
					chartData.categories.push(0);
				} 
				chartData.series.push({name: item.name || '暂无', data: s_data, color: colors[index]})
				s_data = []
			})
			setTimeout(() => {
				_this.showChart("canvasChart"+(index+1), chartData, markLine, scroll);
			})
			_this.idList.shift()
			if (forEach && _this.idList[0] && _this.idList[0].id) {
				getChartData(_this.idList[0].id, ++_this.idIndex, data, _this, update,  true)
			}
		}
	}).catch(err => {
		console.log(err, '子数据接口请求失败, id:', id)
	})
}
