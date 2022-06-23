package io.hotely.hotel.entities;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.redis.core.RedisHash;

import lombok.NonNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RedisHash(value = "Booking", timeToLive = 300000)
@Table(value = "bookings")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Booking {

  /* ID Field is not required for the runtime, this is handled Controller separately,
   * So that the ID Field does not have to be specified by the consumer itself. */
  @Id
  @PrimaryKey
  @PrimaryKeyColumn(name = "id", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
  @CassandraType(type = CassandraType.Name.UUID)
  @JsonProperty("id")
  private UUID id;

  @NonNull
  @Column(value = "customer_id")
  @CassandraType(type = CassandraType.Name.UUID)
  @JsonProperty("customer_id")
  private UUID customerId;

  @NonNull
  @Column("hotel_id")
  @CassandraType(type = CassandraType.Name.INT)
  @JsonProperty("hotel_id")
  private Long hotelId;

  @NonNull
  @Column(value = "room_id")
  @CassandraType(type = CassandraType.Name.INT)
  @JsonProperty("room_id")
  private Long roomId;

  @NonNull
  @Column(value = "booking_status")
  @CassandraType(type = CassandraType.Name.TEXT)
  @JsonProperty("booking_status")
  private BookingStatus bookingStatus;

  @NonNull
  @Column(value = "start_date")
  @CassandraType(type = CassandraType.Name.DATE)
  @JsonProperty("start_date")
  private LocalDate startDate;

  @NonNull
  @Column(value = "end_date")
  @CassandraType(type = CassandraType.Name.DATE)
  @JsonProperty("end_date")
  private LocalDate endDate;

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

    if (obj instanceof Booking) {
      if (((Booking) obj).getId().equals(getId())) {
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
    builder.append(" Room ID: " + roomId + newline);
    builder.append(" Booking Status: " + bookingStatus + newline);
    builder.append(" Start Date: " + startDate + newline);
    builder.append(" End Date: " + endDate + newline);
    builder.append("}");

    return builder.toString();
  }
}
