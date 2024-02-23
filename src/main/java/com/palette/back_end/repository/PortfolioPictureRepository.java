package com.palette.back_end.repository;

import com.palette.back_end.entity.PortfolioPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioPictureRepository extends JpaRepository<PortfolioPicture, Long> {
}
