/** Used to manage state from localStorage */
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
   * THIS OVERWRITES THE CURRENT STATE ON PURPOSE (TO REFRESH THE STATE).
   * @see [Storage.setItem()](https://html.spec.whatwg.org/multipage/webstorage.html#dom-storage-setitem-dev)
   * @param {string} key keyName for Storage.getItem()
   * @param {any} value keyValue for Storage.setItem()
   */
  public static storeStateToLocalStorage(key: string, value: any) {
    window.localStorage.setItem(key, JSON.stringify(value));
  }

  /**
   * NOTE: JSON.parse() is used once implicitly on the return value,
   * meaning there is no need to parse the objects into unless multiple passes is needed.
   * @see [Storage.getItem()](https://html.spec.whatwg.org/multipage/webstorage.html#dom-storage-getitem-dev)
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
