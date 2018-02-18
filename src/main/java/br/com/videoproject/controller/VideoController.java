package br.com.videoproject.controller;

import br.com.videoproject.model.entity.Video;
import br.com.videoproject.model.repository.VideoRepository;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/video")
public class VideoController {

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "/home/break/Development/java";

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
    public String listByName(@PathVariable("name") String name, Model model) {
        List<Video> videoList = videoRepository.findByName(name);
        if(videoList != null) {
            model.addAttribute("videoList", videoList);
        }
        return "video-view";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(Video video, @RequestParam("video") MultipartFile file,
                      RedirectAttributes redirectAttributes) {
        if(file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please, select a video to upload");
            return "redirect:/video";
        }

        try {
            //TODO change to amazon s3 - do this in service class
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        videoRepository.save(video);
        return "redirect:/video";
    }

    public String upload() {
        return "redirect:/video";
    }
}
