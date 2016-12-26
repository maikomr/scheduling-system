package org.mk.scheduling.domain;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maiko on 22/12/2016.
 */
@Data
@Entity
@ToString(exclude = "classes")
public class Student extends AuditingEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Length(min = 1, max = 45)
    @Column(length = 45)
    private String firstName;

    @NotBlank
    @Length(min = 1, max = 45)
    @Column(length = 45)
    private String lastName;

    @ManyToMany
    private List<org.mk.scheduling.domain.Class> classes = new ArrayList<>();

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
