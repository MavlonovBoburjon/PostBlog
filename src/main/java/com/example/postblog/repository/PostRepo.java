package com.example.postblog.repository;

import com.example.postblog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepo extends JpaRepository<Post, Long> {
    Page<Post> findByTitleContainingIgnoreCase(String username, Pageable pageable);

    @Query("""
    SELECT p FROM Post p
    WHERE p.status = 'PUBLISHED' OR (p.status = 'DRAFT' AND p.user.id = :userId)
""")
    Page<Post> findByStatusOrOwner(Pageable pageable, @Param("userId") Long userId);

    @Query("""
    SELECT p FROM Post p
    WHERE (p.status = 'PUBLISHED' OR (p.status = 'DRAFT' AND p.user.id = :userId))
      AND LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
    Page<Post> findByStatusOrOwnerAndTitleContainingIgnoreCase(@Param("userId") Long userId,
                                                               @Param("keyword") String keyword,
                                                               Pageable pageable);
}
