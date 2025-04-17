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
    private String ROOT_FOLDER;        // ví dụ "short_course"

    // helper chung
    private String doUpload(MultipartFile file, String folder, boolean isVideo) throws IOException {
        Map<String,Object> opts = ObjectUtils.asMap("folder", folder);
        if (isVideo) opts.put("resource_type", "video");
        Map result = cloudinary.uploader().upload(file.getBytes(), opts);
        return result.get("secure_url").toString();
    }

//    private String doUpload(MultipartFile file, String folder, boolean isVideo) throws IOException {
//        Map<String,Object> opts = ObjectUtils.asMap("folder", folder);
//        if (isVideo) opts.put("resource_type", "video");
//        // Sử dụng InputStream thay vì byte[] để giảm tải bộ nhớ
//        Map<?,?> result = cloudinary.uploader()
//                .upload(file.getInputStream(), opts);
//        return result.get("secure_url").toString();
//    }

    // 1. Upload avatar cho user
    public String uploadAvatar(MultipartFile file) throws IOException {
        String userId = securityUtil.getAccountId();
        String folder = String.format("%s/avatars/%s", ROOT_FOLDER, userId);
        return doUpload(file, folder, false);
    }

    // 2. Upload thumbnail cho course
    public String uploadCourseThumbnail(String courseId, MultipartFile file) throws IOException {
        String folder = String.format("%s/courses/%s/thumbnail", ROOT_FOLDER, courseId);
        return doUpload(file, folder, false);
    }

    // 3. Upload promo video cho course
    public String uploadCourseVideo(String courseId, MultipartFile file) throws IOException {
        String folder = String.format("%s/courses/%s/video", ROOT_FOLDER, courseId);
        return doUpload(file, folder, true);
    }

    // 4. Xóa file
    public void deleteFile(String fileUrl) throws IOException {
        // parse publicId từ URL
        // ví dụ URL: https://res.cloudinary.com/…/short_course/courses/abc/thumbnail/mythumb.jpg
        String[] parts = fileUrl.split("/");
        // tìm phần sau ROOT_FOLDER
        int idx = Arrays.asList(parts).indexOf(ROOT_FOLDER);
        if (idx < 0 || idx + 2 >= parts.length) {
            throw new IllegalArgumentException("Invalid URL");
        }
        // publicId = ROOT_FOLDER/.../filename(without ext)
        String[] publicIdParts = Arrays.copyOfRange(parts, idx, parts.length);
        String last = publicIdParts[publicIdParts.length - 1];
        publicIdParts[publicIdParts.length - 1] = last.replaceFirst("\\.[^.]+$", "");
        String publicId = String.join("/", publicIdParts);

        // determine resource_type
        boolean isVideo = fileUrl.contains("/video/");
        Map opts = isVideo
                ? ObjectUtils.asMap("resource_type", "video")
                : ObjectUtils.emptyMap();

        cloudinary.uploader().destroy(publicId, opts);
    }
}
