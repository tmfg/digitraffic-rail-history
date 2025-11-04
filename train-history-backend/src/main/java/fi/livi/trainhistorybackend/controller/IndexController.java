package fi.livi.trainhistorybackend.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping(value = "/history", produces = MediaType.TEXT_HTML_VALUE)
    public String index() {
        return "index";
    }
}
