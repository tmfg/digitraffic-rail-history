package fi.livi.trainhistorybackend.controller;

import fi.livi.trainhistorybackend.entities.Composition;
import fi.livi.trainhistorybackend.repositories.CompositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

        // Return content template for HTMX fragment requests, full page otherwise
        return fragment ? "compositions-content" : "compositions";
    }

    @GetMapping(value = "/api/v1/compositions/history/{departure_date}/{train_number}",
                produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_HTML_VALUE})
    public Object getCompositionHistory(@PathVariable final long train_number,
                                       @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departure_date,
                                       @RequestHeader(value = "Accept", defaultValue = MediaType.APPLICATION_JSON_VALUE) String accept,
                                       Model model,
                                       HttpServletResponse response) {
        response.setHeader("Cache-Control", String.format("max-age=%d, public", 10));

        final List<Composition> compositions = compositionRepository.findByTrainNumberAndDepartureDate(train_number, departure_date);

        if (accept.equals(MediaType.APPLICATION_JSON_VALUE)) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(compositions);
        }

        model.addAttribute("compositions", compositions);
        model.addAttribute("trainNumber", train_number);
        model.addAttribute("departureDate", departure_date);

        // Auto-select first version if results exist
        if (!compositions.isEmpty()) {
            model.addAttribute("selectedComposition", compositions.getFirst());
        }

        return "composition-results";
    }

    // HTMX version selection endpoint
    @GetMapping(value = "/compositions/version", produces = MediaType.TEXT_HTML_VALUE)
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
