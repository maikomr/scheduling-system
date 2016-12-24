package org.mk.scheduling.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ToString(exclude = "students")
public class Class extends AuditingEntity {

    @Id
    private String code;

    @NotBlank
    @Length(min = 1, max = 45)
    @Column(length = 45)
    private String title;

    @Length(min = 1, max = 200)
    @Column(length = 200)
    private String description;

    @ManyToMany(mappedBy = "classes")
    private List<Student> students = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
