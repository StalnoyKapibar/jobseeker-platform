package com.jm.jobseekerplatform.dao.interfaces;

import com.jm.jobseekerplatform.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferDao extends JpaRepository<Offer, Long> {

}
