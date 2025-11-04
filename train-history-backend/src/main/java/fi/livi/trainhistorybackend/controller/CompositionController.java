package fi.livi.trainhistorybackend.controller;

import fi.livi.trainhistorybackend.domain.CompositionVersion;
import fi.livi.trainhistorybackend.entities.Composition;
import fi.livi.trainhistorybackend.service.CompositionService;
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
public class CompositionController {
    private final CompositionService compositionService;

    public CompositionController(final CompositionService compositionService) {
        this.compositionService = compositionService;
    }

    // Main compositions page (HTML) - supports both full page and fragment
    @GetMapping(value = "/history/compositions", produces = MediaType.TEXT_HTML_VALUE)
    public String compositionsPage(@RequestParam(required = false, defaultValue = "false") boolean fragment,
                                  final Model model) {
        model.addAttribute("active_section", "compositions");
        model.addAttribute("currentDate", LocalDate.now().toString());

        return fragment ? "modules/composition/compositions-content" : "pages/compositions";
    }

    @GetMapping(value = "/api/v1/compositions/history/{departure_date}/{train_number}",
                produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_HTML_VALUE})
    public Object getCompositionHistory(@PathVariable final long train_number,
                                       @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate departure_date,
                                       @RequestHeader(value = "Accept", defaultValue = MediaType.APPLICATION_JSON_VALUE) final String accept,
                                       final Model model,
                                       final HttpServletResponse response) {
        response.setHeader("Cache-Control", String.format("max-age=%d, public", 10));

        final List<CompositionVersion> compositionVersions = compositionService.findByNumberAndDate(train_number, departure_date);

        if (accept.contains(MediaType.APPLICATION_JSON_VALUE) ||
            accept.contains(MediaType.ALL_VALUE) ||
            accept.contains("application/*")) {
            List<Composition> entities = compositionVersions.stream()
                .map(CompositionVersion::getEntity)
                .collect(Collectors.toList());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(entities);
        }

        if (accept.contains(MediaType.TEXT_HTML_VALUE) ||
            accept.contains("text/*")) {
            model.addAttribute("versions", compositionVersions)
                    .addAttribute("trainNumber", train_number)
                    .addAttribute("departureDate", departure_date)
                    .addAttribute("versionUrl", "/history/compositions/version")
                    .addAttribute("dataTable", "modules/composition/table");

            if (!compositionVersions.isEmpty()) {
                model.addAttribute("selectedVersion", compositionVersions.getFirst());
            }

            return "modules/results";
        }

        return ResponseEntity.status(406).build();
    }

    @GetMapping(value = "/history/compositions/version", produces = MediaType.TEXT_HTML_VALUE)
    public String selectCompositionVersion(@RequestParam final Long version,
                                          @RequestParam long trainNumber,
                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate departureDate,
                                          final Model model) {
        compositionService.findByVersion(trainNumber, departureDate, version)
            .ifPresent(compositionVersion -> {
                model.addAttribute("selectedVersion", compositionVersion)
                        .addAttribute("versionUrl", "/history/compositions/version")
                        .addAttribute("dataTable", "modules/composition/table");
            });

        return "modules/version-info";
    }
}

