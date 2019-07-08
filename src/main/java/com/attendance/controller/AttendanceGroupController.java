package com.attendance.controller;

import com.attendance.module.AttendanceGroup;
import com.attendance.module.Shifts;
import com.attendance.service.IAttendanceGroupService;
import com.attendance.vo.AttendanceGroupVo;
import com.attendance.vo.ShiftsVo;
import com.common.base.controller.BaseController;
import com.common.utils.helper.JsonDateTimeValueProcessor;
import com.common.utils.helper.Pager;
import com.google.gson.JsonObject;
import com.urms.user.module.User;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.lang3.StringUtils;
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
public class AttendanceGroupController extends BaseController {

    @Autowired
    private IAttendanceGroupService attendanceGroupServiceImpl;


    /**
     * 考勤组	列表页面
     * @param httpSession
     * @param response
     * @param menuCode
     * @return
     * @author xuezb
     * @Date 2019年6月10日
     */
    @RequestMapping(value="/attendanceGroup_list")
    public String list(HttpSession httpSession, HttpServletResponse response, String menuCode) {
        this.getRequest().setAttribute("menuCode", menuCode);
        return "/page/attendance/attendanceGroup/attendanceGroup_list";
    }


    /**
     * 考勤组	列表数据加载
     * @param response
     * @param page
     * @param rows
     * @param attendanceGroupVo
     * @return
     * @author xuezb
     * @Date 2019年6月10日
     */
    @RequestMapping(value="/attendanceGroup_load")
    public void load(HttpServletResponse response, Integer page, Integer rows, AttendanceGroupVo attendanceGroupVo){
        Pager pager = this.attendanceGroupServiceImpl.queryEntityList(page, rows, attendanceGroupVo);
        JSONObject json = new JSONObject();
        json.put("total", pager.getRowCount());
        JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
        config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());//格式化日期
        json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
        this.print(json);
    }


    /**
     * 考勤组	编辑
     * @param request
     * @param winName
     * @param attendanceGroupVo
     * @return
     * @author xuezb
     * @throws ParseException
     * @Date 2019年6月10日
     */
    @RequestMapping(value="/attendanceGroup_edit")
    public String edit(HttpServletRequest request, String winName, AttendanceGroupVo attendanceGroupVo) throws ParseException{
        if(StringUtils.isNotBlank(attendanceGroupVo.getId())){
            AttendanceGroup attendanceGroup = this.attendanceGroupServiceImpl.getEntityById(AttendanceGroup.class, attendanceGroupVo.getId());
            BeanUtils.copyProperties(attendanceGroup, attendanceGroupVo);

            if(StringUtils.isNotBlank(attendanceGroupVo.getMemberUserIds())){
                String[] idArr = attendanceGroupVo.getMemberUserIds().split(",");
                String str = "";
                for (String userId : idArr) {
                    User user = this.attendanceGroupServiceImpl.getEntityById(User.class, userId);
                    str += user.getUserName() + ",";
                }
                str = (str.length() > 0 ? str.substring(0, str.length() - 1) : str);
                attendanceGroupVo.setMemberUserNames(str);
            }
            if(StringUtils.isNotBlank(attendanceGroupVo.getPrincipalUserIds())){
                String[] idArr = attendanceGroupVo.getPrincipalUserIds().split(",");
                String str = "";
                for (String userId : idArr) {
                    User user = this.attendanceGroupServiceImpl.getEntityById(User.class, userId);
                    str += user.getUserName() + ",";
                }
                str = (str.length() > 0 ? str.substring(0, str.length() - 1) : str);
                attendanceGroupVo.setPrincipalUserNames(str);
            }
            if(StringUtils.isNotBlank(attendanceGroupVo.getShiftsId())){
                Shifts shifts = this.attendanceGroupServiceImpl.getEntityById(Shifts.class, attendanceGroupVo.getShiftsId());
                attendanceGroupVo.setShiftsName(shifts.getShiftName());
            }

            request.setAttribute("attendanceGroupVo", attendanceGroupVo);
        }else{
            request.setAttribute("attendanceGroupVo", attendanceGroupVo);
        }
        request.setAttribute("winName", winName);
        return "/page/attendance/attendanceGroup/attendanceGroup_edit";
    }


    /**
     * 考勤组	保存or修改
     * @param response
     * @param attendanceGroupVo
     * @author xuezb
     * @Date 2019年6月10日
     */
    @RequestMapping(value="/attendanceGroup_saveOrUpdate")
    public void saveOrUpdate(HttpServletResponse response, AttendanceGroupVo attendanceGroupVo){
        JsonObject json = new JsonObject();
        try {
            AttendanceGroup attendanceGroup = this.attendanceGroupServiceImpl.saveOrUpdate(attendanceGroupVo);
            json.addProperty("result", true);
        } catch (Exception e) {
            json.addProperty("result", false);
            logger.error(e.getMessage(), e);
        }finally{
            this.print(json.toString());
        }
    }


    /**
     * 考勤组	删除
     * @param response
     * @param ids
     * @author xuezb
     * @Date 2019年6月10日
     */
    @RequestMapping(value="/attendanceGroup_delete")
    public void delete(HttpServletResponse response, String ids){
        JsonObject json = new JsonObject();
        try {
            if (StringUtils.isNotBlank(ids)){
                this.attendanceGroupServiceImpl.delete(AttendanceGroup.class, ids);
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
     * @intruduction 班次选择列表
     * @param shiftsVo 筛选条件
     * @return
     * @Date 22019年6月23日
     */
    @RequestMapping(value="/attendanceGroup_selectShifts")
    public String selectShifts(String winName, ShiftsVo shiftsVo) {
        this.getRequest().setAttribute("winName", winName);
        return "/page/attendance/attendanceGroup/attendanceGroup_selectShifts";
    }

    /**
     * @intruduction 选择地址界面
     * @param attendanceGroupVo
     * @return
     * @Date 22019年6月23日
     */
    @RequestMapping(value="/attendanceGroup_selectAddress")
    public String selectAddress(String winName, AttendanceGroupVo attendanceGroupVo) {
        this.getRequest().setAttribute("winName", winName);
        this.getRequest().setAttribute("attendanceGroupVo", attendanceGroupVo);
        return "/page/attendance/attendanceGroup/attendanceGroup_selectAddress";
    }
}
