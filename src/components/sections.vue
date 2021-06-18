<template>
  
  <div v-show="getState" class="container">
    <div class="card-header" style="margin: 10px;">
      <span>Danh sách phòng ban</span> -
      - <button @click="openAddEdit(true)" class=" none-border " v-if="getClickedSection.object != null"> <ficon icon="pen"/></button>
      - <button @click="doDelete()" class=" none-border "> <ficon icon="trash"/></button> 
      - <button @click="openAddEdit(false)" class=" none-border " > <ficon icon="plus"/></button>
    </div>
    <div class="mb-1 pl-3 pb-2">
      <Node v-for="sec in sections" :key="sec.object.id" :section="sec" :count="0"/>
    </div>
    
  </div>
  <addSec v-if="showAddEdit" :sectionRoot="sec" :isUpdate="isUpdate" @closepop="closeAddEdit()" />
  <Popup :title="title" @closeModal="showDeleteWarning=false" @ok="deletesec()" :description="des" :showButton="true" v-if="showDeleteWarning"/>
</template>

<script>
import SectionsService from '../services/SectionsService.js'
import addSec from './addSection.vue'
import Node from './Node.vue'
import Popup from './Popup.vue'

export default {
  components:{Node,addSec,Popup},
  created(){

    if(!this.getState){
      this.$router.push('/login')
    }
  },
   emits:['ServerError','showpop'],
  data(){
    return {
      sections:{},
      sec:{},
      root:null,
      e:false,
      showAddEdit:false,
      isUpdate:false,
      title:'',
      des:'',
      showDeleteWarning:false
    }
  },
  methods:{
    refresh(){
      this.$store.dispatch('sectionsModule/update')

      this.sections =this.$store.getters['sectionsModule/getListSecs']
      
    },

    openAddEdit(pos){
      
      this.sec = this.$store.getters['sectionsModule/getSectionActived']

      this.isUpdate = pos

      this.showAddEdit=true
    },
    closeAddEdit(){
      this.showAddEdit=false

      this.refresh()
    },
    doDelete(){

      this.title="Bạn có muốn xóa "+this.getClickedSection.object.name
      this.showDeleteWarning=true;

    },
    deletesec(){
      this.showDeleteWarning=false

      SectionsService.deleteSec(this.getClickedSection.object.id)
      this.refresh()
      
    },
  },
  computed:{
    getState(){
      return this.$store.getters['authModule/getStateLogin']
    },
    getClickedSection(){ 
      return this.$store.getters['sectionsModule/getSectionActived']
    }
  },
  mounted(){
    this.refresh();
    
  }
}
</script>

<style>
.container-secs{
  margin:20px;
  text-align:left;
}
</style>