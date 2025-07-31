package com.alibaba.csp.sentinel.custom;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.junit.Test;

/**
 * 节点测试
 * @author gao chao
 * @date 2025/7/18
 */
public class NodeTest {

    @Test
    public void nodeTest() {
        // 创建一个来自appA访问的Context
        // Context的名称为entrance1
        ContextUtil.enter("entrance1", "appA");
        // Entry就是一个资源操作对象
        Entry nodeA = null;
        Entry nodeB = null;
        try {
            // 获取资源resource的entry
            nodeA = SphU.entry("resource1");
            // 如果代码走到这个位置，就说明当前请求通过了流控，可以继续记性相关业务处理
            nodeB = SphU.entry("resource2");
            // 如果代码走到这个位置，就说明当前请求通过了流控，可以继续记性相关业务处理
        } catch (BlockException e) {
            // 如果没有通过走到了这里，就表示请求被限流，这里进行降级操作
            e.printStackTrace();
        } finally {
            if (nodeA != null) {
                nodeA.exit();
            }
            if (nodeB != null) {
                nodeB.exit();
            }
        }
        // 释放Context
        ContextUtil.exit();

        // ---------------------创建另一个来自appA访问的Context------------------------------

        // 创建一个来自appA访问的Context
        // Context的名称为entrance2
        ContextUtil.enter("entrance2", "appA");
        // Entry就是一个资源操作对象
        Entry nodeC = null;
        try {
            nodeB = SphU.entry("resource2");
            // 如果代码走到这个位置，就说明当前请求通过了流控，可以继续记性相关业务处理
            nodeC = SphU.entry("resource3");
        } catch (BlockException e) {
            // 如果没有通过走到了这里，就表示请求被限流，这里进行降级操作
            e.printStackTrace();
        } finally {
            if (nodeB != null) {
                nodeB.exit();
            }
            if (nodeC != null) {
                nodeC.exit();
            }
        }
        // 释放Context
        ContextUtil.exit();
    }

}
