package launcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

//@Component
//@DependsOn("tpa-pool")
public class Executor {

    @Autowired(required = false)
    FunctionServer1 functionServer1;

    @Autowired(required = false)
    FunctionServer2 functionServer2;

    @Bean
    String getExample() {

        System.out.println("function server" + functionServer1);

        return "test";
    }

    public void addEntry() {
        functionServer1.addEntry();
    }
}
