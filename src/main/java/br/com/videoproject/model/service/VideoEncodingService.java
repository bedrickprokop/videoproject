package br.com.videoproject.model.service;

import br.com.videoproject.model.entity.Video;

public interface VideoEncodingService {

    Video processVideo(Video video);
}
