package hot.spring.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class HotSpring {
    
    //primary key
    @Id
    //tells JPA how the primary key is managed
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long hotSpringId;
    
    private String hotSpringName;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String county;
    private String directions;
    
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    //joined by the table column "soaker_id"
    @JoinColumn(name = "soaker_id", nullable = false)
    private Soaker soaker;
    
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    //if a HotSpring is deleted, don't delete the amenities table, use PERSIST
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "hot_spring_detail",
   		 joinColumns = @JoinColumn (name = "hot_spring_id"),
   		 inverseJoinColumns = @JoinColumn (name = "detail_id"))
    private Set<Detail> details = new HashSet<>();
    
}


