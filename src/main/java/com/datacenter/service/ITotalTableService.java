package com.datacenter.service;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.datacenter.module.TotalTable;
import com.datacenter.vo.TotalTableVo;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @Description 值班汇总表 service接口
 * @author xuezb
 * @date 2019年2月19日
 */
public interface ITotalTableService extends IBaseService{
	
	/**
	 * 值班汇总表	分页
	 * @param page
	 * @param rows
	 * @param totalTableVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public Pager queryEntityList(Integer page, Integer rows, TotalTableVo totalTableVo);
	
	/**
	 * 值班汇总表	保存or更新
	 * @param totalTableVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public TotalTable saveOrUpdate(TotalTableVo totalTableVo);
	
	/**
	 * 值班汇总表	删除
	 * @param ids
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int deleteEntityByIds(String ids);

	/**
	 * 值班汇总表	下载
	 * @param ttId
	 * @return HSSFWorkbook
	 * @author xuezb
	 * @Date 2019年3月6日
	 */
	public HSSFWorkbook downLoad(String ttId);
	
	/**
	 * 
	 * @方法：@param id
	 * @方法：@return
	 * @描述：修改状态
	 * @return
	 * @author: qinyongqian
	 * @date:2019年6月13日
	 */
	public String saveChangeState(String id);

}