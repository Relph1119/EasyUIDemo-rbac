/**
 * 
 */
$(function() {
		var types = {
			MENU :"菜单",
			FUNCTION:"功能",
			BLOCK:"区域"
		};
		var permissionGrid = $("#permissionGrid");
		permissionGrid.treegrid({
			fit:true,
			border:false,
			url:'/system/permission/list',
			idField:'id',
		    treeField:'name',
		    columns:[[
		        {field:'name', title:'名称',width:180},
		        {field:'permissionKey', title:'标识',width:150,align:'right'},
		        {field:'type', title:'类型',width:80, formatter: function(val){
		        	return types[val];
		        }},
		        {field:'path', title:'路径',width:200},
		        {field:'resource', title:'资源',width:200},
		        {field:'weight', title:'权重',width:80},
		        {field:'description', title:'描述',width:200},
		        {field:'enable', title:'状态',width:80, formatter: function(val){
		        	return val?"启用":"禁用";
		        }}
		    ]],
		    toolbar:[{
		    	text:"创建权限",
		    	handler:formDialog
		    }]
		});
		
		/**
		 * 表单窗口
		 */
		function formDialog(){
			var dialog = $("<div/>").dialog({
				title:'创建权限',
				href:'/system/permission/form',
				width:380,
				height:450,
				onClose:function(){
					$(this).dialog("destroy");
				},
				buttons:[
					{
						text:'保存',
						handler:function(){
							var permissionForm = $("#permissionForm");
							if(permissionForm.form("validate")){
								$.post("/system/permission/save", 
									permissionForm.serialize()
								).success(
									function(){
										permissionGrid.treegrid("reload");
										dialog.dialog('close');
									}
								);
							}
						}
					}
				]
			});
		}
});