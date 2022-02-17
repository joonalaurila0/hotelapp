class api {

  private static host = window.location.host
  private static url = `http://${this.host}`;
  
  public static findHotels(query: string) {
    return fetch(this.url + query, { 
      method: 'GET', 
      headers: { 'Content-Type': 'application/json' }  
    });
  }
}

export default api;
