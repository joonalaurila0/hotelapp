class Api {

  private static host = window.location.host || process.env.host
  private static url = `http://${this.host}`;

  // Makes Calls to the API Gateway that routes calls to the services.
  private static GatewayURI = `http://localhost:8072`;

  public static async findHotels() {
    return fetch(this.GatewayURI + '/hotel-service/hotels/all', { 
      method: 'GET', 
      headers: { 'Content-Type': 'application/json' }  
    });
  }

  public static async findCities() {
    return fetch(this.GatewayURI + '/hotel-service/cities/all', {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' }
    });
  }

  public static async signUp(credentials: string) {
    return fetch(this.url + credentials, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' }
    })
  }

  public static async signIn(credentials: string) {
    return fetch(this.url + credentials, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' }
    })
  }
}

export default Api;
