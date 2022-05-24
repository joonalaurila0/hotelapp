class State {

  public static storeStateToLocalStorage(key: string, value: any) {
    window.localStorage.setItem(key, JSON.stringify(value))
  }

  public static fetchStateByKey(key: string) {
    let item = window.localStorage.getItem(key);
    if (item) {
      return JSON.parse(item);
    }
  }
}

export default State;
