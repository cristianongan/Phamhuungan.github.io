<template>
  <div class="container-user-custom">
      <div class="conatinerS">
        <div class="nav-pop">
            <button type="button" @click="$emit('closepop')"  class="close-button button none-border red">
                <ficon icon="window-close" />
            </button>
        </div>
      <Form @submit="submit" :validation-schema="formSchema" class="formS">
        <span>{{warning}}</span>
       <div class="form-group">
            <label style="display:block;text-align:left">Tên</label>
            <Field type="text" class="form-control" name="name" id="" v-model="name"  placeholder="Your Username"/>
            <ErrorMessage name="name" class="form-text text-muted red"></ErrorMessage>
        </div>
        <div class="form-group">
            <label style="display:block;text-align:left">Mô tả</label>
            <Field type="text" class="form-control" name="description" id="" v-model="description"  placeholder="Your Username"/>
            <ErrorMessage namedescription class="form-text text-muted red"></ErrorMessage>
        </div>
        <button class="close-button button none-border red">
            <ficon icon="check"/> 
            submit
        </button>
    </Form>
    </div>
        
  </div>
</template>

<script>
import { Form, Field,ErrorMessage } from 'vee-validate';
import * as yup from 'yup'
import SectionsService from '../services/SectionsService.js'
import { Code } from '../constants.js';

export default {
    props:['sectionRoot','isUpdate'],
    components:{
        Form,Field,ErrorMessage
    },
    setup(){
        const formSchema ={
            name:yup.string().required('khong duoc de trong'),
            description:yup.string()
        }

        return {
            formSchema
            }
    },
    data(){
        return {
           name:'',
           description:''
        }
    },
    methods:{
        submit(value){
            if(this.isUpdate){
                this.updateSection(value);
            } else {
                this.addSection(value);
            }
        },
        addSection(value){
            console.log('add sec:'+this.sectionRoot.object.id)

            SectionsService.addSec({
                name:value.name,
                parentId:this.sectionRoot?this.sectionRoot.object.id:null,
                description:value.description
            })
            .then(response =>{
                if(response.data.code == Code.ok){
                    alert('success')
                }else {
                    if(response.data.code == Code.Error){
                        alert('loi server')
                    }
                }
            })
            .catch(
                error=>{
                    console.log(error)
                    alert('e')
                }
            )

         },
         updateSection(value){
             SectionsService.updateSec({
                id:this.sectionRoot.object.id,
                name:value.name,
                parentId:this.sectionRoot.object.parent?this.sectionRoot.object.parent.id:null,
                description:value.description
            })
            .then(response =>{
                if(response.data.code == Code.ok){
                    alert('success')
                }else {
                    if(response.data.code == Code.Error){
                        alert('loi server'+response.data.code)
                    }
                }
            })
            .catch(
                error=>{
                    console.log(error)
                    alert('e')
                }
            )
         }
    },
    mounted(){
        if(this.isUpdate){
            this.name = this.sectionRoot.object.name,
            this.description = this.sectionRoot.object.description
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
</style>