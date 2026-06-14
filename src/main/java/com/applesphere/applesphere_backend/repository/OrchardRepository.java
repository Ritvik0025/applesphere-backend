package com.applesphere.applesphere_backend.repository;

import com.applesphere.applesphere_backend.model.Orchard;
import com.applesphere.applesphere_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrchardRepository extends JpaRepository<Orchard, Long> {
    List<Orchard> findByFarmer(User farmer);
}