/**
 * 
 */
$(function() {
		var permissionGrid = $("#permissionGrid");
		permissionGrid.treegrid({
			fit:true,
			border:false,
			url:'/system/permission/list',
			idField:'id',
		    treeField:'name',
		    columns:[[
		        {field:'name', title:'名称',width:180},
		        {field:'permisionKey', title:'标识',width:60,align:'right'},
		        {field:'type', title:'类型',width:80},
		        {field:'path', title:'路径',width:80},
		        {field:'resource', title:'资源',width:80},
		        {field:'weight', title:'权重',width:80},
		        {field:'description', title:'描述',width:80},
		        {field:'enable', title:'状态',width:80}
		    ]],
		    toolbar:[{
		    	text:"创建权限",
		    	handle:function(){
		    		
		    	}
		    }]
		});
});