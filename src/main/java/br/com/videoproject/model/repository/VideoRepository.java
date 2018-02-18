package br.com.videoproject.model.repository;

import br.com.videoproject.model.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {

    List<Video> findByName(String name);

    List<Video> findByDescription(String description);

}
