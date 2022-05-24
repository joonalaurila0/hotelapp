package io.hotely.customer.entities;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Table(value = "invoices")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Invoice {

  @PrimaryKey
  @PrimaryKeyColumn(name = "id", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
  @CassandraType(type = CassandraType.Name.UUID)
  @JsonProperty("id")
  private UUID id;

  @NonNull
  @Column(value = "booking_id")
  @CassandraType(type = CassandraType.Name.UUID)
  @JsonProperty("booking_id")
  private UUID booking_id;

  @NonNull
  @Column(value = "customer_id")
  @CassandraType(type = CassandraType.Name.UUID)
  @JsonProperty("customer_id")
  private UUID customer_id;

  @NonNull
  @CassandraType(type = CassandraType.Name.UUID)
  @Column(value = "total")
  private Float total;

  @NonNull
  @Column(value = "issued")
  @CassandraType(type = CassandraType.Name.TIMESTAMP)
  private Timestamp issued;

  @NonNull
  @Column(value = "paid")
  @CassandraType(type = CassandraType.Name.BOOLEAN)
  private Boolean paid;

  @NonNull
  @Column(value = "payment_date")
  @CassandraType(type = CassandraType.Name.DATE)
  private Date paymentDate;

  @NonNull
  @Column(value = "cancelled")
  @CassandraType(type = CassandraType.Name.BOOLEAN)
  private Boolean cancelled;

  @Override
  public int hashCode() {
    return java.util.Objects.hashCode(id);
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (obj instanceof Invoice) {
      if (((Invoice) obj).getId().equals(getId())) {
          return true;
      }
    }

    return false;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    String newline = System.getProperty("line.separator");

    builder.append(this.getClass().getName() + " {" + newline);
    builder.append(" Id: " + id + newline);
    builder.append(" Total: " + total + newline);
    builder.append(" Paid: " + paid + newline);
    builder.append(" Payment Date: " + paymentDate + newline);
    builder.append(" Cancelled: " + cancelled + newline);
    builder.append("}");

    return builder.toString();
  }
}
