package info.interactivesystems.spade.sentence;

import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;

@Slf4j
@ContextConfiguration(locations = {"classpath:beans.xml"})
public class GunningFogIndexTest extends AbstractTestNGSpringContextTests {

	@Resource
	private GunningFogIndex gfi;

	private String testString =
			"Unlike the other indices, the ARI, along with the Coleman-Liau, relies on a factor of characters per word, "
					+ "instead of the usual syllables per word. Although opinion varies on its accuracy as compared to the "
					+ "syllables/word and complex words indices, characters/word is often faster to calculate, as the number "
					+ "of characters is more readily and accurately counted by computer programs than syllables. In fact, "
					+ "this index was designed for real-time monitoring of readability on electric typewriters.";

	@Test
	public void calculateReadabilityTest() {
		Double readability = gfi.calculateReadability(testString);
		log.info("Test String has a GFI readability of '{}'", readability);
	}
}
