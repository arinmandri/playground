/**
 * 
 * 예시
 * import { MsgClass, useMsgStore } from '@/stores/globalMsg'; const msgStore = useMsgStore();
 * msgStore.addMsg(MsgClass.INFO, '메시지');
 */

import { defineStore } from "pinia";

export const useMsgStore = defineStore("msg", {
  state: () => ({
    msgs: [] as GlobalMsg[],
  }),
  actions: {
    addMsg(msgClass: MsgClass, content: string){
      const msg = GlobalMsg.create(msgClass, content);
      this.msgs.push(msg);
      setTimeout(()=>{
        this.removeMsg(msg.id);
      }, 7000);
    },
    removeMsg(msg_id: number) {
      this.msgs = this.msgs.filter(m => m.id !== msg_id);
    },
  }
});

class GlobalMsg {
  static id = 0;
  private _id: number;
  private _msgClass: MsgClass;
  private _content: string;

  private constructor(
    msgClass: MsgClass,
    content: string,
  ) {
    this._id = GlobalMsg.id++;
    this._msgClass = msgClass;
    this._content = content;
  }

  static create(
    msgClass: MsgClass = MsgClass.INFO,
    content: string = '',): GlobalMsg{
    return new GlobalMsg(msgClass, content);
  }

  get id(): number {
    return this._id;
  }

  get msgClass(): MsgClass{
    return this._msgClass;
  }

  get content(): string {
    return this._content;
  }
}

export enum MsgClass {
  ERROR = 'error',
  WARN = 'warn',
  INFO = 'info',
  DEBUG = 'debug',
};
