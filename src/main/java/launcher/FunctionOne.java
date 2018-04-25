package launcher;

import org.springframework.data.gemfire.function.annotation.FunctionId;
import org.springframework.data.gemfire.function.annotation.OnRegion;

@OnRegion(id = "FunctionOne", region="region2")
public interface FunctionOne {

    @FunctionId("function1")
    void addEntry();

}
