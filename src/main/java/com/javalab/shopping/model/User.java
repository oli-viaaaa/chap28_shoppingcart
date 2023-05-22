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
public class User {
	private String userId;
	private String userEmail;
	private String userName;
	private String userPwd;
	private String useHp;
	private String userDate;

}
