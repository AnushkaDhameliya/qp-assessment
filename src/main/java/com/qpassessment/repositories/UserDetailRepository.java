package com.qpassessment.repositories;

import com.qpassessment.entities.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public interface UserDetailRepository extends JpaRepository<UserDetail,Long> {

    UserDetail findByUsername(String name);

}
