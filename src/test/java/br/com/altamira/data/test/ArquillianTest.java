package br.com.altamira.data.test;

import static org.junit.Assert.*;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

import br.com.altamira.data.dao.purchasing.MaterialDao;
import br.com.altamira.data.dao.purchasing.request.RequestDao;
import br.com.altamira.data.dao.purchasing.request.RequestItemDao;
import br.com.altamira.data.model.purchasing.Material;
import br.com.altamira.data.model.purchasing.Request;
import br.com.altamira.data.model.purchasing.RequestItem;
import br.com.altamira.data.model.sales.order.Order;
import br.com.altamira.data.rest.purchasing.request.RequestEndpoint;
import br.com.altamira.data.serialize.JSonViews;
import br.com.altamira.data.serialize.NullValueSerializer;

@RunWith(Arquillian.class)
public class ArquillianTest {

	@Deployment
	public static WebArchive createDeployment() {
		// resolve given dependencies from Maven POM
		File[] libs = Maven.resolver().offline(false)
				.loadPomFromFile("pom.xml").importRuntimeAndTestDependencies()
				.resolve().withTransitivity().asFile();

		return ShrinkWrap
				.create(WebArchive.class, "altamira-data-test.war")
				// add needed dependencies
				.addAsLibraries(libs)
				// prepare as process application archive for camunda BPM
				// Platform
				.addAsWebResource("META-INF/test-processes.xml",
						"WEB-INF/classes/META-INF/processes.xml")
				// enable CDI
				.addAsWebResource("WEB-INF/beans.xml", "WEB-INF/beans.xml")
				.addAsWebResource("log4j.xml", "log4j.xml")
				// boot JPA persistence unit
				.addAsResource("META-INF/test-persistence.xml",
						"META-INF/persistence.xml")
				// add your own classes (could be done one by one as well)
				// not recursive to skip package 'nonarquillian'
				.addPackages(true, "br.com.altamira.data")
		// now you can add additional stuff required for your test case
		;
	}

	public final static String url = "http://localhost:8080/data/rest/request";

	@Inject
	private RequestDao requestDao;
	
	@Inject 
	private MaterialDao materialDao;
	
	@Inject
	private RequestItemDao requestItemDao;

	@Inject 
	private EntityManager entityManager;
	
	@Inject
	private UserTransaction userTransaction;
	
	@Before
	public void preparePersistenceTest() throws Exception {
	    clearData();
	    //insertData();
	    //startTransaction();
	}

	private void clearData() throws Exception {
	    System.out.println("Dumping old records...");
	    userTransaction.begin();
	    entityManager.joinTransaction();
	    entityManager.createQuery("DELETE FROM RequestItem WHERE id IN (SELECT R.id FROM RequestItem R INNER JOIN R.material M WHERE M.lamination = 'NO' AND M.treatment = 'ZU')").executeUpdate();
	    userTransaction.commit();
	    userTransaction.begin();
	    entityManager.joinTransaction();
	    entityManager.createQuery("DELETE FROM RequestItem WHERE id IN (SELECT R.id FROM RequestItem R INNER JOIN R.material M WHERE M.lamination = 'FQ' AND M.treatment = 'TO')").executeUpdate();
	    userTransaction.commit();
	    userTransaction.begin();
	    entityManager.joinTransaction();
	    entityManager.createQuery("DELETE FROM RequestItem WHERE id IN (SELECT R.id FROM RequestItem R INNER JOIN R.material M WHERE M.lamination = 'FT' AND M.treatment = 'TR')").executeUpdate();
	    userTransaction.commit();
	    userTransaction.begin();
	    entityManager.joinTransaction();
	    entityManager.createQuery("DELETE FROM RequestItem WHERE id IN (SELECT R.id FROM RequestItem R INNER JOIN R.material M WHERE M.lamination = 'WQ' AND M.treatment = 'GU')").executeUpdate();
	    userTransaction.commit();
	    userTransaction.begin();
	    entityManager.joinTransaction();
	    entityManager.createQuery("DELETE FROM RequestItem WHERE id IN (SELECT R.id FROM RequestItem R INNER JOIN R.material M WHERE M.lamination = 'FZ' AND M.treatment = 'RT')").executeUpdate();
	    userTransaction.commit();
	    userTransaction.begin();
	    entityManager.joinTransaction();
	    entityManager.createQuery("DELETE FROM RequestItem WHERE id IN (SELECT R.id FROM RequestItem R INNER JOIN R.material M WHERE M.lamination = 'FQ' AND M.treatment = 'PZ')").executeUpdate();
	    userTransaction.commit();
	    userTransaction.begin();
	    entityManager.joinTransaction();
	    entityManager.createQuery("DELETE FROM RequestItem WHERE id IN (SELECT R.id FROM RequestItem R INNER JOIN R.material M WHERE M.lamination = 'TX' AND M.treatment = 'TR')").executeUpdate();
	    userTransaction.commit();
	    userTransaction.begin();
	    entityManager.joinTransaction();
	    entityManager.createQuery("DELETE FROM Material M WHERE M.lamination = 'NO' AND M.treatment = 'ZU'").executeUpdate();
	    userTransaction.commit();
	    userTransaction.begin();
	    entityManager.joinTransaction();
	    entityManager.createQuery("DELETE FROM Material M WHERE M.lamination = 'FQ' AND M.treatment = 'TO'").executeUpdate();
	    userTransaction.commit();
	    userTransaction.begin();
	    entityManager.joinTransaction();
	    entityManager.createQuery("DELETE FROM Material M WHERE M.lamination = 'FT' AND M.treatment = 'TR'").executeUpdate();
	    userTransaction.commit();
	    userTransaction.begin();
	    entityManager.joinTransaction();
	    entityManager.createQuery("DELETE FROM Material M WHERE M.lamination = 'WQ' AND M.treatment = 'GU'").executeUpdate();
	    userTransaction.commit();
	    userTransaction.begin();
	    entityManager.joinTransaction();
	    entityManager.createQuery("DELETE FROM Material M WHERE M.lamination = 'FZ' AND M.treatment = 'RT'").executeUpdate();
	    userTransaction.commit();
	    userTransaction.begin();
	    entityManager.joinTransaction();
	    entityManager.createQuery("DELETE FROM Material M WHERE M.lamination = 'FQ' AND M.treatment = 'PZ'").executeUpdate();
	    userTransaction.commit();
	    userTransaction.begin();
	    entityManager.joinTransaction();
	    entityManager.createQuery("DELETE FROM Material M WHERE M.lamination = 'TX' AND M.treatment = 'TR'").executeUpdate();
	    userTransaction.commit();
	    userTransaction.begin();
	    entityManager.joinTransaction();
	    entityManager.createQuery("DELETE FROM Material M WHERE M.lamination = 'LX' AND M.treatment = 'LX'").executeUpdate();
	    userTransaction.commit();
	    userTransaction.begin();
	    entityManager.joinTransaction();
	    entityManager.createQuery("DELETE FROM Material M WHERE M.lamination = 'LX' AND M.treatment = 'TX'").executeUpdate();
	    userTransaction.commit();
	    entityManager.clear();
	}

