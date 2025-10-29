package com.projetointegrador.comunicavet.repository;

import com.projetointegrador.comunicavet.model.Clinic;
import com.projetointegrador.comunicavet.model.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClinicRepository extends CrudRepository<Clinic, Long> {
    List<Clinic> findByName(String name);
    Optional<Clinic> findByEmail(String email);

    @Query("""
            SELECT c FROM Clinic c
            JOIN ClinicFocus cf ON cf.clinic = c
            WHERE cf.focus.name IN (:focusNames)
            AND c.address.country.name = :country
            AND c.address.state.name = :state
            AND c.address.city.name = :city
            ORDER BY FUNCTION('distance_function', c.address.location.latitude, c.address.location.longitude, :userLat, :userLon)
            """)
    List<Clinic> filterBy(
            @Param("focusNames") List<String> focusNames, @Param("country")String country,
            @Param("state")String state, @Param("city")String city,
            @Param("userLon") double userLon, @Param("userLat") double userLat
    );
}
