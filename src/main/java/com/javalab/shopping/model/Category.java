package com.javalab.shopping.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class Category {
	private String categoryId;
	private String categoryName;
	private String categoryDesc;
	private String createDate;
}
