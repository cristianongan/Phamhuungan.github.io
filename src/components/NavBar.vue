<template>
<div id="nav-custom" class="container-nav-custom" v-show="getState">
    <div class="nav-menu">
        <router-link to="/home" class="item">Home</router-link>
        <router-link to="/sec" class="item">Sec</router-link>
    </div>
    <div id="menuAccount" class="nav-menu" @mouseover="toggleDropdown" @mouseleave="close">
    <div id="" class="item " v-on:click="toggleDropdown" >
            <a>
            <ficon icon="user-secret" />
            <ficon icon="caret-down" />
            </a>

    </div>
    <div id="dropdown" class="dropdownAccount item" v-show="isActive">
        <ul>
            <li @click="showModal=true"><a >Info</a></li>
            <li><a @click="logout">Logout</a></li>
        </ul>
    </div>
        
    </div>
    <popup :title="'Hi'" :description="'Welcome to ...'" @closeModal="stateWelcomePopup()" v-if="welcomePopup"/>
    <Modal @close="showModal=false" v-if="showModal" :object="object" :titleCard="'Thông tin cá nhân'"/>
</div>

</template>

<script>
import Popup from './Popup.vue';
import Modal from './Modal.vue';
import {Code} from '../constants.js';
import userService from '../services/UsersService.js'
import date from '../services/date.js'

export default {
  components: { Popup,Modal },
    setup(){

    },
    data(){
        return{
           showModal:false,
            isActive:false,
            welcomePopup:false,
            object:{}
        }
    },
    methods:{
        toggleDropdown(){
           this.isActive=true;
        }
        ,
        close(){
            this.isActive=false;
        },
        logout(){
            this.$store.dispatch('authModule/logout')

            this.$router.push('/login')
        },
        stateWelcomePopup(){
            this.welcomePopup=!this.welcomePopup;
        },
        getPersonalInformation(){
            userService.getPersonalInfomation().then(response =>{
            if(response.data.code == Code.ok){
                let data = response.data.object ;

                let rs ={
                    f1:{
                        label:"username",value:data.username,
                        
                    },
                    f2:{
                        label:"role",value:data.role
                    },
                    f3:{
                        label:"Họ tên đầy đủ",value:data.fullName
                    },
                    f4:{
                        label:"Mô tả",value:data.description
                    },
                    f5:{
                        label:"Ngày sinh",value:date.formatDate(data.dob)
                    },
                    f6:{
                        label:"Địa chỉ",value:data.address
                    },
                    f7:{
                        label:"section",value:data.section
                    }
                }
                this.object = rs;
            }

        })
        .catch(error=>{
            console.log(error)
        })

        }
    },
    computed:{
        getState(){
            return this.$store.getters['authModule/getStateLogin']
        }
    },
    mounted(){
        this.getPersonalInformation();
    }
}
</script>

<style scoped src="../css/nav.css">

</style>