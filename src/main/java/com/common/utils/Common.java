package com.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.common.utils.cache.Cache;

/**
 *
 * @author Mr.Wang
 * 读取配置信息
 */
public class Common {
	private static final Logger logger = Logger.getLogger(Common.class);
	//读取配置文件
	public static String getProperty(String key) {
		String str = "";
		try {
			str = new String(ResourceBundle.getBundle("config").getString(key).getBytes("ISO-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("com.common.utils.Common_读取配置信息", e);
		}
		return str;
	}

	//-------配置文件-------------------------------------------------------------------------------------------
//	public static final String GIS_SERVER_URL = (String) Cache.getSysConfig.get("GisServerUrl");//地图服务地址

	public static final String ROOT = getProperty("root");//默认根节点ID

	public static final String PATHUPLOAD = (String) Cache.getSysConfig.get("pathUpload");
	public static final String PATH = (String) Cache.getSysConfig.get("path");//上传文件路径



	public static final String SYSCODE = getProperty("sysCode");//子系统编码



	//默认头像
	public static final String DEFAULTHEAD = "/common/img/head.png";
	//组群默认头像
	public static final String DEFAULT_GROUP_HEAD = "/common/plugins/layui/images/avatar/a0.jpg";

	//是否可以单一登陆
	public static final String SingleLogin = (String) Cache.getSysConfig.get("singleLogin");

	//public static final String MapCode = (String) Cache.getSysConfig.get("MapCode");//地图初始化加载行政区域

	public static final String SOURCE_PC = "1"; //1：电脑端
	public static final String SOURCE_APP = "2"; // 2：手机端
	public static final String SOURCE_WeiXin = "3"; // 3：微信端

	//逻辑删除的标志：0.正常；1.逻辑删除；
	public static final Integer DEL_FLAG_YES = 1;
	public static final Integer DEL_FLAG_NO = 0;

	//AES加密秘钥 
	public static final String AESPassword = "cloudting";

	public static final Integer LOGIN_WAY_WEB = 1;
	public static final Integer LOGIN_WAY_APP = 2;
	
	
	//事件流程相关
	public static final String yhsbrRoleCode = (String) Cache.getSysConfig.get("yhsbrRoleCode");
	public static final String yhsbrRoleName = (String) Cache.getSysConfig.get("yhsbrRoleName");
	public static final String bmaqyRoleCode = (String) Cache.getSysConfig.get("bmaqyRoleCode");
	public static final String bmaqyRoleName = (String) Cache.getSysConfig.get("bmaqyRoleName");
	public static final String bmfzrRoleCode = (String) Cache.getSysConfig.get("bmfzrRoleCode");
	public static final String bmfzrRoleName = (String) Cache.getSysConfig.get("bmfzrRoleName");
	public static final String abbaqyRoleCode = (String) Cache.getSysConfig.get("abbaqyRoleCode");
	public static final String abbaqyRoleName = (String) Cache.getSysConfig.get("abbaqyRoleName");
	public static final String yhclrRoleCode = (String) Cache.getSysConfig.get("yhclrRoleCode");
	public static final String yhclrRoleName = (String) Cache.getSysConfig.get("yhclrRoleName");
	
	public static final String abbzrRoleCode = (String) Cache.getSysConfig.get("abbzrRoleCode");
	public static final String abbzrRoleName = (String) Cache.getSysConfig.get("abbzrRoleName");
	public static final String fgldRoleCode = (String) Cache.getSysConfig.get("fgldRoleCode");
	public static final String fgldRoleName = (String) Cache.getSysConfig.get("fgldRoleName");
	public static final String cwfzjlRoleCode = (String) Cache.getSysConfig.get("cwfzjlRoleCode");
	public static final String cwfzjlRoleName = (String) Cache.getSysConfig.get("cwfzjlRoleName");
	
}
