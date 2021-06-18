 import {Attr} from '../constants.js'

export default {
    getAuthHeader(){
        // let h = KeyWord.authHeader

        return {
            "Authorization":localStorage.getItem(Attr.token),
        }
    },
}