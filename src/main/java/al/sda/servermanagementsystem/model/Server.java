package al.sda.servermanagementsystem.model;

import al.sda.servermanagementsystem.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "server")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Server {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "ip")
    private String ip;
    @Column(name = "location")
    private String location;
    @Column(name = "company_name")
    private String companyName;
    private Status status;


}
