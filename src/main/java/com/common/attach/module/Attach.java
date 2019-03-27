package com.common.attach.module;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.common.base.module.BaseModule;

/**
 * @intruduction 附件表
 * @author Dic
 * @Date 2016年1月21日上午11:26:38
 */
@Entity
@Table(name="P_ATTACH")
public class Attach extends BaseModule{
	private String formId;//表单id
	private String suffix;//后缀
	private Long size;//大小
	private String fileName;//附件名称
	private String fileUploadName;//附件保存名称
	private String path;//具体路径
	private String pathUpload;//上传后的虚拟路径
	private String createId;//创建人id
	private String createLoginName;//创建人登录名称
	private String processId;//步骤节点id
	private String processName;//步骤节点Name
	private String attachType;//附件类型 同一表单对附件进行分类
	private String source;//上传来源 1：电脑端 2：手机端
	
	@Column(name = "FORM_ID", length=32)
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	@Column(name = "SUFFIX_", length=32)
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	@Column(name = "SIZE_")
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	@Column(name = "FILE_NAME", length=256)
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@Column(name = "FILE_UPLOAD_NAME", length=32)
	public String getFileUploadName() {
		return fileUploadName;
	}
	public void setFileUploadName(String fileUploadName) {
		this.fileUploadName = fileUploadName;
	}
	@Column(name = "PATH_", length=512)
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	@Column(name = "PATH_UPLOAD", length=512)
	public String getPathUpload() {
		return pathUpload;
	}
	public void setPathUpload(String pathUpload) {
		this.pathUpload = pathUpload;
	}
	@Column(name = "CREATE_ID", length=32)
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	@Column(name = "CREATE_LOGINNAME", length=32)
	public String getCreateLoginName() {
		return createLoginName;
	}
	public void setCreateLoginName(String createLoginName) {
		this.createLoginName = createLoginName;
	}
	@Column(name = "PROCESS_ID", length=32)
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	@Column(name = "PROCESS_NAME", length=20)
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	@Column(name = "SOURCE_", length=3)
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	@Column(name = "ATTACH_TYPE", length=20)
	public String getAttachType() {
		return attachType;
	}
	public void setAttachType(String attachType) {
		this.attachType = attachType;
	}
}
