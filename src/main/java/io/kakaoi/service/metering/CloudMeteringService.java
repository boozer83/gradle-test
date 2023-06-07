package io.kakaoi.service.metering;

import io.kakaoi.config.ApplicationProperties;
import io.kakaoi.config.Constants;
import io.kakaoi.exception.BusinessException;
import io.kakaoi.service.dto.MeteringDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * Cloud Metering Service
 */
@Service
@Profile("!mock-metering")
public class CloudMeteringService implements MeteringService {

    private static final Logger logger = LoggerFactory.getLogger(CloudMeteringService.class);

    private final KafkaTemplate<String, MeteringDTO.Data> kafkaTemplate;

    public CloudMeteringService(KafkaTemplate<String, MeteringDTO.Data> kafkaTemplate) {

        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendMetering(MeteringDTO.Data data) {
        logger.debug("Metering Message: {}", data);

        ListenableFuture<SendResult<String, MeteringDTO.Data>> futureSendResult = kafkaTemplate.sendDefault(data);
        try {
            futureSendResult.completable().join();

            logger.info("Metering Send Success: {}", data);
        }
        catch (KafkaException e) {
            logger.error("Metering Send Failed: {}", data);
            throw new BusinessException(e, Constants.CommonCode.INTERNAL_SERVER_ERROR);
        }
    }

}
