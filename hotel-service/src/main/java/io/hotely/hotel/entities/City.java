package io.hotely.hotel.entities;

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

@Table(value = "cities")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class City {

  @PrimaryKey
  @PrimaryKeyColumn(name = "id", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
  @CassandraType(type = CassandraType.Name.INT)
  @JsonProperty("id")
  private Long id;

  @NonNull
  @Column("name")
  @CassandraType(type = CassandraType.Name.TEXT)
  @JsonProperty("name")
  private String name;

  @NonNull
  @Column("region")
  @CassandraType(type = CassandraType.Name.TEXT)
  @JsonProperty("region")
  private String region;

  @Column("population")
  @CassandraType(type = CassandraType.Name.BIGINT)
  @JsonProperty("population")
  private int population;

  @NonNull
  @Column("lat")
  @CassandraType(type = CassandraType.Name.DOUBLE)
  @JsonProperty("lat")
  private Double lat;

  @NonNull
  @Column("lng")
  @CassandraType(type = CassandraType.Name.DOUBLE)
  @JsonProperty("lng")
  private Double lng;

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

    if (obj instanceof City) {
      if (((City) obj).getId().equals(getId())) {
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
    builder.append(" Name: " + name + newline);
    builder.append(" Region: " + region + newline);
    builder.append(" Population: " + population + newline);
    builder.append(" Latitude: " + lat + newline);
    builder.append(" Longitude: " + lng + newline);
    builder.append("}");

    return builder.toString();
  }

}
