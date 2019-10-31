package com.jm.jobseekerplatform.dao.interfaces;

import com.jm.jobseekerplatform.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TagDao extends JpaRepository<Tag, Long> {

    Tag findByName(String name);

    public List<Tag> getBySearchParam(String param);

    List<Tag> findAllByVerified(boolean verified);

    List<Tag> findAllByName(Set<String> tagsName);

}
