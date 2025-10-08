package com.projetointegrador.comunicavet.repository;

import com.projetointegrador.comunicavet.model.*;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {

    Optional<Address> findByCityAndStateAndCountry(City city, State state, Country country);

    @Query(
            value = "SELECT a FROM Address a " +
                    "JOIN a.city c " +
                    "JOIN a.state s " +
                    "JOIN a.country co " +
                    "WHERE a.street = ?1 " +
                    "AND a.neighborhood = ?2 " +
                    "AND c.name = ?3 " +
                    "AND s.name = ?4 " +
                    "AND co.name = ?5"
    )
    Optional<Address> findByAllFields(
            String street,
            String neighborhood,
            String city,
            String state,
            String country
    );

    @Query("SELECT a FROM Address a " +
            "WHERE a.city.name = :city " +
            "AND a.state.name = :state " +
            "AND a.country.name = :country"
    )
    Optional<Address> findByCityAndStateAndCountry(
            @Param("city") String city,
            @Param("state") String state,
            @Param("country") String country);
}
