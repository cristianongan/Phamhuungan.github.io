import { createStore } from 'vuex'
import authModule from './authModuls.js'
import sectionsModule from './sectionsModule.js'
import usersModule from './UsersModule.js'

export default createStore({
  state: {

  },
  mutations: {

  },
  actions: {

  },
  modules: {
    authModule,
    sectionsModule,
    usersModule
  }
})
