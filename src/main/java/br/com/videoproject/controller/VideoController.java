package br.com.videoproject.controller;

import br.com.videoproject.model.entity.Video;
import br.com.videoproject.model.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoRepository videoRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        List<Video> videoList = videoRepository.findAll();
        if(videoList != null) {
            model.addAttribute("videoList", videoList);
        }
        return "video-view";
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public String list(@PathVariable("name") String name, Model model) {
        List<Video> videoList = videoRepository.findByName(name);
        if(videoList != null) {
            model.addAttribute("videoList", videoList);
        }
        return "video-view";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(Video video) {
        videoRepository.save(video);
        return "redirect:/";
    }
}
