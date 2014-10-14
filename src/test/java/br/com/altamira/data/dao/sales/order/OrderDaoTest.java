/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.data.dao.sales.order;

import br.com.altamira.data.dao.sales.OrderDao;
import br.com.altamira.data.model.Entity;
import br.com.altamira.data.model.Resource;
import br.com.altamira.data.model.sales.Product;
import br.com.altamira.data.model.sales.Order;
import br.com.altamira.data.model.sales.OrderItem;
import br.com.altamira.data.model.sales.OrderItemPart;
import br.com.altamira.data.serialize.JSonViews;
import br.com.altamira.data.util.MockUtil;
import static br.com.altamira.data.util.MockUtil.getMockupOrder;
import br.com.altamira.data.util.ResourcesFactory;

import java.io.File;
import java.io.IOException;

import static java.lang.Math.abs;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolationException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.After;
import org.junit.AfterClass;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 *
 */
@RunWith(Arquillian.class)
public class OrderDaoTest {

    static final List<Long> orders = Arrays.asList(72244l, 72510l, 72201l, 72270l);

    @Deployment
    public static WebArchive createDeployment() {
        // resolve given dependencies from Maven POM
        File[] libs = Maven.resolver()
                .loadPomFromFile("pom.xml").importRuntimeAndTestDependencies()
                .resolve().withTransitivity().asFile();

        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "OrderDaoTest.war")
                .addClasses(Entity.class, Resource.class, Order.class, OrderItem.class, OrderItemPart.class, Product.class, OrderDao.class)
                .addPackage(JSonViews.class.getPackage())
                .addClass(ResourcesFactory.class)
                .addAsLibraries(libs)
                .addClass(MockUtil.class)
                .addAsWebInfResource("WEB-INF/beans.xml", "beans.xml")
                .addAsWebInfResource("META-INF/jboss-all.xml", "jboss-all.xml")
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
     * Test of list method, of class OrderDao.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void ListTest() throws Exception {

        // Invalid argument test
        try {
            orderDao.list(0, 0);
            fail("Invalid argument test fail, expecting IllegalArgumentException");
        } catch (EJBException e) {
            assertTrue("Invalid argument test fail, unexpected exception "
                    + e.getCausedByException().getMessage(),
                    e.getCausedByException() instanceof ConstraintViolationException);
        }

        // Invalid argument test
        try {
            orderDao.list(-1, 1);
            fail("Invalid argument test fail, expecting IllegalArgumentException");
        } catch (EJBException e) {
            assertTrue("Invalid argument test fail, unexpected exception "
                    + e.getCausedByException().getMessage(),
                    e.getCausedByException() instanceof ConstraintViolationException);
        }

        // Invalid argument test
        try {
            orderDao.list(0, -1);
            fail("Invalid argument test fail, expecting IllegalArgumentException");
        } catch (EJBException e) {
            assertTrue("Invalid argument test fail, unexpected exception "
                    + e.getCausedByException().getMessage(),
                    e.getCausedByException() instanceof ConstraintViolationException);
        }

        // List all test
        assertTrue("List all test fail, unexpected number of items in result: "
                + orderDao.list(0, orders.size()).size(),
                orderDao.list(0, orders.size()).size() == orders.size());

        // List pagination list, first page test
        assertTrue("List pagination list, first page test fail, unexpected number of items in result: "
                + orderDao.list(0, abs(orders.size() / 2) + orders.size() % 2).size(),
                orderDao.list(0, abs(orders.size() / 2) + orders.size() % 2).size() == abs(orders.size() / 2) + orders.size() % 2);

        // List pagination, tail page test
        assertTrue("List pagination, tail page test fail, unexpected number of items in result: "
                + orderDao.list(1, abs(orders.size() / 2) + orders.size() % 2).size(),
                orderDao.list(1, abs(orders.size() / 2) + orders.size() % 2).size() == abs(orders.size() / 2));

        // Empty list test
        assertTrue("Empty list test fail, unexpected number of items in result: "
                + orderDao.list(999999, 10).size(),
                orderDao.list(999999, 10).isEmpty());
    }

