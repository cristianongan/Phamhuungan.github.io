import axios from 'axios'
import {endPoint} from '../constants.js'
import authHeader from './authHeadersService.js'

export default {
    getRoles(){
        return axios.get(endPoint.role,{headers:authHeader.getAuthHeader()})
    }
}