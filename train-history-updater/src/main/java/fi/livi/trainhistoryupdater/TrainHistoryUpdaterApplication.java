package fi.livi.trainhistoryupdater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TrainHistoryUpdaterApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainHistoryUpdaterApplication.class, args);
	}
}
