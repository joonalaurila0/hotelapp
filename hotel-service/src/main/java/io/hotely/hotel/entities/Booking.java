package io.hotely.hotel.entities;

import java.util.Date;
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

@Table(value = "bookings")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Booking {

  public enum BookingStatus {
    Pending,
    Confirmed,
    Canceled,
    Checkedin,
    Checkedout
  };

  @PrimaryKey
  @PrimaryKeyColumn(name = "id", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
  @CassandraType(type = CassandraType.Name.UUID)
  @JsonProperty("id")
  private UUID id;

  @NonNull
  @Column(value = "customer_id")
  @CassandraType(type = CassandraType.Name.UUID)
  @JsonProperty("customer_id")
  private UUID customer_id;

  @Column("hotel_id")
  @CassandraType(type = CassandraType.Name.INT)
  @JsonProperty("hotel_id")
  private Long hotel_id;

  @NonNull
  @Column(value = "room_id")
  @CassandraType(type = CassandraType.Name.INT)
  @JsonProperty("room_id")
  private Long room_id;

  @NonNull
  @Column(value = "booking_status")
  @CassandraType(type = CassandraType.Name.TEXT)
  @JsonProperty("bookingStatus")
  private BookingStatus bookingStatus;

  @NonNull
  @Column(value = "start_date")
  @CassandraType(type = CassandraType.Name.DATE)
  @JsonProperty("startDate")
  private Date startDate;

  @NonNull
  @Column(value = "end_date")
  @CassandraType(type = CassandraType.Name.DATE)
  @JsonProperty("endDate")
  private Date endDate;

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
    builder.append(" Customer ID: " + customer_id + newline);
    builder.append(" Hotel ID: " + hotel_id + newline);
    builder.append(" Room ID: " + room_id + newline);
    builder.append(" Booking Status: " + bookingStatus + newline);
    builder.append(" Start Date: " + startDate + newline);
    builder.append(" End Date: " + endDate + newline);
    builder.append("}");

    return builder.toString();
  }
}
