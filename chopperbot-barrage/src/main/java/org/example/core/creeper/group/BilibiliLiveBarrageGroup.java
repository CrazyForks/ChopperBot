package org.example.core.creeper.group;

import org.example.constpool.ConstPool;
import org.example.core.manager.AbstractCreeperGroup;
import org.springframework.stereotype.Component;

/**
 * @author Genius
 * @date 2023/09/07 17:22
 **/
@Component
public class BilibiliLiveBarrageGroup extends AbstractCreeperGroup {
    @Override
    public String getPlatform() {
        return ConstPool.BILIBILI;
    }

    @Override
    public String getFunctionName() {
        return "live_barrage";
    }
}
