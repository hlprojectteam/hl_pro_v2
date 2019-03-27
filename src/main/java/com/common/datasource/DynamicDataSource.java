package com.common.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
/**
 * @intruduction 数据源动态切换类
 * @author Mr.Wang
 * @Date 2016年1月29日上午9:46:40
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
	@Override
    protected Object determineCurrentLookupKey() {
        return DataSourceSwitcher.getDataSource();
    }
}
