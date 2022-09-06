package io.kakaoi.web.rest;

import io.kakaoi.service.SampleService;
import io.kakaoi.service.dto.PagingDTO;
import io.kakaoi.service.dto.SampleDTO;
import io.kakaoi.web.rest.vm.FilterVM;
import io.kakaoi.web.rest.vm.SampleVM;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "sample")
@RestController
@RequestMapping("/api/sample")
public class SampleController {

    @Autowired
    private SampleService sampleService;

    @GetMapping
    @PreAuthorize("hasPermission(null, 'reader')")
    @Operation(
        summary = "샘플 페이징 조회",
        description = "샘플 리스트를 페이징으로 가져온다."
    )
    public Result<PagingDTO<SampleDTO.Info>> getSamplePage(
        @ParameterObject @ModelAttribute FilterVM filterVM,
        @ParameterObject Pageable pageable
    ) {
        return Result.okWithPaging(sampleService.getInfoPage(filterVM, pageable));
    }

    @GetMapping("/{sampleId}")
    @PreAuthorize("hasPermission(#sampleId, 'sampleId', 'reader')")
    @Operation(
        summary = "샘플 조회",
        description =
            "샘플을 가져온다.\n\n" +
            "* 이와 같이 작성 가능"
    )
    public Result<SampleDTO.Info> getSample(
        @PathVariable("sampleId") String sampleId
    ) {
        return Result.ok(sampleService.getInfo(sampleId));
    }

    @PostMapping
    @PreAuthorize("hasPermission(null, 'member')")
    @Operation(
        summary = "샘플 생성",
        description = "샘플을 생성한다."
    )
    public Result<SampleDTO.Info> createSample(
        @Valid @RequestBody SampleVM.Apply sampleVM
    ) {
        return Result.ok(sampleService.createSample(sampleVM));
    }

    @PutMapping("/{sampleId}")
    @PreAuthorize("hasPermission(#sampleId, 'sampleId', 'member')")
    @Operation(
        summary = "샘플 수정",
        description = "샘플을 수정한다."
    )
    public Result<SampleDTO.Info> updateSample(
        @PathVariable("sampleId") String sampleId,
        @Valid @RequestBody SampleVM.Apply sampleVM
    ) {
        return Result.ok(sampleService.createSample(sampleVM));
    }

    @DeleteMapping("/{sampleId}")
    @PreAuthorize("hasPermission(#sampleId, 'sampleId', 'member')")
    @Operation(
        summary = "샘플 삭제",
        description = "샘플을 삭제한다."
    )
    public Result<?> deleteSample(
        @PathVariable("sampleId") String sampleId
    ) {
        sampleService.deleteSample(sampleId);
        return Result.ok();
    }

}
