package io.hotely.customer.entities;

import io.hotely.customer.entities.enums.Role;
import io.hotely.customer.entities.enums.UserStatus;

import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.annotation.Id;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RedisHash(value = "Customer", timeToLive = 300000)
@Table(value = "customers")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Customer {

  @Id
  @PrimaryKey
  @PrimaryKeyColumn(name = "id", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
  @CassandraType(type = CassandraType.Name.UUID)
  @JsonProperty("id")
  private UUID id;

  @NonNull
  @Column(value = "email")
  @CassandraType(type = CassandraType.Name.TEXT)
  private String email;

  @NonNull
  @Column(value = "password")
  @CassandraType(type = CassandraType.Name.TEXT)
  private String password;

  @NonNull
  @Column(value = "role")
  @CassandraType(type = CassandraType.Name.TEXT)
  private Role role;

  @NonNull
  @Column(value = "userstatus")
  @CassandraType(type = CassandraType.Name.TEXT)
  private UserStatus userstatus;

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

    if (obj instanceof Customer) {
      if (((Customer) obj).getId().equals(getId())) {
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
    builder.append(" Email: " + email + newline);
    builder.append(" Password: " + password + newline);
    builder.append(" Role: " + role + newline);
    builder.append(" UserStatus: " + userstatus + newline);
    builder.append("}");

    return builder.toString();
  }
}
