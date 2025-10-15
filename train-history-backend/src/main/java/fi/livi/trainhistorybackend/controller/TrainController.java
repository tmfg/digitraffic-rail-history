package fi.livi.trainhistorybackend.controller;

import fi.livi.trainhistorybackend.entities.Train;
import fi.livi.trainhistorybackend.repositories.TrainRepository;
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
public class TrainController {
    @Autowired
    private TrainRepository trainRepository;

    // Main trains page (HTML) - supports both full page and fragment
    @GetMapping(value = "/trains", produces = MediaType.TEXT_HTML_VALUE)
    public String trainsPage(@RequestParam(required = false, defaultValue = "false") boolean fragment,
                            Model model) {
        model.addAttribute("active_section", "trains");
        model.addAttribute("currentDate", LocalDate.now().toString());

        // Return fragment for HTMX requests, full page otherwise
        return fragment ? "trains-fragment" : "trains";
    }

    // JSON API endpoint at /api/v1/ for backward compatibility
    @RequestMapping(value = "/api/v1/trains/history/{departure_date}/{train_number}",
                   produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Train> getTrainJsonApiV1(@PathVariable final long train_number,
                                         @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departure_date,
                                         HttpServletResponse response) {
        response.setHeader("Cache-Control", String.format("max-age=%d, public", 10));
        return trainRepository.findByTrainNumberAndDepartureDate(train_number, departure_date);
    }

    // Original JSON API endpoint (preserved for backward compatibility)
    @RequestMapping(value = "trains/history/{departure_date}/{train_number}",
                   produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Train> getTrainJson(@PathVariable final long train_number,
                                   @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departure_date,
                                   HttpServletResponse response) {
        response.setHeader("Cache-Control", String.format("max-age=%d, public", 10));
        return trainRepository.findByTrainNumberAndDepartureDate(train_number, departure_date);
    }

    // HTMX search endpoint
    @PostMapping(value = "/trains/search", produces = MediaType.TEXT_HTML_VALUE)
    public String searchTrains(@RequestParam long trainNumber,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
                              Model model,
                              HttpServletResponse response) {
        response.setHeader("Cache-Control", String.format("max-age=%d, public", 10));

        List<Train> trains = trainRepository.findByTrainNumberAndDepartureDate(trainNumber, departureDate);
        model.addAttribute("trains", trains);
        model.addAttribute("trainNumber", trainNumber);
        model.addAttribute("departureDate", departureDate);

        // Auto-select first version if only one exists
        if (trains.size() == 1) {
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

