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
	        {field:'permissionKey', title:'标识',width:150},
	        {field:'type', title:'类型',width:80, align:'center', formatter: function(val){
	        	return types[val];
	        }},
	        {field:'path', title:'路径',width:200},
	        {field:'resource', title:'资源',width:200},
	        {field:'weight', title:'权重',width:80, align:'center'},
	        {field:'description', title:'描述',width:200},
	        {
	        	field:'enable', title:'状态',width:80, align:'center', formatter: function(val){
	        		return val?"启用":"禁用";
	        	}	
	        },
	        {
	        	field:'edit', title:'操作',width:100, align:'center' ,formatter: function(val, row){
	        		var btns = [];
	        		btns.push('<a data-id="'+ row.id +'" class="action fa fa-pencil-square-o edit">编辑</a>');
	        		btns.push('<a data-id="'+ row.id +'" class="action fa fa-trash-o delete">删除</a>');
	        		return btns.join("");
	        	}	
	        }
	    ]],
	    toolbar:[{
	    	text:"创建权限",
	    	handler:function(){
	    		formDialog();
	    	}
	    }]
	});
	
	var gridPanel = permissionGrid.treegrid("getPanel");
	
	//给操作按钮绑定事件
	gridPanel.on("click", "a.edit", function(){
		var id = this.dataset.id;
		formDialog(id);
	});
	gridPanel.on("click", "a.delete", function(){
		var id = this.dataset.id;
		$.messager.confirm("提示", "是否删除", function(r){
			if(r){
				$.get("/system/permission/delete?id=" +id).success(
					function(){
						//删除成功
						permissionGrid.treegrid("reload");
					}
				);
			}
		})
	});
	
	/**
	 * 表单窗口
	 */
	function formDialog(id){
		var dialog = $("<div/>").dialog({
			iconCls:'fa fa-plus',
			title:(id ? '编辑':'创建') + '权限',
			href:'/system/permission/' + (id ? 'load?id=' + id:'form'),
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
							$.post("/system/permission/" + (id ? 'update':'save'), 
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