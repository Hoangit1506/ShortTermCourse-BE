package com.short_term_course.controller;

import com.short_term_course.dto.api.ApiResponse;
import com.short_term_course.service.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
@Slf4j
public class UploadController {
    private final UploadService uploadService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/avatar")
    public ResponseEntity<ApiResponse<String>> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            String url = uploadService.uploadAvatar(file);
            return ResponseEntity.ok(ApiResponse.<String>builder()
                    .code("upload-s-01").message("Avatar uploaded").data(url).build());
        } catch (IOException e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.<String>builder().success(false).message("Upload failed").build());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/courses/{courseId}/thumbnail")
    public ResponseEntity<ApiResponse<String>> uploadCourseThumbnail(
            @PathVariable String courseId,
            @RequestParam("file") MultipartFile file) {
        try {
            String url = uploadService.uploadCourseThumbnail(courseId, file);
            return ResponseEntity.ok(ApiResponse.<String>builder()
                    .code("upload-s-02").message("Thumbnail uploaded").data(url).build());
        } catch (IOException e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.<String>builder().success(false).message("Upload failed").build());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/courses/{courseId}/video")
    public ResponseEntity<ApiResponse<String>> uploadCourseVideo(
            @PathVariable String courseId,
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.getSize() > 100L * 1024 * 1024) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.<String>builder().success(false).message("File too large").build());
            }
            String url = uploadService.uploadCourseVideo(courseId, file);
            return ResponseEntity.ok(ApiResponse.<String>builder()
                    .code("upload-s-03").message("Video uploaded").data(url).build());
        } catch (IOException e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.<String>builder().success(false).message("Upload failed").build());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','LECTURER','USER')")
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteFile(@RequestParam("url") String fileUrl) {
        try {
            uploadService.deleteFile(fileUrl);
            return ResponseEntity.ok(ApiResponse.<Void>builder()
                    .code("upload-s-04").message("Deleted").build());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.<Void>builder().success(false).message("Delete failed").build());
        }
    }
}