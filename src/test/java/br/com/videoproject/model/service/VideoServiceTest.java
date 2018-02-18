package br.com.videoproject.model.service;

import br.com.videoproject.App;
import br.com.videoproject.config.H2Config;
import br.com.videoproject.model.entity.Video;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {App.class, H2Config.class})
@WebAppConfiguration
public class VideoServiceTest {

    @Autowired
    private VideoService videoService;

    private Video video;

    @Before
    public void setup() {
        video = new Video("Video test", "Video description test", "path/to/video");
    }

    @Test
    public void createSuccess(){
        video = videoService.add(video);
        Assert.assertNotNull(video.getId());
    }
}
