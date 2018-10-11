/**
 * 
 */
$(function() {
	var roleGrid = $("#roleGrid");
	var rolePermissionPanel = $("#rolePermissionPanel");
	var rolePermissionTree = $("#rolePermissionTree");
	var currentRow;
	
	roleGrid.datagrid({
		fit:true,
		border:false,
		singleSelect: true,
		url:'/system/role/list',
	    columns:[[
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
	    }],
	    onSelect:function(index, row){
	    	//记录当前选中行
	        currentRow = row;
	        
	    	rolePermissionPanel.panel('setTitle', "为[" + row.roleName + "]分配权限");
	    	
	    	$.each(rolePermissionTree.tree("getChecked"), function () {
	    		rolePermissionTree.tree("uncheck", this.target)
	    	});
	    	
	    	//加载当前选择抉择的已有权限
	    	$.get("/system/role/permission/" + row.id, function (data) {
	            $.each(data, function () {
	              var node = rolePermissionTree.tree('find', this.id);
	              //只当前权限是树的叶子节点时才执行节点的check方法进行选择
	              if (rolePermissionTree.tree('isLeaf', node.target)) {
	                rolePermissionTree.tree('check', node.target)
	              }
	            });
	          });
	    }
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
	
	
	rolePermissionTree.tree({
		url:'/system/role/permission/tree',
		checkbox:true
	});
	
	//权限保存的按钮时间
	$("#rolePermissionSave").on('click', function () {
	    if (currentRow) {
	      //获取打钩和实心的节点
	      var nodes = rolePermissionTree.tree("getChecked", ["checked", "indeterminate"]);
	      var permissionIds = [];
	      $.each(nodes, function () {
	        permissionIds.push(this.id);
	      });
	
	      var params = "roleId=" + currentRow.id + "&permissionId=" + permissionIds.join("&permissionId=");
	
	      $.post("/system/role/permission/save", params, function (resp) {
	        if (resp.success) {
	          $.messager.alert("提示", "授权成功！");
	        }
	      })
	    }
	});
});