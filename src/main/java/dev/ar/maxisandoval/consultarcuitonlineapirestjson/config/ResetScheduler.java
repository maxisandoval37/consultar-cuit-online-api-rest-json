package dev.ar.maxisandoval.consultarcuitonlineapirestjson.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResetScheduler {

    private final ApplicationContext applicationContext;

    @Scheduled(fixedRate = 3600000) // 3600000 ms = 1 hora
    public void resetDatabase() {
        ((ConfigurableApplicationContext) applicationContext).close();
        System.exit(0);
    }
}