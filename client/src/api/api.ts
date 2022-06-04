import { Hotel } from '../hotelview/hotelview';
import { ISO8601Date } from '../util';
import State from '../state';

class Api {

  private static host = window.location.host || process.env.host;
  private static url = `http://${this.host}`;

  // Makes Calls to the API Gateway that routes calls to the services.
  private static GatewayURI = `http://localhost:8072`;

  // Maintains the keycloak session.
  // TOKEN EXPIRATION IS 5 MINUTES
  private static authToken = () => State.fetchStateByKey('token') ?? null;

  public static temporaryTokenLogger() {
    console.log("Api.authToken ::", Api.authToken());
  }

  // be279449-eea0-427c-9707-77a7346ae53d
  public static async getCustomer(customerId: string) {
    return fetch(this.GatewayURI + `/customer-service/customers/${customerId}`, {
      method: 'GET',
      headers: { 
        'Authorization': `Bearer ${this.authToken()}`,
        'Content-Type': 'application/json'
      }
    });
  };

  public static async getCustomers() {
    return fetch(this.GatewayURI + `/customer-service/customers/all`, {
      method: 'GET',
      headers: { 
        'Authorization': `Bearer ${this.authToken()}`,
        'Content-Type': 'application/json'
      }
    });
  };

  public static async createCustomer(id: string, email: string) {
    return fetch(this.GatewayURI + `/customer-service/customers/createwid`, {
      method: 'POST',
      headers: { 
        'Authorization': `Bearer ${this.authToken()}`,
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: new URLSearchParams({
        'id': id,
        'email': email,
        'password': 'meow',
        'userstatus': 'ACTIVE',
        'role': 'USER'
      })
    });
  };

  /* The parameters are generally specified in snake_case for the Java API
   * especially when using application/json  */
  public static async createBooking(booking: Booking) {
    return fetch(this.GatewayURI + '/hotel-service/bookings/create', { 
      method: 'POST',
      headers: { 
        'Authorization': `Bearer ${this.authToken()}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        customer_id: booking.customer_id,
        hotel_id: booking.hotel_id,
        room_id: booking.room_id,
        booking_status: booking.booking_status,
        start_date: booking.start_date,
        end_date: booking.end_date,
      })
    });
  };

  /* The parameters are generally specified in snake_case for the Java API, 
   * especially when using application/json  */
  public static async createInvoice(invoice: Invoice) {
    return fetch(this.GatewayURI + '/customer-service/invoices/create', { 
      method: 'POST',
      headers: { 
        'Authorization': `Bearer ${this.authToken()}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        booking_id: invoice.bookingId,
        customer_id: invoice.customerId,
        total: invoice.total,
        issued: invoice.issued,
        payment_date: invoice.payment_date,
        paid: invoice.paid,
        cancelled: invoice.cancelled
      })
    });
  };

  public static async findHotels() {
    return fetch(this.GatewayURI + '/hotel-service/hotels/all', { 
      method: 'GET', 
      headers: { 
        'Content-Type': 'application/json' 
      }  
    });
  };

  public static async findCities() {
    return fetch(this.GatewayURI + '/hotel-service/cities/all', {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' }
    });
  };

  public static async findRooms() {
    return fetch(this.GatewayURI + '/hotel-service/rooms/all', {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' }
    })
  }

  // find invoices of of a customer
  public static async findInvoices(customerId: string) {
    return fetch(this.GatewayURI + `/customer-service/invoices/customer/${customerId}`, {
      method: 'GET',
      headers: { 
        'Authorization': `Bearer ${this.authToken()}`,
        'Content-Type': 'application/json'
      }
    })
  }

  // find bookings of of a customer
  public static async findBookings(customerId: string) {
    return fetch(this.GatewayURI + `/hotel-service/bookings/customer/${customerId}`, {
      method: 'GET',
      headers: { 
        'Authorization': `Bearer ${this.authToken()}`,
        'Content-Type': 'application/json'
      }
    })
  }

  public static async createBookingAndInvoice({ profileId, hotel, roomId, roomPrice }: 
    { profileId: string, hotel: Hotel, roomId: number, roomPrice: number }) {

    // Create a booking for the user
    const response: Response = 
      (await this.createBooking({
            customer_id: profileId,
            hotel_id: hotel.id,
            room_id: roomId,
            booking_status: BookingStatus.Pending,
            start_date: new Date().toISOString(),
            end_date: ISO8601Date(7)
          }));
    const booking = await response.json();
    console.log("Booking made ::", booking);
    booking ? window.alert(`Room ${roomId} booked from the ${hotel.name}!`) : null;

    // If booking was made succesfully proceed..
    if (booking) {
      // Create an invoice upon succesful booking
      this.createInvoice({
        bookingId: booking.id,
        customerId: profileId,
        total: roomPrice,
        issued: new Date().toJSON(),
        payment_date: ISO8601Date(),
        paid: false,
        cancelled: false,
      }).then((res) => {
          console.log("Invoice succesfully created ::", res.json());
      })
    }
  }

  public static async signUp(credentials: string) {
    return fetch(this.url + credentials, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' }
    });
  };
};

export type Booking = {
  id?: string,
  customer_id: string,
  hotel_id: number,
  room_id: number,
  booking_status: BookingStatus,
  start_date: string,
  end_date: string
}

export enum BookingStatus {
  Pending,
  Confirmed,
  Canceled,
  Checkedin,
  Checkedout
}

export type Invoice = {
  id?: string,
  bookingId: string,
  customerId: string,
  total: number,
  issued: string,
  payment_date: string,
  paid: boolean,
  cancelled: boolean
}

export default Api;
