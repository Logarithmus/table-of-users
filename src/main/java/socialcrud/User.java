package socialcrud;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users")
public class User {
	@Id
	@Column(columnDefinition = "VARCHAR(64)")
    private String id;
    private String provider; // Facebook, Google, GitHub, etc.
	private String name;
    
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp firstLogin;
    
    private Timestamp lastLogin;
    private Boolean isBanned;
}