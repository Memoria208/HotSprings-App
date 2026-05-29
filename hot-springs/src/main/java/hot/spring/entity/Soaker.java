package hot.spring.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Soaker {
    
    /*add fields that are in the relationship diagram
     * first tell JPA where primary key column is for each primary key field
     * */
    
    //primary key
    @Id
    //tells JPA how the primary key is managed
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long soakerId;
    
    private String soakerName;
    
    /*unique key so that no emails can be contributed to more than one
     * soaker name
     * */
    @Column (unique = true)
    private String soakerEmail;
    
    /*relationships*/
    
    /*to prevent recurion problems*/
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    //joined using the java field name "Soaker"
    @OneToMany (mappedBy = "soaker", cascade = CascadeType.ALL)
    private Set<HotSpring> hotSprings = new HashSet<>();
}



