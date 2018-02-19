package br.com.videoproject.controller;

import br.com.videoproject.model.entity.Video;
import br.com.videoproject.model.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        List<Video> videoList = videoService.list();
        model.addAttribute("videoList", videoList);
        return "video-view";
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public String findByName(@PathVariable("name") String name, Model model) {
        List<Video> videoList = videoService.findByName(name);
        model.addAttribute("videoList", videoList);
        return "video-view";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(Video video, @RequestParam("video") MultipartFile file,
                      RedirectAttributes redirectAttributes) {

        try {
            video.setBytes(file.getBytes());
            videoService.add(video);

            //redirectAttributes.addFlashAttribute("message",
            //        "You successfully uploaded '" + file.getOriginalFilename() + "'");
            return "redirect:/video";
        } catch (IOException e) {

            //TODO create exception treatment
            return "redirect:/video";
        }
    }
}
