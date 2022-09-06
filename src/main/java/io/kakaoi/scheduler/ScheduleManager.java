package io.kakaoi.scheduler;

import io.kakaoi.service.metering.MeteringService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleManager {

    private final Logger logger = LoggerFactory.getLogger(ScheduleManager.class);

    @Autowired
    private MeteringService meteringService;

    @Scheduled(cron="${application.cron-job.metering:-}")
    @SchedulerLock(name = "sendMeteringLog", lockAtMostFor = "PT1M", lockAtLeastFor = "PT1M")
    public void metering() throws Exception {

        logger.info("###########################  Metering Schedule Start  ###########################");
        // 미터링 로그 전송
        meteringService.sendMeteringLog();

        logger.info("###########################  Metering Schedule End  ###########################");
    }

}
