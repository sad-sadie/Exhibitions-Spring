package com.my.exhibitions.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "exhibitions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Exhibition {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exhibition_sequence")
    @SequenceGenerator(name = "exhibition_sequence", sequenceName = "exhibition_sequence", allocationSize = 1)
    private long id;

    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 21, message = "Theme should be between 3 and 21 characters")
    private String theme;

    @Column(nullable = false)
    @Size(min = 3, message = "Description should be between 3 and 21 characters")
    private String description;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Date cannot be empty")
    @Future(message = "Date should be in future")
    private Date startDate;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Date cannot be empty")
    @Future(message = "Date should be in future")
    private Date endDate;

    @Column(nullable = false)
    @Min(value = 5, message = "Price should not be less than 5 UAH")
    private double price;


    @ManyToMany
    @JoinTable(
            name = "expositions",
            joinColumns = {@JoinColumn(name = "exhibition_id")},
            inverseJoinColumns = @JoinColumn(name = "hall_id")
    )
    private Set<Hall> halls;

    @ManyToMany
    @JoinTable(
            name = "orders",
            joinColumns = {@JoinColumn(name = "exhibition_id")},
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;

    public void removeHalls() {
        halls = null;
    }

    public void removeUsers() {
        users = null;
    }
}