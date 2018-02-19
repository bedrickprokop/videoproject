package br.com.videoproject.model.service;

import br.com.videoproject.App;
import br.com.videoproject.config.H2Config;
import br.com.videoproject.config.S3Config;
import br.com.videoproject.model.entity.Video;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations = "classpath:application.properties")
@ContextConfiguration(classes = {App.class, H2Config.class, S3Config.class})
@WebAppConfiguration
public class VideoServiceTest {

    @Autowired
    private VideoService videoService;

    private Video video1;
    private Video video2;
    private Video video3;

    @Before
    public void setup() {
        byte[] bytes = new byte[12];
        for (int i = 0; i < 12; i++) {
            bytes[i] = (byte) (4 * (i + 1) / (i + 1));
        }
        video1 = new Video("video_test1.dv", "Video description test 1", ".dv", bytes);
        video2 = new Video("video_test2.dv", "Video description test 2", ".dv", null);
        video3 = new Video("video_test3", "Video description test 3", "", bytes);
    }

    @Test @Ignore
    public void createSuccess() {
        video1 = videoService.add(video1);
        Assert.assertNotNull(video1.getId());
    }

    @Test(expected = RuntimeException.class) @Ignore
    public void createFailWithNullName() {
        video1.setName(null);
        videoService.add(video1);
    }

    @Test(expected = RuntimeException.class) @Ignore
    public void createFailWithInvalidType() {
        videoService.add(video3);
    }

    @Test(expected = RuntimeException.class) @Ignore
    public void createFailWithNullBytes() {
        videoService.add(video2);
    }

    @Test @Ignore
    public void list() {
        videoService.add(video1);
        List<Video> videoList = videoService.list();

        Assert.assertNotNull(videoList);
    }

    @Test @Ignore
    public void findByName() {
        videoService.add(video1);
        List<Video> videoList = videoService.findByName("video_test1.dv");
        Video video11 = videoList.get(0);

        Assert.assertEquals(video11.getName(), video1.getName());
    }

    @Test @Ignore
    public void findByEmptyName() {
        videoService.add(video1);
        List<Video> videoList = videoService.findByName("");

        Assert.assertNotNull(videoList);
    }
}
