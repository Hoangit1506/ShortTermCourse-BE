package com.short_term_course.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.short_term_course.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UploadService {
    private final Cloudinary cloudinary;
    private final SecurityUtil securityUtil;

    @Value("${app.cloudinary.folder}")
    private String ROOT_FOLDER;

    private String doUpload(MultipartFile file, String folder, boolean isVideo) throws IOException {
        Map<String,Object> opts = ObjectUtils.asMap("folder", folder);
        if (isVideo) opts.put("resource_type", "video");
        Map result = cloudinary.uploader().upload(file.getBytes(), opts);
        return result.get("secure_url").toString();
    }

    public String uploadAvatar(MultipartFile file) throws IOException {
        String userId = securityUtil.getAccountId();
        String folder = String.format("%s/avatars/%s", ROOT_FOLDER, userId);
        return doUpload(file, folder, false);
    }

    public String uploadCourseThumbnail(String courseId, MultipartFile file) throws IOException {
        String folder = String.format("%s/courses/%s/thumbnail", ROOT_FOLDER, courseId);
        return doUpload(file, folder, false);
    }

    public String uploadCourseVideo(String courseId, MultipartFile file) throws IOException {
        String folder = String.format("%s/courses/%s/video", ROOT_FOLDER, courseId);
        return doUpload(file, folder, true);
    }

    public void deleteFile(String fileUrl) throws IOException {
        String[] parts = fileUrl.split("/");
        int idx = Arrays.asList(parts).indexOf(ROOT_FOLDER);
        if (idx < 0 || idx + 2 >= parts.length) {
            throw new IllegalArgumentException("Invalid URL");
        }
        String[] publicIdParts = Arrays.copyOfRange(parts, idx, parts.length);
        String last = publicIdParts[publicIdParts.length - 1];
        publicIdParts[publicIdParts.length - 1] = last.replaceFirst("\\.[^.]+$", "");
        String publicId = String.join("/", publicIdParts);

        boolean isVideo = fileUrl.contains("/video/");
        Map opts = isVideo
                ? ObjectUtils.asMap("resource_type", "video")
                : ObjectUtils.emptyMap();

        cloudinary.uploader().destroy(publicId, opts);
    }
}
