package server;

import java.util.concurrent.atomic.AtomicInteger;

import com.gemstone.gemfire.cache.GemFireCache;
import com.gemstone.gemfire.cache.execute.FunctionContext;
import com.gemstone.gemfire.cache.execute.RegionFunctionContext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.CacheServerApplication;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.EnableManager;
import org.springframework.data.gemfire.function.annotation.GemfireFunction;
import org.springframework.data.gemfire.function.config.EnableGemfireFunctions;

/**
 * The GemFireServerApplication class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@SpringBootApplication
@CacheServerApplication(name = "SpringGemFireServerApplication")
@EnableGemfireFunctions
@SuppressWarnings("unused")
public class GemFireServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GemFireServerApplication.class, args);
	}

	@Configuration
	@EnableLocator
	@EnableManager
	@Profile("locator-manager")
	static class LocatorManagerConfiguration {
	}

	@Bean("region1")
	public ReplicatedRegionFactoryBean<Object, Object> regionOne(GemFireCache gemfireCache) {

		ReplicatedRegionFactoryBean<Object, Object> regionOne =
			new ReplicatedRegionFactoryBean<Object, Object>();

		regionOne.setCache(gemfireCache);
		regionOne.setClose(false);
		regionOne.setPersistent(false);

		return regionOne;
	}

	@Bean("region2")
	public ReplicatedRegionFactoryBean<Object, Object> regionTwo(GemFireCache gemfireCache) {

		ReplicatedRegionFactoryBean<Object, Object> regionTwo =
			new ReplicatedRegionFactoryBean<Object, Object>();

		regionTwo.setCache(gemfireCache);
		regionTwo.setClose(false);
		regionTwo.setPersistent(false);

		return regionTwo;
	}

	@Bean("region3")
	public ReplicatedRegionFactoryBean<Object, Object> regionThree(GemFireCache gemfireCache) {

		ReplicatedRegionFactoryBean<Object, Object> regionThree =
			new ReplicatedRegionFactoryBean<Object, Object>();

		regionThree.setCache(gemfireCache);
		regionThree.setClose(false);
		regionThree.setPersistent(false);

		return regionThree;
	}

	@Bean
	public ApplicationFunctions applicationFunctions() {
		return new ApplicationFunctions();
	}

	public static class ApplicationFunctions {

		private static final AtomicInteger functionOneInvocationCount = new AtomicInteger(0);

		// On Region 2
		@GemfireFunction(id = "function1")
		public void addEntry(FunctionContext functionContext) {
			((RegionFunctionContext) functionContext).getDataSet()
				.put("invocationCount", functionOneInvocationCount.incrementAndGet());
		}

		// On Region 1
		@GemfireFunction(id = "function2")
		public void deleteRegion1(FunctionContext functionContext) {
			((RegionFunctionContext) functionContext).getDataSet().clear();
		}

		// On Region 3
		@GemfireFunction(id = "function3")
		public void deleteRegion3(FunctionContext functionContext) {
			((RegionFunctionContext) functionContext).getDataSet().clear();
		}
	}
}
