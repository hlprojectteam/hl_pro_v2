package com.common.attach.controller;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.common.attach.module.Attach;
import com.common.attach.service.IAttachService;
import com.common.base.controller.BaseController;
import com.common.utils.Common;
import com.google.gson.JsonObject;
import com.urms.user.module.User;

/**
 * @intruduction 附件上传
 * @author Dic
 * @Date 2016年1月18日上午9:02:39
 */
@Controller
public class AttachController extends BaseController{
	
	@Autowired
	public IAttachService attachServiceImpl;
	
	/**
	 * @intruduction 文件上传
	 * @param file 文件
	 * @param httpSession
	 * @param response
	 * @param formId 表单id & 事件id
	 * @param sysCode 系统编码
	 * @param processId 步骤节点id 可以为空
	 * @param processName 步骤节点名称 可以为空
	 * @param attachType 附件类型 自行设计 例：身份证（正反）、户口本（）
	 * @author Dic
	 * @Date 2016年4月12日下午5:19:16
	 */
	@RequestMapping(value="/attach_upload",method = RequestMethod.POST) 
	public void upload(MultipartFile file,HttpSession httpSession,HttpServletResponse response,String formId,String sysCode,String processId, String processName,String attachType,String source){
		User user =(User)this.getHttpSession().getAttribute("user");
		JsonObject json = new JsonObject();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String timePath = sdf.format(new Date());//时间目录
			String fileUploadName = System.currentTimeMillis()+"";//保存的名称
			String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);// 取文件格式后缀名
			String path = Common.PATH;
			path = path + File.separator + timePath;
			File filePath =new File(path);
			if(!filePath.exists()&&!filePath.isDirectory())//如果文件夹不存在则创建  
				filePath.mkdir();
			String fullPath = path + File.separator + fileUploadName + "."  +suffix;//全路径
			String pathUpload = "/"+Common.PATHUPLOAD +"/"+ timePath +"/"+ fileUploadName + "." + suffix;
			File f =new File(fullPath);
			FileUtils.copyInputStreamToFile(file.getInputStream(), f);// 复制临时文件到指定目录下
			Attach attach = new Attach();
			attach.setFileName(file.getOriginalFilename());//文件名称
			attach.setCreateId(user.getId());//登录id
			attach.setCreateLoginName(user.getLoginName());//登录名
			attach.setFileUploadName(fileUploadName);
			if(StringUtils.isNotBlank(sysCode))
				attach.setSysCode(sysCode);
			else
				attach.setSysCode(user.getSysCode());
			attach.setPath(fullPath);
			attach.setPathUpload(pathUpload);//虚拟路径
			attach.setSuffix(suffix);//后缀名
			attach.setFormId(formId);//表单id
			if(!StringUtils.isEmpty(source)){
				attach.setSource(source); //来源 APP 或者 微信
			}else{
				attach.setSource(Common.SOURCE_PC); //来源
			}
			attach.setSize(file.getSize());
			attach.setProcessId(processId);//步骤名称
			if(StringUtils.isNotBlank(processName))
				attach.setProcessName(new String(processName.getBytes("ISO-8859-1"),"UTF-8"));//步骤name
			if(StringUtils.isNotBlank(attachType)){
				attach.setAttachType(attachType);
			}
			this.attachServiceImpl.save(attach);//保存
			json.addProperty("result", true);
			json.addProperty("originalName", file.getOriginalFilename());//初始名
			json.addProperty("name", fileUploadName+"."+suffix);//保持到服务器名称 
			json.addProperty("url", pathUpload);//访问的url
			json.addProperty("size", file.getSize());//大小
			json.addProperty("type", "."+suffix);//后缀类型 如 .jpg
			json.addProperty("state", "SUCCESS");//
			logger.info("上传文件原名："+file.getOriginalFilename()+";服务器名称:"+ fileUploadName +"."+suffix+";时间："+new Date());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			json.addProperty("result", false);
		}
		this.print(json.toString());
	}
	
	/**
	 * @intruduction 文件上传 带水印
	 * @param file 文件
	 * @param httpSession
	 * @param response
	 * @param formId 表单id & 事件id
	 * @param sysCode 系统编码
	 * @param processId 步骤节点id 可以为空
	 * @param processName 步骤节点名称 可以为空
	 * @author Dic
	 * @Date 2016年4月12日下午5:19:16
	 */
	@RequestMapping(value="/attachPhoto_uploadWaterMark",method = RequestMethod.POST) 
	public void uploadWaterMark(MultipartFile file,HttpSession httpSession,HttpServletResponse response,String formId,String sysCode,String processId, String processName,String attachType){
		User user =(User)this.getHttpSession().getAttribute("user");
		JsonObject json = new JsonObject();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String timePath = sdf.format(new Date());//时间目录
			String fileUploadName = System.currentTimeMillis()+"";//保存的名称
			String suffix =  file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);// 取文件格式后缀名
			String path = Common.PATH;
			path = path + File.separator + timePath;
			File filePath =new File(path);
			if(!filePath.exists()&&!filePath.isDirectory())//如果文件夹不存在则创建  
				filePath.mkdir();
			String fullPath = path + File.separator + fileUploadName + "."  +suffix;//全路径
			String pathUpload = "/"+Common.PATHUPLOAD +"/"+ timePath +"/"+ fileUploadName + "." + suffix;
			File f =new File(fullPath);
			FileUtils.copyInputStreamToFile(file.getInputStream(), f);// 复制临时文件到指定目录下
			//添加水印
			AttachController.waterMark("云汀信息",fullPath,fullPath,"幼圆",Font.BOLD,"#FFFFFF",28,125, 60, 0.4f);
			Attach attach = new Attach();
			attach.setFileName(file.getOriginalFilename());//文件名称
			attach.setCreateId(user.getId());//登录id
			attach.setCreateLoginName(user.getLoginName());//登录名
			attach.setFileUploadName(fileUploadName);
			attach.setSysCode(sysCode);
			attach.setPath(fullPath);
			attach.setPathUpload(pathUpload);//虚拟路径
			attach.setSuffix(suffix);//后缀名
			attach.setFormId(formId);//表单id
			attach.setSource(Common.SOURCE_PC);//来源
			attach.setSize(file.getSize());
			attach.setProcessId(processId);//步骤名称
			if(StringUtils.isNotBlank(processName))
				attach.setProcessName(new String(processName.getBytes("ISO-8859-1"),"UTF-8"));//步骤name
			if(StringUtils.isNotBlank(attachType)){
				attach.setAttachType(attachType);
			}
			this.attachServiceImpl.save(attach);//保存
			json.addProperty("result", true);
			logger.info("上传文件："+fileUploadName+";时间："+new Date());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			json.addProperty("result", false);
		}
		this.print(json.toString());
	}
	
	/**
	 * @intruduction 通过表单id获得附件集合
	 * @param httpSession
	 * @param response
	 * @author Dic
	 * @Date 2016年1月21日下午2:38:17
	 */
	@RequestMapping(value="/queryAttachList",method = RequestMethod.POST) 
	public void queryAttachList(HttpSession httpSession,HttpServletResponse response,String formId){
		List<Attach> attachList = this.getAttachServiceImpl().queryAttchListByFormId(formId);
		JSONObject json = new JSONObject();
		json.put("attachList", JSONArray.fromObject(attachList));
		this.print(json.toString());
	}
	
	/**
	 * 
	 * @ClassName:AttachController.java
	 * @Description:通过表单id，附件类型获得附件集合
	 * @param httpSession
	 * @param response
	 * @param formId
	 * @param type
	 * @author qinyongqian
	 * @date 2016-8-25
	 */
	@RequestMapping(value="/queryAttachListByType",method = RequestMethod.POST) 
	public void queryAttachListByType(HttpSession httpSession,HttpServletResponse response,String formId,String type){
		List<Attach> attachList = this.getAttachServiceImpl().queryAttchByFormIdAndType(formId, type);
		JSONObject json = new JSONObject();
		json.put("attachList", JSONArray.fromObject(attachList));
		this.print(json.toString());
	}
	
	/**
	 * 
	 * @ClassName:AttachController.java
	 * @Description:根据FORMID只获取为图片的附件
	 * @param httpSession
	 * @param response
	 * @param formId
	 * @author qinyongqian
	 * @date 2016-8-8
	 */
	@RequestMapping(value="/queryPhotoAttachList",method = RequestMethod.POST) 
	public void queryPhotoAttachList(HttpSession httpSession,HttpServletResponse response,String formId){
		List<Attach> attachList = this.getAttachServiceImpl().queryAttchByFormIdAndOnlyPicture(formId);
		JSONObject json = new JSONObject();
		json.put("attachList", JSONArray.fromObject(attachList));
		this.print(json.toString());
	}
	
	/**
	 * @intruduction 文件下载
	 * @param request
	 * @param response
	 * @param attachId
	 * @return
	 * @author Dic
	 * @throws UnsupportedEncodingException 
	 * @Date 2016年4月12日下午4:39:43
	 */
	@RequestMapping("/attach_download")
    public String download(HttpServletRequest request,HttpServletResponse response, String attachId) throws UnsupportedEncodingException {
		Attach attach = this.getAttachServiceImpl().getEntityById(Attach.class, attachId);
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        /*response.setHeader("Content-Disposition", "attachment;fileName=" + attach.getFileName());*/
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(attach.getFileName().getBytes("utf-8"), "iso-8859-1"));
        try {
            InputStream inputStream = new FileInputStream(new File(attach.getPath()));
            OutputStream os = response.getOutputStream();
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
            os.close(); // 这里主要关闭。
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
	
	
	/**
	 * @intruduction 删除附件
	 * @param httpSession
	 * @param response
	 * @param ids id主键数组
	 * @author Dic
	 * @Date 2016年1月26日下午4:42:04
	 */
	@RequestMapping(value="/attach_delete",method = RequestMethod.POST) 
	public void deleteAttach(HttpSession httpSession,HttpServletResponse response,String ids){
		JSONObject json = new JSONObject();
		try {
			this.getAttachServiceImpl().deleteAttach(ids);
			json.put("result", true);
		} catch (Exception e) {
			json.put("result", false);
		}
		this.print(json.toString());
	}
	
	/**
	 * @intruduction 上传头像
	 * @param __avatar1
	 * @param httpSession
	 * @param response
	 * @param formId
	 * @param sysCode
	 * @author Dic
	 * @Date 2016年1月28日上午11:03:19
	 */
	@RequestMapping(value="/upload_avatar",method = RequestMethod.POST) 
	public void uploadAvatar(MultipartFile __avatar1,HttpSession httpSession,HttpServletResponse response){
		User user = this.getSessionUser();
		List<Attach> list = this.getAttachServiceImpl().queryAttchListByFormId(user.getId());
		if (list.size()>0) {
			 this.getAttachServiceImpl().delete(Attach.class, list.get(0).getId());
		}
		JsonObject json = new JsonObject();
		try {
			String path = Common.PATH;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String timePath = sdf.format(new Date());//时间目录
			path = path + File.separator + timePath;
			File filePath =new File(path);
			if(!filePath.exists()&&!filePath.isDirectory())//如果文件夹不存在则创建  
				filePath.mkdir();
			String suffix = ".jpg";// 取文件格式后缀名
			String fileUploadName = System.currentTimeMillis()+"";//保存的名称
			String fullPath = path + File.separator + fileUploadName + suffix;//全路径
			String pathUpload = "/"+Common.PATHUPLOAD +"/"+ timePath +"/"+ fileUploadName + suffix;
			File f =new File(fullPath);
			FileUtils.copyInputStreamToFile(__avatar1.getInputStream(), f);// 复制临时文件到指定目录下
			Attach attach = new Attach();
			attach.setFileName("头像");//文件名称
			attach.setCreateId(user.getId());//登录id
			attach.setCreateLoginName(user.getLoginName());//登录名
			attach.setFileUploadName(fileUploadName);
			attach.setSysCode(user.getSysCode());;
			attach.setPath(fullPath);
			attach.setPathUpload(pathUpload);//虚拟路径
			attach.setSuffix(suffix);//后缀名
			attach.setFormId(user.getId());//表单id
			this.attachServiceImpl.save(attach);//保存
			json.addProperty("success", true);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			json.addProperty("success", false);
		}
		this.print(json.toString());
	}
	
	/**
	* 给图片添加文字水印
	* @param pressText 水印文字
	* @param srcImageFile 源图像地址
	* @param destImageFile 目标图像地址
	* @param fontName 水印的字体名称
	* @param fontStyle 水印的字体样式
	* @param color 水印的字体颜色  如：#FFFFFF 白色
	* @param fontSize 水印的字体大小
	* @param x 修正值
	* @param y 修正值
	* @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	*/
	 public final static void waterMark(String pressText,String srcImageFile, String destImageFile,String fontName,int fontStyle,String color,int fontSize,int x,int y, float alpha) {
		 try {
			 File img = new File(srcImageFile);
			 Image src = ImageIO.read(img);
			 int width = src.getWidth(null);
			 int height = src.getHeight(null);
			 BufferedImage image = new BufferedImage(width, height,
			 BufferedImage.TYPE_INT_RGB);
			 Graphics2D g = image.createGraphics();
			 // 开文字抗锯齿 去文字毛刺
			 g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			 g.drawImage(src, 0, 0, width, height, null);
			 // 设置颜色
			 g.setColor(Color.decode(color));
			 // 设置 Font
			 g.setFont(new Font(fontName,fontStyle,fontSize));
			 g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,alpha));
			 // 第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y) .
			 g.drawString(pressText, x, y);
			 g.dispose();
			 ImageIO.write((BufferedImage) image, "JPEG", new File(destImageFile));// 输出到文件流
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	 }
	 
	public IAttachService getAttachServiceImpl() {
		return attachServiceImpl;
	}
	public void setAttachServiceImpl(IAttachService attachServiceImpl) {
		this.attachServiceImpl = attachServiceImpl;
	}
	
}
