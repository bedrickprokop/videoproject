package br.com.videoproject.model.service;

import br.com.videoproject.App;
import br.com.videoproject.config.H2Config;
import br.com.videoproject.config.S3Config;
import br.com.videoproject.model.entity.Video;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations="classpath:application.properties")
@ContextConfiguration(classes = {App.class, H2Config.class, S3Config.class})
@WebAppConfiguration
public class VideoServiceTest {

    @Autowired
    private VideoService videoService;

    private Video video1;
    private Video video2;

    @Before
    public void setup() {
        byte[] bytes = new byte[12];
        for (int i = 0; i < 12; i++) {
            bytes[i] = (byte) (4 * (i + 1) / (i + 1));
        }
        video1 = new Video("Video test 1", "Video description test 1", bytes);
        video2 = new Video("Video test 2", "Video description test 2", null);
    }

    @Test
    public void createSuccess() {
        video1 = videoService.add(video1);
        Assert.assertNotNull(video1.getId());
    }

    @Test(expected = RuntimeException.class)
    public void createFailWithNullName() {
        video1.setName(null);
        videoService.add(video1);
    }

    @Test(expected = RuntimeException.class)
    public void createFailWithNullBytes() {
        videoService.add(video2);
    }

    @Test
    public void list() {
        videoService.add(video1);
        List<Video> videoList = videoService.list();

        Assert.assertNotNull(videoList);
    }

    @Test
    public void findByName() {
        videoService.add(video1);
        List<Video> videoList = videoService.findByName("Video test 1");
        Video video11 = videoList.get(0);

        Assert.assertEquals(video11.getName(), video1.getName());
    }

    @Test
    public void findByEmptyName() {
        videoService.add(video1);
        List<Video> videoList = videoService.findByName("");

        Assert.assertNotNull(videoList);
    }
}
