package fi.livi.trainhistorybackend.controller;

import fi.livi.trainhistorybackend.entities.Train;
import fi.livi.trainhistorybackend.repositories.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;


@Controller
public class TrainController {
    @Autowired
    private TrainRepository trainRepository;

    @RequestMapping("trains/history/{departure_date}/{train_number}")
    @ResponseBody
    public List<Train> getTrain(@PathVariable final long train_number,
                                @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departure_date,
                                HttpServletResponse response) {

        response.setHeader("Cache-Control", String.format("max-age=%d, public", 10));

        return trainRepository.findByTrainNumberAndDepartureDate(train_number, departure_date);
    }
}
