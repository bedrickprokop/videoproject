package br.com.videoproject.model.service;

import br.com.videoproject.App;
import br.com.videoproject.config.H2Config;
import br.com.videoproject.model.entity.Video;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {App.class, H2Config.class})
@WebAppConfiguration
public class VideoServiceTest {

    @Autowired
    private VideoService videoService;

    private Video video1;
    private Video video2;

    @Before
    public void setup() {
        video1 = new Video("Video test 1", "Video description test 1", "path/to/video");
        video2 = new Video("Video test 2", "Video description test 2", "path/to/video");
    }

    @Test
    public void createSuccess(){
        video1 = videoService.add(video1);
        Assert.assertNotNull(video1.getId());
    }

    @Test(expected = RuntimeException.class)
    public void createFail(){
        video1.setName(null);
        video1.setDescription(null);
        video1.setPath(null);
        videoService.add(video1);
    }

    @Test
    public void list(){
        videoService.add(video1);
        videoService.add(video2);

        List<Video> videoList = videoService.list();

        Assert.assertNotNull(videoList);
    }

    @Test
    public void findByName(){
        videoService.add(video1);
        List<Video> videoList = videoService.findByName("Video test 1");
        Video video11 = videoList.get(0);

        Assert.assertEquals(video11.getName(), video1.getName());
    }

    @Test
    public void findByEmptyName(){
        videoService.add(video1);
        List<Video> videoList = videoService.findByName("");

        Assert.assertNotNull(videoList);
    }
}
