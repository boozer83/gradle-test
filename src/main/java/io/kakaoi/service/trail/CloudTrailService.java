package io.kakaoi.service.trail;

import io.kakaoi.config.ApplicationProperties;
import io.kakaoi.config.Constants;
import io.kakaoi.exception.BusinessException;
import io.kakaoi.service.dto.TrailDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
@Profile("!mock-trail")
public class CloudTrailService implements TrailService {

    private static final Logger logger = LoggerFactory.getLogger(CloudTrailService.class);

    private final KafkaTemplate<String, TrailDTO.Data> kafkaTemplate;

    private final ApplicationProperties applicationProperties;

    public CloudTrailService(
        KafkaTemplate<String, TrailDTO.Data> kafkaTemplate,
        ApplicationProperties applicationProperties) {

        this.kafkaTemplate = kafkaTemplate;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void sendTrail(TrailDTO.Data data) {

        logger.debug("Trail Message: {}", data);

        ListenableFuture<SendResult<String, TrailDTO.Data>> futureSendResult =
            kafkaTemplate.send(applicationProperties.getTrail().getTopic(), data);

        try {
            futureSendResult.completable().join();

            logger.info("Trail Send Success: {}", data);
        }
        catch (KafkaException e) {
            logger.error("Trail Send Failed: {}", data);
            throw new BusinessException(e, Constants.CommonCode.INTERNAL_SERVER_ERROR);
        }
    }
}
