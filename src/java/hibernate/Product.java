package hibernate;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product implements Serializable{
    Integer id;
    String title;
    String description;
    Double price;
    Integer qty;
    Date created_at;
    Status status;
    Category category;
    Color color;
    Size size;
}
