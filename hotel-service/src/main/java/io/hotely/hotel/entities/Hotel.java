package io.hotely.hotel.entities;

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

// timeToLive = 5 minutes
@RedisHash(value = "Hotel", timeToLive = 300000)
@Table(value = "hotels")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Hotel {

  @Id
  @PrimaryKey
  @PrimaryKeyColumn(name = "id", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
  @CassandraType(type = CassandraType.Name.INT)
  @JsonProperty("id")
  private Integer id;

  @NonNull
  @Column("email")
  @CassandraType(type = CassandraType.Name.TEXT)
  @JsonProperty("email")
  private String email;

  @NonNull
  @Column("img")
  @CassandraType(type = CassandraType.Name.TEXT)
  @JsonProperty("img")
  private String img;

  @NonNull
  @Column("location")
  @CassandraType(type = CassandraType.Name.TEXT)
  @JsonProperty("location")
  private String location;

  @NonNull
  @Column("name")
  @CassandraType(type = CassandraType.Name.TEXT)
  @JsonProperty("name")
  private String name;

  @NonNull
  @Column("phone")
  @CassandraType(type = CassandraType.Name.TEXT)
  @JsonProperty("phone")
  private String phone;

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

    if (obj instanceof Hotel) {
      if (((Hotel) obj).getId().equals(getId())) {
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
    builder.append(" Image: " + img + newline);
    builder.append(" Location: " + location + newline);
    builder.append(" Name: " + name + newline);
    builder.append(" Email: " + email + newline);
    builder.append(" Phone: " + phone + newline);
    builder.append("}");

    return builder.toString();
  }

}
