package com.form.backend.repositorio;

import com.form.backend.model.Pack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackRepository extends JpaRepository<Pack, Long> {
    List<Pack> findByActivoTrue();
}
