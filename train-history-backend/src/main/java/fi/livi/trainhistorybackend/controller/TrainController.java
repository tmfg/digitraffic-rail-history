package fi.livi.trainhistorybackend.controller;

import fi.livi.trainhistorybackend.entities.Train;
import fi.livi.trainhistorybackend.repositories.TrainRepository;
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
public class TrainController {
    @Autowired
    private TrainRepository trainRepository;

    // Main trains page (HTML) - supports both full page and fragment
    @GetMapping(value = "/trains", produces = MediaType.TEXT_HTML_VALUE)
    public String trainsPage(@RequestParam(required = false, defaultValue = "false") boolean fragment,
                            Model model) {
        model.addAttribute("active_section", "trains");
        model.addAttribute("currentDate", LocalDate.now().toString());

        return fragment ? "trains-content" : "trains";
    }

    @GetMapping(value = "/api/v1/trains/history/{departure_date}/{train_number}",
                produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_HTML_VALUE})
    public Object getTrainHistory(@PathVariable final long train_number,
                                  @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departure_date,
                                  @RequestHeader(value = "Accept", defaultValue = MediaType.APPLICATION_JSON_VALUE) String accept,
                                  Model model,
                                  HttpServletResponse response) {
        response.setHeader("Cache-Control", String.format("max-age=%d, public", 10));

        final List<Train> trains = trainRepository.findByTrainNumberAndDepartureDate(train_number, departure_date);

        if (accept.equals(MediaType.APPLICATION_JSON_VALUE)) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(trains);
        }

        model.addAttribute("trains", trains);
        model.addAttribute("trainNumber", train_number);
        model.addAttribute("departureDate", departure_date);

        // Auto-select first version if results exist
        if (!trains.isEmpty()) {
            model.addAttribute("selectedTrain", trains.get(0));
        }

        return "train-results";
    }

    // HTMX version selection endpoint
    @PostMapping(value = "/trains/version", produces = MediaType.TEXT_HTML_VALUE)
    public String selectTrainVersion(@RequestParam String selectedVersion,
                                    @RequestParam long trainNumber,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
                                    Model model) {
        if (selectedVersion != null && !selectedVersion.isEmpty()) {
            String[] parts = selectedVersion.split("_");
            if (parts.length == 2) {
                ZonedDateTime fetchDate = ZonedDateTime.parse(parts[0]);
                String version = parts[1];

                List<Train> trains = trainRepository.findByTrainNumberAndDepartureDate(trainNumber, departureDate);
                Optional<Train> selectedTrain = trains.stream()
                    .filter(t -> t.id.fetchDate.equals(fetchDate) && t.version.toString().equals(version))
                    .findFirst();

                selectedTrain.ifPresent(train -> model.addAttribute("selectedTrain", train));
            }
        }

        return "train-details";
    }
}

