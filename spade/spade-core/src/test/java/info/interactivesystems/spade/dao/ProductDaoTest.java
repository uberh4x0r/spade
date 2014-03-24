package info.interactivesystems.spade.dao;

import static org.assertj.core.api.Assertions.assertThat;
import info.interactivesystems.spade.entities.Product;

import javax.annotation.Resource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:beans.xml" })
public class ProductDaoTest extends AbstractTestNGSpringContextTests {

    @Resource
    private ProductDao productDao;

    @Test
    public void find() {
        assertThat(productDao).isNotNull();

        Product result = productDao.find("1934148644");
        assertThat(result.getName())
            .isEqualTo(
                "Nikon D90 inBrief laminated reference card by Blue Crane Digital (Apr 15, 2009)");
    }
}