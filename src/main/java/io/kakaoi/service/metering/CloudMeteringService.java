package io.kakaoi.service.metering;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kakaoi.config.ApplicationProperties;
import io.kakaoi.service.dto.IAMDTO;
import io.kakaoi.service.dto.MeteringDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

/**
 * Cloud Metering Service 구현체
 */
@Service
@Profile("!mock-metering")
public class CloudMeteringService implements MeteringService {

    private static final Logger logger = LoggerFactory.getLogger(CloudMeteringService.class);

    private static final DateTimeFormatter METERING_DT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssXXX");

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, MeteringDTO.Data> kafkaTemplate;

    /**
     * 미터링 로그 전송
     */
    public void sendMeteringLog() {
        ZonedDateTime startDateTime = ZonedDateTime.now();

        //this.sendProjectLog(project, startDateTime);
    }

    /**
     * send log
     */
    private void sendProjectLog(IAMDTO.ProjectSummery project, ZonedDateTime startDateTime) {
        MeteringDTO.Log log = new MeteringDTO.Log();
        log.setKicDomainId(project.getDomain().getId());
        log.setKicProductId(applicationProperties.getMetering().getProductId());
        log.setKicProjectId(project.getId());
        log.setKicRegionCode(applicationProperties.getMetering().getRegionCode());
        // TODO: 미터링 대상 Resource 구분 값 삽입
        log.setKicResourceId("");
        log.setMt(true);
        log.setComment("");
        log.setMeteringStartDatetime(startDateTime.format(METERING_DT_FORMAT));
        // TODO: 대상 서비스 별 미터링 인터벌 설정 (5m: 5분 간격 / 1h: 한시간 간격)
        log.setMeteringInterval("5m");
        // TODO: 대상 서비스 별 미터링 값 옵션 설정
        log.setValueOption("average");

        // TODO: 대상 서비스 별 미터링 메타데이터 구성
        MeteringDTO.Metadata metadata = new MeteringDTO.Metadata();
        metadata.setValue(0);
        metadata.setBilling(true);

        log.setMetadata(Collections.singletonList(metadata));

        try {
            MeteringDTO.Data data = new MeteringDTO.Data();

            String message = objectMapper.writeValueAsString(log);
            data.setMessage(message);
            data.setServiceId(applicationProperties.getMetering().getServiceId());

            logger.debug("Metering Message: {}", data);

            // TODO: 리소스 이름 추가
            MeteringSendLoggingCallback loggingCallback = new MeteringSendLoggingCallback("", data);

            kafkaTemplate.sendDefault(data).addCallback(loggingCallback);
        }
        catch (Exception ignore) {}
    }

    private static class MeteringSendLoggingCallback implements ListenableFutureCallback<SendResult<String, MeteringDTO.Data>> {

        private static final Logger log = LoggerFactory.getLogger(MeteringSendLoggingCallback.class);

        private final String resourceName;

        private final MeteringDTO.Data data;

        public MeteringSendLoggingCallback(String resourceName, MeteringDTO.Data data) {
            this.resourceName = resourceName;
            this.data = data;
        }

        @Override
        public void onFailure(Throwable ex) {
            log.error("Metering Send Failed: " + getInfoString(), ex);
        }

        @Override
        public void onSuccess(SendResult<String, MeteringDTO.Data> result) {
            log.info("Metering Send Success: {}", getInfoString());
        }

        private String getInfoString() {
            return MessageFormat.format("[resourceName: {0}, value: {1}]", resourceName, data);
        }
    }

}
