package fi.livi.trainhistorybackend;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

@AutoConfigureMockMvc
@SpringBootTest(classes = TrainHistoryBackendApplication.class)
public abstract class BaseTest {

}