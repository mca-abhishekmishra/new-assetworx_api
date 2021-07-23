package com.softtek.assetworx_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.entity.UserAuthorityPrivilege;

@Repository
public interface UserAuthorityPrivilegeRepository extends JpaRepository<UserAuthorityPrivilege, String> {

	Optional<UserAuthorityPrivilege> findByName(String privilegeName);

	UserAuthorityPrivilege findByNameAndPrivilegeIdNotLike(String name, String privilegeId);

}
