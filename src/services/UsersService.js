import axios from 'axios'
import { endPoint,KeyWord} from '../constants.js'
import authHeader from './authHeadersService.js'

export default {


    getListUserByPage(page){
        return axios.get(endPoint.users+KeyWord.slash+page,{headers:authHeader.getAuthHeader()})
        
    },

    getListUserByPageAndKey(page,prams){
        return axios.get(endPoint.usersf+KeyWord.slash+page,{headers:authHeader.getAuthHeader(),params:prams})
    },

    deleteUser(id){
        return axios.delete(endPoint.deleteUser+KeyWord.slash+id,{headers:authHeader.getAuthHeader()})
    },

    updateUser(account){
        return axios.put(endPoint.updateUser,account,{headers:authHeader.getAuthHeader()})
    },

    registerUser(account){
        return axios.post(endPoint.res,account,{headers:authHeader.getAuthHeader()})
    },

    getPersonalInfomation(){
        return axios.get(endPoint.personalInformation,{headers:authHeader.getAuthHeader()})
        
    },

    updatePassword(account){
        return axios.post(endPoint.updatePassword,account,{headers:authHeader.getAuthHeader()})
    }

}