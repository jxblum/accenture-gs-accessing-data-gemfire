package launcher;

import org.springframework.data.gemfire.function.annotation.FunctionId;
import org.springframework.data.gemfire.function.annotation.OnRegion;

@OnRegion(region = "region1")
public interface FunctionServer2 {

    @FunctionId("function2")
    void deleteRegion();

}
