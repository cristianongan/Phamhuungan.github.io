<template>
<div class="container-form">
  <form @submit.prevent="login()" ref="form" class="login-box">
         <span class="form-header-custom">Đăng nhập</span>
          <span>{{warning}}</span>
          <div class="form-input">
            <label for="floatingInput">Tài khoản</label>
            <input type="text" class="form-control" id="floatingInput" placeholder="username" v-model="username">
          </div>
            <span v-show="e.eUsername" class="red">{{e.eUsername}}</span>
          <div class="form-input">
            <label for="floatingInput">Mật khẩu</label>
            <input type="password" class="form-control" id="floatingInput" placeholder="password" v-model="password">
          </div>  
          <span v-show="e.ePassword" class="red">{{e.ePassword}}</span>        
          <div class="tool-form ">
            <div >
              <input type="checkbox" style="vertical-align: middle;" v-model="checkbox">
              <span style="vertical-align: middle;">Lưu tài khoản</span>
            </div>

            <a href="" class="forgot-pasword">Quên mật khẩu?</a>
          </div>
          <button>Đăng nhập</button>
          <div class="form-group form-des" style="margin: 4% auto;">
            <span>Bạn chưa có tài khoản? </span><a href="" @click.prevent="$emit('status')">Đăng kí ngay</a>
          </div>
      </form>
    </div>
</template>

<script>
import {Code} from '../constants.js'
import validate from '../services/Validate.js'

export default {
    data(){
        let e= {
            eUsername:'',
            ePassword:''
        }

        return {
        warning:'',
        username:"",
        password:"",
        checkbox:false,
        e
        }
    },
    methods:{
        login(){   
            // this.$refs.form.onChange(validate)

            if(this.validate()){
                this.$store.dispatch('authModule/login',{username:this.username,password:this.password}).then(data =>{
                     if(data.code==Code.ok){
                        this.warning=''

                        this.$router.push('/home')
                    }else{
                        if(data.code==Code.WrongPassword){
                        this.warning='Sai ten dang nhap hoac mat khau'
                        }
                    }
                })
            }
      },

      validate(){

          this.e.eUsername = validate.required(this.username)
          this.e.ePassword = validate.required(this.password);

            if(this.e.eUsername || this.e.ePassword)
                return false

          return true;
      }
    }
}
</script>

<style scoped src="../css/sign-form.css">

</style>