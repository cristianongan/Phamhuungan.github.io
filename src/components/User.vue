<template>
  <div class="container-user-custom">
      <div class="conatinerS">
        <div class="nav-pop">
            <button type="button" @click="$emit('closepop')"  class="close-button button none-border red">
                <ficon icon="window-close" />
            </button>
        </div>
      <form @submit.prevent="onSub"  class="formS" >
        <span>{{warning}}</span>
        <div class="form-group">
            <label style="display:block;text-align:left">Tên tài khoản</label>
            <input type="text" class="form-control" name="username" id="" v-model="username"  placeholder="Your Username"/>
            <div class="alert alert-warning alert-dismissible" role="alert" v-if="eUsername">
                <span>{{eUsername}}</span>
            </div>
        </div>
       <div class="form-group" v-if="!isUpdate">
            <label style="display:block;text-align:left">Mật khẩu</label>
            <input type="password" name="password" id="" v-model="password" placeholder="Your password" class="form-control"/>
            <div class="alert alert-warning alert-dismissible" role="alert" v-if="ePassword">
                <span>{{ePassword}}</span>
            </div>
        </div>
         
        <div class="form-group">
            <label style="display:block;text-align:left">Tên đầy đủ</label>
            <input type="text" name="fullName" id="" v-model="fullName" placeholder=" fullName" class="form-control"/>
            
        </div>
        <div class="form-group">
            <label style="display:block;text-align:left">Mô tả</label>
        <input type="text" name="description" id="" v-model="description" placeholder=" description" class="form-control"/>

        </div>
        <div class="form-group">
            <label style="display:block;text-align:left">Ngày sinh</label>
            <date-picker  name="dob"  :inputFormat="'dd-MM-yyyy'"  v-model="dob" class="form-control" />
            <div class="alert alert-warning alert-dismissible" role="alert" v-if="eDob">
                <span>{{eDob}}</span>
            </div>
        </div>
        <div class="form-group">
            <label style="display:block;text-align:left">Địa chỉ</label>
        <input type="text" name="address" id="" v-model="address" placeholder=" address" class="form-control"/>

        </div>
        <div class="d-flex justify-content-between " style="margin:20px 0">
                <select v-model="section"  name="section" class="custom-select " style="width:200px">
                    <option v-for="s in sec" :selected="s.object.name == section" :key="s.object.id" :value='s.object.id'>{{s.object.name}}</option>
                </select>


                <select name="roles" v-model="roles" class="custom-select " style="width:200px">
                    <option value="1" >admin</option>
                    <option value="2">user</option>
                </select>

         </div>
       
        
        
        
        <button type="submit"  class="close-button button none-border red">
            <ficon icon="check"/> 
            submit
        </button>
        

       
      </Form>

      </div>
        
  </div>
</template>

<script>
import {Code} from '../constants.js'
// import SectionsService from '../services/SectionsService.js'
import userService from '../services/UsersService.js'
// import { Form, Field,ErrorMessage } from 'vee-validate';
// import * as yup from 'yup'
// import { date } from 'yup/lib/locale';
import moment from 'moment';
import validate from '../services/Validate.js'

