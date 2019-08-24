package mej.spring.webfluxrest.controllers;

import mej.spring.webfluxrest.domain.Category;
import mej.spring.webfluxrest.repositories.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

public class CategoryControllerTest {

    private static final String CATEGORY_ID = "dummyId";
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
        BDDMockito.given(categoryRepository.findById(CATEGORY_ID))
                .willReturn(Mono.just(new Category("Cat")));

        webTestClient
                .get()
                .uri("/api/v1/category/" + CATEGORY_ID)
                .exchange()
                .expectBody(Category.class);
    }

    @Test
    public void postCategory() {
        BDDMockito.given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(new Category("cat")));

        Mono<Category> catToSave = Mono.just(new Category("some cat"));

        webTestClient.post()
                .uri("/api/v1/categories")
                .body(catToSave, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }


    @Test
    public void putCategory() {
        BDDMockito.given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(new Category()));

        Mono<Category> catToUpdate = Mono.just(new Category("some cat"));

        webTestClient.put()
                .uri("/api/v1/category/" + CATEGORY_ID)
                .body(catToUpdate, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }
}