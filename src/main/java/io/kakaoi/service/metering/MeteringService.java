package io.kakaoi.service.metering;

import io.kakaoi.service.dto.MeteringDTO;

/**
 * 미터링 서비스 인터페이스
 */
public interface MeteringService {

    /**
     * 미터링 로그를 전송한다.
     */
    void sendMetering(MeteringDTO.Data data);

}
