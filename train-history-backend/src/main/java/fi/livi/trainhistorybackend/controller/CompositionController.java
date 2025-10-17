package fi.livi.trainhistorybackend.controller;

import fi.livi.trainhistorybackend.entities.Composition;
import fi.livi.trainhistorybackend.repositories.CompositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

@Controller
public class CompositionController {
    @Autowired
    private CompositionRepository compositionRepository;

    @RequestMapping("compositions/history/{departure_date}/{train_number}")
    @ResponseBody
    public List<Composition> getTrain(@PathVariable final long train_number,
                                      @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                      final LocalDate departure_date,
                                      final HttpServletResponse response) {

        response.setHeader("Cache-Control", String.format("max-age=%d, public", 10));

        return compositionRepository.findByTrainNumberAndDepartureDate(train_number, departure_date);
    }
}
