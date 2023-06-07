package io.kakaoi.service.trail;

import io.kakaoi.service.dto.TrailDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("mock-trail")
public class LogPrintTrailService implements TrailService {

    private static final Logger log = LoggerFactory.getLogger(LogPrintTrailService.class);

    @Override
    public void sendTrail(TrailDTO.Data data) {
        log.info("trail data: {}", data.toString());
    }
}
