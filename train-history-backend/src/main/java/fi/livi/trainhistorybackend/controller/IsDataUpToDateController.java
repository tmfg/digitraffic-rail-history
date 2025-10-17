package fi.livi.trainhistorybackend.controller;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fi.livi.trainhistorybackend.repositories.TrainRepository;
import jakarta.servlet.http.HttpServletResponse;


@Controller
public class IsDataUpToDateController {
    @Autowired
    private TrainRepository trainRepository;

    @RequestMapping("/api/v1/trains/history/is-up-to-date")
    @ResponseBody
    public ZonedDateTime getTrain(final HttpServletResponse response) {
        final ZonedDateTime latestFetchDate = trainRepository.findLatestFetchDate();
        if (latestFetchDate.isBefore(ZonedDateTime.now().minusMinutes(15))) {
            response.setStatus(500);
        } else {
            response.setStatus(200);
        }
        return latestFetchDate;
    }
}