    @Test
    public void SearchTest() throws Exception {
        
        // Search number test
        log.log(Level.INFO, "Number match test start: {0}", orders.get(0).toString());
        assertTrue("Number match test fail," +
                " search for " + orders.get(0).toString() + 
                " unexpected number of items in result: " +
                orderDao.search(orders.get(0).toString(), 0, orders.size()).size(), 
                orderDao.search(orders.get(0).toString(), 0, orders.size()).size() == 1);
        
        // Number substring test
        log.log(Level.INFO, "Number substring match test start: {0}", orders.get(0).toString().substring(0, 3));
        assertTrue("Number substring match test fail," +
                " search for " + orders.get(0).toString().substring(0, 3) + 
                " unexpected number of items in result: " +
                orderDao.search(orders.get(0).toString().substring(0, 3), 0, orders.size()).size(), 
                orderDao.search(orders.get(0).toString().substring(0, 3), 0, orders.size()).size() == orders.size() - 1);
        
        // Search exactly match string test
        log.log(Level.INFO, "Search exactly match string test start: {0}", getMockupOrder(orders.get(0)).getCustomer());
        assertTrue("Exactly match string test fail," +
                " search for " + getMockupOrder(orders.get(0)).getCustomer() + 
                " unexpected number of items in result: " + orderDao.search(getMockupOrder(orders.get(0)).getCustomer(), 0, orders.size()).size(), 
                orderDao.search(getMockupOrder(orders.get(0)).getCustomer(), 0, orders.size()).size() == 1);
        
        // Substring test
        log.log(Level.INFO, "Substring test start: {0}", getMockupOrder(orders.get(0)).getCustomer().substring(5, 15));
        assertTrue("Substring test fail," +
                " search for " + getMockupOrder(orders.get(0)).getCustomer().substring(5, 15) + 
                " unexpected number of items in result: " +
                orderDao.search(getMockupOrder(orders.get(0)).getCustomer().substring(5, 15), 0, orders.size()).size(),
                orderDao.search(getMockupOrder(orders.get(0)).getCustomer().substring(5, 15), 0, orders.size()).size() == 1);
        
        // Substring lowercase test
        log.log(Level.INFO, "Substring lowercase test start: {0}", getMockupOrder(orders.get(0)).getCustomer().substring(5, 15));
        assertTrue("Substring lowercase test fail," +
                " search for " + getMockupOrder(orders.get(0)).getCustomer().substring(5, 15).toLowerCase() + 
                " unexpected number of items in result: " +
                orderDao.search(getMockupOrder(orders.get(0)).getCustomer().substring(5, 15).toLowerCase(), 0, orders.size()).size(),
                orderDao.search(getMockupOrder(orders.get(0)).getCustomer().substring(5, 15).toLowerCase(), 0, orders.size()).size() == 1);
                
        // Substring uppercase test
        log.log(Level.INFO, "Substring uppercase test start: {0}", getMockupOrder(orders.get(0)).getCustomer().substring(5, 15));;
        assertTrue("Substring uppercase test fail," +
                " search for " + getMockupOrder(orders.get(0)).getCustomer().substring(5, 15).toUpperCase() + 
                " unexpected number of items in result " +
                orderDao.search(getMockupOrder(orders.get(0)).getCustomer().substring(5, 15).toUpperCase(), 0, 1).size(),
                orderDao.search(getMockupOrder(orders.get(0)).getCustomer().substring(5, 15).toUpperCase(), 0, 1).size() == 1);
    }
    