	/*private void insertData() throws Exception {
		userTransaction.begin();
		entityManager.joinTransaction();
	    System.out.println("Inserting records...");
	    for (String title : GAME_TITLES) {
	        Game game = new Game(title);
	        entityManager.persist(game);
	    }
	    userTransaction.commit();
	    // clear the persistence context (first-level cache)
	    entityManager.clear();
	}*/

	/*private void startTransaction() throws Exception {
		userTransaction.begin();
		entityManager.joinTransaction();
	}*/
	
	@After
	public void commitTransaction() throws Exception {
		//userTransaction.commit();
	}
	
	
	@Test
	@InSequence(10)
	public void MaterialDaoTest() {
		Material material = new Material();
		
		material.setLamination("LX");
		material.setTreatment("LX");
		material.setThickness(BigDecimal.valueOf(2.57));
		material.setWidth(BigDecimal.valueOf(200.0057));
		material.setLength(BigDecimal.valueOf(1000.0059));
		material.setTax(BigDecimal.valueOf(4.599));
		
		Material entity = materialDao.find(material);
		
		if (entity == null) {
			entity = materialDao.create(material);
		}
		
		assertNotNull(entity);
		assertNotNull(entity.getId());
		assertEquals(entity.getLamination(), material.getLamination());
		assertEquals(entity.getTreatment(), material.getTreatment());
		assertEquals(entity.getThickness(), material.getThickness());
		assertEquals(entity.getWidth(), material.getWidth());
		assertEquals(entity.getLength(), material.getLength());
		assertEquals(entity.getTax(), material.getTax());
		
		material = entity;
		
		material.setLamination("LX");
		material.setTreatment("TX");
		material.setThickness(BigDecimal.valueOf(0.571));
		material.setWidth(BigDecimal.valueOf(100.529));
		material.setLength(BigDecimal.valueOf(2000.537));
		material.setTax(BigDecimal.valueOf(1.599));
		
		Material updated = materialDao.update(material);
		
		Material found = materialDao.find(updated);
		
		assertNotNull(found);
		assertTrue(found.equals(updated));
		
		Material find = materialDao.find(entity.getLamination(), entity.getTreatment(), entity.getThickness(), entity.getWidth(), entity.getLength());
		
		assertTrue(found.equals(find));
		
		Material removed = materialDao.remove(entity);
		
		//assertNull(remove.getId()); // Id is not changed to null after remove
		
		Material notfound = materialDao.find(removed);
		
		assertNull(notfound);
		
	}
	
	/**
	 * This test case verify if MaterialDao.create handle correct with id, 
	 * to avoid persistence detach object exception when id is not null.
	 * The possible values to id is null or zero
	 */
	@Test
	@InSequence(11)
	public void MaterialDaoNonNullIdUseCaseTest() {
		Material material = new Material();
		
		material.setId(0l); // test handling with persistence detatch exception
		material.setLamination("LX");
		material.setTreatment("LX");
		material.setThickness(BigDecimal.valueOf(2.0));
		material.setWidth(BigDecimal.valueOf(200.0));
		material.setLength(BigDecimal.valueOf(1000.0));
		material.setTax(BigDecimal.valueOf(4.5));
		
		Material entity = materialDao.create(material);
		
		assertNotNull(entity);
		assertNotNull(entity.getId());
	}
	
