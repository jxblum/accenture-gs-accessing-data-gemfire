package launcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Executor {

    @Autowired(required = false)
	FunctionOne functionOne;

    @Autowired(required = false)
    FunctionTwo functionTwo;

    @Bean
    String getExample() {

        System.out.println("Function Server " + this.functionOne);

        return "test";
    }

    public void addEntry() {
        this.functionOne.addEntry();
    }
}
