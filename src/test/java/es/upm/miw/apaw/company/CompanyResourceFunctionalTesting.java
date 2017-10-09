package es.upm.miw.apaw.company;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import es.upm.miw.apaw.company.api.daos.DaoFactory;
import es.upm.miw.apaw.company.api.daos.memory.DaoMemoryFactory;
import es.upm.miw.apaw.company.api.resources.CompanyResource;
import es.upm.miw.apaw.company.http.HttpClientService;
import es.upm.miw.apaw.company.http.HttpException;
import es.upm.miw.apaw.company.http.HttpMethod;
import es.upm.miw.apaw.company.http.HttpRequest;
import es.upm.miw.apaw.company.http.HttpRequestBuilder;

public class CompanyResourceFunctionalTesting {

    @Before
    public void before() {
        DaoFactory.setFactory(new DaoMemoryFactory());
    }
    
    private void createCompany() {
        HttpRequest request = new HttpRequestBuilder().method(HttpMethod.POST).path(CompanyResource.COMPANIES).body("company1").build();
        new HttpClientService().httpRequest(request);
    }

    @Test
    public void testCreateCompany() {
        this.createCompany();
    }

    @Test(expected = HttpException.class)
    public void testCreateCompanyNameEmpty() {
        HttpRequest request = new HttpRequestBuilder().method(HttpMethod.POST).path(CompanyResource.COMPANIES).body("").build();
        new HttpClientService().httpRequest(request);
    }

    @Test(expected = HttpException.class)
    public void testCreateWithoutCompanyName() {
        HttpRequest request = new HttpRequestBuilder().method(HttpMethod.POST).path(CompanyResource.COMPANIES).build();
        new HttpClientService().httpRequest(request);
    }

    @Test
    public void testReadCompany() {
        this.createCompany();
        HttpRequest request = new HttpRequestBuilder().method(HttpMethod.GET).path(CompanyResource.COMPANIES).path(CompanyResource.ID)
                .expandPath("1").build();
        assertEquals("{\"id\":1,\"name\":\"company1\"}", new HttpClientService().httpRequest(request).getBody());
    }
    
    @Test(expected = HttpException.class)
    public void testReadCompanyCompanyIdNotFound() {
        HttpRequest request = new HttpRequestBuilder().method(HttpMethod.GET).path(CompanyResource.COMPANIES).path(CompanyResource.ID)
                .expandPath("2").build();
        new HttpClientService().httpRequest(request);
    }
    
    @Test
    public void testCompanyList() {
        this.createCompany();
        HttpRequest request = new HttpRequestBuilder().method(HttpMethod.GET).path(CompanyResource.COMPANIES).build();
        assertEquals("[{\"id\":1,\"name\":\"company1\"}]", new HttpClientService().httpRequest(request).getBody());
    }
    
    @Test
    public void testDeleteCompany() {
        this.createCompany();
        HttpRequest request = new HttpRequestBuilder().method(HttpMethod.DELETE).path(CompanyResource.COMPANIES).path(CompanyResource.ID)
                .expandPath("1").build();
        new HttpClientService().httpRequest(request);
    }
    
    @Test(expected = HttpException.class)
    public void testDeleteCompanyCompanyIdNotFoundException() {
        HttpRequest request = new HttpRequestBuilder().method(HttpMethod.DELETE).path(CompanyResource.COMPANIES).path(CompanyResource.ID)
                .expandPath("2").build();
        new HttpClientService().httpRequest(request);
    }

    

}
