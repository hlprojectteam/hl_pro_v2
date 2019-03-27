package com.common.utils.helper;

import java.util.UUID;

/**
 * @intruduction 生成32位uuid
 * @author Mr.Wang
 * @Date 2016年3月13日上午9:47:22
 */
public class CreateUUID {
	
	public static String uuid(){
		return UUID.randomUUID().toString().trim().replaceAll("-", "");
	}
}
