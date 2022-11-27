package com.nhira.abnrecipeapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Entity(name = "app_user")
@Inheritance
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity{

    private String name;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Recipe> recipes;
}
