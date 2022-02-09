
export interface GuidRequest {
  amount: number;
  braced: boolean;
  dashed: boolean;
}

export const emptyRequest: GuidRequest = {
  amount: 0,
  braced: false,
  dashed: true
}
