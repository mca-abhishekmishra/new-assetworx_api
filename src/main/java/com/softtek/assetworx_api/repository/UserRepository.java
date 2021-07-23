package com.softtek.assetworx_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.entity.AssetworxUser;

@Repository
public interface UserRepository extends JpaRepository<AssetworxUser, String> {

	AssetworxUser findByUserName(String userName);

	AssetworxUser findByEmail(String email);

	AssetworxUser findFirstByUserNameAndUserIdNotLike(String userName, String userId);

	@Query(value = "select user from AssetworxUser user join fetch user.authorties auth "
			+ " where auth.authorityId = :authId  ")
	List<AssetworxUser> findUsersByRole(@Param("authId") String authId);

//	@Query(value = "select user from AssetworxUser user " + " join fetch user.sharedDashBoards das "
//			+ " join fetch  user.authorties auth " + " where das.id = :dashboardID and auth.authorityId = :authId ")
//	List<AssetworxUser> findUserWithRoleAndSharedDashBoardId(@Param("authId") String authId,
//			@Param("dashboardID") String dashboardId);

	List<AssetworxUser> findByUserNameIn(List<String> toUserNames);

	@Query(value = "select user from AssetworxUser user join fetch user.authorties auth "
			+ " where auth.authorityId in :roleIds  ")
	List<AssetworxUser> findUsersByRoleIn(@Param("roleIds") List<String> roleIds);

	@Query(value = "select user from AssetworxUser user " + " join fetch user.sharedReports repo "
			+ " join fetch  user.authorties auth "
			+ " where repo.id = :reportId and auth.authorityId in :roleIdList ")
	List<AssetworxUser> findUserWithSharedReportIdAndRoleIdIn(@Param("roleIdList") List<String> roleIdList,
			@Param("reportId") String reportId);
	
	@Query(value = "select user from AssetworxUser user " + " join fetch user.sharedDashBoards das "
			+ " join fetch  user.authorties auth "
			+ " where das.id = :dashboardId and auth.authorityId in :roleIdList ")
	List<AssetworxUser> findUserWithSharedDashBoardIdAndRoleIdIn(@Param("roleIdList") List<String> roleIdList,
			@Param("dashboardId") String dashBoardId);

	@Query(value = "select user from AssetworxUser user join fetch user.sharedReports repo "
			+ " where repo.id = :reportId ")
	List<AssetworxUser> findUsersWithSharedReportId(@Param("reportId") String reportId);

	@Query(value = "select user from AssetworxUser user join fetch user.sharedDashBoards das "
			+ " where das.id = :dashboardId ")
	List<AssetworxUser> findUsersWithSharedDashBoardId(@Param("dashboardId") String dashBoardId);

	AssetworxUser findByEmailAndIsActive(String email, boolean isActive);
}
