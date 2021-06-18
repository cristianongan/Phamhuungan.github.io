<template>
  <div class="pop-container">

      <div class="modal-dialog">
          <div class="modal-content">
              <div class="modal-header" >
                  <h5 class="modal-title">{{title}}</h5>
                  <button @click="closeModal()" class="close-button button none-border red ">
                    <ficon icon="window-close" />
                </button>
              </div>
              <div class="modal-body">
                  <p>{{description}}</p>
                  <button @click="doSubmit()" class="close-button button none-border red " v-if="showButton">
                      Ok
                  </button>
              </div>
          </div>
      </div>
  </div>
</template>

<script>
export default {
    props:{
        title:{},
        description:{},
        showButton:{}
    },
    methods:{
        closeModal(){
            this.$emit('closeModal')
        },
        doSubmit(){
            this.$emit('ok')
        }
    }
}
</script>

<style>
.pop-container{
    position:fixed;
    width: 400px;
    top:0px;
    left: 40%;
    margin: 20px auto;
    transition: 1;
}
</style>