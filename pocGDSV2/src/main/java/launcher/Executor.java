package launcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Executor {

    @Autowired
    FunctionServer1 functionServer1;

    @Autowired
    FunctionServer2 functionServer2;

    @Bean
    String getExample()
    {
        System.out.println("function server" + functionServer1);

        return "test";
    }


    public void addEntry()
    {
        functionServer1.addEntry();
    }


}
