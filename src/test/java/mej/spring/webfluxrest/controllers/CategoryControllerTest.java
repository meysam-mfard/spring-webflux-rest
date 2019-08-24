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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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
        given(categoryRepository.findAll())
                .willReturn(Flux.just("cat1", "cat2").map(Category::new));
        webTestClient
                .get()
                .uri("/api/v1/categories/")
                .exchange()
                .expectBodyList(Category.class).hasSize(2);
    }

    @Test
    public void getCategory() {
        given(categoryRepository.findById(CATEGORY_ID))
                .willReturn(Mono.just(new Category("Cat")));

        webTestClient
                .get()
                .uri("/api/v1/category/" + CATEGORY_ID)
                .exchange()
                .expectBody(Category.class);
    }

    @Test
    public void postCategory() {
        given(categoryRepository.saveAll(any(Publisher.class)))
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
        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(new Category()));

        Mono<Category> catToUpdate = Mono.just(new Category("some cat"));

        webTestClient.put()
                .uri("/api/v1/category/" + CATEGORY_ID)
                .body(catToUpdate, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void patchCategory_noChange() {
        final Category UNCHANGED_CAT = new Category("some description");

        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(UNCHANGED_CAT));

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(new Category()));

        Mono<Category> catToUpdateMono = Mono.just(UNCHANGED_CAT);

        webTestClient.patch()
                .uri("/api/v1/category/dsklfhadslfhadsflk")
                .body(catToUpdateMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository, never()).save(any());
    }

    @Test
    public void patchCategory_withChange() {
        final Category CAT1 = new Category("some description");
        final Category CAT2 = new Category("another description");

        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(CAT1));

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(new Category()));

        Mono<Category> catToUpdateMono = Mono.just(CAT2);

        webTestClient.patch()
                .uri("/api/v1/category/dsklfhadslfhadsflk")
                .body(catToUpdateMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository).save(any());
    }
}