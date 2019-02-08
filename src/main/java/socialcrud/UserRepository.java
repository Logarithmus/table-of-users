package socialcrud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;

import socialcrud.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
public interface UserRepository extends JpaRepository<User, String> {

	@Transactional
  	@Modifying
	@Query("UPDATE User u SET u.isBanned = :isBanned WHERE u.id = :id")
	void updateIsBanned(String id, boolean isBanned);
}