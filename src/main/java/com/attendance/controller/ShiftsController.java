package com.attendance.controller;

import com.attendance.module.Shifts;
import com.attendance.service.IShiftsService;
import com.attendance.vo.ShiftsVo;
import com.common.base.controller.BaseController;
import com.common.utils.helper.JsonDateTimeValueProcessor;
import com.common.utils.helper.Pager;
import com.google.gson.JsonObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.Date;


@Controller
@RequestMapping("/attendance")
public class ShiftsController extends BaseController {

    @Autowired
    private IShiftsService shiftsServiceImpl;


    /**
     * 考勤班次	列表页面
     * @param httpSession
     * @param response
     * @param menuCode
     * @return
     * @author xuezb
     * @Date 2019年6月10日
     */
    @RequestMapping(value="/shifts_list")
    public String list(HttpSession httpSession, HttpServletResponse response, String menuCode) {
        this.getRequest().setAttribute("menuCode", menuCode);
        return "/page/attendance/shifts/shifts_list";
    }


    /**
     * 考勤班次	列表数据加载
     * @param response
     * @param page
     * @param rows
     * @param shiftsVo
     * @return
     * @author xuezb
     * @Date 2019年6月10日
     */
    @RequestMapping(value="/shifts_load")
    public void load(HttpServletRequest request, HttpServletResponse response, Integer page, Integer rows, ShiftsVo shiftsVo){
        Pager pager = this.shiftsServiceImpl.queryEntityList(page, rows, shiftsVo);
        JSONObject json = new JSONObject();
        json.put("total", pager.getRowCount());
        JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
        config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());//格式化日期
        json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
        this.print(json);
    }


    /**
     * 考勤班次	编辑
     * @param request
     * @param winName
     * @param shiftsVo
     * @return
     * @author xuezb
     * @throws ParseException
     * @Date 2019年6月10日
     */
    @RequestMapping(value="/shifts_edit")
    public String edit(HttpServletRequest request, String winName, ShiftsVo shiftsVo) throws ParseException{
        if(StringUtils.isNotBlank(shiftsVo.getId())){
            Shifts shifts = this.shiftsServiceImpl.getEntityById(Shifts.class, shiftsVo.getId());
            BeanUtils.copyProperties(shifts, shiftsVo);
            request.setAttribute("shiftsVo", shiftsVo);
        }else{
            request.setAttribute("shiftsVo", shiftsVo);
        }
        request.setAttribute("winName", winName);
        return "/page/attendance/shifts/shifts_edit";
    }


    /**
     * 考勤班次	保存or修改
     * @param response
     * @param shiftsVo
     * @author xuezb
     * @Date 2019年6月10日
     */
    @RequestMapping(value="/shifts_saveOrUpdate")
    public void saveOrUpdate(HttpServletResponse response, ShiftsVo shiftsVo){
        JsonObject json = new JsonObject();
        try {
            Shifts shifts = this.shiftsServiceImpl.saveOrUpdate(shiftsVo);
            json.addProperty("result", true);
        } catch (Exception e) {
            json.addProperty("result", false);
            logger.error(e.getMessage(), e);
        }finally{
            this.print(json.toString());
        }
    }


    /**
     * 考勤班次	删除
     * @param response
     * @param ids
     * @author xuezb
     * @Date 2019年6月10日
     */
    @RequestMapping(value="/shifts_delete")
    public void delete(HttpServletResponse response, String ids){
        JsonObject json = new JsonObject();
        try {
            if (StringUtils.isNotBlank(ids)){
                this.shiftsServiceImpl.delete(Shifts.class, ids);
                json.addProperty("result", true);
            }
        } catch (Exception e) {
            json.addProperty("result", false);
            logger.error(e.getMessage(), e);
        }finally{
            this.print(json.toString());
        }
    }

}