	@Test
	@InSequence(20)
	public void RequestDaoTest() throws Exception {
		Request request = new Request();
		
		request.setCreated(new Date());
		request.setCreator("RequestDaoTest");
		request.setSent(null);
		
		requestDao.create(request);
		
		assertNotNull(request.getId());
		
		Material material = new Material();
		
		material.setLamination("TX");
		material.setTreatment("TR");
		material.setThickness(BigDecimal.valueOf(2.0));
		material.setWidth(BigDecimal.valueOf(200.0));
		material.setLength(BigDecimal.valueOf(0));
		material.setTax(BigDecimal.valueOf(4.5));

		materialDao.create(material);
		
		RequestItem item = new RequestItem();
		
		item.setRequest(request);
		item.setArrival(new Date());
		item.setWeight(BigDecimal.valueOf(1234.0));
		item.setMaterial(material);
		
		Set<RequestItem> items = new HashSet<RequestItem>();
		items.add(item);
		
		request.setSent(new Date());
		request.setItem(items);
		
		// Insert Item
		Request entity = requestDao.update(request);
		
		// be sure that the item was stored correctly
		for (RequestItem r : entity.getItem()) {
			assertNotNull(r.getId());
			assertNotNull(r.getMaterial().getId());
		}
		
		Request found = requestDao.find(entity.getId());
		
		assertTrue(entity.equals(found));
		assertFalse(entity.getItem().isEmpty());
		assertEquals(1, entity.getItem().size());
		assertNotNull(entity.getItem().contains(item));
		
		// be sure that the item was stored correctly
		for (RequestItem r : found.getItem()) {
			assertNotNull(r.getId());
			assertNotNull(r.getMaterial().getId());
		}
		
		List<Request> list = requestDao.list(0, 10);
		
		assertNotNull(list);
		assertNotEquals(0, list.size());
		
		Long id = entity.getId();
		
		requestDao.remove(entity);
		
		Request exist = requestDao.find(id);
		
		assertNull(exist);
	}
	
	/**
	 * This test case verify if RequestDao.create handle correct with id, 
	 * to avoid persistence detach object exception when id is not null.
	 * The possible values to id is null or zero
	 */
	@Test
	@InSequence(21)
	public void RequestDaoNonNullIdUseCaseTest() {
		Request request = new Request();
		
		request.setId(0l); // test handling with persistence detatch exception
		request.setCreated(new Date());
		request.setCreator("RequestDaoTest");
		request.setSent(null);
		
		Request entity = requestDao.create(request);
		
		assertNotNull(entity);
		assertNotNull(entity.getId());
	}
	
	@Test
	@InSequence(30)
	public void RequestItemDaoTest() throws Exception {
		
		Request request = requestDao.current();
		
		if (request == null) {
			request = new Request();
			
			request.setCreated(new Date());
			request.setCreator("RequestDaoTest");
			request.setSent(null);
			
			request = requestDao.create(request);
		}
		
		assertNotNull(request.getId());
		
		Material material = new Material();
		
		material.setLamination("FT");
		material.setTreatment("TR");
		material.setThickness(BigDecimal.valueOf(2.0));
		material.setWidth(BigDecimal.valueOf(200.0));
		material.setLength(BigDecimal.valueOf(0));
		material.setTax(BigDecimal.valueOf(4.5));
		
		RequestItem entity = new RequestItem();
		
		entity.setRequest(request);
		entity.setMaterial(material);
		entity.setArrival(new Date());
		entity.setWeight(BigDecimal.valueOf(1234.0));

		requestItemDao.create(entity);
		
		// be sure that the item was stored correctly
		assertNotNull(entity.getId());
		assertNotNull(entity.getMaterial().getId());
		
		RequestItem found = requestItemDao.find(entity.getId());
		
		assertTrue(found.equals(entity));
		assertTrue(found.getMaterial().equals(entity.getMaterial()));
		
		material.setLamination("TT");
		material.setTreatment("TT");
		material.setThickness(BigDecimal.valueOf(3.5));
		material.setWidth(BigDecimal.valueOf(100.5));
		material.setLength(BigDecimal.valueOf(2000.5));
		material.setTax(BigDecimal.valueOf(1.5));
		
		entity.setArrival(new Date());
		entity.setWeight(BigDecimal.valueOf(9999.09));
		entity.setMaterial(material);
		
		requestItemDao.update(entity);
		
		RequestItem find = requestItemDao.find(entity.getId());
		
		assertTrue(find.equals(entity));
		assertTrue(find.getMaterial().equals(entity.getMaterial()));
		
		List<RequestItem> list = requestItemDao.list(request.getId(), 0, 10);
		
		assertNotNull(list);
		assertNotEquals(0, list.size());
		
		Long id = entity.getId();
		
		requestItemDao.remove(entity);
		
		RequestItem exist = requestItemDao.find(id);
		
		assertNull(exist);
		
	}
	
