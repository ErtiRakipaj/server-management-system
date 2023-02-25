package al.sda.servermanagementsystem.model;

import al.sda.servermanagementsystem.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "server")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Server {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "ip")
    private String ip;
    @Column(name = "location")
    private String location;
    @Column(name = "company_name")
    private String companyName;
    @Enumerated(EnumType.STRING)
    private Status status;

    private String owner;

}
