<template >

    <div class="pop-container" @keydown="onKey">

      <div class="modal-dialog">
          <div class="modal-content">
              <div class="modal-header" >
                  <h5 class="modal-title">{{titleCard}}</h5>
                  <button @click="showPasswordForm=true" class="close-button button none-border blue">Đổi mật khẩu</button>
                  <button @click="closeModal()" class="close-button button none-border red ">
                    <ficon icon="window-close" />
                </button>
              </div>
              <div class="modal-body">
                    <ul class="list-group">
                        <li class="list-group-item" v-for="field in object" :key="field">
                            <p>{{field.label}}</p>
                            :
                            <p>{{field.value}}</p>
                        </li>
                    </ul>
              </div>
          </div>
      </div>
  </div>
  <PasswordForm  v-if="showPasswordForm" @close="showPasswordForm=false" :username="object.f1.value" />
</template>

<script>
import PasswordForm from './PasswordForm.vue'

export default {
    components:{
        PasswordForm
    },
    props:{
        object:{},
        titleCard:{}
    },
    methods:{
        closeModal(){
            this.$emit('close');
        },
        onKey(key){
            console.log('vao onkey'+key.key)

            if(key.key == 'Escape'){
                this.showPasswordForm = false
            }
                
        }
    },
    data(){
        return {
            showPasswordForm:false
        }
    }

}
</script>

<style scoped>
li{
    display: flex;
}
</style>