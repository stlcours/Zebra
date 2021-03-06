/*
 * Copyright (c) 2011-2018, Meituan Dianping. All Rights Reserved.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dianping.zebra.group.router;

import com.dianping.zebra.config.ConfigService;
import com.dianping.zebra.group.config.datasource.entity.DataSourceConfig;

import java.util.Map;

public class BackupDataSourceRouter extends RegionAwareRouter {
	private Map<String, DataSourceConfig> dataSourceConfigs;

	public BackupDataSourceRouter(Map<String, DataSourceConfig> dataSourceConfigs, String configManagerType, ConfigService configService, String routerStrategy) {
		super(dataSourceConfigs, configManagerType, configService, routerStrategy);
		this.dataSourceConfigs = dataSourceConfigs;
	}

	public String getName() {
		return "backup";
	}

	public RouterTarget select(RouterContext routerContext) {
		RouterTarget target = super.select(routerContext);

		if (target == null) {
			for (DataSourceConfig config : dataSourceConfigs.values()) {
				if (config.getActive() && config.getCanRead() && config.getWeight() == 0) {
					target = new RouterTarget(config.getId());

					break;
				}
			}
		}

		return target;
	}
}
