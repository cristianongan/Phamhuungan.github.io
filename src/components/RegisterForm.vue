<template>
  <div class="container-form">
  <form @submit.prevent="register()" :validation-schema="formLoginSchema" class="login-box">
         <span class="form-header-custom">Đăng kí</span>
          <span>{{warning}}</span>
          <div class="form-input">
            <label for="floatingInput">Tài khoản</label>
            <input type="text" class="form-control" id="floatingInput" placeholder="username" v-model="account.username">
          </div>
          <span v-show="e.eUsername" class="red">{{e.eUsername}}</span>

          <div class="form-input">
            <label for="floatingInput">Mật khẩu</label>
            <input type="password" class="form-control" id="floatingInput" placeholder="password" v-model="account.password">
          </div>
          <span v-show="e.ePassword" class="red">{{e.ePassword}}</span>
          <div class="form-input" >
            <label for="floatingInput">Nhập lại mật khẩu</label>
            <input type="password" class="form-control" id="floatingInput" placeholder="password" v-model="account.rePassword">
          </div>
          <span v-show="e.eRepassword" class="red">{{e.eRepassword}}</span>
          <div class="form-input" >
            <label for="floatingInput" >Họ và tên</label>
            <input type="text" class="form-control" id="floatingInput" placeholder="full name" v-model="account.fullName" >
          </div>
          <span v-show="e.eFullname" class="red">{{e.eFullname}}</span>
          <div class="form-input" >
            <label for="floatingInput" >Email</label>
            <input type="text" class="form-control" id="floatingInput" placeholder="email" v-model="account.email">
          </div>
          <span v-show="e.eEmail" class="red">{{e.eEmail}}</span>
          
            <div class="tool-re" >
                <input type="checkbox" style="vertical-align: middle;" v-model="check">
                <span style="">Tôi đồng ý với bộ luật vô hình của trang web</span>
            </div>
            <span v-show="e.eRule" class="red">{{e.eRule}}</span>

          <button type="submit">Đăng kí</button>
          <div class="form-group form-des" style="margin: 4% auto;">
            <span>Bạn đã có tài khoản? </span><a href="" @click.prevent="$emit('status')">Đăng nhập ngay</a>
          </div>
      </form>
    </div>
</template>

<script>
import validate from '../services/Validate.js'
import userService from '../services/UsersService.js'
import { Code } from '../constants.js'

export default {
    data(){
        let account ={
            username:'',
            password:'',
            rePassword:'',
            fullName:'',
            email:'',
            
        }
        let e = {
            eUsername:'',
            ePassword:'',
            eRepassword:'',
            eFullname:'',
            eEmail:'',
            eRule:''
        }
        return {
            account,e,warning:'',check:false
        }
    },
    methods:{
        register(){
           if( this.validate()){
             console.log('vo register')

             userService.registerUser(this.account)
             .then(response => {
               let data = response.data

               if(data.code == Code.ok){
                 alert('success')
               }
             })
             .catch(error=>{
               alert('wrong'+error)
             })
           }
        },
        validate(){
            this.e.eUsername = validate.required(this.account.username)
            this.e.ePassword = validate.required(this.account.password)
            this.e.eRepassword = validate.equal(this.account.password , this.account.rePassword)
            this.e.eFullname = validate.required(this.account.fullName)
            this.e.eEmail = validate.required(this.account.email)
            this.e.eRule = this.check?undefined:'Bạn phải đồng ý với quy định trên'

            return validate.checkObj(this.e);
        }
    }
}
</script>

<style scoped src="../css/sign-form.css">

</style>