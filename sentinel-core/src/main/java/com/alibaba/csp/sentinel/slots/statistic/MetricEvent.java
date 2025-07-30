/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.slots.statistic;

/**
 * @author Eric Zhao
 * 指标类型，例如通过数量、阻塞数量、异常数量、成功数量、响应时间等。
 */
public enum MetricEvent {

    /**
     * Normal pass.
     * 表示到来的数量，即此刻通过 Sentinel-Go 规则的流量数量
     */
    PASS,
    /**
     * Normal block.
     * 表示被拦截的流量数量
     */
    BLOCK,
    EXCEPTION,
    SUCCESS,
    // 单次请求的request time
    RT,

    /**
     * Passed in future quota (pre-occupied, since 1.5.0).
     */
    OCCUPIED_PASS
}