export default {
    name:"User",
    props:['currentAccount'],
    setup(){
        // const formUserSchema = {
        //     username: yup.string().required('khong duoc bo trong'),
        //     password: yup.string().required('khong duoc bo trong'),
        //     fullName: yup.string().required('khong duoc bo trong'),
        //     description: yup.string().required('khong duoc bo trong'),
        //     dob: yup.date().required('khong duoc bo trong'),
        //     address: yup.string().required('khong duoc bo trong'),
        //     section: yup.string().required('khong duoc bo trong'),
        //     roles: yup.string().required('khong duoc bo trong')
        // }

        // const formUpdateUserSchema ={
        //     username: yup.string().required('khong duoc bo trong'),
        //     fullName: yup.string().required('khong duoc bo trong'),
        //     description: yup.string().required('khong duoc bo trong'),
        //     dob: yup.date().required('khong duoc bo trong'),
        //     address: yup.string().required('khong duoc bo trong'),
        //     section: yup.string().required('khong duoc bo trong'),
        //     roles: yup.number().required('khong duoc bo trong')
        // }


        // return{

        //   formUserSchema,
        //   formUpdateUserSchema
          
        // }

    },
    data(){
        return{
            sec:[],
            username:'',
            user:this.currentAccount,
            isUpdate:false,
            password:'',
            fullName:'',
            description:'',
            dob:this.formatDate(''),
            address:'',
            section:'',
            roles:'',
            warning:'',
            eUsername:'',
            ePassword:'',
            eDob:''
        }
    },
    created(){
        this.getSecs()
    },
    methods:{
        getSecs(){

            this.sec = this.$store.getters['sectionsModule/getListSecs']

        },
        validateForm(){
            this.eUsername = validate.required(this.username);
            this.ePassword = validate.required(this.password);
            this.eDob = validate.date(this.formatDate(this.dob))

            if(this.eUsername || this.ePassword || this.eDob){
                return false;
            }

            return true;
        },
        register(){
            
            const account ={
                username:this.username,
                password:this.password,
                role:{
                    id:this.role
                },
                fullName:this.fullName,
                description:this.description,
                DOB:this.formatDate(this.dob),
                createdDate:new Date(),
                address:this.address,
                section:{id:this.section}

            }

            userService.registerUser(account)
            .then(response=>{
                let data = response.data

                switch (data.code){
                    case Code.ok:
                        this.$emit('closepop')
                         alert('sucess')
                         break
                    case Code.Dup:
                        this.warning='trung username'
                        break
                }
            })
            .catch(error=>{
                console.log(error)
                 alert('e')
            })
        },
        update(){
            
            const account ={
                id:this.user.id,
                username:this.username,
                role:{
                    id:this.role
                },
                fullName:this.fullName,
                description:this.description,
                DOB:this.formatDate(this.dob),
                createdDate:new Date(),
                address:this.address,
                section:{id:this.section}

            }

            userService.updateUser(account)
            .then(response=>{
                let data = response.data

                switch (data.code){
                    case Code.ok:

                        this.$emit('closepop')
                         alert('sucess')
                         break
                    case Code.Dup:

                        this.warning='trung username'
                }
            })
            .catch(error=>{
                console.log(error)
                 alert('e')
            })
        },
        onSub(){
            if(this.validateForm()){

                if(this.isUpdate){

                    this.update()

                }else{

                    this.register()

                }
            }
        },
        formatDate(value){
             if (value) {
                return moment(String(value)).format('DD-MM-YYYY')
            }
        },
        format(value) {
             return moment(value).format('YYYY-MM-DDThh:mm:ss')
},
    },
    components:{
        // Form, Field,ErrorMessage
    }, 
    mounted(){
        

        if(this.currentAccount.id!=null ){
            

            this.isUpdate= true

            this.fullName=this.user.fullName,
            this.description=this.user.description,
             this.dob=this.user.dob!=null? moment(this.user.dob).toDate():'',
            this.address=this.user.address,
            this.section=this.user.sectionId,
            this.roles=this.user.roleId,
            this.username=this.user.username

            console.log(this.user.section)
        }else{

            this.isUpdate=false;
        }
 

    }
}
</script>

<style scoped>
.container-user-custom{
    display:flex;
    flex-direction: column;
    align-items: center;
    position: fixed;
    width: 100%;
    height: 100%;
    top:0px;
    background-color: rgba(128, 128, 128, 0.26);
    overflow: scroll;
}
.formS{

    margin:25px;
    width: 450px;
    
}



.nav-pop{
    margin: 0 0 20px 0;
    display: flex;
    flex-direction: row-reverse;
}
.conatinerS{
    background-color: white;
    padding :0;
    border-radius: 7px;
    margin:50px auto;
}
.ef{
    position:fixed;
    left:0;
    bottom:0;
}
</style>