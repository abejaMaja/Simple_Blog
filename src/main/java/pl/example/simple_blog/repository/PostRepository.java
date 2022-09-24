package pl.example.simple_blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.example.simple_blog.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
