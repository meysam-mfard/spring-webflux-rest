package mej.spring.webfluxrest.controllers;

import mej.spring.webfluxrest.domain.Category;
import mej.spring.webfluxrest.repositories.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CategoryControllerTest {

    CategoryRepository categoryRepository;
    CategoryController categoryController;
    WebTestClient webTestClient;

    @Before
    public void setUp() throws Exception {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    public void getCategories() {
        BDDMockito.given(categoryRepository.findAll())
                .willReturn(Flux.just("cat1", "cat2").map(Category::new));
        webTestClient
                .get()
                .uri("/api/v1/categories/")
                .exchange()
                .expectBodyList(Category.class).hasSize(2);
    }

    @Test
    public void getCategory() {
        BDDMockito.given(categoryRepository.findById("dummyId"))
                .willReturn(Mono.just(new Category("Cat")));

        webTestClient.get()
                .uri("/api/v1/categories/dummyId")
                .exchange()
                .expectBody(Category.class);
    }
}