function handelData(data, val) {
	for (var i = 0; i < data.length; i++) {
		data[i].value = val;
	}
}
function handelData1(data, val) {
	for (var i = 8; i < 36; i++) {
		data[i].value = val;
	}
}
function handelData2(data, val) {
	for (var i = 36; i < data.length; i++) {
		data[i].value = val;
	}
}
$(function() {
	handelData(dataOrganization, 255);
	handelData(dataHome, 255);
	handelData(dataCity, 255);
	handelData(dataHealthy, 255);
	handelData1(dataHealthy, 150);
	handelData2(dataHealthy, 70);
//	var colorCyle = [ '#58fbff','#cbf451','#7788f8','#fadf75']; // 各点的颜色
//	var colorCyle = [ '#88d9e6','#6ec468','#7788f8','#fbd59e']; // 各点的颜色
	var colorCyle = [ '#00fdff','#81d867','#f9fc00','#fac447'];
	var myChart = echarts.init(document.getElementById('chinaMap'));
	var convertData = function(data) {
		var res = [];
		for (var i = 0; i < data.length; i++) {
			var geoCoord = geoCoordMap[data[i].name];
			if (geoCoord) {
				res.push({
					name : data[i].name,
					value : geoCoord.concat(data[i].value)
				});
			}
		}
		return res;
	};

	var option = {
		tooltip : {
			trigger : 'item',
			backgroundColor : 'rgba(0,0,0,0.3)',
			formatter : '{a}: {b}',
			textStyle : {
				color : "#fff",
				fontStyle : "normal",// 'italic''oblique'
				fontWight : "normal",// 'normal' 'bold' 'bolder' 'lighter'
										// 100,200,300
				fontFamily : "sans-serif",// 'serif' , 'monospace', 'Arial',
											// 'Courier New', 'Microsoft YaHei',
				fontSize : "18",

			},
			extraCssText : 'box-shadow: 3px 3px 6px rgba(0, 0, 0, 0.75);'
		},
		legend : {
			orient : 'vertical',
			data : [ '机构养老', '居家养老', '城市养老运营', '健康医疗' ],
			textStyle : {
				color : '#fff'
			},
			top : '10%',
			left : '5%'
		},
//		 visualMap: {
//		 min: 0,
//		 max: 200,
//		 calculable: false,
//		 inRange: {
//		 color: colorCyle,
//		 },
//		 textStyle: {
//		 color: '#fff'
//		 }
//		 },
//		xAxis : {
//			show : true,
//		},
//		yAxis : {
//			show : true,
//		},
		geo : {
			map : 'china',
			layoutCenter : [ '30%', '30%' ],
			label : {
				emphasis : {
					show : false
				}
			},
//			regions : [ {
//				name : '广东',
//				itemStyle : {
//					normal : {
//						areaColor : 'red',
//						color : 'red'
//					}
//				},
//				label:{
//					normal:{
//						show:true
//					}
//				}
//			} ],
			roam : false,
			itemStyle : {
				normal : {
					areaColor : '#0c83d9',// 地图颜色
					borderWidth : "1.5",
					borderColor : '#7eeafe',// 边框颜色
					opacity : 0.5
				},
				emphasis : {
					areaColor : '#2d6897', // 鼠标移动上去后地图的颜色
//					shadowColor : 'rgba(93, 182, 216, 0.5)',
//					shadowBlur : 0,
//					shadowOffsetX : -10,
//					shadowOffsetY : 10,
				}
			}
		},
		series : [ {
			name : '机构养老',
			type : 'effectScatter',
			z:4,
			coordinateSystem : 'geo',
			data : convertData(dataOrganization),
			symbolSize : function(val) {
				return val[2] / 10;
			},
			showEffectOn : 'render',
			rippleEffect : {
				brushType : 'stroke',
//				period : 1,
				scale : 4
			},
			hoverAnimation : true,
			itemStyle : {
				normal : {
					color : colorCyle[0],
				}
			},
		}, {
			name : '居家养老',
			type : 'effectScatter',
			z:5,
			coordinateSystem : 'geo',
			data : convertData(dataHome),
			symbolSize : function(val) {
				return val[2] / 10;
			},
			showEffectOn : 'render',
			rippleEffect : {
				brushType : 'stroke',
//				period : 1,
				scale : 4
			},
			hoverAnimation : true,
			itemStyle : {
				normal : {
					color : colorCyle[1],
				}
			},
		}, {
			name : '城市养老运营',
			type : 'effectScatter',
			z:2,
			coordinateSystem : 'geo',
			data : convertData(dataCity),
			symbolSize : function(val) {
				return val[2] / 10;
			},
			showEffectOn : 'render',
			rippleEffect : {
				brushType : 'stroke',
//				period : 1,
				scale : 4
			},
			hoverAnimation : true,
			itemStyle : {
				normal : {
					color : colorCyle[2],
				}
			},
		}, {
			name : '健康医疗',
			type : 'effectScatter',
			z:1,
			coordinateSystem : 'geo',
			data : convertData(dataHealthy),
			symbolSize : function(val) {
				return val[2] / 10;
			},
			showEffectOn : 'render',
			rippleEffect : {
				brushType : 'stroke',
//				period : 1,
				scale : 4
			},
			hoverAnimation : true,
			itemStyle : {
				normal : {
					color : colorCyle[3],
				}
			},
		}]
	};
	myChart.setOption(option);
});

function end() {
	$("#video").fadeOut(8000);
}