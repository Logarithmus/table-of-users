package socialcrud;

import org.springframework.data.jpa.repository.JpaRepository;

import socialcrud.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
public interface UserRepository extends JpaRepository<User, String> {

}