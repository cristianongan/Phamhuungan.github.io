<template>

  <div class="fullbody">
    
      <LoginForm v-if="!isRegister" @status="isRegister=!isRegister"/>
      <RegisterForm v-else @status="isRegister=!isRegister" />

    <div class="picture">
    
    </div>
    <div class="red-bar">

    </div>
  
      <div class="nagakawa-logo">

      
      </div>
      <div class="des">
          <span>Tiêu chuẩn cuộc sống</span>
      </div>

       
  </div>
     

    
</template>
<script>

import * as yup from 'yup';
import LoginForm from'./LoginForm.vue'
import RegisterForm from './RegisterForm.vue'

export default {
    data(){
      return {
        
        isRegister:false
      }
    },
    methods:{
      
    },
    components:{
     LoginForm,RegisterForm
    },
    setup(){
      const formLoginSchema ={
        username: yup.string().required().min(2,'dcm'),
        password: yup.string().required().min(1)
      }

      return {
       formLoginSchema
      }
    }


}
</script>

<style scoped>
   
    .fullbody{
      height: 100%;
      width: 100%;
      overflow: hidden;
    }
    .picture{
      height: 50%;
      width: 100%;
      background-image: url('../img/802549.jpg') ;
      background-repeat: no-repeat;
      background-size: 100% 100%;
      opacity: 1;
    }
    .nagakawa-logo{
      width: 43%;
      height: 18%;
      background-image: url('../img/logo-ko-r.png');
      background-repeat: no-repeat;
      background-size: 100% 100%;
      opacity: 1;
      box-shadow: 0px 3px 4px #00000029;
      margin-top: 10px;
      margin-left:9% ; 
    }
    .red-bar{
      width: 100%;
      height: 8px;
      background-color: #DB4437 ;
      background-repeat: no-repeat;
      opacity: 1;
    }
    .des{
      text-align: left;
      font: Roboto,Regular  ;
      font-size: 36px;
      letter-spacing: 0px;
      color: #00833E;
      opacity: 1;
      width: 25% ;
      padding:0px;
      margin-left: 19%;
      margin-top: 3%;
    }
    /* .login-container{
      position: fixed;
      top:0;
      left:0;
      height: 100%;
      width: 100%;
      padding-top: 29%;
      padding-left:60% ;
    } */
    

</style>