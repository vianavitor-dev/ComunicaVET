package com.projetointegrador.comunicavet.repository;

import com.projetointegrador.comunicavet.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {
    List<Address> findByStreet(String street);

    List<Address> findByNeighborhood(String neighborhood);

    List<Address> findByNeighborhoodAndCity(String neighborhood, City city);

    Optional<Address> findByLocation(Location location);

    List<Address> findByCityAndCountry(City city, Country country);

    List<Address> findByCityAndStateAndCountry(City city, State state, Country country);

    @Query(value = "SELECT a FROM Address a JOIN City c JOIN State s JOIN Country cc " +
            "WHERE street = ?1 AND neighborhood = ?2 AND c.name = ?3 AND s.name = ?4 AND cc.name = ?5")
    List<Address> findByAllFields(
            String street, String neighborhood, City city, State state, Country country
    );
}
