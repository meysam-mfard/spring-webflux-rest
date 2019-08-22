package mej.spring.webfluxrest.init;

import lombok.extern.log4j.Log4j2;
import mej.spring.webfluxrest.domain.Category;
import mej.spring.webfluxrest.domain.Vendor;
import mej.spring.webfluxrest.repositories.CategoryRepository;
import mej.spring.webfluxrest.repositories.VendorRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.LinkedList;
import java.util.List;

@Component
@Log4j2
public class InitData {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public InitData(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initialize() {

        //Categories
        Flux<Category> savedCategory = Flux
                .just("Fruits", "Bread", "Nuts")
                .map(Category::new)
                .flatMap(this.categoryRepository::save);

        categoryRepository
                .deleteAll()
                .thenMany(savedCategory)
                .thenMany(this.categoryRepository.findAll())
                .subscribe(category -> log.info("#### saved category: " + category));

        //log.info("#### Loaded Categories: " + categoryRepository.count().block());



        //Vendors
        LinkedList<Vendor> vendorList = new LinkedList<>(List.of(
                Vendor.builder().firstName("FN1").lastName("LN1").build()
                , Vendor.builder().firstName("FN2").lastName("LN2").build()
                , Vendor.builder().firstName("FN3").lastName("LN3").build()
                , Vendor.builder().firstName("FN4").lastName("LN4").build()));

        vendorRepository
                .deleteAll()
                .thenMany(vendorRepository.saveAll(vendorList))
                .thenMany(vendorRepository.findAll())
                .subscribe(vendor -> log.info("#### saved vendor: " + vendor));

        //log.info("#### Loaded Vendors: " + vendorRepository.count().block());
    }
}
