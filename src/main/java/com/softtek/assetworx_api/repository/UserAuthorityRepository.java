package com.softtek.assetworx_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.entity.UserAuthority;


@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthority, String> {

	Optional<UserAuthority> findByName(String authorityName);

}
