package fi.livi.trainhistorybackend.controller;

import fi.livi.trainhistorybackend.domain.TrainVersion;
import fi.livi.trainhistorybackend.entities.Train;
import fi.livi.trainhistorybackend.service.TrainService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class TrainController {
    private final TrainService trainService;

    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

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

        final List<TrainVersion> trainVersions = trainService.findByNumberAndDate(train_number, departure_date);

        if (accept.equals(MediaType.APPLICATION_JSON_VALUE)) {
            List<Train> entities = trainVersions.stream()
                .map(TrainVersion::getEntity)
                .collect(Collectors.toList());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(entities);
        }

        model.addAttribute("versions", trainVersions);
        model.addAttribute("trainNumber", train_number);
        model.addAttribute("departureDate", departure_date);

        if (!trainVersions.isEmpty()) {
            model.addAttribute("selectedVersion", trainVersions.getFirst());
        }

        return "modules/train/results";
    }

    @GetMapping(value = "/trains/version", produces = MediaType.TEXT_HTML_VALUE)
    public String selectTrainVersion(@RequestParam Long version,
                                    @RequestParam long trainNumber,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
                                    Model model) {
        trainService.findByVersion(trainNumber, departureDate, version)
            .ifPresent(trainVersion -> model.addAttribute("selectedVersion", trainVersion));

        return "modules/table";
    }
}

