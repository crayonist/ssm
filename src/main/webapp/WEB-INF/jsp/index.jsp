<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Bootstrap 101 Template</title>
<!-- Bootstrap -->

<link href="${pageContext.request.contextPath }/css/bootstrap.min.css"
	type="text/css" rel="stylesheet">
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/jquery-3.2.1.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/bootstrap.min.js"></script>

<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/echarts/echarts.js"></script>


<script type="text/javascript">
	// 表单的做法：
	/* function queryItems() {
		$("form").action = "/items/getItemsList"
		$("form").submit();
	} */
	
	// Lucene查询尝试：
	/* function queryItems() {
		var url = "searchItems";
		$("form").attr("action", url);  
		$("form").submit();
	} */

	// 
	function queryItems() {
		var url = "queryItems";
		$("form").attr("action", url);  
		$("form").submit();
	}
	
	// 排序
	var isSort = "true";
	/* function sortItems() {
		$.ajax({
			url : 'getItemsList',
			data : { isSort: isSort },
			async : false,
			type : 'post',
			success : function(data) {
				// location.href = location.href;
				
				// location.href = location.href;
				$(document).ready(function() { 
					console.log(data);
					$("html").html(data); 
				});
			}
		}); 
	}*/
	function sortItems() {
		$("form").action = "/items/getItemsList"
		$("form").submit();
	}
</script>

</head>
<body>
	<div class="panel panel-default table-responsive">
		<div class="panel-heading" align="center">商品列表管理</div>
		<div class="panel-body">
			<form method="post">
				<input name="itemsCustom.name"
					value="${itemsQueryVo.itemsCustom.name }" class="form-control"
					style="display: inline-block; width: 100px;" placeholder="商品名称">
				<input name="itemsCustom.id" value="${itemsQueryVo.itemsCustom.id }"
					class="form-control" style="display: inline-block; width: 100px;"
					placeholder="商品编号">
				<button onclick="queryItems()" id="activeButton" type="button"
					style="display: inline-block; width: 70px;" class="btn btn-default">查询</button>
				<input type="hidden" name="isSort" value="${itemsQueryVo.isSort }" />
				<button onclick="sortItems()" type="button"
					style="display: inline-block; width: 70px;" class="btn btn-default">排序</button>
				<script type="text/javascript">
					$("#activeButton").mouseover(function() {
						this.className = "btn btn-primary";
					});
					$("#activeButton").mouseleave(function() {
						this.className = "btn btn-default";
					});
				</script>
			</form>
		</div>
		<table class="table table-hover table-bordered">
			<tbody align="center">
				<tr>
					<td>商品编号</td>
					<td>商品名称</td>
					<td>商品价格</td>
					<td>商品描述</td>
					<td>商品图片</td>
					<td>创建时间</td>
				</tr>
				<c:if test="${itemsList != null}">
					<c:forEach items="${itemsList }" var="item">
						<tr>
							<td>${item.id }</td>
							<td>${item.name }</td>
							<td>${item.price }</td>
							<td>${item.detail }</td>
							<td>${item.pic }</td>
							<td><fmt:formatDate value="${item.createtime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
						</tr>
					</c:forEach>
				</c:if>
				<c:if test="${empty itemsList}">
					<tr>
						<td colspan="6">没有找到相关商品信息！</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>
	<div class="panel panel-default" style="height: 400px">
		<div class="panel-heading" align="center">商品价格图表</div>
		<div class="panel-body" id="echarts_div" style="height: 400px">
			<script type="text/javascript">
				// 路径配置
				require.config({
					paths : {
						echarts : '${pageContext.request.contextPath }/js/echarts'
					}
				});
				// 使用
				require(
					[  
						'echarts',
						// 'echarts/chart/bar', // 使用柱状图就加载bar模块，按需加载
						'echarts/chart/line',
						'echarts/chart/bar',
						'echarts/chart/macarons'
					], function(ec) {
					// 基于准备好的dom，初始化echarts图表
					var myChart = ec.init(document.getElementById('echarts_div'),'macarons');

					var x_itemName = [];
					var price = [];
					<c:forEach items="${itemsList }" var="item">
					x_itemName.push("${item.name}");
					price.push("${item.price}");
					</c:forEach>
					option = {
						title : {
							text : '商品的价格展示',
							subtext : '纯属虚构'
						},
						tooltip : {
							trigger : 'axis'
						},
						legend : {
							data : [ '价格', '销量' ]
						},
						toolbox : {
							show : true,
							feature : {
								mark : {
									show : true
								},
								dataView : {
									show : true,
									readOnly : false
								},
								magicType : {
									show : true,
									type : [ 'line', 'bar' ]
								},
								restore : {
									show : true
								},
								saveAsImage : {
									show : true
								}
							}
						},
						calculable : true,
						xAxis : [ {
							type : 'category',
							boundaryGap : false,
							data : x_itemName
						} ],
						yAxis : [ {
							type : 'value',
							axisLabel : {
								formatter : '{value}'
							}
						} ],
						series : [ {
							name : '价格',
							type : 'line',
							data : price
						}, ]
					};

					// 为echarts对象加载数据 
					myChart.setOption(option);
				});
			</script>

		</div>
	</div>
</body>
</html>