	/**
	 * This test case verify if RequestDao.create handle correct with id, 
	 * to avoid persistence detach object exception when id is not null.
	 * The possible values to id is null or zero
	 */
	@Test
	@InSequence(31)
	public void RequestItemDaoNonNullIdUseCaseTest() {
		Request request = requestDao.current();
		
		if (request == null) {
			request = new Request();
			
			request.setCreated(new Date());
			request.setCreator("RequestDaoTest");
			request.setSent(null);
			
			request = requestDao.create(request);
		}
		
		assertNotNull(request.getId());
		
		Material material = new Material();
		
		material.setLamination("FT");
		material.setTreatment("TR");
		material.setThickness(BigDecimal.valueOf(2.0));
		material.setWidth(BigDecimal.valueOf(200.0));
		material.setLength(BigDecimal.valueOf(0));
		material.setTax(BigDecimal.valueOf(4.5));
		
		RequestItem requestItem = new RequestItem();
		
		requestItem.setId(0l); // test handling with persistence detatch exception
		requestItem.setRequest(request);
		requestItem.setMaterial(material);
		requestItem.setArrival(new Date());
		requestItem.setWeight(BigDecimal.valueOf(1234.0));

		RequestItem entity = requestItemDao.create(requestItem);
		
		// be sure that the item was stored correctly
		assertNotNull(entity);
		assertNotNull(entity.getId());
	}	
	@Test
	@InSequence(40)
	public void RequestEndpointGetCurrentTest() throws Exception {

		UriBuilder context = UriBuilder.fromUri(url);
		
		ClientRequest client = new ClientRequest(context.path("/current").build().toString());
		client.accept(MediaType.APPLICATION_JSON);
		client.header("Content-Type", MediaType.APPLICATION_JSON);

		ClientResponse<Request> response = client.get(Request.class);

		assertEquals(200, response.getStatus());

		Request entity = response.getEntity();
		Assert.assertNotNull(entity);
		
	}
	
	// Test another way to get the current request, pass ZERO as id
	@Test
	@InSequence(41)
	public void RequestEndpointFindByIdZeroTest() throws Exception {
		
		UriBuilder context = UriBuilder.fromUri(url);
		
		ClientRequest client = new ClientRequest(context.path("/{id}").build(0).toString());
		client.accept(MediaType.APPLICATION_JSON);
		client.header("Content-Type", MediaType.APPLICATION_JSON);

		ClientResponse<Request> response = client.get(Request.class);

		assertEquals(200, response.getStatus());

		Request entity = response.getEntity();
		Assert.assertNotNull(entity);
	}
	
	@Test
	@InSequence(42)
	public void RequestEndpointFindByIdTest() throws Exception {
		UriBuilder context = UriBuilder.fromUri(url);
		
		ClientRequest client = new ClientRequest(context.path("/{id}").build(requestDao.current().getId()).toString());
		client.accept(MediaType.APPLICATION_JSON);
		client.header("Content-Type", MediaType.APPLICATION_JSON);

		ClientResponse<Request> response = client.get(Request.class);

		assertEquals(200, response.getStatus());

		Request entity = response.getEntity();
		Assert.assertNotNull(entity);
	}
	
	@Test
	@InSequence(50)
	public void RequestEndpointUpdateTest() throws Exception {

		Request request = requestDao.current();
		
		assertNotNull(request);
		
		RequestItem item = new RequestItem();
		
		assertNotNull(item);
		
		Material material = new Material();
		
		material.setLamination("NO");
		material.setTreatment("ZU");
		material.setThickness(BigDecimal.valueOf(2.0));
		material.setWidth(BigDecimal.valueOf(200.0));
		material.setLength(BigDecimal.valueOf(12.3));
		material.setTax(BigDecimal.valueOf(4.5));
		
		material = materialDao.create(material);
		
		assertNotNull(material);
		
		item.setArrival(new Date());
		item.setWeight(BigDecimal.valueOf(8899.0));
		item.setMaterial(material);
		
		request.getItem().add(item);
		
		request.setCreated(new Date());
		request.setCreator("XXXX");
		request.setSent(new Date());
		
		UriBuilder context = UriBuilder.fromUri(url);
		
		ClientRequest client = new ClientRequest(context.path("/{id}").build(request.getId()).toString());
		client.accept(MediaType.APPLICATION_JSON);
		client.header("Content-Type", MediaType.APPLICATION_JSON);
		
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.registerModule(new Hibernate4Module());
		mapper.getSerializerProvider().setNullValueSerializer(new NullValueSerializer());
		ObjectWriter writer = mapper.writerWithView(JSonViews.EntityView.class);

		client.body(MediaType.APPLICATION_JSON, writer.writeValueAsString(request));
		//client.body(MediaType.APPLICATION_JSON, request);
		
		ClientResponse<Request> response = client.put(Request.class);
		
		assertEquals(200, response.getStatus());
		
		Request entity = response.getEntity();

		assertNotNull(entity);
		
		// be sure that the fields deserialize are correct
		assertNotNull(entity.getId());
		assertEquals(entity.getCreated(), request.getCreated());
		assertEquals(entity.getCreator(), request.getCreator());

		for(RequestItem i : entity.getItem()) {
			assertTrue(entity.getItem().contains(i));
		}

	}
	
