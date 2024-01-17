package com.example.daycarat.domain.gem.repository;

import com.example.daycarat.domain.gem.entity.Gem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GemRepository extends JpaRepository<Gem, Long>, GemRepositoryCustom {
}
