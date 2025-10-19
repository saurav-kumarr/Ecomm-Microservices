package com.ecommerce.user.model;




import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
//@JsonPropertyOrder({ "id", "firstName", "lastName" })
//@Entity(name = "user")
//@NoArgsConstructor
//@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String firstName;
    private String lastName;

    @Indexed(unique = true)
    private String email;
    private String phone;
    private UserRole role = UserRole.CUSTOMER;

    /*@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id", referencedColumnName = "id")*/
    private Address address;

    //@CreationTimestamp
    @CreatedDate
    private LocalDateTime createdAt;

    //@UpdateTimestamp
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
