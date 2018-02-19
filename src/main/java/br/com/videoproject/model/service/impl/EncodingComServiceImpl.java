package br.com.videoproject.model.service.impl;

import br.com.videoproject.model.entity.Video;
import br.com.videoproject.model.service.VideoEncodingService;
import org.springframework.stereotype.Service;

@Service
public class EncodingComServiceImpl implements VideoEncodingService {

    @Override
    public Video processVideo(Video video) {

        //send video to encoding.com via post

        while (!status.equalsXXX) {

            //get status video
            //set new video url
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return video;
    }
}
