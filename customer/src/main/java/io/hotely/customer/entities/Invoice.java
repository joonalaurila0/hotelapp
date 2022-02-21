package io.hotely.customer.entities;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/* specify @JoinColumn by the owner entity
 * that stores the id value and has a foreign key
 * to the Customer entity. */

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Invoice {

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
  @Type(type = "uuid-char")
  private UUID id;

  @NonNull private Double invoiceAmount;
  @NonNull private Timestamp issued;
  @NonNull private Boolean paid;
  @NonNull private Boolean canceled;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customerId")
  private Customer customer;


  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    String Newline = System.getProperty("line.separator");

    result.append(this.getClass().getName() + " Object {" + Newline);
    result.append(" Invoice Amount: " + invoiceAmount + Newline);
    result.append(" Issued: " +  issued + Newline);
    result.append(" Paid: " +  paid + Newline);
    result.append(" Canceled: " +  canceled + Newline);
    result.append(" Customer: " +  customer + Newline);
    result.append("}");

    return result.toString();
  }
}
