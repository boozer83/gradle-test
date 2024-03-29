package io.kakaoi.service.metering;

import io.kakaoi.service.dto.MeteringDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * 미터링 목업
 */
@Service
@Profile("mock-metering")
public class LogPrintMeteringService implements MeteringService {

    private static final Logger log = LoggerFactory.getLogger(LogPrintMeteringService.class);

    @Override
    public void sendMetering(MeteringDTO.Data data) {
        log.info("metering data: {}", data);
    }

}
