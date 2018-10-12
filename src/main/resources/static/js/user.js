/**
 * 
 */
$(function() {
	var userGrid = $("#userGrid");
	
	userGrid.datagrid({
		fit:true,
		border:false,
		singleSelect: true,
		url:'/system/user/list',
		pagination:true,
		rownumbers:true,
	    columns:[[
	        {field:'account', title:'帐号',width:180},
	        {field:'userName', title:'姓名',width:150},
	        {field:'tel', title:'电话',width:200},
	        {
	        	field:'roles', title:'角色',width:200, formatter: function(val){
	        		var roles = [];
	        		$.each(val, function(){
	        			roles.push(this.roleName);
	        		});
	        		return roles.join(",");
	        	}	
	        },
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
	    	text:"创建用户",
	    	handler:function(){
	    		formDialog();
	    	}
	    }]
	});
	
	var gridPanel = userGrid.treegrid("getPanel");
	
	//给操作按钮绑定事件
	gridPanel.on("click", "a.edit", function(){
		var id = this.dataset.id;
		formDialog(id);
	});
	gridPanel.on("click", "a.delete", function(){
		var id = this.dataset.id;
		$.messager.confirm("提示", "是否删除", function(r){
			if(r){
				$.get("/system/user/delete?id=" +id).success(
					function(){
						//删除成功
						userGrid.datagrid("reload");
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
			title:(id ? '编辑':'创建') + '用户',
			href:'/system/user/' + (id ? 'load?id=' + id:'form'),
			width:380,
			height:330,
			modal:true,
			onClose:function(){
				$(this).dialog("destroy");
			},
			buttons:[
				{
					text:'保存',
					handler:function(){
						var userForm = $("#userForm");
						if(userForm.form("validate")){
							$.post("/system/user/" + (id ? 'update':'save'), 
								userForm.serialize()
							).success(
								function(){
									userGrid.datagrid("reload");
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