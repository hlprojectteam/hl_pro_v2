package com.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @Description 文件工具类
 * @author qinyongqian
 * @date 2017-1-8
 * 
 */
public class FileTools {

	/**
	 * 下载文件时指定下载名
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param filePath
	 *            文件全路径
	 * @param fileName
	 *            指定客户端下载时显示的文件名
	 * @throws IOException
	 */
	public static void downloadFile(HttpServletRequest request,
			HttpServletResponse response, String filePath, String fileName)
			throws IOException {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		bis = new BufferedInputStream(new FileInputStream(filePath));
		bos = new BufferedOutputStream(response.getOutputStream());

		long fileLength = new File(filePath).length();

		response.setCharacterEncoding("UTF-8");
		response.setContentType("multipart/form-data");
		/*
		 * 解决各浏览器的中文乱码问题
		 */
		String userAgent = request.getHeader("User-Agent");
		byte[] bytes = userAgent.contains("MSIE") ? fileName.getBytes()
				: fileName.getBytes("UTF-8"); // fileName.getBytes("UTF-8")处理safari的乱码问题
		fileName = new String(bytes, "ISO-8859-1"); // 各浏览器基本都支持ISO编码
		response.setHeader("Content-disposition",
				String.format("attachment; filename=\"%s\"", fileName));

		response.setHeader("Content-Length", String.valueOf(fileLength));
		byte[] buff = new byte[2048];
		int bytesRead;
		while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			bos.write(buff, 0, bytesRead);
		}
		bis.close();
		bos.close();

	}

	/**
	 * 下载文件时不指定下载文件名称
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param filePath
	 *            文件全路径
	 * @throws IOException
	 */
	public static void downloadFile(HttpServletRequest request,
			HttpServletResponse response, String filePath) throws IOException {
		File file = new File(filePath);
		downloadFile(request, response, filePath, file.getName());
	}
	
	/**
	 * 
	 * @ClassName:FileTools.java
	 * @Description:删除文件夹下所有文件，不包括子文件夹
	 * @param path
	 * @author qinyongqian
	 * @date 2017-1-8
	 */
	public static void delAllFile(String path) {
	       File file = new File(path);
	       String[] tempList = file.list();
	       File temp = null;
	       for (int i = 0; i < tempList.length; i++) {
	          if (path.endsWith(File.separator)) {
	             temp = new File(path + tempList[i]);
	          } else {
	              temp = new File(path + File.separator + tempList[i]);
	          }
	          if (temp.isFile()) {
	             temp.delete();
	          }
	       }
	}

}
