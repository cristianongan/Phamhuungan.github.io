<template>
  <div v-if="getState" >
    <NavBar v-show="getState"/>
    <router-view @ServerError="showSE" @showpop="changepop()"/>
  </div>
  <div v-else style="width:100%;height:100%">
    <login/>
  </div>
  <popup :title="'Lỗi server'" :description="'Vui lòng thử lại sau'" @closeModal="showSE()" v-if="serverEP"/>
</template>

<script>
import NavBar from './components/NavBar.vue'
import Popup from './components/Popup.vue'
import login from './components/login.vue'


export default{
  components:{NavBar,
    Popup,
    login
   },
   
  data(){
    return {
      isLoggedin:Boolean,
      serverEP:false,
    }
  },
  methods:{
    showSE(){
      this.serverEP=!this.serverEP
    }
  },

  setup(){
    
  },
  created(){
    this.$store.commit('authModule/startup')
    this.$store.dispatch('sectionsModule/update')

    this.isLoggedin = this.$store.getters['authModule/getStateLogin'];

    if(!this.isLoggedin){
      this.$router.push('/login')
    }else{
      this.$router.push('/home')
    }
  },
  computed:{
        getState(){
            return this.$store.getters['authModule/getStateLogin'];
        }
    }

}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}


.sidebar{
  width: 20%;
}
.content-app{
  width: 75%;
}
</style>
