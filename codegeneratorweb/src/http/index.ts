import axios from 'axios';
import { CodegenerateRequest } from '../model/codegenerate-request';
import { GuidRequest } from '../model/guid-request';

const prod = process.env.NODE_ENV === 'production';

const host = prod ? 'https://moss1019-codegen.herokuapp.com/' : 'http://192.168.1.100';
const port = prod ? '' : ':8080';

const baseUrl = `${host}${port}/api`;

const config: any = {
  headers: {'Content-Type': 'application/json'}
}

export function generateApp(request: CodegenerateRequest, onSuccess: (fileBytes: any) => void, onError: (err: any) => void) {
  axios.post(`${baseUrl}/generated-apps`, JSON.stringify(request), config)
    .then((res) => onSuccess(res.data))
    .catch(err => onError(err));
}

export function generateGuids(request: GuidRequest, onSuccess: (guids: any) => void, onError: (err: any) => void) {
  axios.post(`${baseUrl}/guids`, JSON.stringify(request), config)
    .then((res) => onSuccess(res.data))
    .catch((err) => onError(err));
}
