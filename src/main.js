
import { createApp } from 'vue'
import App from './App.vue'
import store from './store'
import router from './router'
import { library } from "@fortawesome/fontawesome-svg-core"
import { faUserSecret,faCaretDown,faTrash,faUserPlus,
    faRemoveFormat,faBeer,faPlus,
    faSearchPlus,faSearch,faWindowClose,faCheck,faPen } from "@fortawesome/free-solid-svg-icons"
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome"
import './css/global.css'
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import datepicker from 'vue3-datepicker'
import popup from './components/Popup.vue'




library.add(faUserSecret)
library.add(faCaretDown)
library.add(faTrash)
library.add(faUserPlus)
library.add(faRemoveFormat)
library.add(faBeer)
library.add(faSearchPlus)
library.add(faSearch)
library.add(faWindowClose)
library.add(faCheck)
library.add(faPlus)
library.add(faPen)

createApp(App)
.use(router).use(store)
.component('ficon',FontAwesomeIcon)
.component('date-picker', datepicker)
.component('popup',popup)
.mount('#app')