	@Test
	@InSequence(51)
	public void RequestEndpointListTest() throws Exception {
		UriBuilder context = UriBuilder.fromUri(url);
		
		Client client = ClientBuilder.newBuilder().build();
		WebTarget target = client.target(context.build().toString());
		ResteasyWebTarget rtarget = (ResteasyWebTarget)target;
		
		RequestEndpoint r = rtarget.proxy(RequestEndpoint.class);
		
		Response response = r.list(0, 10);
		
		List<Request> list = response.readEntity(new GenericType<List<Request>>(){});
		
		Assert.assertNotNull(list);
		
	}

	@Test
	@InSequence(60)
	public void RequestItemEndpointCreateTest() throws Exception {
		
		Request request = requestDao.current();
		
		assertNotNull(request);
		
		UriBuilder context = UriBuilder.fromUri(url);
		
		ClientRequest client = new ClientRequest(context.path("/{id}/item").build(request.getId()).toString());
		client.accept(MediaType.APPLICATION_JSON);
		client.header("Content-Type", MediaType.APPLICATION_JSON);
		
		Material material = new Material();
		
		material.setLamination("FZ");
		material.setTreatment("RT");
		material.setThickness(BigDecimal.valueOf(2.0));
		material.setWidth(BigDecimal.valueOf(200.0));
		material.setLength(BigDecimal.valueOf(12.3));
		material.setTax(BigDecimal.valueOf(4.5));
		
		RequestItem item = new RequestItem();
		
		item.setArrival(new Date());
		item.setWeight(BigDecimal.valueOf(1234.0));
		item.setMaterial(material);
		
		client.body(MediaType.APPLICATION_JSON, item);
		
		ClientResponse<RequestItem> response = client.post(RequestItem.class);
		
		assertEquals(201, response.getStatus());
		
		RequestItem entity = response.getEntity();
		
		// Be sure that the fields deserialize are correct
		assertNotNull(entity);
		assertNotNull(entity.getId());
		assertEquals(item.getArrival(), entity.getArrival());
		assertEquals(item.getWeight(), entity.getWeight());
		assertNotNull(entity.getMaterial());
		assertNotNull(entity.getMaterial().getId());
		assertEquals(entity.getMaterial().getLamination(), material.getLamination());
		assertEquals(entity.getMaterial().getTreatment(), material.getTreatment());
		assertEquals(entity.getMaterial().getThickness(), material.getThickness());
		assertEquals(entity.getMaterial().getWidth(), material.getWidth());
		assertEquals(entity.getMaterial().getLength(), material.getLength());
		assertEquals(entity.getMaterial().getTax(), material.getTax());
		
	}

	@Test
	@InSequence(61)
	public void RequestItemEndpointFindByIdTest() throws Exception {
		
		Request request = requestDao.current();
		
		assertNotNull(request);
		
		RequestItem item = null;
		
		List<RequestItem> list = requestItemDao.list(request.getId(), 0, 10);
		
		if (list.isEmpty()) {
			Material material = new Material();
			
			material.setLamination("FQ");
			material.setTreatment("TO");
			material.setThickness(BigDecimal.valueOf(2.0));
			material.setWidth(BigDecimal.valueOf(200.0));
			material.setLength(BigDecimal.valueOf(12.3));
			material.setTax(BigDecimal.valueOf(4.5));
			
			Material m = materialDao.find(material);
			
			if (m != null) {
				material = m;
			}
			
			item = new RequestItem();
			
			item.setArrival(new Date());
			item.setWeight(BigDecimal.valueOf(1234.0));
			item.setMaterial(material);
			item.setRequest(request);
			
			requestItemDao.create(item);
		} else {
			item = list.get(0);
		}
		
		assertNotNull(item);
		
		UriBuilder context = UriBuilder.fromUri(url);
		
		ClientRequest client = new ClientRequest(context.path("/{id}/item/{item}").build(request.getId(), item.getId()).toString());
		client.accept(MediaType.APPLICATION_JSON);
		client.header("Content-Type", MediaType.APPLICATION_JSON);

		ClientResponse<RequestItem> response = client.get(RequestItem.class);

		assertEquals(200, response.getStatus());

		RequestItem entity = response.getEntity();
		Assert.assertNotNull(entity);
		
	}
	
