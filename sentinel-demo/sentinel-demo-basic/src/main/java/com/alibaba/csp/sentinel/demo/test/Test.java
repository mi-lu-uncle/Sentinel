package com.alibaba.csp.sentinel.demo.test;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;

/**
 * @author gao chao
 * @date 2025/7/18
 */
public class Test {

    public static void main(String[] args) throws BlockException {
        ContextUtil.enter("entrance1", "appA");
        Entry nodeA = SphU.entry("nodeA");
        if (nodeA != null) {
            nodeA.exit();
        }

        ContextUtil.enter("entrance1", "appB");
        Entry nodeB = SphU.entry("nodeB");
        if (nodeB != null) {
            nodeB.exit();
        }
        ContextUtil.exit();
    }

}
