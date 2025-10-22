package fi.livi.trainhistorybackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @Value("${fintraffic-url}")
    private String fintrafficUrl;

    @Value("${digitraffic-url}")
    private String digitrafficUrl;

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        model.addAttribute("fintrafficUrl", fintrafficUrl);
        model.addAttribute("digitrafficUrl", digitrafficUrl);
    }
}
