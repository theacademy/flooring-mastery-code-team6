import controller.FlooringMasterController;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;


public class App {

    public static void main(String[] args) throws IOException {

        ApplicationContext appContext
                = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        FlooringMasterController controller = appContext.getBean("controller", FlooringMasterController.class);
        controller.run();
    }
}