	@Test
	@InSequence(62)
	public void RequestItemEndpointUpdateTest() throws Exception {

		Request request = requestDao.current();
		
		assertNotNull(request);
		
		RequestItem item = null;
		
		List<RequestItem> list = requestItemDao.list(request.getId(), 0, 10);

		Material material = new Material();

		if (list.isEmpty()) {
			material = new Material();
			
			material.setLamination("WQ");
			material.setTreatment("GU");
			material.setThickness(BigDecimal.valueOf(2.0));
			material.setWidth(BigDecimal.valueOf(200.0));
			material.setLength(BigDecimal.valueOf(12.3));
			material.setTax(BigDecimal.valueOf(4.5));
			
			
			item = new RequestItem();
			
			item.setArrival(new Date());
			item.setWeight(BigDecimal.valueOf(1234.0));
			item.setMaterial(material);
			item.setRequest(request);
			
			requestItemDao.create(item);
		} else {
			item = list.get(0);
		}
		
		assertNotNull(item);
		
		material.setLamination("FQ");
		material.setTreatment("PZ");
		material.setThickness(BigDecimal.valueOf(2.0));
		material.setWidth(BigDecimal.valueOf(200.0));
		material.setLength(BigDecimal.valueOf(12.3));
		material.setTax(BigDecimal.valueOf(4.5));
		
		item.setArrival(new Date());
		item.setWeight(BigDecimal.valueOf(8899.0));
		item.setMaterial(material);

		UriBuilder context = UriBuilder.fromUri(url);
		
		ClientRequest client = new ClientRequest(context.path("/{id}/item/{item}").build(request.getId(), item.getId()).toString());
		client.accept(MediaType.APPLICATION_JSON);
		client.header("Content-Type", MediaType.APPLICATION_JSON);
		
		//ObjectMapper mapper = new ObjectMapper();
		
		//mapper.registerModule(new Hibernate4Module());

		//client.body(MediaType.APPLICATION_JSON, mapper.writeValueAsString(item));
		client.body(MediaType.APPLICATION_JSON, item);
		
		ClientResponse<RequestItem> response = client.put(RequestItem.class);
		
		assertEquals(200, response.getStatus());
		
		RequestItem entity = response.getEntity();

		assertNotNull(entity);
		
		// be sure that the fields deserialize are correct
		assertNotNull(entity.getId());
		assertEquals(item.getArrival(), entity.getArrival());
		assertEquals(item.getWeight(), entity.getWeight());
		assertNotNull(entity.getMaterial());
		assertNotNull(entity.getMaterial().getId());
		assertEquals(entity.getMaterial().getLamination(), material.getLamination());
		assertEquals(entity.getMaterial().getTreatment(), material.getTreatment());
		assertEquals(entity.getMaterial().getThickness(), material.getThickness());
		assertEquals(entity.getMaterial().getWidth(), material.getWidth());
		assertEquals(entity.getMaterial().getLength(), material.getLength());
		assertEquals(entity.getMaterial().getTax(), material.getTax());
		
		
	}
	
	@Test
	@InSequence(63)
	public void RequestItemEndpointListTest() throws Exception {
		
		UriBuilder context = UriBuilder.fromUri(url);
		
		Request request = requestDao.current();
		
		if (request == null) {
			request = new Request();
			
			request.setCreated(new Date());
			request.setCreator("ItemEndpointListTest");
			request.setSent(null);
			
			requestDao.create(request);
		}
		
		assertNotNull(request.getId());
		
		Material material = new Material();
		
		material.setLamination("QQ");
		material.setTreatment("PQ");
		material.setThickness(BigDecimal.valueOf(2.0));
		material.setWidth(BigDecimal.valueOf(200.0));
		material.setLength(BigDecimal.valueOf(0));
		material.setTax(BigDecimal.valueOf(4.5));
		
		RequestItem entity = new RequestItem();
		
		entity.setRequest(request);
		entity.setMaterial(material);
		entity.setArrival(new Date());
		entity.setWeight(BigDecimal.valueOf(1234.0));

		requestItemDao.create(entity);

		ClientRequest client = new ClientRequest(context.path("/{id}/item").build(request.getId()).toString());
		client.accept(MediaType.APPLICATION_JSON);
		client.header("Content-Type", MediaType.APPLICATION_JSON);
		
		ClientResponse<List<RequestItem>> response = client.get(new GenericType<List<RequestItem>>(){});
		
		assertEquals(200, response.getStatus());

		List<RequestItem> list = response.getEntity();
		
		Assert.assertNotNull(list);
		
		assertFalse(list.isEmpty());
		
	}
	
	@Test
	@InSequence(64)
	public void RequestItemEndpointDeleteByIdTest() throws Exception {
		
		RequestItem item = requestItemDao.list(requestDao.current().getId(), 0, 10).get(0);
		
		assertNotNull(item);
		
		UriBuilder context = UriBuilder.fromUri(url);
		
		ClientRequest client = new ClientRequest(context.path("/{id}/item/{item}").build(requestDao.current().getId(), item.getId()).toString());
		client.accept(MediaType.APPLICATION_JSON);
		client.header("Content-Type", MediaType.APPLICATION_JSON);

		ClientResponse<RequestItem> response = client.delete(RequestItem.class);

		assertEquals(204, response.getStatus());

		RequestItem entity = requestItemDao.find(item.getId());
		assertNull(entity);
		
	}
	
	@Test
	@InSequence(70)
	public void RequestEndpointDeleteByIdTest() throws Exception {
		
		Request request = requestDao.current();
		
		assertNotNull(request);
		
		UriBuilder context = UriBuilder.fromUri(url);
		
		ClientRequest client = new ClientRequest(context.path("/{id}").build(request.getId()).toString());
		client.accept(MediaType.APPLICATION_JSON);
		client.header("Content-Type", MediaType.APPLICATION_JSON);

		ClientResponse<Request> response = client.delete(Request.class);

		assertEquals(204, response.getStatus());

		Request entity = requestDao.find(request.getId());
		assertNull(entity);
		
	}	
	
