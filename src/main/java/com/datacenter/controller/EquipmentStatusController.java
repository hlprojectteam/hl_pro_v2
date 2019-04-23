package com.datacenter.controller;

import com.common.base.controller.BaseController;
import com.common.utils.helper.JsonDateTimeValueProcessor;
import com.common.utils.helper.Pager;
import com.datacenter.module.EquipmentStatus;
import com.datacenter.service.IEquipmentStatusService;
import com.datacenter.vo.EquipmentStatusVo;
import com.google.gson.JsonObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description 设备状态	控制层
 * @author xuezb
 * @date 2019年2月18日
 */
@Controller
@RequestMapping("/datecenter/equipmentStatus")
public class EquipmentStatusController extends BaseController {

    @Autowired
    private IEquipmentStatusService equipmentStatusServiceImpl;

    @Autowired
    private TotalTableController totalTableController;


    /**
     * 设备状态	列表页面
     * @param httpSession
     * @param response
     * @param menuCode
     * @param ttId
     * @param dutyDateStr
     * @return
     * @author xuezb
     * @Date 2019年2月18日
     */
    @RequestMapping(value="/equipmentStatus_list")
    public String list(HttpSession httpSession, HttpServletResponse response, String menuCode, String ttId, String dutyDateStr) {
        this.getRequest().setAttribute("menuCode", menuCode);
        this.getRequest().setAttribute("ttId", ttId);
        this.getRequest().setAttribute("dutyDateStr", dutyDateStr);
        return "/page/datecenter/equipmentStatus_list";
    }


    /**
     * 设备状态	列表数据加载
     * @param response
     * @param page
     * @param rows
     * @param equipmentStatusVo
     * @return
     * @author xuezb
     * @Date 2019年2月18日
     */
    @RequestMapping(value="/equipmentStatus_load")
    public void load(HttpServletResponse response, Integer page, Integer rows, EquipmentStatusVo equipmentStatusVo){
        Pager pager = this.equipmentStatusServiceImpl.queryEntityList(page, rows, equipmentStatusVo);
        JSONObject json = new JSONObject();
        json.put("total", pager.getRowCount());
        JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
        config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());//格式化日期
        json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
        this.print(json);
    }


    /**
     * 设备状态	编辑
     * @param request
     * @param winName
     * @param equipmentStatusVo
     * @return
     * @author xuezb
     * @throws ParseException
     * @Date 2019年2月18日
     */
    @RequestMapping(value="/equipmentStatus_edit")
    public String edit(HttpServletRequest request, String winName, EquipmentStatusVo equipmentStatusVo) throws ParseException{
        if(StringUtils.isNotBlank(equipmentStatusVo.getDutyDateStr())){
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date dutyDate = fmt.parse(equipmentStatusVo.getDutyDateStr());
            equipmentStatusVo.setDutyDate(dutyDate);
        }
        if(StringUtils.isNotBlank(equipmentStatusVo.getId())){
            EquipmentStatus equipmentStatus = this.equipmentStatusServiceImpl.getEntityById(EquipmentStatus.class, equipmentStatusVo.getId());
            BeanUtils.copyProperties(equipmentStatus, equipmentStatusVo);
            request.setAttribute("equipmentStatusVo", equipmentStatusVo);
        }else{
            request.setAttribute("equipmentStatusVo", equipmentStatusVo);
        }
        request.setAttribute("winName", winName);
        return "/page/datecenter/equipmentStatus_edit";
    }


    /**
     * 设备状态	保存or修改
     * @param response
     * @param equipmentStatusVo
     * @author xuezb
     * @Date 2019年2月18日
     */
    @RequestMapping(value="/equipmentStatus_saveOrUpdate")
    public void saveOrUpdate(HttpServletResponse response, EquipmentStatusVo equipmentStatusVo){
        JsonObject json = new JsonObject();
        try {
            EquipmentStatus equipmentStatus = this.equipmentStatusServiceImpl.saveOrUpdate(equipmentStatusVo);
            json.addProperty("result", true);
            json.addProperty("id",equipmentStatus.getId());
        } catch (Exception e) {
            json.addProperty("result", false);
            logger.error(e.getMessage(), e);
        }finally{
            this.print(json.toString());
        }
    }


    /**
     * 设备状态	删除
     * @param response
     * @param ids
     * @author xuezb
     * @Date 2019年2月18日
     */
    @RequestMapping(value="/equipmentStatus_delete")
    public void delete(HttpServletResponse response, String ids){
        JsonObject json = new JsonObject();
        try {
            if (StringUtils.isNotBlank(ids)){
                this.equipmentStatusServiceImpl.delete(EquipmentStatus.class, ids);
                json.addProperty("result", true);
            }
        } catch (Exception e) {
            json.addProperty("result", false);
            logger.error(e.getMessage(), e);
        }finally{
            this.print(json.toString());
        }
    }


    /**
     * 导出Excel
     * @param request
     * @param response
     * @param equipmentStatusVo
     * @return
     * @author xuezb
     * @Date 2019年3月5日
     */
    @RequestMapping(value="/equipmentStatus_export")
    public void export(HttpServletRequest request, HttpServletResponse response, EquipmentStatusVo equipmentStatusVo){
        //excel文件名
        String fileName = "设备状态汇总";

        //获取excle文档对象
        HSSFWorkbook wb = this.equipmentStatusServiceImpl.export(equipmentStatusVo);

        //将文件存到指定位置
        try {
            this.totalTableController.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
