<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
	 <a class="easyui-linkbutton" onclick="importItems()">一键添加商品数据到索引库</a>
</div>

<script type="text/javascript">
	function importItems() {
		$.post("/index/import",null, function(){
			$.messager.alert('提示','商品数据添加成功!') ;
		});
	}
</script>