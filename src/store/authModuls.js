import authService from '../services/AuthService.js'
import {Attr,Code} from '../constants.js'

const auth ={
    namespaced:true,
    state:{
        isLoggedin:false,
        
    },
    mutations:{
        logOut(state ){
            state.isLoggedin = false;

            localStorage.removeItem(Attr.token);
            localStorage.removeItem(Attr.role);
        },

        login(state , data){
            state.isLoggedin = true;

            localStorage.setItem(Attr.token,data.object)
            localStorage.setItem(Attr.role,data.role)
        },

        startup(state){
            if(localStorage.getItem(Attr.token) && localStorage.getItem(Attr.role))
                state.isLoggedin=true;
        }
    },
    actions:{
        loginAndSave(context,account){
            

            return authService.auth(account)
           .then(response =>{
                let data =response.data;

               if(data != null){
                   context.commit('login',data)
                    return data;
               }
                    return {code:Code.ServerError}
           }).catch(error =>{
               console.log(error)
                return {code:Code.WrongPassword}
           })
        },
        login(context , account){
            return authService.auth(account)
            .then(response =>{
                let data =response.data;

               if(data != null){
                //    context.commit('login',data)
                    context.state.isLoggedin = true
                    return data;
               }
                    return {code:Code.ServerError}
           }).catch(error =>{
               console.log(error)
                return {code:Code.WrongPassword}
           })
        },

        logout(context){
            context.commit('logOut')
        }
    },
    getters:{
        getStateLogin:(state)=>{
            return state.isLoggedin;
        }
    }
}

export default auth;