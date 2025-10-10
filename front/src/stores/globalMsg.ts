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
    isDev: import.meta.env.DEV as boolean,
  }),
  actions: {
    addMsg(msgClass: MsgClass, content: string){

      //// 개발중일 때만 Debug 메시지 표시
      if (msgClass === MsgClass.DEBUG && !this.isDev) {
        return;
      }

      const msg = GlobalMsg.create(msgClass, content);
      this.msgs.push(msg);

      //// 타이머: 시간 지나면 자동삭제
      const timeoutId = setTimeout(() => {
        this.removeMsg(msg.id);
      }, 7000);
      msg.timeoutId = timeoutId;
    },

    //// 메시지 제거
    removeMsg(msg_id: number) {
      this.msgs = this.msgs.filter(m => m.id !== msg_id);
    },

    //// 자동삭제 타이머 제거
    disableAutoRemove(msg_id: number) {
      const msg = this.msgs.find(m => m.id === msg_id);
      if (msg?.timeoutId) {
        clearTimeout(msg.timeoutId);
        msg.timeoutId = null;
      }
    },
  }
});

class GlobalMsg {
  static id = 0;
  private _id: number;
  private _msgClass: MsgClass;
  private _content: string;
  private _timeoutId: number | null = null;

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

  get timeoutId(): number | null {
    return this._timeoutId;
  }

  set timeoutId(tid: number | null) {
    this._timeoutId = tid;
  }
}

export enum MsgClass {
  ERROR = 'error',
  WARN = 'warn',
  INFO = 'info',
  DEBUG = 'debug',
};
