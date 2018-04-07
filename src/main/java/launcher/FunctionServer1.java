package launcher;

import org.springframework.data.gemfire.function.annotation.FunctionId;
import org.springframework.data.gemfire.function.annotation.OnRegion;

@OnRegion(region="region2")
public interface FunctionServer1 {

    @FunctionId("function1")
    void addEntry();

}
