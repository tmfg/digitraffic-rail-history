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

    public TrainController(final TrainService trainService) {
        this.trainService = trainService;
    }

    // Main trains page (HTML) - supports both full page and fragment
    @GetMapping(value = "/history/trains", produces = MediaType.TEXT_HTML_VALUE)
    public String trainsPage(@RequestParam(required = false, defaultValue = "false") boolean fragment,
                            final Model model) {
        model.addAttribute("active_section", "trains");
        model.addAttribute("currentDate", LocalDate.now().toString());

        return fragment ? "modules/train/trains-content" : "pages/trains";
    }

    @GetMapping(value = "/api/v1/trains/history/{departure_date}/{train_number}",
                produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_HTML_VALUE})
    public Object getTrainHistory(@PathVariable final long train_number,
                                  @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate departure_date,
                                  @RequestHeader(value = "Accept", defaultValue = MediaType.APPLICATION_JSON_VALUE) final String accept,
                                  final Model model,
                                  final HttpServletResponse response) {
        response.setHeader("Cache-Control", String.format("max-age=%d, public", 10));

        final List<TrainVersion> trainVersions = trainService.findByNumberAndDate(train_number, departure_date);

        if (accept.contains(MediaType.APPLICATION_JSON_VALUE) ||
            accept.contains(MediaType.ALL_VALUE) ||
            accept.contains("application/*")) {
            List<Train> entities = trainVersions.stream()
                .map(TrainVersion::getEntity)
                .collect(Collectors.toList());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(entities);
        }

        if (accept.contains(MediaType.TEXT_HTML_VALUE) ||
            accept.contains("text/*")) {
            model.addAttribute("versions", trainVersions)
                    .addAttribute("trainNumber", train_number)
                    .addAttribute("departureDate", departure_date)
                    .addAttribute("versionUrl", "/history/trains/version")
                    .addAttribute("dataTable", "modules/train/table");

            if (!trainVersions.isEmpty()) {
                model.addAttribute("selectedVersion", trainVersions.getFirst());
            }

            return "modules/results";
        }

        return ResponseEntity.status(406).build();
    }

    @GetMapping(value = "/history/trains/version", produces = MediaType.TEXT_HTML_VALUE)
    public String selectTrainVersion(@RequestParam final Long version,
                                    @RequestParam long trainNumber,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate departureDate,
                                    final Model model) {
        trainService.findByVersion(trainNumber, departureDate, version)
            .ifPresent(trainVersion -> model
                    .addAttribute("selectedVersion", trainVersion)
                    .addAttribute("versionUrl", "/history/trains/version")
                    .addAttribute("dataTable", "modules/train/table"));

        return "modules/version-info";
    }
}