    /**
     * Test of findByNumber method, of class OrderDao.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void FindByNumberTest() throws Exception {

        // Invalid number
        try {
            orderDao.findByNumber(0);
            fail("Invalid number test fail, expecting IllegalArgumentException");
        } catch (EJBException e) {
            assertTrue("Invalid number test fail, unexpected exception "
                    + e.getCausedByException().getMessage(),
                    e.getCausedByException() instanceof ConstraintViolationException);
        }

        // Not found
        try {
            orderDao.findByNumber(999999);
            fail("Not found test fail, expecting NoResultException");
        } catch (EJBException e) {
            assertTrue("Not found test fail, unexpected exception "
                    + e.getCausedByException().getMessage(),
                    e.getCausedByException() instanceof NoResultException);
        }
    }

    /**
     * Test of create method, of class OrderDao.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void CreateTest() throws Exception {

        // Unique constraint violation test
        try {
            orderDao.create(getMockupOrder(orders.get(0)));
        } catch (EJBException e) {
            assertTrue("Unique constraint test fail, unexpected exception " + e.getCausedByException().getMessage(), e.getCausedByException().getCause() instanceof org.hibernate.exception.ConstraintViolationException);
        }
        
    }

    /**
     * Test of update method, of class OrderDao.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void UpdateTest() throws Exception {

        // Unique constraint violation test
        try {
            orderDao.update(orderDao.findByNumber(orders.get(0)));
        } catch (EJBException e) {
            assertTrue("Unique constraint test fail, unexpected exception " + e.getCausedByException().getMessage(), e.getCausedByException().getCause() instanceof org.hibernate.exception.ConstraintViolationException);
        }
        
        // Invalid arguments id test
        try {
            orderDao.update(orderDao.findByNumber(orders.get(0)));
        } catch (EJBException e) {
            assertTrue("Unique constraint test fail, unexpected exception " + e.getCausedByException().getMessage(), e.getCausedByException().getCause() instanceof org.hibernate.exception.ConstraintViolationException);
        }        
        
    }

    /**
     * Test of remove method, of class OrderDao.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void RemoveByNumberTest() throws Exception {

        // Number not found test
        try {
            orderDao.remove(orders.get(0));
        } catch (EJBException e) {

        }

    }

    /**
     * Test of remove method, of class OrderDao.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void RemoveByEntityTest() throws Exception {

        // Reference integrity constraint violation test
        try {
            orderDao.remove(orders.get(0));
        } catch (EJBException e) {

        }

        // Detached entity test
        try {
            orderDao.remove(orders.get(1));
        } catch (EJBException e) {

        }
    }

//    @Test
//    @RunAsClient
//    @InSequence(20)
//    public void shouldReturnAValidationErrorWhenGettingAOrder(@ArquillianResource URL baseURL) {
//        Client client = ClientBuilder.newBuilder()
////                .register(JacksonJsonProvider.class)
//                .build();
//        Response response = client.target(baseURL + "r/orders/{id}")
//                .resolveTemplate("id", "test")
//                .request(MediaType.APPLICATION_JSON)
//                .header("Accept-Language", "en")
//                .get();
//        response.bufferEntity();
//
//        log.log(Level.INFO, "shouldReturnAValidationErrorWhenGettingAOrder: {0}", response);
//        Assert.assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
//    }
//
//    @Test
//    @RunAsClient
//    @InSequence(30)
//    public void shouldReturnAnEmptyOrder(@ArquillianResource URL baseURL) {
//        Client client = ClientBuilder.newBuilder()
//                .register(JacksonJsonProvider.class)
//                .build();
//        Response response = client.target(baseURL + "r/orders/{id}")
//                .resolveTemplate("id", "10")
//                .request(MediaType.APPLICATION_JSON)
//                .get();
//        response.bufferEntity();
//
//        log.log(Level.INFO, "shouldReturnAnEmptyOrder {0}", response);
//        Assert.assertEquals(null, response.readEntity(Order.class));
//    }
//    
//    @Test
//    @RunAsClient
//    @InSequence(50)
//    public void shouldReturnACreatedOrder(@ArquillianResource URL baseURL) {
//        Order order = new Order();
//        order.setId(20l);
//        order.setCustomer("sam");
//        Form form = new Form();
//        form.param("id", String.valueOf(order.getId()));
//        form.param("name", order.getCustomer());
//
//        Client client = ClientBuilder.newBuilder()
//                .register(JacksonJsonProvider.class)
//                .build();
//        Response response = client.target(baseURL + "r/orders/create")
//                .request(MediaType.APPLICATION_JSON)
//                .post(javax.ws.rs.client.Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
//        response.bufferEntity();
//
//        log.log(Level.INFO, "shouldReturnACreatedOrder: {0}", response);
//        Assert.assertEquals(order, response.readEntity(Order.class));
//    }

}
