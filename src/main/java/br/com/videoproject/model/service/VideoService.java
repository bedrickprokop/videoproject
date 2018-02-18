package br.com.videoproject.model.service;

import br.com.videoproject.model.entity.Video;

import java.util.List;

public interface VideoService {

    Video add(Video video);

    List<Video> list();

    List<Video> findByName(String name);

}
