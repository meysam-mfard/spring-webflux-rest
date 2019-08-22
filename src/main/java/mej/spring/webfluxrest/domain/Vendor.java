package mej.spring.webfluxrest.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Vendor {

    @Id
    private String id;

    private String firstName;
    private String lastName;

    public Vendor(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
