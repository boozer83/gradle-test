package io.kakaoi.service.trail;

import io.kakaoi.service.dto.TrailDTO;

/**
 * 트레일 서비스 인터페이스
 */
public interface TrailService {

    /**
     * 트레일 로그를 전송한다.
     */
    void sendTrail(TrailDTO.Data data);
}
