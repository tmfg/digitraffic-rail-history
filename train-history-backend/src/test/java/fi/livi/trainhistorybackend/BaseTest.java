package fi.livi.trainhistorybackend;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@AutoConfigureMockMvc
@SpringBootTest(classes = TrainHistoryBackendApplication.class)
public abstract class BaseTest {

}