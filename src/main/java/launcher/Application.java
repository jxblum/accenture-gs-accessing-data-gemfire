package launcher;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Properties;

import com.gemstone.gemfire.cache.GemFireCache;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.client.ClientCacheFactoryBean;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.client.PoolFactoryBean;
import org.springframework.data.gemfire.function.config.EnableGemfireFunctionExecutions;
import org.springframework.data.gemfire.support.ConnectionEndpoint;
import org.springframework.data.gemfire.support.ConnectionEndpointList;

@SpringBootApplication(scanBasePackages = "launcher")
@EnableAutoConfiguration
@EnableGemfireFunctionExecutions(basePackageClasses = FunctionOne.class)
@SuppressWarnings("unused")
public class Application {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    ApplicationRunner runner(final ClientCache clientCache,
            final FunctionOne functionOne, final FunctionTwo functionTwo, final FunctionThree functionThree) {

        return new ApplicationRunner() {

            private Region<Object, Object> regionOne = clientCache.getRegion("region1");
            private Region<Object, Object> regionTwo = clientCache.getRegion("region2");

            @Override
            public void run(ApplicationArguments args) throws Exception {

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

    @Bean
    Properties gemfireProperties(@Value("${gemfire.client.error-log:error}") String logLevel) {

        Properties gemfireProperties = new Properties();

        gemfireProperties.setProperty("log-level", logLevel);

        return gemfireProperties;
    }

    @Bean
    ClientCacheFactoryBean gemfireCache(Properties gemfireProperties) {

        ClientCacheFactoryBean gemfireCache = new ClientCacheFactoryBean();

        gemfireCache.setClose(true);
        // NOTE: setting the Pool name is not strictly necessary!
        // All this does is configure the "DEFAULT" Pool to have the same settings as the "tpa-pool" Pool.
        // Yet, the "DEFAULT" Pool is not being used by any of the example/test application Regions.
        // So, it is pointless.
        gemfireCache.setPoolName("tpa-pool");
        gemfireCache.setProperties(gemfireProperties);

        return gemfireCache;
    }

    @Bean("tpa-pool")
    PoolFactoryBean tpaPool(
        @Value("${gemfire.client.max-connections:20}") int maxConnections,
        @Value("${gemfire.client.min-connections:10}") int minConnections,
        @Value("${gemfire.client.read-timeout:600000}") int readTimeout,
        @Value("${gemfire.client.retry-attempts:10}") int retryAttempts,
        @Value("${gemfire.locator.host:localhost}") String locatorHost,
        @Value("${gemfire.locator.port:10334}") int locatorPort) {

        PoolFactoryBean tpaPool = new PoolFactoryBean();

        tpaPool.setLocators(ConnectionEndpointList.from(
            new ConnectionEndpoint(locatorHost, locatorPort)));

        tpaPool.setMaxConnections(maxConnections);
        tpaPool.setMinConnections(minConnections);
        tpaPool.setReadTimeout(readTimeout);
        tpaPool.setRetryAttempts(retryAttempts);

        return tpaPool;
    }

    @Bean("region1")
    public ClientRegionFactoryBean<Object, Object> region1(GemFireCache gemfireCache) {

        ClientRegionFactoryBean<Object, Object> clientRegion =
            new ClientRegionFactoryBean<Object, Object>();

        clientRegion.setCache(gemfireCache);
        clientRegion.setClose(false);
        clientRegion.setPoolName("tpa-pool");
        clientRegion.setShortcut(ClientRegionShortcut.PROXY);

        return clientRegion;
    }

    @Bean("region2")
    public ClientRegionFactoryBean<Object, Object> region2(GemFireCache gemfireCache) {

        ClientRegionFactoryBean<Object, Object> clientRegion =
            new ClientRegionFactoryBean<Object, Object>();

        clientRegion.setCache(gemfireCache);
        clientRegion.setClose(false);
        clientRegion.setPoolName("tpa-pool");
        clientRegion.setShortcut(ClientRegionShortcut.PROXY);

        return clientRegion;
    }

    @Bean("region3")
    public ClientRegionFactoryBean<Object, Object> region3(GemFireCache gemfireCache) {

        ClientRegionFactoryBean<Object, Object> clientRegion =
            new ClientRegionFactoryBean<Object, Object>();

        clientRegion.setCache(gemfireCache);
        clientRegion.setClose(false);
        clientRegion.setPoolName("tpa-pool");
        clientRegion.setShortcut(ClientRegionShortcut.PROXY);

        return clientRegion;
    }
}
