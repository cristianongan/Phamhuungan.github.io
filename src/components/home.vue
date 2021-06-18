<template>
  <div >
    <div class="navSearch">
        <button class="button none-border" @click="advancePop=!advancePop"><ficon icon="search-plus"/></button>
        <input type="text" @change="isAdvanced=true" placeholder="Search here" v-model="key">
        <button class="button none-border" @click="search" ref="advance"><ficon icon="search" /></button>
        
    </div>
      <div class="cont" style="height:600px;overflow:scroll" @scroll="scrolling()">
        <table class="container_table" style="width:95%;">
          <thead>
            <th class="thstt"></th>
            <th>Tài khoản</th>
            <th>Bộ phận làm việc</th>
            <th>Họ và tên</th>
            <th>Mô tả</th>
            <th>Ngày sinh</th>
            <th>Địa chỉ</th>
            <th>Ngày tạo mơi</th>
            <th><button class="button none-border green" @click="changepop()"><ficon icon="user-plus"  /></button></th>
          </thead>
          <tbody>
            <tr v-for="(user, i) in list" :key="user.id">
              <td>{{i+1}}</td>
              <td>{{user.username}}</td>
              <td>{{user.section}}</td>
              <td>{{user.fullName}}</td>
              <td>{{user.description}}</td>
              <td>{{formatDate(user.dob) }}</td>
              <td>{{user.address}}</td>
              <td>{{formatDate(user.createdDate)}}</td>
              <td style="text-align:center">
                <button @click="deleteUser(user.id)" class="button none-border red">
                  <ficon icon="trash" />
                </button>
                <button @click="changepopu(user)" class="button none-border blue"> 
                    <ficon icon="pen" />
                </button>
              </td>
            </tr>
          </tbody>
      </table>
      </div>
    <div class="paging">
      
      <ul class="pagination">
        <li v-if="paging.activePage>1 " @click="refreshPage(1)"><a  >&laquo;</a></li>
        <li v-for="index in paging.listPaging" :key="index" @click="refreshPage(index)">{{index}}</li>
        <li v-if="paging.activePage<paging.totalPage-1" @click="refreshPage(paging.totalPage)"><a  >&raquo;</a></li>
      </ul>
      
    </div>
  </div>
  <User v-if="pop" @closepop="changepop()" v-model:currentAccount="account"/>
  <div class="modal advancePop " style="" role="dialog"  ref="advanceModal" v-show="advancePop" >
    <h4>Ngày sinh</h4>
          <div class="d-flex justify-content-between " >
            <div style="width:200px">
              <label>Bắt đầu từ ngày</label>
              <date-picker :inputFormat="'dd-MM-yyyy'" @selected="changeAdvance()"  v-model="startDate" class="form-control" />
            </div>
            <div style="width:200px">
            <label>Đến ngày</label>
            <date-picker   :inputFormat="'dd-MM-yyyy'" @selected="changeAdvance()"  v-model="endDate" class="form-control" />
            </div>
          </div>
        </div>

</template>

<script>
import usersService from '../services/UsersService.js'
import { Code } from '../constants.js'
import User from './User.vue'
import date from '../services/date.js'

export default {
  props:{
   
  },
  emits:['ServerError','showpop'],
  setup(){
   let paging= {
      activePage:1,
      totalPage:1,
      listPaging:[],
       account:{},
       
    }


    return{
      paging
    }
  },
  data(){
    return {
      list:[],
      isAdvanced:false,
      key:'',
      pop:false,
      startDate:new Date(),
      endDate:new Date(),
      advancePop:false
    }
  },
  methods:{
    changeAdvance(){

      this.isAdvanced=true
    },
     getListUsers(){
       usersService.getListUserByPage(this.paging.activePage-1)
      .then(response=>{
            let data= response.data

            if(data.code==Code.ok){
              this.paging.totalPage=Math.floor(data.totalResult/data.pageSize) + ((data.totalResult%data.pageSize>1)?1:data.totalResult%data.pageSize)

              this.refreshPagingList()

              this.list= data.result
            }

      })
      .catch(error=>{
        if(error.response.status == '403'){
          console.log('403')

          this.$store.dispatch('authModule/logout')
        }else {
          console.log("e:"+error)

          this.$emit('ServerError',error)

          return null;

        }

        
      })
    },
    showPop(){
      this.$emit('update:user')
    },
    getListUsersByKey(){

       usersService.getListUserByPageAndKey(this.paging.activePage-1,{
         key:this.key,
         startDate:this.startDate? date.formatDate(this.startDate):'',
         endDate:this.endDate? date.formatDate(this.endDate):''
       })
      .then(response=>{
            let data= response.data

            if(data.code==Code.ok){
              this.paging.totalPage=Math.floor(data.totalResult/data.pageSize) + 
                (data.totalResult%data.pageSize>1)?1:data.totalResult%data.pageSize
              
              this.refreshPagingList()

              this.list= data.result
            }

      })
      .catch(error=>{
        console.log("e:"+error)

        this.$emit('ServerError',error)

        return null;
      })
    },
    refreshPage(i){
      
      this.paging.activePage=i;

      this.refresh()
    },
    refreshPagingList(){
      this.paging.listPaging = []

      for(let i =this.paging.activePage-2 ;i<=this.paging.activePage+2;i++){
        if(i>0 && i <=this.paging.totalPage){
          this.paging.listPaging.push(i)
        }

      }
    },

    refresh(){
      let rs=[]

      if(this.isAdvanced){

        this.getListUsersByKey()
      } else {

        this.getListUsers()
      } 

      for(let i=this.paging.activePage-1;i<this.paging.activePage+1;i++){
        if(i>0 && i<=this.paging.totalPage){
          rs.push(i)
        }
      }

      this.paging.listPaging = rs;
    },

    updateUser(user){

      this.$emit('update:user',user);

      this.$emit('showpop');
    },
    deleteUser(id){
      usersService.deleteUser(id).then(
        this.refresh()
      ).catch(error=>{
        alert(error)
      })
        
      

      
    },

    search(){
      this.isAdvanced=true

      this.refresh()
    },

    formatDate(value){
      return date.formatDate(value)
    },
    changepop(){

      this.refresh()

      this.pop=!this.pop

      this.account ={}

      
    },
    changepopu(account){

      this.account = account;

      this.pop=!this.pop
    },
    positionningAdvanceSearch(){
      let advanceButton = this.$refs.advance;
      let advanceModal = this.$refs.advanceModal;
    
      advanceModal.style.left = (advanceButton.getBoundingClientRect().left -200 )+'px';
      advanceModal.style.top = (advanceButton.getBoundingClientRect().top +50)+'px';
    },
    scrolling(){
      this.positionningAdvanceSearch()


    }
  },
  computeds:{
    
  },
  mounted(){
    this.positionningAdvanceSearch()

    this.refresh()

    window.onscroll=this.scrolling
  },
  components:{
   User
  }


}
</script>

<style scoped src="../css/home.css">

</style>