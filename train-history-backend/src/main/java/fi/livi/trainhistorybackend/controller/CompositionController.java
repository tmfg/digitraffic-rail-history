package fi.livi.trainhistorybackend.controller;

import fi.livi.trainhistorybackend.entities.Composition;
import fi.livi.trainhistorybackend.repositories.CompositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class CompositionController {
    @Autowired
    private CompositionRepository compositionRepository;

    // Main compositions page (HTML) - supports both full page and fragment
    @GetMapping(value = "/compositions", produces = MediaType.TEXT_HTML_VALUE)
    public String compositionsPage(@RequestParam(required = false, defaultValue = "false") boolean fragment,
                                  Model model) {
        model.addAttribute("active_section", "compositions");
        model.addAttribute("currentDate", LocalDate.now().toString());

        // Return fragment for HTMX requests, full page otherwise
        return fragment ? "compositions-fragment" : "compositions";
    }

    // JSON API endpoint at /api/v1/ for backward compatibility
    @RequestMapping(value = "/api/v1/compositions/history/{departure_date}/{train_number}",
                   produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Composition> getCompositionJsonApiV1(@PathVariable final long train_number,
                                                     @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departure_date,
                                                     HttpServletResponse response) {
        response.setHeader("Cache-Control", String.format("max-age=%d, public", 10));
        return compositionRepository.findByTrainNumberAndDepartureDate(train_number, departure_date);
    }

    // Original JSON API endpoint (preserved for backward compatibility)
    @RequestMapping(value = "compositions/history/{departure_date}/{train_number}",
                   produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Composition> getCompositionJson(@PathVariable final long train_number,
                                               @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departure_date,
                                               HttpServletResponse response) {
        response.setHeader("Cache-Control", String.format("max-age=%d, public", 10));
        return compositionRepository.findByTrainNumberAndDepartureDate(train_number, departure_date);
    }

    // HTMX search endpoint
    @PostMapping(value = "/compositions/search", produces = MediaType.TEXT_HTML_VALUE)
    public String searchCompositions(@RequestParam long trainNumber,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
                                    Model model,
                                    HttpServletResponse response) {
        response.setHeader("Cache-Control", String.format("max-age=%d, public", 10));

        List<Composition> compositions = compositionRepository.findByTrainNumberAndDepartureDate(trainNumber, departureDate);
        model.addAttribute("compositions", compositions);
        model.addAttribute("trainNumber", trainNumber);
        model.addAttribute("departureDate", departureDate);

        // Auto-select first version if only one exists
        if (compositions.size() == 1) {
            model.addAttribute("selectedComposition", compositions.get(0));
        }

        return "composition-results";
    }

    // HTMX version selection endpoint
    @PostMapping(value = "/compositions/version", produces = MediaType.TEXT_HTML_VALUE)
    public String selectCompositionVersion(@RequestParam String selectedVersion,
                                          @RequestParam long trainNumber,
                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
                                          Model model) {
        if (selectedVersion != null && !selectedVersion.isEmpty()) {
            String[] parts = selectedVersion.split("_");
            if (parts.length == 2) {
                ZonedDateTime fetchDate = ZonedDateTime.parse(parts[0]);
                String version = parts[1];

                List<Composition> compositions = compositionRepository.findByTrainNumberAndDepartureDate(trainNumber, departureDate);
                Optional<Composition> selectedComposition = compositions.stream()
                    .filter(c -> c.id.fetchDate.equals(fetchDate) && c.version.toString().equals(version))
                    .findFirst();

                selectedComposition.ifPresent(composition -> model.addAttribute("selectedComposition", composition));
            }
        }

        return "composition-details";
    }
}

