package com.swp391_se1866_group2.hiv_and_medical_system.blogpost.service;


import com.swp391_se1866_group2.hiv_and_medical_system.blogpost.dto.request.BlogPostCreationRequest;
import com.swp391_se1866_group2.hiv_and_medical_system.blogpost.dto.request.BlogPostUpdateRequest;
import com.swp391_se1866_group2.hiv_and_medical_system.blogpost.dto.response.BlogPostResponse;
import com.swp391_se1866_group2.hiv_and_medical_system.blogpost.entity.BlogPost;
import com.swp391_se1866_group2.hiv_and_medical_system.blogpost.repository.BlogPostRepository;
import com.swp391_se1866_group2.hiv_and_medical_system.common.exception.AppException;
import com.swp391_se1866_group2.hiv_and_medical_system.common.exception.ErrorCode;
import com.swp391_se1866_group2.hiv_and_medical_system.common.mapper.BlogPostMapper;
import com.swp391_se1866_group2.hiv_and_medical_system.doctor.dto.response.DoctorResponse;
import com.swp391_se1866_group2.hiv_and_medical_system.doctor.entity.Doctor;
import com.swp391_se1866_group2.hiv_and_medical_system.doctor.repository.DoctorRepository;
import com.swp391_se1866_group2.hiv_and_medical_system.doctor.service.DoctorService;
import com.swp391_se1866_group2.hiv_and_medical_system.image.entity.Image;
import com.swp391_se1866_group2.hiv_and_medical_system.image.service.ImageService;
import com.swp391_se1866_group2.hiv_and_medical_system.user.entity.User;
import com.swp391_se1866_group2.hiv_and_medical_system.user.repository.UserRepository;
import com.swp391_se1866_group2.hiv_and_medical_system.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class BlogPostService {
    BlogPostMapper blogPostMapper;
    BlogPostRepository blogPostRepository;
    ImageService imageService;
    DoctorService doctorService;
    UserRepository userRepository;
    DoctorRepository doctorRepository;

    public BlogPostResponse createBlog(BlogPostCreationRequest request, MultipartFile image) {
        if(blogPostRepository.existsByTitle(request.getTitle())){
            throw new AppException(ErrorCode.BLOG_POST_EXISTED);
        }
        BlogPost blogPost = blogPostMapper.toBlogPost(request);
        Doctor doctor = doctorRepository.findById(request.getDoctorId()).get();
        blogPost.setDoctor(doctor);

        if(image != null){
            blogPost = imageService.saveBlogPostImage(image ,blogPost);
        }
        return blogPostMapper.toBlogPostResponse(blogPostRepository.save(blogPost));
    }

    public List<BlogPostResponse> getAllBlogs(Pageable pageable, String title){
        Slice<BlogPostResponse> slicedBlog;
        if (title == null){
            slicedBlog = blogPostRepository.getAllBlogPosts(pageable).get();
        }
        else {
            slicedBlog = blogPostRepository.searchByTitle(title, pageable).get();
        }

        return slicedBlog.getContent();
    }

    public List<BlogPostResponse> getAllBlogsByDoctorId(Pageable pageable, String doctorId){
        Slice<BlogPostResponse> slicedBlog = blogPostRepository.getBlogsByDoctorId(doctorId, pageable).get();

        return slicedBlog.getContent();

    }


        public BlogPostResponse getBlogById(int blogId) {
        return  blogPostRepository.searchById(blogId)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_POST_NOT_EXISTED));
    }

    public BlogPostResponse updateBlog(int blogId, BlogPostUpdateRequest request, MultipartFile image) {
        BlogPost blogPost = blogPostRepository.findById(blogId)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_POST_NOT_EXISTED));

        blogPostMapper.updateBlogPost(blogPost, request);
        if(image != null && !image.isEmpty()){
            blogPost = imageService.saveBlogPostImage(image,blogPost);
        }
        return blogPostMapper.toBlogPostResponse(blogPostRepository.save(blogPost));
    }


}
