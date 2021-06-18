<template>
    <div :class="{active : getClickedSection==section}" class="contenttree" @click="clickListener()">
            <p v-for="index in count" :key="index" style="margin-left:30px"></p>
            <span>{{section.object.name}}</span>
            <!-- (<p>{{section.object.code}}</p>) -->
            <button @click="show=!show" v-if="section.childrens.length > 0" class=" none-border "> <ficon icon="caret-down"/></button>
            
    </div>
    <div v-if="show" style="display:block">
        <Node v-for="sec in section.childrens" :key="sec.object.id" :section="sec" :count="temp"/>
    </div>  

</template>

<script>

export default {
    components:{
        
    },
    props:{
       section:{},
       count:{}
    },
    data(){
        return{
            show:false,

            temp:this.count+1
        }
    },
    beforeCreate(){
        this.$options.components.TreeSec = require('./Node.vue').default;
    },
    methods:{
        clickListener(){
            this.$store.dispatch('sectionsModule/updateSection',this.section)

        }
    },
    computed:{
        getClickedSection(){
            
            return this.$store.getters['sectionsModule/getSectionActived']
        }
    }
}
</script>

<style>
.contenttree{
    display:flex;
    cursor: pointer;
}

.active {
    background-color: rgba(0, 0, 0, 0.322);
    color: white;
}
</style>