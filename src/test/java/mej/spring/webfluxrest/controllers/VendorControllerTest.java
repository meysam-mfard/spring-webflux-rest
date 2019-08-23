package mej.spring.webfluxrest.controllers;

import mej.spring.webfluxrest.domain.Vendor;
import mej.spring.webfluxrest.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;

public class VendorControllerTest {

    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController controller;

    @Before
    public void setUp() throws Exception {
        vendorRepository = Mockito.mock(VendorRepository.class);
        controller = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    public void getVendors() {
        BDDMockito.given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("fn1").lastName("ln1").build(),
                        Vendor.builder().firstName("fn2").lastName("ln2").build()));

        webTestClient.get()
                .uri("/api/v1/vendors")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    public void getVendor() {
        BDDMockito.given(vendorRepository.findById("dummyId"))
                .willReturn(Mono.just(Vendor.builder().firstName("fn1").lastName("ln1").build()));

        webTestClient.get()
                .uri("/api/v1/vendors/dummyId")
                .exchange()
                .expectBody(Vendor.class);
    }
}