package uk.co.zoneofavoidance.my.tasks;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MyTasksApplication.class)
@WebAppConfiguration
public class MyTasksApplicationTests {

	@Test
	public void contextLoads() {
	}

}
