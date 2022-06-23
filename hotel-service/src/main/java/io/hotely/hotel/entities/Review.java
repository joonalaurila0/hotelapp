package io.hotely.hotel.entities;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.NonNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Table(value = "reviews")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Review {

  enum RoomStatus {
    Available,
    Reserved,
    Occupied,
    NotAvailable,
    BeingServiced,
    Other
  }

  @PrimaryKey
  @PrimaryKeyColumn(name = "id", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
  @CassandraType(type = CassandraType.Name.UUID)
  @JsonProperty("id")
  private UUID id;

  @NonNull
  @Column("customer_id")
  @CassandraType(type = CassandraType.Name.UUID)
  @JsonProperty("customer_id")
  private UUID customerId;

  @NonNull
  @Column("hotel_id")
  @CassandraType(type = CassandraType.Name.INT)
  @JsonProperty("hotel_id")
  private Long hotelId;

  @NonNull
  @Column("rating")
  @CassandraType(type = CassandraType.Name.INT)
  @JsonProperty("rating")
  private Integer rating;

  @NonNull
  @Column("description")
  @CassandraType(type = CassandraType.Name.TEXT)
  @JsonProperty("description")
  private String description;

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

    if (obj instanceof Review) {
      if (((Review) obj).getId().equals(getId())) {
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
    builder.append(" Customer ID: " + customerId + newline);
    builder.append(" Hotel ID: " + hotelId + newline);
    builder.append(" rating: " + rating + newline);
    builder.append(" description: " + description + newline);
    builder.append("}");

    return builder.toString();
  }
}
