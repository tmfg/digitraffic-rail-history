package fi.livi.trainhistorybackend.controller;

import java.time.ZonedDateTime;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fi.livi.trainhistorybackend.repositories.TrainRepository;


@Controller
public class IsDataUpToDateController {
    @Autowired
    private TrainRepository trainRepository;

    @RequestMapping("trains/history/is-up-to-date")
    @ResponseBody
    public ZonedDateTime getTrain(HttpServletResponse response) {
        ZonedDateTime latestFetchDate = trainRepository.findLatestFetchDate();
        if (latestFetchDate.isBefore(ZonedDateTime.now().minusMinutes(15))) {
            response.setStatus(500);
        } else {
            response.setStatus(200);
        }
        return latestFetchDate;
    }
}
