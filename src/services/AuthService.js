import {endPoint} from '../constants.js'
import axios from 'axios'

export default {
    auth(account){
       return axios.post(endPoint.auth,account,{headers:{}});
    }




}