class State {

  private static instance: State;
  private constructor() {}

  public static getInstance(): State {
    if (!State.instance) {
      State.instance = new State();
    }

    return State.instance;
  }

  /**
   * NOTE: JSON.stringify() is used once implicitly on the keyValue, 
   * meaning there is no need to turn the objects into strings unless multiple passes is needed.
   * @see [Storage.setItem()](https://developer.mozilla.org/en-US/docs/Web/API/Storage/setItem)
   * @param key keyName for Storage.getItem()
   * @param value keyValue for Storage.setItem()
   */
  public static storeStateToLocalStorage(key: string, value: any) {
    window.localStorage.setItem(key, JSON.stringify(value))
  }

  /**
   * NOTE: JSON.parse() is used once implicitly on the return value, 
   * meaning there is no need to parse the objects into unless multiple passes is needed.
   * @see [Storage.getItem()](https://developer.mozilla.org/en-US/docs/Web/API/Storage/getItem)
   * @param key keyName for Storage.getItem()
   */
  public static fetchStateByKey(key: string) {
    let item = window.localStorage.getItem(key) || null;
    if (item) {
      return JSON.parse(item);
    }
    return null;
  }
}

export default State;
