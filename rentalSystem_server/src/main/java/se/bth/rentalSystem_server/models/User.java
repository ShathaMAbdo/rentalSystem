package se.bth.rentalSystem_server.models;

import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@Entity
//@Table(name = "users", uniqueConstraints = {
//        @UniqueConstraint(columnNames = {
//                "userName"
//        }),
//        @UniqueConstraint(columnNames = {
//                "email"
//        })
//})
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    //@NaturalId
    @NotBlank
    @Email
    private String email;
    private String phon;
    private String city;
    private CustomerStatus status;
    private String payCard;
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
    @NotBlank
    private String passwordConfirm;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;


}
