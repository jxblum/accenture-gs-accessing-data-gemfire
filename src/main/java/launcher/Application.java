package launcher;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.Pool;
import com.gemstone.gemfire.cache.client.PoolManager;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(scanBasePackages = "launcher")
@ImportResource("classpath:application-context.xml")
@SuppressWarnings("unused")
public class Application {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    ApplicationRunner runner(final ClientCache clientCache, final FunctionOne functionOne,
            final FunctionTwo functionTwo, final FunctionThree functionThree) {

        return new ApplicationRunner() {

            @Override
            public void run(ApplicationArguments args) throws Exception {

                Region<Object, Object> regionOne = clientCache.getRegion("region1");
                Region<Object, Object> regionTwo = clientCache.getRegion("region2");

                assertThat(regionOne).isNotNull();
                assertThat(regionOne.getName()).isEqualTo("region1");
                assertThat(regionOne.getAttributes()).isNotNull();
                assertThat(regionOne.getAttributes().getPoolName()).isEqualTo("tpa-pool");
                assertThat(regionTwo).isNotNull();
                assertThat(regionTwo.getName()).isEqualTo("region2");
                assertThat(regionTwo.getAttributes()).isNotNull();
                assertThat(regionTwo.getAttributes().getPoolName()).isEqualTo("tpa-pool");

                Pool defaultPool = PoolManager.find("DEFAULT");

                assertThat(defaultPool).isNotNull();
                assertThat(defaultPool.getMaxConnections()).isEqualTo(30);
                assertThat(defaultPool.getMinConnections()).isEqualTo(10);
                assertThat(defaultPool.getReadTimeout()).isEqualTo(30000);
                assertThat(defaultPool.getRetryAttempts()).isEqualTo(100);

                functionOne.addEntry();

                assertThat(regionTwo.get("invocationCount")).isEqualTo(1);
                assertThat(regionOne.containsKey("testKey")).isFalse();

                regionOne.put("testKey", "testValue");

                assertThat(regionOne.get("testKey")).isEqualTo("testValue");

                functionTwo.deleteRegion();

                assertThat(regionOne.containsKey("testKey")).isFalse();

                System.err.println("SUCCESS");
            }
        };
    }
}
