/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.data.rest.sales;

import static br.com.altamira.data.util.MockUtil.getMockupOrder;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.altamira.data.dao.sales.OrderDao;
import br.com.altamira.data.model.Entity;
import br.com.altamira.data.model.Resource;
import br.com.altamira.data.model.sales.Order;
import br.com.altamira.data.model.sales.OrderItem;
import br.com.altamira.data.model.sales.OrderItemPart;
import br.com.altamira.data.model.sales.Product;
import br.com.altamira.data.serialize.JSonViews;
import br.com.altamira.data.util.MockUtil;
import br.com.altamira.data.util.ResourcesFactory;

/**
 *
 *
 */
@RunWith(Arquillian.class)
public class OrderEndpointTest {

    static final List<Long> orders = Arrays.asList(72244l, 72510l, 72201l, 72270l);

    @Deployment
    public static WebArchive createDeployment() {
        // resolve given dependencies from Maven POM
        File[] libs = Maven.resolver()
                .loadPomFromFile("pom.xml").importRuntimeAndTestDependencies()
                .resolve().withTransitivity().asFile();

        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "OrderEndpointTest.war")
                .addClasses(Entity.class, Resource.class, Order.class, OrderItem.class, OrderItemPart.class, Product.class, OrderDao.class)
                .addClass(OrderEndpoint.class)
                .addPackage(JSonViews.class.getPackage())
                .addClass(ResourcesFactory.class)
                .addAsLibraries(libs)
                .addClass(MockUtil.class)
                .addAsWebInfResource("WEB-INF/beans.xml", "beans.xml")
                //.addAsWebInfResource("META-INF/jboss-all.xml", "jboss-all.xml")
                //.addAsWebResource("log4j.xml", "log4j.xml")
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml");

        for (Long number : orders) {
            war.addAsResource(MessageFormat.format("order_{0}.json", number.toString()));
        }
        System.out.print(war.toString(Formatters.VERBOSE));

        return war;
    }

    @Inject
    private Logger log;

    @EJB
    private OrderDao orderDao;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws IOException {
        // Create test
        for (Long number : orders) {
            Order order = getMockupOrder(number);
            try {
                order = orderDao.create(order);
                assertNotNull("Setup test fail, can not create use case for order number " + number.toString() + ", entity is null.", order);
                assertNotNull("Setup test fail, can not create use case for order number " + number.toString() + ", id is null.", order.getId());
            } catch (EJBException e) {
                fail("Setup test fail, can not create use case for order number " + number.toString() + ", exception: " + e.getCausedByException().getMessage());
            }
            //log.log(Level.INFO, "Order mockup data created for number ", number);
        }
    }

    @After
    public void tearDown() {
        // Remove test
        for (Long number : orders) {

            // Remove by Entity test
            try {
                orderDao.remove(number);
            } catch (EJBException e) {
                // TODO: implement rollback transaction after execute each test
                assertTrue("Setup test fail, can not remove use for next test ", e.getCausedByException() instanceof NoResultException);
            }
        }
    }

    /**
     * Test of list method, of class OrderEndpoint.
     *
     * @param baseURL
     * @throws java.lang.Exception
     */
    @Test
    @RunAsClient
    public void ListTest(@ArquillianResource URL baseURL) throws Exception {
        Client client = ClientBuilder.newBuilder()
                //.register(JacksonJsonProvider.class)
                .build();
        Response response = client.target(baseURL + "sales/order")
                //.resolveTemplate("id", "test")
                .request(MediaType.APPLICATION_JSON)
                //.header("Accept-Language", "en")
                .get();
        response.bufferEntity();

        log.log(Level.INFO, "shouldReturnAValidationErrorWhenGettingAOrder: {0}", response);
        Assert.assertEquals(Status.BAD_REQUEST, response.getStatus());
    }

    /**
     * Test of findByNumber method, of class OrderEndpoint.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testFindByNumber() throws Exception {
    }

    /**
     * Test of create method, of class OrderEndpoint.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testCreate() throws Exception {
    }

    /**
     * Test of update method, of class OrderEndpoint.
     */
    @Test
    public void testUpdate() {

    }

    /**
     * Test of deleteById method, of class OrderEndpoint.
     */
    @Test
    public void testDeleteById() {

    }

    /**
     * Test of getCORSHeadersFromPath method, of class OrderEndpoint.
     */
    @Test
    public void testGetCORSHeadersFromPath() {

    }

    /**
     * Test of getCORSHeadersFromNumberPath method, of class OrderEndpoint.
     */
    @Test
    public void testGetCORSHeadersFromNumberPath() {

    }

    /**
     * Test of search method, of class OrderEndpoint.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testSearch() throws Exception {

    }

}
