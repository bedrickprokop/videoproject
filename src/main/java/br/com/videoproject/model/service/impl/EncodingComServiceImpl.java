package br.com.videoproject.model.service.impl;

import br.com.videoproject.model.entity.Video;
import br.com.videoproject.model.service.VideoEncodingService;
import br.com.videoproject.model.util.EncodingComUtil;
import org.springframework.stereotype.Service;

@Service
public class EncodingComServiceImpl implements VideoEncodingService {

    @Override
    public Video processVideo(Video video) {

        String mediaId = EncodingComUtil.addMedia(video.getInputUrl());

        String destination = "";
        while (destination.isEmpty()) {
            destination = EncodingComUtil.getStatus(mediaId);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        video.setOutputUrl(destination);
        return video;
    }
}
