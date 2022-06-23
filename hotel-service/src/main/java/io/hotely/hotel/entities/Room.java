package io.hotely.hotel.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.NonNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RedisHash(value = "Room", timeToLive = 300000)
@Table(value = "rooms")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Room {

  public enum RoomStatus {
    Available,
    Reserved,
    Occupied,
    NotAvailable,
    BeingServiced,
    Other
  };

  @Id
  @PrimaryKey
  @PrimaryKeyColumn(name = "id", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
  @CassandraType(type = CassandraType.Name.INT)
  @JsonProperty("id")
  private Integer id;

  @NonNull
  @Column("hotel_id")
  @CassandraType(type = CassandraType.Name.INT)
  @JsonProperty("hotel_id")
  private Long hotel_id;

  @NonNull
  @Column("availability")
  @CassandraType(type = CassandraType.Name.BOOLEAN)
  @JsonProperty("availability")
  private Boolean availability;

  @NonNull
  @Column("booking_price")
  @CassandraType(type = CassandraType.Name.FLOAT)
  @JsonProperty("booking_price")
  private Float booking_price;

  @NonNull
  @Column("capacity")
  @CassandraType(type = CassandraType.Name.INT)
  @JsonProperty("capacity")
  private Integer capacity;

  @NonNull
  @Column("room_area")
  @CassandraType(type = CassandraType.Name.INT)
  @JsonProperty("room_area")
  private Integer room_area;

  @NonNull
  @Column("room_status")
  @CassandraType(type = CassandraType.Name.TEXT)
  @JsonProperty("room_status")
  private RoomStatus room_status;

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

    if (obj instanceof Room) {
      if (((Room) obj).getId().equals(getId())) {
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
    builder.append(" Hotel ID: " + hotel_id + newline);
    builder.append(" room_area: " + room_area + newline);
    builder.append(" room_status: " + room_status + newline);
    builder.append(" capacity: " + capacity + newline);
    builder.append(" booking_price: " + booking_price + newline);
    builder.append(" availability: " + availability + newline);
    builder.append("}");

    return builder.toString();
  }

}
