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
package com.alibaba.csp.sentinel.slotchain;

import com.alibaba.csp.sentinel.context.Context;

/**
 * @author qinan.qn
 * @author jialiang.linjl
 */
public class DefaultProcessorSlotChain extends ProcessorSlotChain {

    /**
     * 该链表包含两个特殊节点 (first 和 end) 以及两个空指针节点 (null)
     *
     * 节点说明：
     *  ┌──────────────┬──────────────┐
     *  │   first节点  │   null节点    │
     *  │   (蓝色)     │   (粉色)      │
     *  │   next: ──────> null        │
     *  └──────────────┴──────────────┘
     *          ▲
     *          │
     *  ┌──────────────┬──────────────┐
     *  │   end节点    │   null节点    │
     *  │   (蓝色)     │   (粉色)      │
     *  │   next: ──────> null        │
     *  │   ↑(辅助指针)                │
     *  └──────────────┴──────────────┘
     *
     * 关键特性:
     *  1. first 和 end 都是有效数据节点
     *  2. 每个有效节点都指向独立的 null 节点
     *  3. end 节点额外指向 first 节点形成特殊连接
     *  4. 蓝色节点表示有效数据，粉色节点表示空指针
     */
    // 头节点
    AbstractLinkedProcessorSlot<?> first = new AbstractLinkedProcessorSlot<>() {

        @Override
        public void entry(Context context, ResourceWrapper resourceWrapper, Object t, int count, boolean prioritized, Object... args)
            throws Throwable {
            super.fireEntry(context, resourceWrapper, t, count, prioritized, args);
        }

        @Override
        public void exit(Context context, ResourceWrapper resourceWrapper, int count, Object... args) {
            super.fireExit(context, resourceWrapper, count, args);
        }

    };
    // 尾节点
    AbstractLinkedProcessorSlot<?> end = first;

    @Override
    public void addFirst(AbstractLinkedProcessorSlot<?> protocolProcessor) {
        protocolProcessor.setNext(first.getNext());
        first.setNext(protocolProcessor);
        if (end == first) {
            end = protocolProcessor;
        }
    }

    @Override
    public void addLast(AbstractLinkedProcessorSlot<?> protocolProcessor) {
        end.setNext(protocolProcessor);
        end = protocolProcessor;
    }

    /**
     * Same as {@link #addLast(AbstractLinkedProcessorSlot)}.
     *
     * @param next processor to be added.
     */
    @Override
    public void setNext(AbstractLinkedProcessorSlot<?> next) {
        addLast(next);
    }

    @Override
    public AbstractLinkedProcessorSlot<?> getNext() {
        return first.getNext();
    }

    /**
     * first#transformEntry -> ProcessorSlot#entry ->
     * AbstractLinkedProcessorSlot#fireEntry -> next.transformEntry -> next.entry ->
     * AbstractLinkedProcessorSlot#fireEntry -> next.transformEntry -> next.entry -> ...
     *
     * @param context         current {@link Context} 当前请求上下文
     * @param resourceWrapper current resource 资源包装对象，表示被保护的资源
     * @param t           generics parameter, usually is a {@link com.alibaba.csp.sentinel.node.Node} 泛型参数，通常为节点对象
     * @param count           tokens needed 需要的令牌数
     * @param prioritized     whether the entry is prioritized 是否优先级请求
     * @param args            parameters of the original call 原始调用参数
     * @throws Throwable
     */
    @Override
    public void entry(Context context, ResourceWrapper resourceWrapper, Object t, int count, boolean prioritized, Object... args)
        throws Throwable {
        first.transformEntry(context, resourceWrapper, t, count, prioritized, args);
    }

    @Override
    public void exit(Context context, ResourceWrapper resourceWrapper, int count, Object... args) {
        first.exit(context, resourceWrapper, count, args);
    }

}
