package com.common.utils.tld;

import com.common.utils.cache.Cache;
import com.urms.dataDictionary.module.CategoryAttribute;

import org.apache.commons.lang.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zengcong
 * @Description: 数据字典翻译工具类
 * @date 2018/3/8 09:58
 */
public class DictUtils {
    /**
     * 根据数据字典编码,key 获取对应的值
     * @param dictCode
     * @param key
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String getDictValue(String dictCode,String key){
        String value = "";
        if(StringUtils.isNotBlank(dictCode) && StringUtils.isNotBlank(key)){
            if(Cache.getDictByCode.get(dictCode) == null){
                return null;
            }
            Iterator i$ = ((Set) Cache.getDictByCode.get(dictCode)).iterator();
            while(i$.hasNext()) {
                CategoryAttribute ca = (CategoryAttribute)i$.next();
                if (ca.getAttrKey().equals(key)) {
                    value = ca.getAttrValue();
                    break;
                }
            }
        }
        return value;
    }

    /**
     *
     * @ClassName:DictUtils.java
     * @Description:根据数据字典编码,attvalue 获取对应的attkey
     * @param dictCode
     * @param attValue
     * @return
     * @author qinyongqian
     * @date 2018年4月11日
     */
    public static String getDictAttrKey(String dictCode,String attValue){
        String key = "";
        if(StringUtils.isNotBlank(dictCode) && StringUtils.isNotBlank(attValue)){
            if(Cache.getDictByCode.get(dictCode) == null){
                return null;
            }
            @SuppressWarnings("rawtypes")
            Iterator i$ = ((Set) Cache.getDictByCode.get(dictCode)).iterator();
            while(i$.hasNext()) {
                CategoryAttribute ca = (CategoryAttribute)i$.next();
                if (ca.getAttrValue().equals(attValue)) {
                    key = ca.getAttrKey();
                    break;
                }
            }
        }
        return key;
    }

    /**
     * 根据数据字典编码,key 获取对应的多值 "/"隔开
     * @param dictCode
     * @param key
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static String getManyDictValue(String dictCode,String key){

        String value = "";
        if(StringUtils.isNotBlank(dictCode) && StringUtils.isNotBlank(key)){
            if(Cache.getDictByCode.get(dictCode) == null){
                return null;
            }
            StringBuffer stringBuffer = new StringBuffer();
            Set<CategoryAttribute> set = (Set)Cache.getDictByCode.get(dictCode);
            if (set != null && key != null) {
                Iterator i$ = set.iterator();

                while(i$.hasNext()) {
                    CategoryAttribute ca = (CategoryAttribute)i$.next();
                    if (key.indexOf(ca.getAttrKey())>-1) {
                        stringBuffer.append(ca.getAttrValue());
                        stringBuffer.append("/");
                    }
                }
                value = stringBuffer.toString();
                value = value.substring(0,(value.length()-1));
            }
        }

        return value;
    }

    /**
     * 根据身份证号码获取年龄
     * @param idNo
     * @return
     */
    public static String getAgeByIDNo(String idNo){
        String result = "";
        if(idNo != null && (idNo.length() == 18 || idNo.length() ==15)){
            String strBirthday = "";
            if (idNo.length() == 18){
                strBirthday = idNo.substring(6, 10) + "-" + idNo.substring(10, 12) + "-" + idNo.substring(12, 14);
            }
            if (idNo.length() == 15){
                strBirthday = "19" + idNo.substring(6, 8) + "-" + idNo.substring(8, 10) + "-" + idNo.substring(10, 12);
            }
            // 先截取到字符串中的年、月、日
            String strs[] = strBirthday.trim().split("-");
            int selectYear = Integer.parseInt(strs[0]);
            int selectMonth = Integer.parseInt(strs[1]);
            int selectDay = Integer.parseInt(strs[2]);
            // 得到当前时间的年、月、日
            Calendar cal = Calendar.getInstance();
            int yearNow = cal.get(Calendar.YEAR);
            int monthNow = cal.get(Calendar.MONTH) + 1;
            int dayNow = cal.get(Calendar.DATE);

            // 用当前年月日减去生日年月日
            int yearMinus = yearNow - selectYear;
            int monthMinus = monthNow - selectMonth;
            int dayMinus = dayNow - selectDay;

            int age = yearMinus;// 先大致赋值
            if (yearMinus < 0) {// 选了未来的年份
                age = 0;
            } else if (yearMinus == 0) {// 同年的，要么为1，要么为0
                if (monthMinus < 0) {// 选了未来的月份
                    age = 0;
                } else if (monthMinus == 0) {// 同月份的
                    if (dayMinus < 0) {// 选了未来的日期
                        age = 0;
                    } else if (dayMinus >= 0) {
                        age = 1;
                    }
                } else if (monthMinus > 0) {
                    age = 1;
                }
            } else if (yearMinus > 0) {
                if (monthMinus < 0) {// 当前月>生日月
                } else if (monthMinus == 0) {// 同月份的，再根据日期计算年龄
                    if (dayMinus < 0) {
                    } else if (dayMinus >= 0) {
                        age = age + 1;
                    }
                } else if (monthMinus > 0) {
                    age = age + 1;
                }
            }
            result = String.valueOf(age);
            return result;
        }

        return result;
    }


    /**
     * 根据行政区划编码获取对应的行政区划名称
     * @param code
     * @return
     */
    public static String getAreaValue(String code){
        String value = "";
        if(StringUtils.isNotBlank(code) ){
            if(Cache.getDict.get(code) != null){
                value = Cache.getDict.get(code);
            }
        }
        return value;
    }

    /**
     * 获取时间差
     * @param start
     * @param end
     * @return
     */
    public static String getLengthTime(Date start,Date end){
        String time = "-";
        if(start != null){
            if(end == null){
                end = new Date();
            }
            long startTime = start.getTime();
            long endTime = end.getTime();
            long lengthTime = endTime - startTime;
            long nd = 1000 * 24 * 60 * 60;
            long nh = 1000 * 60 * 60;
            long nm = 1000 * 60;
            long ns = 1000;
            // 计算差多少天
            long day = lengthTime / nd;
            // 计算差多少小时
            long hour = lengthTime % nd / nh;
            // 计算差多少分钟
            long min = lengthTime % nd % nh / nm;
            // 计算差多少秒//输出结果
            long sec = lengthTime % nd % nh % nm / ns;
            if(lengthTime >= nm && lengthTime < nh) {
                time = min + "分钟"+sec+"秒";
            }else if (lengthTime >= nh && lengthTime < nd) {
                time= hour + "小时" + min + "分钟"+sec+"秒";
            }else if(lengthTime >= nd){
                time= day + "天" + hour + "小时" + min + "分钟"+sec+"秒";
            }else {
                time = sec + "秒";
            }
        }
        return time;
    }
}
