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
	
	public static final String CategoryCode = getProperty("categoryCode");//移动端需要的下拉键值



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
	
	
	/****************************消息推送 start*********************************/
	public static final String msg_ApnsProduction= (String) Cache.getSysConfig.get("msgApnsProduction");//IOS广播消息是否在生产环境
	public static final String msg_platform= (String) Cache.getSysConfig.get("msgPlatform");//消息在什么平台上推送 1:ios 2:android 3:android_ios
	public static final String msg_setMessage= (String) Cache.getSysConfig.get("msgSetMessage");//消息是否在APP打开时，在前台弹出提示

	//消息类型 0系统管理 1教育考试 2营运动态 3规章制度 4安全管理 5党建 6考勤 
	public static final int msgSYS = 0;
	public static final int msgJY = 1;
	public static final int msgDT = 2;
	public static final int msgZD = 3;
	public static final int msgAQ = 4;
	public static final int msgDJ = 5;
	public static final int msgKQ = 6;
	
	//消息标题
	public static final String msgTitle_DJ_ldgz_todo="党建活动亮点工作待评审";
	public static final String msgTitle_DJ_ldgz_finish="党建活动亮点工作回复";
	public static final String msgTitle_DJ_hdfb_info="党建活动发布";
	public static final String msgTitle_DJ_yj_todo="党建建议待回复";
	public static final String msgTitle_DJ_yj_finish="党建建议回复";
	public static final String msgTitle_AQ_jyxc_todo="安全管理建言献策提交";
	public static final String msgTitle_AQ_jyxc_finish="安全管理建言献策已回复";
	public static final String msgTitle_AQ_sj_todo="安全管理事件待办";
	public static final String msgTitle_AQ_sj_finish="安全管理事件回复";
	public static final String msgTitle_JY_jyks_todo="考试通知";
	public static final String msgTitle_DT_dt_info="营运动态发布通知";
	public static final String msgTitle_DT_zbbb_info="值班报表发布通知";
	public static final String msgTitle_ZD_zd_info="规章制度发布通知";
	public static final String msgTitle_KQ_dm="考勤点名";
	public static final String msgTitle_KQ_qj="请假待办";
	public static final String msgTitle_SYS_info="系统通知";
	
	/****************************消息推送 end*********************************/
	//百度地图的key
	public static final String bMapKey = (String) Cache.getSysConfig.get("bMapKey");
	
}
