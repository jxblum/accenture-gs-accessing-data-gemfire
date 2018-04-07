package launcher;

import org.springframework.data.gemfire.function.annotation.FunctionId;
import org.springframework.data.gemfire.function.annotation.OnRegion;


@OnRegion(region = "region3")
public interface FunctionServer3 {

    @FunctionId("function3")
    void deleteRegion();
}
