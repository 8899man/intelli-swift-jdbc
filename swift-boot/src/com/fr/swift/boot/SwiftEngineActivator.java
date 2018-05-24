package com.fr.swift.boot;

import com.fr.module.Activator;
import com.fr.swift.cube.queue.ProviderTaskManager;
import com.fr.swift.log.SwiftLoggers;
import com.fr.swift.service.LocalSwiftServerService;
import com.fr.swift.service.SwiftAnalyseService;
import com.fr.swift.service.SwiftHistoryService;
import com.fr.swift.service.SwiftIndexingService;
import com.fr.swift.service.SwiftRealTimeService;
import com.fr.swift.util.Crasher;

/**
 * @author anchore
 * @date 2018/5/24
 */
public class SwiftEngineActivator extends Activator {
    @Override
    public void start() {
        startSwift();
    }

    private void startSwift() {
        try {
            new LocalSwiftServerService().start();
            new SwiftAnalyseService().start();
            new SwiftHistoryService().start();
            new SwiftIndexingService().start();
            new SwiftRealTimeService().start();
            ProviderTaskManager.start();
            SwiftLoggers.getLogger().info("swift engine started");
        } catch (Exception e) {
            SwiftLoggers.getLogger().error("swift engine start failed", e);
            Crasher.crash(e);
        }
    }

    @Override
    public void stop() {
        SwiftLoggers.getLogger().info("swift engine stopped");
    }
}