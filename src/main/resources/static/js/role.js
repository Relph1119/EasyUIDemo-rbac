/**
 * 
 */
$(function() {
	var roleGrid = $("#roleGrid");
	roleGrid.datagrid({
		fit:true,
		border:false,
		url:'/system/role/list',
	    columns:[[
	        {field:'id', checkbox:true},
	        {field:'roleName', title:'名称',width:180},
	        {field:'roleKey', title:'标识',width:150},
	        {field:'description', title:'描述',width:200},
	        {
	        	field:'enable', title:'状态',width:80, align:'center', formatter: function(val){
	        		return val?"可用":"禁用";
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
	    	iconCls : 'fa fa-plus',
	    	text:"创建角色",
	    	handler:function(){
	    		formDialog();
	    	}
	    }]
	});
	
	var gridPanel = roleGrid.treegrid("getPanel");
	
	//给操作按钮绑定事件
	gridPanel.on("click", "a.edit", function(){
		var id = this.dataset.id;
		formDialog(id);
	});
	gridPanel.on("click", "a.delete", function(){
		var id = this.dataset.id;
		$.messager.confirm("提示", "是否删除", function(r){
			if(r){
				$.get("/system/role/delete?id=" +id).success(
					function(){
						//删除成功
						roleGrid.datagrid("reload");
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
			title:(id ? '编辑':'创建') + '角色',
			href:'/system/role/' + (id ? 'load?id=' + id:'form'),
			width:380,
			height:250,
			onClose:function(){
				$(this).dialog("destroy");
			},
			buttons:[
				{
					text:'保存',
					handler:function(){
						var roleForm = $("#roleForm");
						if(roleForm.form("validate")){
							$.post("/system/role/" + (id ? 'update':'save'), 
								roleForm.serialize()
							).success(
								function(){
									roleGrid.datagrid("reload");
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