package com.boeani.expenseTrackerApi.repositories;

import com.boeani.expenseTrackerApi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