	@Test
	@InSequence(80)
	public void RequestEndpointReportTest() throws Exception {
		
		Request request = requestDao.current();
		
		assertNotNull(request.getId());
		
		Material material = new Material();
		
		material.setLamination("QQ");
		material.setTreatment("PQ");
		material.setThickness(BigDecimal.valueOf(2.0));
		material.setWidth(BigDecimal.valueOf(200.0));
		material.setLength(BigDecimal.valueOf(0));
		material.setTax(BigDecimal.valueOf(4.5));
		
		RequestItem entity = new RequestItem();
		
		entity.setRequest(request);
		entity.setMaterial(material);
		entity.setArrival(new Date());
		entity.setWeight(BigDecimal.valueOf(1234.0));

		requestItemDao.create(entity);
		
		assertNotNull(request);
		
		UriBuilder context = UriBuilder.fromUri(url);
		
		ClientRequest client = new ClientRequest(context.path("/{id}/report").build(request.getId()).toString());
		client.accept("application/pdf");

		Response response = client.get();

		assertEquals(200, response.getStatus());

	}	
	
	@Test
	public void ImportBOM() throws Exception {
		String json = "{\"number\":72510,\"customer\":\"SOCIEDADE BRAS. DE DEF. DA TRAD. FAMILIA PROPRIED.\",\"representative\":\"SUPORTE COMÉRCIO E REPRESENTAÇÃO LTDA\",\"created\":\"2014-09-23T00:00:00\",\"delivery\":\"2014-11-27T00:00:00\",\"quotation\":\"00087776\",\"comment\":\" \",\"finish\":\"PINTURA A PÓ\",\"project\":72510,\"item\":[{\"item\":1,\"description\":\"ÁREA I COMPONENTES DE PORTA-PALETES   MONTANTES  10 montantes para porta-paletes medindo 1000 x 3360 mm. Colunas no padrão Altamira 80 mm.   Atenção: Verificar no item 'COR DA PINTURA' os acessórios que serão galvanizados e sem pintura.\",\"product\":[{\"code\":\"PPLCOL00080180000000\",\"color\":\"CZ-PAD\",\"description\":\"COLUNA NORMAL 1,80 3360MM\",\"quantity\":20.000,\"width\":0.000,\"height\":0.000,\"length\":3360.000,\"weight\":233.495},{\"code\":\"PPLDIAGALCP155000000\",\"color\":\"S/ COR\",\"description\":\"DIAGONAL GALV. esp1,55 mm C/ PARAF 1313MM\",\"quantity\":30.000,\"width\":0.000,\"height\":0.000,\"length\":1313.000,\"weight\":42.923},{\"code\":\"PPLSAP01CHU000000000\",\"color\":\"GALV-E\",\"description\":\"SAPATA PPL NORMAL COM 01 CHUMBADOR\",\"quantity\":20.000,\"width\":0.000,\"height\":0.000,\"length\":0.000,\"weight\":17.206},{\"code\":\"PPLTRAGALCP155000000\",\"color\":\"S/ COR\",\"description\":\"TRAVESSA GALV esp 1,55 mm C/PARAF 905MM\",\"quantity\":40.000,\"width\":0.000,\"height\":0.000,\"length\":905.000,\"weight\":40.351}]},{\"item\":5,\"description\":\"LONGARINAS  56 pares de longarinas medindo 1500 mm aptos a suportar uma carga uniformemente distribuida de 1000 kgf cada. OBS: Garras para encaixe em colunas padrão Altamira 80 mm.  Atenção: Verificar no item 'COR DA PINTURA' os acessórios que serão galvanizados e sem pintura.\",\"product\":[{\"code\":\"PPLLOZCJ087000140000\",\"color\":\"CZ-PAD\",\"description\":\"LONGARINA Z87 CH14 1500MM\",\"quantity\":112.000,\"width\":0.000,\"height\":0.000,\"length\":1500.000,\"weight\":636.112}]},{\"item\":12,\"description\":\"ÁREA II COMPONENTES DE PORTA-PALETES  MONTANTES  4 montantes para porta-paletes medindo 1000 x 960 mm. Colunas no padrão Altamira 80 mm.  Atenção: Verificar no item 'COR DA PINTURA' os acessórios que serão galvanizados e sem pintura.\",\"product\":[{\"code\":\"PPLCOL00080180000000\",\"color\":\"CZ-PAD\",\"description\":\"COLUNA NORMAL 1,80 960MM\",\"quantity\":8.000,\"width\":0.000,\"height\":0.000,\"length\":960.000,\"weight\":26.685},{\"code\":\"PPLDIAGALCP155000000\",\"color\":\"S/ COR\",\"description\":\"DIAGONAL GALV. esp1,55 mm C/ PARAF 1083MM\",\"quantity\":4.000,\"width\":0.000,\"height\":0.000,\"length\":1083.000,\"weight\":4.772},{\"code\":\"PPLSAP01CHU000000000\",\"color\":\"GALV-E\",\"description\":\"SAPATA PPL NORMAL COM 01 CHUMBADOR\",\"quantity\":8.000,\"width\":0.000,\"height\":0.000,\"length\":0.000,\"weight\":6.882},{\"code\":\"PPLTRAGALCP155000000\",\"color\":\"S/ COR\",\"description\":\"TRAVESSA GALV esp 1,55 mm C/PARAF 905MM\",\"quantity\":8.000,\"width\":0.000,\"height\":0.000,\"length\":905.000,\"weight\":8.070}]},{\"item\":16,\"description\":\"LONGARINAS  3 pares de longarinas medindo 2400 mm aptos a suportar uma carga uniformemente distribuida de 500 kgf cada. OBS: Garras para encaixe em colunas padrão Altamira 80 mm. Acompanham 3 transversinas por par de longarinas.  Atenção: Verificar no item 'COR DA PINTURA' os acessórios que serão galvanizados e sem pintura.\",\"product\":[{\"code\":\"PPLLOZCJ087000140000\",\"color\":\"CZ-PAD\",\"description\":\"LONGARINA Z87 CH14 2400MM\",\"quantity\":6.000,\"width\":0.000,\"height\":0.000,\"length\":2400.000,\"weight\":51.408},{\"code\":\"PPLSUPPARAFTRE000000\",\"color\":\"GALV-E\",\"description\":\"SUPORTE TRANSV. ENCAIXE PARAFUSADA\",\"quantity\":18.000,\"width\":0.000,\"height\":0.000,\"length\":0.000,\"weight\":8.223},{\"code\":\"PPLTREPARAF000000000\",\"color\":\"CZ-PAD\",\"description\":\"TRANSV ENCAIXE PARAF. 75 MM MED 890 MM\",\"quantity\":9.000,\"width\":890.000,\"height\":0.000,\"length\":0.000,\"weight\":25.517}]},{\"item\":20,\"description\":\"MONTANTES ( IMPRESSORAS E DUPLICADORES )  3 montantes para porta-paletes, sendo: 2 montantes 420 x 2000 mm com extensão tripla medindo 420 x 800 mm e 1 montante medindo 420 x 2000 mm com extensão tripla medindo 420 x 640 mm. Colunas padrão Altamira 80 mm.  Atenção: Verificar no item 'COR DA PINTURA' os acessórios que serão galvanizados e sem pintura.\",\"product\":[{\"code\":\"PPLCOL00080180000000\",\"color\":\"CZ-PAD\",\"description\":\"COLUNA NORMAL 1,80 2000MM\",\"quantity\":6.000,\"width\":0.000,\"height\":0.000,\"length\":2000.000,\"weight\":41.695},{\"code\":\"PPLCOL00080180000000\",\"color\":\"CZ-PAD\",\"description\":\"COLUNA NORMAL 1,80 640MM\",\"quantity\":1.000,\"width\":0.000,\"height\":0.000,\"length\":640.000,\"weight\":2.224},{\"code\":\"PPLCOL00080180000000\",\"color\":\"CZ-PAD\",\"description\":\"COLUNA NORMAL 1,80 800MM\",\"quantity\":2.000,\"width\":0.000,\"height\":0.000,\"length\":800.000,\"weight\":5.559},{\"code\":\"PPLCOL00080200000160\",\"color\":\"CZ-PAD\",\"description\":\"COLUNINHA PPL CH14 C/ PARAF. 160MM\",\"quantity\":7.000,\"width\":0.000,\"height\":0.000,\"length\":160.000,\"weight\":4.568},{\"code\":\"PPLDIAGALCP155000000\",\"color\":\"S/ COR\",\"description\":\"DIAGONAL GALV. esp1,55 mm C/ PARAF 605MM\",\"quantity\":11.000,\"width\":0.000,\"height\":0.000,\"length\":605.000,\"weight\":7.684},{\"code\":\"PPLSAP01CHU000000000\",\"color\":\"GALV-E\",\"description\":\"SAPATA PPL NORMAL COM 01 CHUMBADOR\",\"quantity\":9.000,\"width\":0.000,\"height\":0.000,\"length\":0.000,\"weight\":7.743},{\"code\":\"PPLTRAGALCP155000000\",\"color\":\"S/ COR\",\"description\":\"TRAVESSA GALV esp 1,55 mm C/PARAF 325MM\",\"quantity\":19.000,\"width\":0.000,\"height\":0.000,\"length\":325.000,\"weight\":7.770}]}]}";
		
		UriBuilder context = UriBuilder.fromUri(url);
		
		ClientRequest client = new ClientRequest(context.path("/sales/order").build().toString());
		client.accept(MediaType.APPLICATION_JSON);
		client.header("Content-Type", MediaType.APPLICATION_JSON);
		
		client.body(MediaType.APPLICATION_JSON, json);
		
		ClientResponse<Order> response = client.post(Order.class);
		
		assertEquals(201, response.getStatus());
	}
}
