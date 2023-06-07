package io.kakaoi.web.rest;

import io.kakaoi.service.dto.TrailDTO;
import io.kakaoi.service.trail.TrailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "trail")
@RestController
public class TrailController {

    private final TrailService trailService;

    public TrailController(TrailService trailService) {
        this.trailService = trailService;
    }

    @PostMapping("/proxy/trail/send")
    public Result<?> sendTrail(
        @RequestBody TrailDTO.Data trailData
        ) {

        trailService.sendTrail(trailData);
        return Result.ok();
    }
}
