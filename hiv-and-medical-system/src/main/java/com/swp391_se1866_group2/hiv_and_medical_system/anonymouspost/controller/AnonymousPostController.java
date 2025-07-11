package com.swp391_se1866_group2.hiv_and_medical_system.anonymouspost.controller;

import com.swp391_se1866_group2.hiv_and_medical_system.anonymouspost.dto.request.AnonymousPostCreationRequest;
import com.swp391_se1866_group2.hiv_and_medical_system.anonymouspost.dto.response.AnonymousPostResponse;
import com.swp391_se1866_group2.hiv_and_medical_system.anonymouspost.service.AnonymousPostService;
import com.swp391_se1866_group2.hiv_and_medical_system.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/anonymous-posts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AnonymousPostController {
    AnonymousPostService anonymousPostService;

    @PostMapping
    public ApiResponse<AnonymousPostResponse> createAnonymousPost(@RequestBody @Valid AnonymousPostCreationRequest request){
        return ApiResponse.<AnonymousPostResponse>builder()
                .success(true)
                .data(anonymousPostService.createAnonymousPost(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<AnonymousPostResponse>> getAllAnonymousPosts(@RequestParam(name = "title", defaultValue = "") String title, @RequestParam(defaultValue = "desc") String sortOrder, @RequestParam(name = "sortBy", defaultValue = "created_at") String sortBy , @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable;
        if(sortOrder.equalsIgnoreCase("desc")){
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }else{
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        }
        return ApiResponse.<List<AnonymousPostResponse>>builder()
                .success(true)
                .data(anonymousPostService.getAllAnonymousPosts(pageable, title))
                .build();
    }

}
