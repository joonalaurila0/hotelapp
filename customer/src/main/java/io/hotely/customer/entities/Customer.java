package io.hotely.customer.entities;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/* Avoid @Data for JPA's 
 * Always cascade from Parent-side to Child-side,
 * so no CascadeType.* for @ManyToOne since entity
 * state transitions should be propagated from
 * parent-side netities to child-side ones.
 *
 * Set mappedBy and orphanRemoval on the parent-side,
 * and use lazy fetching on both sides of the
 * association. 
 *
 * To conform to Hibernate docs, if entities are
 * stored in a Set or are reattached to a new 
 * Persistence Context, then we should override
 * equals() and hashCode(). This is done to respect
 * the consistency of entity equality across all its
 * state transitions. */

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Customer {

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
  @Type(type = "uuid-char")
  private UUID id;

  @NonNull
  private String name;

  @NonNull 
  @NaturalId(mutable = false)
  @Column(name = "email", unique = true, nullable = false, length = 100)
  private String email;

  @NonNull 
  private String phone;

  @NonNull 
  private String address;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer", orphanRemoval = true)
  private Set<Invoice> invoices;

  /* Lombok has already provided accessors and mutators
   * for invoices, but I will give methods for dealing
   * with individual invoices now. */
  public void addInvoice(Invoice invoice) {
    this.invoices.add(invoice);
    invoice.setCustomer(this);
  }

  public void removeInvoice(Invoice invoice) {
    invoice.setCustomer(null);
    this.invoices.remove(invoice);
  }

  /* auxiliary method for removing all invoices
   * from the Customer */
  public void removeInvoices() {
    Iterator<Invoice> iterator = this.invoices.iterator();
    while (iterator.hasNext()) {
      Invoice invoice = iterator.next();
      invoice.setCustomer(null);
      iterator.remove();
    }
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (object == null) {
      return false;
    }

    if (getClass() != object.getClass()) {
      return false;
    }

    Customer other = (Customer) object;
    return Objects.equals(id, other.getId());
  }

  /* using hash of email as natural identifier,
   * since they're required to be unique. */
  @Override
  public int hashCode() {
    return Objects.hash(email);
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    String Newline = System.getProperty("line.separator");

    result.append(this.getClass().getName() + " Object {" + Newline);
    result.append(" Name: " + name + Newline);
    result.append(" Email: " +  email + Newline);
    result.append(" Phone: " +  phone + Newline);
    result.append(" Address: " +  address + Newline);
    result.append("}");

    return result.toString();
  }
}
