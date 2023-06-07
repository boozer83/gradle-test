package io.kakaoi.web.rest;

import io.kakaoi.service.dto.MeteringDTO;
import io.kakaoi.service.metering.MeteringService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "metering")
@RestController
public class MeteringController {

    private final MeteringService meteringService;

    public MeteringController(MeteringService meteringService) {
        this.meteringService = meteringService;
    }

    @PostMapping("/proxy/metering/send")
    public Result<?> sendMetering(
        @RequestBody MeteringDTO.Data meteringData
    ) {
        meteringService.sendMetering(meteringData);
        return Result.ok();
    }

}
