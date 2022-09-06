package io.kakaoi.web.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Health")
@RestController
public class HealthController {

    private static final Logger log = LoggerFactory.getLogger(HealthController.class);

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    @Operation(
        summary = "헬스 체크",
        description = "App 헬스 체크"
    )
    public Result<?> health() {
        log.trace("check health");
        return Result.ok(true);
    }

}
