class Api {

  private static host = window.location.host || process.env.host
  private static url = `http://${this.host}`;

  private static hotel_service_api = `http://localhost:8001`;

  public static async findHotels() {
    return fetch(this.hotel_service_api + '/hotels/all', { 
      method: 'GET', 
      headers: { 'Content-Type': 'application/json' }  
    });
  }

  public static async findCities() {
    return fetch(this.hotel_service_api + '/cities/all', {
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
