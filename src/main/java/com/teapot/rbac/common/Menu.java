package com.teapot.rbac.common;

import lombok.Data;

@Data
public class Menu {
	private Long id;
	
	private String text;
	
	private String state = "open";
	
	private Long parentId;
	
	private String href;
}
