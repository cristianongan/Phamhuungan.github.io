<template>
<div class="pop-container container-custom" >
  <div class="modal-dialog">
      <div class="modal-content">
          <div class="modal-header" >
                <h5 class="modal-title">Thay đổi password</h5>
                <button @click="closePopup()" class="close-button button none-border red ">
                    <ficon icon="window-close" />
                </button>
          </div>
        <div class="modal-body" >
      <form   @submit.prevent="submit()" >
          <div class="form-group">
              <label for="exampleInputEmail1">Old Password</label>
                <input type="password" class="form-control" placeholder="Enter your old password" v-model="oldPassword"/>
                <small v-if="eOldPassword" id="emailHelp" class="form-text text-muted">{{eOldPassword}}</small>
          </div>
        <div class="form-group">
              <label for="exampleInputEmail1">New Password</label>
                <input type="password" class="form-control"  placeholder="Enter your new password" v-model="newPassword"/>
                <small v-if="eNewPassword" id="emailHelp" class="form-text text-muted">{{eNewPassword}}</small>
          </div>
        <div class="form-group">
              <label for="exampleInputEmail1">Re Password</label>
                <input type="password" class="form-control" placeholder="re Enter your new password" v-model="rePassword"/>
                <small v-if="eRePassword" role="alert" id="emailHelp" class="form-text text-muted">{{eRePassword}}</small>
          </div>
          <button type="submit"  class="btn btn-primary">Hoàn tất</button>
      </form>
        </div>
      </div>
  </div>
</div>
  <Popup v-show="showPopup" :title="notification" :description="des" @closeModal="showPopup=false"/>
</template>

<script>
import usersService from '../services/UsersService.js'
import {Code} from '../constants.js'
import Popup from './Popup.vue'
import validate from '../services/Validate.js'

export default {
    components:{
        Popup
    },
    props:{
        username:{}
    },
    data(){
        return {
            oldPassword:'',
            newPassword:'',
            rePassword:'',
            notification:'',
            des:'',
            showPopup:false,
            eOldPassword:'',
            eNewPassword:'',
            eRePassword:'',
            
        }
    },
    methods:{
        submit(){
            

            if(this.validate()){
                usersService.updatePassword({
                    username:this.username,
                    password:this.oldPassword,
                    newPassword:this.newPassword
                })
                .then(response =>{
                    if(response.data.code == Code.ok){
                        this.notification="Thành công"
                        this.des="Cập nhật mật khẩu thành công"

                        this.showPopup = true ;
                    } else {
                        this.notification="Thất bại"
                        this.des="Xem lại mật khẩu cũ"
                        
                        this.showPopup = true ;

                    }
                })
            }
        },
        validate(){
            

            this.eOldPassword = validate.required(this.oldPassword)
            this.eNewPassword = validate.required(this.newPassword)
            this.eRePassword = validate.required(this.rePassword)

            console.log('vo submit'+this.eOldPassword)

            if(this.eOldPassword || this.eNewPassword || this.eRePassword){
                return false
            }
            if(this.newPassword != this.rePassword){
                this.eRePassword = "Không trùng với mật khẩu mới"

                return false
            }

            return true;
        },
        closePopup(){
            this.$emit('close')
        }
    }

}
</script>

<style scoped>
.container-custom{
    width: 600px;
    left:35%
}
.form-custom{
    margin: 15px;
    width: 600px;
    
}
.modal-content{
    padding: 20px;
}
</style>